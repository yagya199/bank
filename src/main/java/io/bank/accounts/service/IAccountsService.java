package io.bank.accounts.service;

import io.bank.accounts.dto.CustomerDto;
import org.springframework.web.bind.annotation.RequestParam;

public interface IAccountsService {

     void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(@RequestParam String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

}
