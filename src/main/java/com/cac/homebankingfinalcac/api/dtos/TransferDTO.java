package com.cac.homebankingfinalcac.api.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransferDTO {

    private Long idTransfer;

    private Long originAccount;

    private Long targetAccount;

    private LocalDate transferDate; //No hace falta colocar "transferDate": "11/07/2023" en el Body

    private BigDecimal amountTransfer; //monto de la transferencia

}
