package com.servicehub.dto;

import com.servicehub.entity.ServiceProvider;

/**
 * Public view of a provider, used when customers browse providers.
 * Email, mobile number and address are intentionally NOT included.
 */
public record ProviderSummaryDTO(
        Long id,
        String firstName,
        String lastName,
        Integer experience,
        String city,
        String categoryName) {

    public static ProviderSummaryDTO from(ServiceProvider p) {
        String category = p.getServiceCategory() == null
                ? null
                : p.getServiceCategory().getCategoryName();
        return new ProviderSummaryDTO(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getExperience(),
                p.getCity(),
                category);
    }
}