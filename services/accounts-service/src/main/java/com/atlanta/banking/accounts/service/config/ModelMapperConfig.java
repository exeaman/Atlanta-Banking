package com.atlanta.banking.accounts.service.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.atlanta.banking.accounts.service.dto.AccountResponseDto;
import com.atlanta.banking.accounts.service.entity.Account;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Account, AccountResponseDto>() {
            @Override
            protected void configure() {
                map().setAccountNumber(source.getAccountNumber());
                //Rest of the fields will be matched automatically.
            }
        });

        return modelMapper;
    }
}
