package com.cac.homebankingfinalcac.api.mappers;

import com.cac.homebankingfinalcac.api.dtos.AccountDTO;
import com.cac.homebankingfinalcac.api.dtos.UserDTO;
import com.cac.homebankingfinalcac.domain.models.AccountEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountMapper {

    public AccountEntity dtoToAccountMap(AccountDTO dto) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setBalance(dto.getAmount());
        return accountEntity;
    }

    public AccountDTO accountToDtoMap(AccountEntity accountEntity) {
        AccountDTO dto = new AccountDTO();
        dto.setIdAccount(accountEntity.getIdAccount());
        dto.setAmount(accountEntity.getBalance());
        if (accountEntity.getOwner() != null){
            UserDTO userDtoOwner = UserMapper.userToDtoMap(accountEntity.getOwner());
            dto.setOwner(userDtoOwner);
        }
        return dto;
    }

}