package com.cac.homebankingfinalcac.api.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {

    private Long idAccount;
    private BigDecimal amount;
    private UserDTO owner; //propietario de la cuenta.

}