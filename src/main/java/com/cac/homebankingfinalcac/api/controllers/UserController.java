package com.cac.homebankingfinalcac.api.controllers;

import com.cac.homebankingfinalcac.api.dtos.UserDTO;
import com.cac.homebankingfinalcac.application.exceptions.PersonalizedException;
import com.cac.homebankingfinalcac.application.services.UserService;
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
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    public UserController() {
    }

    //GET USERS - Lista de todos los usuarios
    @GetMapping(value = "/users")
    public ResponseEntity<ResponseDTO> getUsers() {
        try {
            List<UserDTO> listUsers = userService.getUsers();

            ResponseDTO respOk = new ResponseDTO(true, listUsers, HttpStatus.OK.value(), "Usuarios devueltos con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);

        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }

    }

    //GET USER - Devuelve un usuario:
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id);
            ResponseDTO respOk = new ResponseDTO(true, user, HttpStatus.OK.value(), "Usuario devuelto con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }
    }

    //POST - Crear un Usuario:
    @PostMapping(value = "/users")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserDTO userDto) {
        try {
            if(!Utils.isNullOrEmpty(userDto.getUsername(), userDto.getPassword())){
                LOGGER.error("El username y/o la password son requeridos");
                throw new PersonalizedException(HttpStatus.BAD_REQUEST, "El username y/o la password son requeridos");
            }
            if(!Utils.isUsernameValid(userDto.getUsername()) || !Utils.isPassValid(userDto.getPassword())){
                LOGGER.error("El username y/o la password deben contener un formato válido");
                throw new PersonalizedException(HttpStatus.BAD_REQUEST, "Parámetros con formato inválido.");
            }
            UserDTO userCreated = userService.createUser(userDto);
            ResponseDTO respOk = new ResponseDTO(true, userCreated, HttpStatus.CREATED.value(), "Usuario creado con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.CREATED);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }

    }

    //PUT - Actualizar un Usuario
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDto){
        try {
            UserDTO userUpdated = userService.updateUser(id, userDto);
            ResponseDTO respOk = new ResponseDTO(true, userUpdated, HttpStatus.OK.value(), "Usuario actualizado con éxito");
            return new ResponseEntity<>(respOk, HttpStatus.OK);
        }catch (PersonalizedException pe){
            ResponseDTO respFail = new ResponseDTO(false, null, pe.getStatus().value(), pe.getMessage());
            return new ResponseEntity<>(respFail, pe.getStatus());
        }

    }

    //DELETE - Eliminar un Usuario:
    @DeleteMapping("/usersDel/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id){
        try {
            if(userService.deleteUserById(id)){
                return ResponseEntity.status(HttpStatus.OK).body("El usuario se ha eliminado correctamente");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario no se pudo eliminar porque no existe");
            }
        }catch (PersonalizedException pe){
            return ResponseEntity.status(pe.getStatus()).body(pe.getMessage());
        }
    }

}