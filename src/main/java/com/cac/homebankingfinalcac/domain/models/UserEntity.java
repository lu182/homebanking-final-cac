package com.cac.homebankingfinalcac.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", nullable = false)
    private Long idUser;

    private String username;

    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true) //1 owner puede tener 1 o varias cuentas
    private List<AccountEntity> userAccounts; //cuentas del usuario. Cascade: si se elimina un usuario, se eliminan su cuentas asociadas

}