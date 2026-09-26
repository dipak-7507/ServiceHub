package com.servicehub.controller;

import com.servicehub.dto.ProviderLoginDTO;
import com.servicehub.dto.ServiceProviderDTO;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.service.ServiceProviderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.servicehub.exception.UnauthorizedAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import com.servicehub.security.JwtUtil;
import com.servicehub.dto.LoginResponseDTO;

@RestController
@RequestMapping("/providers")
public class ServiceProviderController {
    private final ServiceProviderService serviceProviderService;
    private final JwtUtil jwtUtil;

    public ServiceProviderController(ServiceProviderService serviceProviderService, JwtUtil jwtUtil) {
        this.serviceProviderService = serviceProviderService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ServiceProvider saveProvider(
            @Valid @RequestBody ServiceProviderDTO serviceProviderDTO) {

        return serviceProviderService.saveProvider(serviceProviderDTO);
    }

    @GetMapping
    public List<ServiceProvider> getAllProviders() {

        return serviceProviderService.getAllProvider();
    }

    @GetMapping("/{id}")
    public ServiceProvider getProviderById(@PathVariable Long id) {

        ServiceProvider provider = serviceProviderService.getProviderById(id);
        String loggedInEmail = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        if (!provider.getEmail().equals(loggedInEmail)) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this provider's data");
        }
        return provider;
    }

    @PutMapping("/{id}")
    public ServiceProvider updateProvider(
            @PathVariable Long id,
            @Valid @RequestBody ServiceProviderDTO serviceProviderDTO) {

        ServiceProvider existing = serviceProviderService.getProviderById(id);

        String loggedInEmail =
                SecurityContextHolder.getContext()
                        .getAuthentication().getName();
        if (!existing.getEmail().equals(loggedInEmail)) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this provider's data");

        }
        return serviceProviderService.updateProvider(id, serviceProviderDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProvider(@PathVariable Long id) {

        ServiceProvider existing = serviceProviderService.getProviderById(id);

        String loggedInEmail =
                SecurityContextHolder.getContext()
                        .getAuthentication().getName();
        if (!existing.getEmail().equals(loggedInEmail)) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this provider's data");
        }

        serviceProviderService.deleteProvider(id);
    }

    @GetMapping("/category/{categoryName}")
    public List<ServiceProvider> getProviderByCategory(
            @PathVariable String categoryName) {
        return serviceProviderService
                .getProviderByCategory(categoryName);
    }


    @PostMapping("/login")
    public LoginResponseDTO login(
            @RequestBody ProviderLoginDTO loginDTO) {
        ServiceProvider provider = serviceProviderService.login(loginDTO);

        String token = jwtUtil.generateToken(provider.getEmail());


        return new LoginResponseDTO(token);
    }
}