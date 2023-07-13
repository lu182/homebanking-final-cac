package com.cac.homebankingfinalcac.api.controllers;

import com.cac.homebankingfinalcac.api.dtos.AccountDTO;
import com.cac.homebankingfinalcac.application.exceptions.PersonalizedException;
import com.cac.homebankingfinalcac.application.services.AccountService;
import com.cac.homebankingfinalcac.infrastructure.utils.responsegeneric.ResponseDTO;
import com.cac.homebankingfinalcac.infrastructure.utils.validations.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    public AccountController() {
    }

    //GET ACCOUNTS - Lista de todas las cuentas
    @GetMapping(value = "/accounts")
    public ResponseEntity<ResponseDTO> getAccounts(){
        try {
            List<AccountDTO> listAccounts = accountService.getAccounts();
            ResponseDTO respOk = new ResponseDTO(true, listAccounts, HttpStatus.OK.value(), "Cuentas devueltas con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //GET ACCOUNTS BY ID - Devuelve una sola Cuenta:
    @GetMapping(value = "/accounts/{id}")
    public ResponseEntity<ResponseDTO> getAccountById(@PathVariable Long id){
        try {
            AccountDTO account = accountService.getAccountById(id);
            ResponseDTO respOk = new ResponseDTO(true, account, HttpStatus.OK.value(), "Cuenta devuelta con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //POST - Crear una Cuenta:
    @PostMapping(value = "/accounts")
    public ResponseEntity<ResponseDTO> createAccount(@RequestBody AccountDTO accountDto){
        try {
            if(!Utils.isParametersAccountValid(accountDto.getAmount())){
                LOGGER.error("Parámetro amount con formato inválido o es requerido");
                throw new PersonalizedException(HttpStatus.BAD_REQUEST, "Parámetro amount con formato inválido o es requerido");
            }else{
                AccountDTO accountCreated = accountService.createAccount(accountDto);
                ResponseDTO respOk = new ResponseDTO(true, accountCreated, HttpStatus.CREATED.value(), "Cuenta creada con éxito");
                return new ResponseEntity<>(respOk, HttpStatus.CREATED);
            }
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //PUT - Actualizar una Cuenta
    @PutMapping(value = "/accounts/{id}")
    public ResponseEntity<ResponseDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO accountDto){
        try {
            AccountDTO accountUpdated = accountService.updateAccount(id, accountDto);
            ResponseDTO respOk = new ResponseDTO(true, accountUpdated, HttpStatus.OK.value(), "Cuenta actualizada con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //DELETE - Eliminar una Cuenta:
    @DeleteMapping("/accountsDel/{id}")
    public ResponseEntity<String> deleteAccountById(@PathVariable Long id){
        try {
            if(accountService.deleteAccountById(id)) {
                return ResponseEntity.status(HttpStatus.OK).body("La cuenta se ha eliminado correctamente");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La cuenta no se pudo eliminar porque no existe");
            }
        }catch (PersonalizedException pe){
            return ResponseEntity.status(pe.getStatus()).body(pe.getMessage());
        }

    }

}