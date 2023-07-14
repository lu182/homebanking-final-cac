package com.cac.homebankingfinalcac.application.services;

import com.cac.homebankingfinalcac.api.dtos.AccountDTO;
import com.cac.homebankingfinalcac.api.mappers.AccountMapper;
import com.cac.homebankingfinalcac.application.exceptions.PersonalizedException;
import com.cac.homebankingfinalcac.domain.models.AccountEntity;
import com.cac.homebankingfinalcac.domain.models.UserEntity;
import com.cac.homebankingfinalcac.infrastructure.repositories.AccountRepository;
import com.cac.homebankingfinalcac.infrastructure.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public AccountService() {
    }

    //GET ACCOUNTS:
    @Transactional
    public List<AccountDTO> getAccounts() throws PersonalizedException{
        try {
            List<AccountEntity> accountEntity = accountRepository.findAll();
            if(accountEntity.isEmpty()){
                LOGGER.error("Error al obtener las cuentas. Lista vacía");
                throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener las cuentas. Lista vacía.");
            }
            return accountEntity.stream()
                    .map(AccountMapper::accountToDtoMap)
                    .collect(Collectors.toList());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al obtener las cuentas");
        }
    }

    //GET ACCOUNTS BY ID:
    @Transactional
    public AccountDTO getAccountById(Long id) throws PersonalizedException{
        try {
            AccountEntity accountEntity = accountRepository.findById(id).orElse(null); // O: .findById(id).get()
            AccountDTO accountDto = AccountMapper.accountToDtoMap(accountEntity);
            return accountDto;
        }catch (NullPointerException e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.NOT_FOUND, "La cuenta con ID " + id + " no existe");
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al obtener la cuenta con ID: " + id);
        }
    }

    //POST ACCOUNT:
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDto) throws PersonalizedException {
        try {
            /*if(accountRepository.existsByAccountNumber(accountDto.getAccountNumber()).intValue() > 0){
                LOGGER.error("La cuenta con número de cuenta: " + accountDto.getAccountNumber() + " ya existe.");
                throw new PersonalizedException(HttpStatus.BAD_REQUEST, "Ya hay una cuenta con ese número de cuenta");
            }*/

            //Obtengo el id del UserDTO owner q viene en el accountDto
            Optional<UserEntity> userEntity = userRepository.findById(accountDto.getOwner().getId());
            //Mapeo el accountDto q me vino a una accountEntity(accountModel)
            AccountEntity accountModel = AccountMapper.dtoToAccountMap(accountDto);
            //Le seteo el user owner completo a la entidad accountEntity(accountModel)
            accountModel.setOwner(userEntity.get());
            //Guardo esa accountEntity(accountModel) al repo
            LOGGER.info("Cuenta creada con éxito en la BD");
            accountModel = accountRepository.save(accountModel);
            //Y finalmente la convierto/mapeo en dto para mostrarse como resultado
            return AccountMapper.accountToDtoMap(accountModel);

        }catch (ClassCastException e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "No se puede crear la cuenta");

        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());

        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al crear la cuenta");
        }
    }

    //PUT ACCOUNT:
    //TODO: CQRS
    @Transactional
    public AccountDTO updateAccount(Long id, AccountDTO accountDto){
        try {
            Optional<AccountEntity> accountFound = accountRepository.findById(id);
            if(accountFound.isPresent()){
                AccountEntity accountEntity = accountFound.get();
                if(accountDto.getAmount() != null){
                    accountEntity.setBalance(accountDto.getAmount());
                }
                if (accountDto.getOwner() != null){
                    UserEntity userEntity = userRepository.getReferenceById(accountDto.getOwner().getId());
                    if (userEntity != null){
                        accountEntity.setOwner(userEntity);
                    }
                }
                LOGGER.info("Cuenta actualizada con éxito en la BD");
                AccountEntity accountSaved = accountRepository.save(accountEntity);

                return AccountMapper.accountToDtoMap(accountSaved);

            }else{
                LOGGER.error("No se encontró la cuenta con ID: " + id);
                throw new PersonalizedException(HttpStatus.NOT_FOUND, "La cuenta que intenta actualizar no existe");
            }
        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al actualizar la cuenta");
        }

    }

    //DELETE ACCOUNT:
    @Transactional
    public boolean deleteAccountById(Long id) {
        try {
            if(accountRepository.existsById(id)){
                accountRepository.deleteById(id);
                return true;
            }else{
                LOGGER.error("Cuenta no encontrada con ID: " + id);
                throw new PersonalizedException(HttpStatus.NOT_FOUND, "La cuenta que intenta eliminar no existe");
            }
        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al eliminar la cuenta");
        }

    }

}