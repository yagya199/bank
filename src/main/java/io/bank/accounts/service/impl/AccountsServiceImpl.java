package io.bank.accounts.service.impl;

import io.bank.accounts.constants.AccountsConstants;
import io.bank.accounts.dto.AccountsDto;
import io.bank.accounts.dto.CustomerDto;
import io.bank.accounts.entity.Accounts;
import io.bank.accounts.entity.Customer;
import io.bank.accounts.exception.CustomerAlreadyExistsException;
import io.bank.accounts.exception.ResourceNotFoundException;
import io.bank.accounts.mapper.AccountsMapper;
import io.bank.accounts.mapper.CustomerMapper;
import io.bank.accounts.repository.AccountsRepository;
import io.bank.accounts.repository.CustomerRepository;
import io.bank.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {
    // we are using @allargsconstructor , so no need for autowired
    private AccountsRepository accountsRepository;

    private CustomerRepository customerRepository;

    /**
     * @param customerDto
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer= CustomerMapper.mapToCustomer(customerDto,new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already exists with given mobile number"+ customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer=customerRepository.save(customer);

        accountsRepository.save(createNewAccounts(savedCustomer));

    }

    /**
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

     Customer customer=customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("Customer","mobileNumber",mobileNumber)
        );

     Accounts accounts=accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
             ()-> new ResourceNotFoundException("Account","mobileNumber",mobileNumber)
     );

     CustomerDto customerDto=CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
     customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));

        return customerDto;
    }

    /**
     * @param customerDto
     * @return
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }


    private Accounts createNewAccounts(Customer customer){

        Accounts newAccount=new Accounts();

        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber= 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anonymous");

        return newAccount;

    }



}
