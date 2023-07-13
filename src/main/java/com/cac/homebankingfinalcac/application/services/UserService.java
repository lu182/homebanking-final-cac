package com.cac.homebankingfinalcac.application.services;

import com.cac.homebankingfinalcac.api.dtos.UserDTO;
import com.cac.homebankingfinalcac.api.mappers.UserMapper;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    public UserService() {
    }

    //GET USERS:
    public List<UserDTO> getUsers() throws PersonalizedException {
        try {
            List<UserEntity> usersEntitie = userRepository.findAll();
            if(usersEntitie.isEmpty()){
                LOGGER.error("Error al obtener los usuarios. Lista vacía");
                throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener los usuarios. Lista vacía.");
            }
            List<UserDTO> usersDto = usersEntitie.stream()
                    .map(UserMapper::userToDtoMap)
                    .collect(Collectors.toList());

            return usersDto;

        } catch (Exception e) {
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al obtener los usuarios");
        }
    }

    //GET USERS BY ID:
    public UserDTO getUserById(Long id) throws PersonalizedException {
        try {
            UserEntity userEntity = userRepository.findById(id).orElse(null);

            UserDTO userDto = UserMapper.userToDtoMap(userEntity);
            return userDto;

        }catch (NullPointerException e){
            //Logger para consola
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.NOT_FOUND, "El usuario con ID " + id + " no existe");
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al obtener el usuario con ID: " + id);
        }
    }

    //POST:
    public UserDTO createUser(UserDTO userDto) throws PersonalizedException{
        try {
            if (userRepository.existsByUsername(userDto.getUsername()).intValue() > 0){
                LOGGER.error("El usuario con username: " + userDto.getUsername() + " ya existe.");
                throw new PersonalizedException(HttpStatus.BAD_REQUEST, "Ya hay un usuario con ese username");
            }
            LOGGER.info("Usuario creado con éxito en la BD");
            return UserMapper.userToDtoMap(userRepository.save(UserMapper.dtoToUserMap(userDto)));

        }catch (ClassCastException e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "No se puede crear el usuario");
        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al crear el usuario");
        }

    }

    //PUT:
    //TODO: CQRS
    public UserDTO updateUser(Long id, UserDTO userDto) {
        try {
            Optional<UserEntity> userFound = userRepository.findById(id);

            if (userFound.isPresent()) {
                UserEntity userEntity = userFound.get();

                UserEntity userUpdated = UserMapper.dtoToUserMap(userDto);

                userUpdated.setUserAccounts(userEntity.getUserAccounts());

                if (userDto.getAccountsId() != null){
                    List<AccountEntity> accountsIdsEntityList = accountRepository.findAllById(userDto.getAccountsId());
                    List<AccountEntity> accountsIdsEntityListFiltered =
                            accountsIdsEntityList.stream()
                                    .filter(e-> !userEntity.getUserAccounts().contains(e))
                                    .collect(Collectors.toList());
                    userUpdated.getUserAccounts().addAll(accountsIdsEntityListFiltered);
                    userUpdated.setUserAccounts(accountsIdsEntityList);
                }
                userUpdated.setIdUser(userEntity.getIdUser());

                LOGGER.info("Usuario actualizado con éxito en la BD");
                UserEntity userSaved = userRepository.save(userUpdated);

                return UserMapper.userToDtoMap(userSaved);

            }else{
                LOGGER.error("Usuario no encontrado con ID: " + id);
                throw new PersonalizedException(HttpStatus.NOT_FOUND, "El usuario que intenta actualizar no existe");
            }
        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al actualizar el usuario");
        }

    }

    //DELETE:
    public boolean deleteUserById(Long id) {
        try {
            if(userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return true;
            }else{
                LOGGER.error("Usuario no encontrado con ID: " + id);
                throw new PersonalizedException(HttpStatus.NOT_FOUND, "El usuario que intenta eliminar no existe");
            }
        }catch (PersonalizedException pe){
            LOGGER.error("Clase: {} -> Mensaje: {}", pe.getClass().getCanonicalName(), pe.getMessage());
            pe.printStackTrace();
            throw new PersonalizedException(pe.getStatus(), pe.getMessage());
        }catch (Exception e){
            LOGGER.error("Clase: {} -> Mensaje: {}", e.getClass().getCanonicalName(), e.getMessage());
            e.printStackTrace();
            throw new PersonalizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Error general al elminar el usuario");
        }
    }

}