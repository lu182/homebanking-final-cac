package com.cac.homebankingfinalcac.api.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private List<Long> accountsId; //Lista de los id's de las cuentas del usuario

}
