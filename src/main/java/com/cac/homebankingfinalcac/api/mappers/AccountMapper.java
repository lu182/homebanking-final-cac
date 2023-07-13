package com.cac.homebankingfinalcac.api.mappers;

import com.cac.homebankingfinalcac.api.dtos.AccountDTO;
import com.cac.homebankingfinalcac.api.dtos.UserDTO;
import com.cac.homebankingfinalcac.domain.models.AccountEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {

    //TODO: Aplicar Patrón Builder.

    public AccountEntity dtoToAccountMap(AccountDTO dto) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(dto.getAmount());
        //accountEntity.setOwner(dto.getOwner()); //NO VA? - Ver de pasarle solo el id de owner - Pendiente ver con profe
        return accountEntity;
    }

    public AccountDTO accountToDtoMap(AccountEntity accountEntity) {
        AccountDTO dto = new AccountDTO();
        dto.setIdAccount(accountEntity.getIdAccount());
        dto.setAmount(accountEntity.getBalance());
        UserDTO userDtoOwner = UserMapper.userToDtoMap(accountEntity.getOwner());
        dto.setOwner(userDtoOwner);
        return dto;
    }

}