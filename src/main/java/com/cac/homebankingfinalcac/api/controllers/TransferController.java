package com.cac.homebankingfinalcac.api.controllers;

import com.cac.homebankingfinalcac.api.dtos.TransferDTO;
import com.cac.homebankingfinalcac.application.exceptions.PersonalizedException;
import com.cac.homebankingfinalcac.application.services.TransferService;
import com.cac.homebankingfinalcac.infrastructure.utils.responsegeneric.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    private TransferService transferService;

    public TransferController() {
    }

    //GET TRANSFERS - Lista de todas las transferencias
    @GetMapping(value = "/transfers")
    public ResponseEntity<ResponseDTO> getTransfers(){
        try {
            List<TransferDTO> listTransfers = transferService.getTransfers();
            ResponseDTO respOk = new ResponseDTO(true, listTransfers, HttpStatus.OK.value(),
                    "Transferencias devueltas con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //GET TRANSFER BY ID:
    @GetMapping(value = "/transfers/{id}")
    public ResponseEntity<ResponseDTO> getTransferById(@PathVariable Long id){
        try {
            TransferDTO transfer = transferService.getTransferById(id);
            ResponseDTO respOk = new ResponseDTO(true, transfer, HttpStatus.OK.value(),
                    "Transferencia devuelta con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //POST - Crear una transferencia:
    @PostMapping(value = "/transfers")
    public ResponseEntity<ResponseDTO> createTransfer(@RequestBody TransferDTO transferDto){
        try {
            TransferDTO transferCreated = transferService.performTransfer(transferDto);
            ResponseDTO respOk = new ResponseDTO(true, transferCreated, HttpStatus.CREATED.value(),
                    "Transferencia creada con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.CREATED);
        }catch (PersonalizedException pe) {
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //PUT - Actualizar un transferencia:
    @PutMapping(value = "/transfers/{id}")
    public ResponseEntity<ResponseDTO> updateTransfer(@PathVariable Long id, @RequestBody TransferDTO transferDto){
        try {
            TransferDTO transferUpdated = transferService.updateTransfer(id, transferDto);
            ResponseDTO respOk = new ResponseDTO(true, transferUpdated, HttpStatus.OK.value(), "Transferencia actualizada con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //DELETE - Eliminar una Transferencia:
    @DeleteMapping(value = "/transfers/{id}")
    public ResponseEntity<String> deleteTransfer(@PathVariable Long id){
        try{
            if(transferService.deleteTransfer(id)){
                return ResponseEntity.status(HttpStatus.OK).body("La transferencia se ha eliminado correctamente");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La transferencia no se pudo eliminar porque no existe");
            }
        }catch (PersonalizedException pe){
            return ResponseEntity.status(pe.getStatus()).body(pe.getMessage());
        }
    }

}