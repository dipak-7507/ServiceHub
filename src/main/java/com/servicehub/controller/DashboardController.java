package com.servicehub.controller;


import com.servicehub.dto.DashboardDTO;
import com.servicehub.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService){

        this.dashboardService = dashboardService;
    }

    @GetMapping("/provider/{providerId}")

    public DashboardDTO getProviderDashboard(
            @PathVariable Long providerId){

        return dashboardService
                .getProviderDashboard(providerId);

    }
}
