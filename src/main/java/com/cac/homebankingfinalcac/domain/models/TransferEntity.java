package com.cac.homebankingfinalcac.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TRANSFERS")
@Data
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transfer", nullable = false)
    private Long idTransfer;

    private Long originAccount;

    private Long targetAccount;

    private LocalDate transferDate;

    private BigDecimal amountTransfer; //monto de la transferencia

}