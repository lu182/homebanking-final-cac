package com.cac.homebankingfinalcac.application.services;

import com.cac.homebankingfinalcac.api.dtos.TransferDTO;
import com.cac.homebankingfinalcac.api.mappers.TransferMapper;
import com.cac.homebankingfinalcac.application.exceptions.PersonalizedException;
import com.cac.homebankingfinalcac.domain.models.AccountEntity;
import com.cac.homebankingfinalcac.domain.models.TransferEntity;
import com.cac.homebankingfinalcac.infrastructure.repositories.AccountRepository;
import com.cac.homebankingfinalcac.infrastructure.repositories.TransferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    public TransferService() {

    }

    //GET ALL TRANSFERS:
    public List<TransferDTO> getTransfers() throws PersonalizedException {
        try {
            List<TransferEntity> transfers = transferRepository.findAll();
            if(transfers.isEmpty()){
                LOGGER.error("Error al obtener las transferencias. Lista vacía");
                throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener las transferencias. Lista vacía.");
            }
            return transfers.stream()
                    .map(TransferMapper::transferToDto)
                    .collect(Collectors.toList());

        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al obtener las transferencias");
        }
    }

    //GET TRANSFER BY ID:
    public TransferDTO getTransferById(Long id) {
        try {
            TransferEntity transfer = transferRepository.findById(id).orElseThrow(() ->
                    new PersonalizedException(HttpStatus.NOT_FOUND, "La transferencia con ID: " + id + " no existe"));
            return TransferMapper.transferToDto(transfer);

        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al obtener la transferencia con ID: " + id);
        }
    }

    //CREATE TRANSFER:
    @Transactional
    public TransferDTO performTransfer(TransferDTO transferDto){
        //1- Comprobar si las cuentas de origen y destino existen:
        AccountEntity originAccount = accountRepository.findById(transferDto.getOriginAccount()).orElseThrow(() ->
                new PersonalizedException(HttpStatus.NOT_FOUND, "La cuenta de orígen con id: " + transferDto.getOriginAccount() + " no existe"));
        AccountEntity destinationAccount = accountRepository.findById(transferDto.getTargetAccount()).orElseThrow(() ->
                new PersonalizedException(HttpStatus.NOT_FOUND, "La cuenta de destino con id: " + transferDto.getTargetAccount() + " no existe"));

        //2- Comprobar si la cuenta de origen tiene fondos suficientes:
        //obtenemos el saldo actual de la cuenta y lo comparamos con el amount(monto) de la transferencia.
        //Si ese valor da menor q 0, arroja exception.
        if(originAccount.getBalance().compareTo(transferDto.getAmountTransfer()) < 0){
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Fondos insuficientes para realizar transferencia desde cuenta de origen con id: " + transferDto.getOriginAccount());
        }

        //3- Realizar la transferencia:
        //A la cuenta de origin y destino le modificamos el balance(saldo) q tienen actualmente.
        originAccount.setBalance(originAccount.getBalance().subtract(transferDto.getAmountTransfer())); //A una le sustraemos
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferDto.getAmountTransfer())); //y a la otra le agregamos

        //4- Guardar las cuentas (entities) actualizadas, en el repo:
        accountRepository.save(originAccount);
        accountRepository.save(destinationAccount);

        //5- Crear la transferencia y guardarla en la base de datos:
        TransferEntity transferEntity = new TransferEntity();
        LocalDate date = LocalDate.now(); //Creamos un objeto del tipo LocalDate para obtener la fecha actual
        transferEntity.setTransferDate(date); //Seteamos el objeto fecha actual en el transferDto
        transferEntity.setOriginAccount(originAccount.getIdAccount());
        transferEntity.setTargetAccount(destinationAccount.getIdAccount());
        transferEntity.setAmountTransfer(transferDto.getAmountTransfer());
        LOGGER.info("Transferencia creada con éxito en la BD");
        transferEntity = transferRepository.save(transferEntity);

        //6- Devolver el DTO de la transferencia realizada (la mapeamos como dto y la devolvemos):
        return TransferMapper.transferToDto(transferEntity);
    }

    //UPDATE TRANSFER:
    public TransferDTO updateTransfer(Long id, TransferDTO transferDto){
        try {
            TransferEntity transferFound = transferRepository.findById(id).orElseThrow(() ->
                    new PersonalizedException(HttpStatus.NOT_FOUND, "La transferencia con ID:" + id + " no existe"));

            TransferEntity transferUpdated = TransferMapper.dtoToTransfer(transferDto);

            transferUpdated.setTransferDate(LocalDate.now()); //seteo fecha actual

            transferUpdated.setIdTransfer(transferFound.getIdTransfer());

            LOGGER.info("Transferencia actualizada con éxito en la BD");
            return TransferMapper.transferToDto(transferRepository.save(transferUpdated));

        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al actualizar la transferencia");
        }
    }

    //DELETE TRANSFER:
    public boolean deleteTransfer(Long id){
        try {
            if(transferRepository.existsById(id)){
                transferRepository.deleteById(id);
                return true;
            }else{
                LOGGER.error("Transferencia no encontrada con ID: " + id);
                throw new PersonalizedException(HttpStatus.NOT_FOUND, "La transferencia que intenta eliminar no existe");
            }
        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al eliminar la transferencia");
        }
    }

}