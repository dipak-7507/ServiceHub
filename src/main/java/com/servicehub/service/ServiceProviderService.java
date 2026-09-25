package com.servicehub.service;

import com.servicehub.dto.ProviderLoginDTO;
import com.servicehub.dto.ServiceProviderDTO;
import com.servicehub.entity.ServiceCategory;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.repository.ServiceCategoryRepository;
import com.servicehub.repository.ServiceProviderRepository;
import com.servicehub.exception.ProviderNotFoundException;
import com.servicehub.exception.CategoryNotFoundException;
import com.servicehub.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class ServiceProviderService {
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final PasswordEncoder passwordEncoder;

    public ServiceProviderService(
            ServiceProviderRepository serviceProviderRepository,
            ServiceCategoryRepository serviceCategoryRepository,
            PasswordEncoder passwordEncoder) {

        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceCategoryRepository = serviceCategoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ServiceProvider saveProvider(ServiceProviderDTO serviceProviderDTO) {
        ServiceCategory category = serviceCategoryRepository
                .findById(serviceProviderDTO.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found"));
        ServiceProvider serviceProvider = new ServiceProvider();

        serviceProvider.setFirstName(serviceProviderDTO.getFirstName());
        serviceProvider.setLastName(serviceProviderDTO.getLastName());
        serviceProvider.setEmail(serviceProviderDTO.getEmail());
        serviceProvider.setMobileNumber(serviceProviderDTO.getMobileNumber());
        serviceProvider.setPassword(
                passwordEncoder.encode(serviceProviderDTO.getPassword()));
        serviceProvider.setExperience(serviceProviderDTO.getExperience());
        serviceProvider.setCity(serviceProviderDTO.getCity());
        serviceProvider.setAddress(serviceProviderDTO.getAddress());

        serviceProvider.setCreatedAt(LocalDateTime.now());

        serviceProvider.setServiceCategory(category);

        return serviceProviderRepository.save(serviceProvider);

    }

    public List<ServiceProvider> getAllProvider(){
        return serviceProviderRepository.findAll();
    }

    public ServiceProvider getProviderById(Long id){
        return serviceProviderRepository.findById(id)
                .orElseThrow(()->
                        new ProviderNotFoundException("Provider with id " + id + " not found"));

    }


    public ServiceProvider updateProvider(
            Long id, ServiceProviderDTO
                    serviceProviderDTO){
        ServiceProvider serviceProvider =
                serviceProviderRepository.findById(id)
                        .orElseThrow(()->
                                new ProviderNotFoundException( "Provider with id " + id + " not found"));

        ServiceCategory category =
                serviceCategoryRepository.findById(
                                serviceProviderDTO.getCategoryId())
                        .orElseThrow(()->
                                new CategoryNotFoundException("Category not found"));
        serviceProvider.setFirstName(serviceProviderDTO.getFirstName());
        serviceProvider.setLastName(serviceProviderDTO.getLastName());
        serviceProvider.setEmail(serviceProviderDTO.getEmail());
        serviceProvider.setMobileNumber(serviceProviderDTO.getMobileNumber());
        serviceProvider.setPassword(
                passwordEncoder.encode(serviceProviderDTO.getPassword()));
        serviceProvider.setExperience(serviceProviderDTO.getExperience());
        serviceProvider.setCity(serviceProviderDTO.getCity());
        serviceProvider.setAddress(serviceProviderDTO.getAddress());
        serviceProvider.setServiceCategory(category);

        return serviceProviderRepository.save(serviceProvider);

    }

    public void deleteProvider(Long id){
        ServiceProvider serviceProvider =
                serviceProviderRepository.findById(id)
                        .orElseThrow(()->
                                new ProviderNotFoundException( "Provider with id " + id + " not found"));

        serviceProviderRepository.delete(serviceProvider);
    }

    public List<ServiceProvider>getProviderByCategory(String categoryName){
        return serviceProviderRepository
                .findByServiceCategoryCategoryName(categoryName);
    }

    public ServiceProvider login(ProviderLoginDTO loginDTO){

        ServiceProvider provider =
                serviceProviderRepository
                        .findByEmail(loginDTO.getEmail())
                        .orElseThrow(()->
                                new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(
                loginDTO.getPassword(), provider.getPassword())){
            throw new InvalidCredentialsException("Invalid email or password");

        }
        return provider;
    }

}