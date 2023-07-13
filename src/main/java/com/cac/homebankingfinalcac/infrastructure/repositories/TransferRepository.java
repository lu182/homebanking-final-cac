package com.cac.homebankingfinalcac.infrastructure.repositories;

import com.cac.homebankingfinalcac.domain.models.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, Long> {

}
