package com.rewards.controller;

import com.rewards.model.Rewards;
import com.rewards.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RewardsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RewardsService rewardsService;

    @InjectMocks
    private RewardsController rewardsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(rewardsController).build();  // Manually setup MockMvc
    }

    @Test
    void testGetRewardsByCustomerId_Success() throws Exception {
        // Given
        String customerId = "123";
        Rewards rewards = new Rewards();
        rewards.setCustomerId(customerId);
        rewards.setTotalRewards(150);  // example total reward points
        when(rewardsService.getRewardsByCustomerId(customerId)).thenReturn(rewards);

        // When & Then
        mockMvc.perform(get("/rewards/{customerId}", customerId))
                .andExpect(status().isOk())  // Expect 200 OK status
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.totalRewards").value(150));
    }



    @Test
    void testGetRewardsByCustomerIdAndMonth_Success() throws Exception {
        // Given
        String customerId = "123";
        String year = "2025";
        String month = "01";
        Rewards rewards = new Rewards();
        rewards.setCustomerId(customerId);
        rewards.setTotalRewards(200);  // example total reward points
        when(rewardsService.getRewardsByCustomerIdAndMonth(customerId, month, year)).thenReturn(rewards);

        // When & Then
        mockMvc.perform(get("/rewards/{customerId}/{year}/{month}", customerId, year, month))
                .andExpect(status().isOk())  // Expect 200 OK status
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.totalRewards").value(200));
    }


}