package com.cac.homebankingfinalcac.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNTS")
@Data
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account", nullable = false)
    private Long idAccount;

    @Column(name = "account_balance", nullable = false)
    private BigDecimal balance; //saldo

    @ManyToOne //muchas cuentas le pertenecen a un único owner
    //@JoinColumn(name = "user_id", nullable = false) //A pesar de estar comentado, se genera lo mismo la FK en BD.
    private UserEntity owner;

}