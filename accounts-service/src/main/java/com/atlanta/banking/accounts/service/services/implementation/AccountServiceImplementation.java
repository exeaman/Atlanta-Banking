package com.atlanta.banking.accounts.service.services.implementation;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.atlanta.banking.accounts.service.exception.AccountDuplicacyException;
import com.atlanta.banking.accounts.service.exception.AccountNotFoundException;
import com.atlanta.banking.accounts.service.exception.InvalidAccountStateException;
import com.atlanta.banking.accounts.service.exception.InvalidCustomerException;
import com.atlanta.banking.accounts.service.repository.AccountCustomerMapRepository;
import com.atlanta.banking.accounts.service.repository.AccountRepository;
import com.atlanta.banking.accounts.service.services.AccountNumberSequenceGeneratorService;
import com.atlanta.banking.accounts.service.services.AccountService;
import com.atlanta.banking.accounts.service.services.UserIdSequenceGeneratorService;
import com.atlanta.banking.accounts.service.utils.AccountStatus;
import com.atlanta.banking.accounts.service.utils.AccountType;

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

        if (!validateCustomer(accountCreationRequestDto.getCustomerId().toString()))
            throw new InvalidCustomerException(
                    "Account microservice issues " + accountCreationRequestDto.getCustomerId().toString());

        if (!canOpen(accountCreationRequestDto.getCustomerId(), accountCreationRequestDto.getAccountType()))
            throw new AccountDuplicacyException("A " + accountCreationRequestDto.getAccountType().toString()
                    + " account already exists with customer ID :" + accountCreationRequestDto.getCustomerId());
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
                getAccount(accountNumber),
                AccountResponseDto.class);
    }

    @Override
    public List<AccountResponseDto> getAccountByCustomerId(String customerId) {
        if (!validateCustomerFien.validateCustomer(customerId))
            throw new InvalidCustomerException(
                    "No customer found with ID " + customerId);
        List<String> allUserIds = accMapRepo.findAllByCustomerId(UUID.fromString(customerId)).stream()
                .map(e -> e.getUserId()).toList();
        List<AccountResponseDto> accountsByCustomerId = new ArrayList<>();
        for (String userId : allUserIds) {
            accountsByCustomerId.add(mapper.map(accountRepo.findByUserId(userId), AccountResponseDto.class));
        }
        if (accountsByCustomerId.isEmpty())
            throw new AccountNotFoundException("No accounts exists with customer ID :" + customerId);
        return accountsByCustomerId;
    }

    @Override
    public Boolean hasAccount(UUID customerId) {
        if (!validateCustomerFien.validateCustomer(customerId.toString()))
            throw new InvalidCustomerException(
                    "No customer found with ID " + customerId);
        List<String> allUserIds = accMapRepo.findAllByCustomerId(customerId).stream()
                .map(e -> e.getUserId()).toList();
        List<AccountResponseDto> accountsByCustomerId = new ArrayList<>();
        for (String userId : allUserIds) {
            accountsByCustomerId.add(mapper.map(accountRepo.findByUserId(userId), AccountResponseDto.class));
        }
        return allUserIds.isEmpty() ? false : true;
    }

    @Override
    public void freezeAccount(String accountNumber) {
        Account account = getAccount(accountNumber);

        if (account.getAccountStatus() == AccountStatus.FROZEN || account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new InvalidAccountStateException(
                    "Account is already " + account.getAccountStatus().toString() + " " + accountNumber);
        }
        account.setAccountStatus(AccountStatus.FROZEN);
        account.setIsActive(false);
        accountRepo.save(account);

    }

    @Override
    public void unFreezeAccount(String accountNumber) {
        Account account = getAccount(accountNumber);

        if (account.getAccountStatus() == AccountStatus.ACTIVE) {
            throw new InvalidAccountStateException(
                    "Account is already " + account.getAccountStatus().toString() + " " + accountNumber);
        }
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setIsActive(true);
        accountRepo.save(account);
    }

    @Override
    public void closeAccount(String accountNumber) {
        Account account = getAccount(accountNumber);

        if (account.getAccountStatus() == AccountStatus.CLOSED)
            throw new InvalidAccountStateException("Account is already closed " + accountNumber);

        account.setAccountStatus(AccountStatus.CLOSED);
        account.setIsActive(false);
        accountRepo.save(account);
    }

    @Override
    public void credit(String accountNumber, BigDecimal amount) {
        Account account = getAccount(accountNumber);

        if (account.getIsActive()) {
            BigDecimal balance = account.getBalance();
            balance = balance.add(amount);
            account.setBalance(balance);
        }

        accountRepo.save(account);
    }

    @Override
    public void debit(String accountNumber, BigDecimal amount) {
        Account account = getAccount(accountNumber);

        if (account.getIsActive()) {
            BigDecimal balance = account.getBalance();
            if (balance.compareTo(amount) < 0)
                throw new InvalidAccountStateException("Insufficient Funds in account " + accountNumber);
            balance = balance.subtract(amount);
            account.setBalance(balance);
        }

        accountRepo.save(account);
    }

    @Override
    public Boolean isAccountActive(String accountNumber) {
        return getAccount(accountNumber).getIsActive();
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        return getAccount(accountNumber).getBalance();
    }

    @Override
    public Boolean validateCustomer(String customerId) {
        return validateCustomerFien.validateCustomer(customerId);
    }

    private Account getAccount(String accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("No account exists with the number " + accountNumber));
    }

    private Boolean canOpen(UUID customerId, AccountType accountType) {
        if(!hasAccount(customerId)) return true;
        List<AccountResponseDto> accounts = getAccountByCustomerId(customerId.toString());

        for (AccountResponseDto acc : accounts) {
            if (acc.getAccountType() == accountType)
                return false;
        }
        return true;
    }
}
