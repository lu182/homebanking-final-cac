package com.cac.homebankingfinalcac.api.mappers;

import com.cac.homebankingfinalcac.api.dtos.UserDTO;
import com.cac.homebankingfinalcac.domain.models.AccountEntity;
import com.cac.homebankingfinalcac.domain.models.UserEntity;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {

    //TODO: Aplicar Patrón Builder

    public UserEntity dtoToUserMap(UserDTO dto){
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }

    public UserDTO userToDtoMap(UserEntity user){
        UserDTO dto = new UserDTO();
        List<Long> accountsId = new ArrayList<>();

        dto.setId(user.getIdUser());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        if (user.getUserAccounts() != null){
            for (AccountEntity a : user.getUserAccounts()){
                Long id = a.getIdAccount();
                accountsId.add(id);
            }
        }
        dto.setAccountsId(accountsId);
        return dto;
    }

}