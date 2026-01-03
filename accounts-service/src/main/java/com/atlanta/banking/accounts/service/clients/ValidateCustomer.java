package com.atlanta.banking.accounts.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface ValidateCustomer {

    @GetMapping("/api/customer/check-customer/{customerId}")
    public boolean validateCustomer(@PathVariable String customerId);
}
