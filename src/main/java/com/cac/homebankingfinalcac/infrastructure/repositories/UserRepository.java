package com.cac.homebankingfinalcac.infrastructure.repositories;

import com.cac.homebankingfinalcac.domain.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //Consulta nativa para validar si el username del usuario ya existe en la BD:
    @Query(value = "select count(*) > 0 from users where username = :username", nativeQuery = true)
    Long existsByUsername(@Param("username") String username);

}