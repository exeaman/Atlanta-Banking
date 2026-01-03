package com.atlanta.banking.accounts.service.services.implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.atlanta.banking.accounts.service.clients.ValidateCustomer;
import com.atlanta.banking.accounts.service.dto.AccountCreationRequestDto;
import com.atlanta.banking.accounts.service.dto.AccountResponseDto;
import com.atlanta.banking.accounts.service.entity.Account;
import com.atlanta.banking.accounts.service.entity.AccountCustomerMap;
import com.atlanta.banking.accounts.service.exception.AccountNotFoundException;
import com.atlanta.banking.accounts.service.exception.InvalidCustomerException;
import com.atlanta.banking.accounts.service.repository.AccountCustomerMapRepository;
import com.atlanta.banking.accounts.service.repository.AccountRepository;
import com.atlanta.banking.accounts.service.services.AccountNumberSequenceGeneratorService;
import com.atlanta.banking.accounts.service.services.AccountService;
import com.atlanta.banking.accounts.service.services.UserIdSequenceGeneratorService;
import com.atlanta.banking.accounts.service.utils.AccountStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImplementation implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImplementation.class);
    private final ModelMapper mapper;
    private final UserIdSequenceGeneratorService userIdGen;
    private final AccountNumberSequenceGeneratorService accNumGen;
    private final AccountRepository accountRepo;
    private final AccountCustomerMapRepository accMapRepo;
    private final ValidateCustomer validateCustomerFien;

    @Override
    @Transactional
    public AccountResponseDto openAccount(AccountCreationRequestDto accountCreationRequestDto) {

        if (validateCustomer(accountCreationRequestDto.getCustomerId().toString()))
            throw new InvalidCustomerException(
                    "No customer found with ID " + accountCreationRequestDto.getCustomerId().toString());

        // Account Entity Object
        Account account = new Account();

        // Account Customer Map Object
        AccountCustomerMap accMap = new AccountCustomerMap();

        String accNumber = accNumGen.generateAccountNumber(accountCreationRequestDto.getAccountType());
        String userId = userIdGen.generateUserId();

        // Acc creation and saving
        account.setUserId(userId);
        account.setAccountNumber(accNumber);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAccountType(accountCreationRequestDto.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency("INR");

        // Map creation and saving
        accMap.setAccountNumber(accNumber);
        accMap.setUserId(userId);
        accMap.setCustomerId(accountCreationRequestDto.getCustomerId());
        accMapRepo.save(accMap);
        logger.info("Account Created Successfully!");
        return mapper.map(accountRepo.save(account), AccountResponseDto.class);

    }

    @Override
    public AccountResponseDto getAccountByAccountNumber(String accountNumber) {
        return mapper.map(
                accountRepo.findByAccountNumber(accountNumber).orElseThrow(
                        () -> new AccountNotFoundException("No account with such number exists " + accountNumber)),
                AccountResponseDto.class);
    }

    @Override
    public List<AccountResponseDto> getAccountByCustomerId(UUID customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountByCustomerId'");
    }

    @Override
    public void freezeAccount(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'freezeAccount'");
    }

    @Override
    public void unFreezeAccount(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unFreezeAccount'");
    }

    @Override
    public void closeAccount(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeAccount'");
    }

    @Override
    public void credit(String accountNumber, BigDecimal amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'credit'");
    }

    @Override
    public void debit(String accountNumber, BigDecimal amount) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debit'");
    }

    @Override
    public Boolean isAccountActive(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountActive'");
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalance'");
    }

    @Override
    public Boolean validateCustomer(String customerId) {
        return validateCustomerFien.validateCustomer(customerId);
    }

}
