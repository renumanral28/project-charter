package com.rewards.controller;

import com.rewards.model.Rewards;
import com.rewards.service.RewardsService;
import com.rewards.exception.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsService rewardsService;

    private static final String CUSTOMER_ID = "12345";
    private static final String YEAR = "2024";
    private static final String MONTH = "05";

    private Rewards mockRewards;

    @BeforeEach
    void setUp() {
        mockRewards = new Rewards();
        mockRewards.setCustomerId(CUSTOMER_ID);
      //  mockRewards.setPoints(100);
    }

    @Test
    void testGetRewardsByCustomerId_Success() throws Exception {
        when(rewardsService.getRewardsByCustomerId(CUSTOMER_ID)).thenReturn(mockRewards);

        mockMvc.perform(get("/rewards/{customerId}", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID));

        verify(rewardsService, times(1)).getRewardsByCustomerId(CUSTOMER_ID);
    }

    @Test
    void testGetRewardsByCustomerId_CustomerNotFound() throws Exception {
        when(rewardsService.getRewardsByCustomerId(CUSTOMER_ID))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/rewards/{customerId}", CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found"));

        verify(rewardsService, times(1)).getRewardsByCustomerId(CUSTOMER_ID);
    }

    @Test
    void testGetRewardsByCustomerIdAndDate_Success() throws Exception {
        when(rewardsService.getRewardsByCustomerIdAndMonth(CUSTOMER_ID, MONTH, YEAR)).thenReturn(mockRewards);

        mockMvc.perform(get("/rewards/{customerId}/{year}/{month}", CUSTOMER_ID, YEAR, MONTH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(CUSTOMER_ID));
        verify(rewardsService, times(1)).getRewardsByCustomerIdAndMonth(CUSTOMER_ID, MONTH, YEAR);
    }

    @Test
    void testGetRewardsByCustomerIdAndDate_CustomerNotFound() throws Exception {
        when(rewardsService.getRewardsByCustomerIdAndMonth(CUSTOMER_ID, MONTH, YEAR))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/rewards/{customerId}/{year}/{month}", CUSTOMER_ID, YEAR, MONTH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found"));

        verify(rewardsService, times(1)).getRewardsByCustomerIdAndMonth(CUSTOMER_ID, MONTH, YEAR);
    }
}