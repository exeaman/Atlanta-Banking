package com.atlanta.banking.customer.service.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.atlanta.banking.customer.service.dto.CustomerRequestDto;
import com.atlanta.banking.customer.service.dto.CustomerResponseDto;
import com.atlanta.banking.customer.service.entity.Customer;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // From Customer Request DTO -> Customer Entity
        mapper.addMappings(new PropertyMap<CustomerRequestDto, Customer>() {
            @Override
            protected void configure() {
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());
                
                // Normalize email to lowercase
                using(ctx -> {
                    String email = (String) ctx.getSource();
                    return email == null ? null : email.trim().toLowerCase();
                }).map(source.getEmail(), destination.getEmail());

                map().setPhoneNumber(source.getPhoneNumber());
                map().setDateOfBirth(source.getDateOfBirth());
                map().setAddressLine1(source.getAddressLine1());
                map().setAddressLine2(source.getAddressLine2());
                map().setCity(source.getCity());
                map().setState(source.getState());
                map().setPostalCode(source.getPostalCode());
                map().setCountry(source.getCountry());

                // Explicitly skip server-managed/internal fields
                skip(destination.getCustomerId());
                skip(destination.getCreatedAt());
                skip(destination.getUpdatedAt());
                skip(destination.getIsActive());
                skip(destination.getKycStatus());
            }
        });

        // From Customer Entity -> Customer Response DTO
        mapper.addMappings(new PropertyMap<Customer, CustomerResponseDto>() {
            @Override
            protected void configure() {
                map().setCustomerId(source.getCustomerId());
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());
                map().setEmail(source.getEmail());
                map().setPhoneNumber(source.getPhoneNumber());
                map().setDateOfBirth(source.getDateOfBirth());
                map().setAddressLine1(source.getAddressLine1());
                map().setAddressLine2(source.getAddressLine2());
                map().setCity(source.getCity());
                map().setState(source.getState());
                map().setPostalCode(source.getPostalCode());
                map().setCountry(source.getCountry());
                map().setKycStatus(source.getKycStatus());
                map().setIsActive(source.getIsActive());
                map().setCreatedAt(source.getCreatedAt());
            }
        });

        return mapper;
    }
}
