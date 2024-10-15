package io.bank.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {
    @NotEmpty(message="Name field should not be empty")
    @Size(min=3,max=30,message="Name length must be between 3 and 30")
    private String name;

    @NotEmpty(message = "Email not be empty")
    @Email(message = "Please enter correct email")
    private String email;

    @Pattern(regexp="$[0-9]{10}",message = "mobile number must be 10 digit")
    private String mobileNumber;

    private AccountsDto accountsDto;
}
