package com.rewards.controller;

import com.rewards.model.Rewards;
import com.rewards.service.RewardsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsService rewardsService;

    @Test
    public void testGetRewardsByCustomerId_success() throws Exception {
        String customerId = "1";
        Rewards rewards = new Rewards();
        rewards.setCustomerId(customerId);
        given(rewardsService.getRewardsByCustomerId(customerId)).willReturn(rewards);
        mockMvc.perform(get("/{customerId}/rewards", customerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId)) ;
    }

    @Test
    public void testGetRewardsByCustomerId_notFound() throws Exception {
        String customerId = "2";
        given(rewardsService.getRewardsByCustomerId(customerId)).willReturn(null);
        mockMvc.perform(get("/{customerId}/rewards", customerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRewardsByCustomerId_withMonthFilter() throws Exception {
        String customerId = "1";
        String month = "2025-01";
        Rewards rewards = new Rewards();
        rewards.setCustomerId(customerId);
        rewards.setTotalRewards(200);
        when(rewardsService.getRewardsByCustomerIdAndMonth(customerId, month)).thenReturn(rewards);

        mockMvc.perform(get("/{customerId}/rewards?month={month}", customerId, month)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Expect 200 OK status
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.totalRewards").value(200));
    }
}