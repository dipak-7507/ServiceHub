package com.servicehub.controller;

import com.servicehub.dto.ProviderLoginDTO;
import com.servicehub.dto.ServiceProviderDTO;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.service.ServiceProviderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/providers")
public class ServiceProviderController {
    private final ServiceProviderService serviceProviderService;

    public ServiceProviderController(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @PostMapping
    public ServiceProvider saveProvider(
            @Valid @RequestBody ServiceProviderDTO serviceProviderDTO) {

        return serviceProviderService.saveProvider(serviceProviderDTO);
    }
    @GetMapping
    public List<ServiceProvider> getAllProviders(){

        return serviceProviderService.getAllProvider();
    }

    @GetMapping("/{id}")
    public ServiceProvider getProviderById(@PathVariable Long id){

        return serviceProviderService.getProviderById(id);
    }

    @PutMapping("/{id}")
    public ServiceProvider updateProvider(
            @PathVariable Long id,
            @Valid @RequestBody ServiceProviderDTO serviceProviderDTO){

       return serviceProviderService.updateProvider(id,serviceProviderDTO);

    }
    @DeleteMapping("/{id}")
    public void deleteProvider(@PathVariable Long id){

        serviceProviderService.deleteProvider(id);
    }
    @GetMapping("/category/{categoryName}")
        public List<ServiceProvider>getProviderByCategory(
                @PathVariable String categoryName){
        return serviceProviderService
                .getProviderByCategory(categoryName);
        }


        @PostMapping("/login")
        public ServiceProvider login(
                @RequestBody ProviderLoginDTO loginDTO){
             return serviceProviderService.login(loginDTO);
        }
}
