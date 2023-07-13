package com.cac.homebankingfinalcac.infrastructure.repositories;

import com.cac.homebankingfinalcac.domain.models.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    //Consulta nativa para validar si el accountNumber de la cuenta ya existe en la BD:
    @Query(value = "select count(*) > 0 from accounts where account_number = :account_number", nativeQuery = true)
    Long existsByAccountNumber(@Param("account_number") int account_number);

}