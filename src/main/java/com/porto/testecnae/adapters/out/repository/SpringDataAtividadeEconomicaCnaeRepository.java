package com.porto.testecnae.adapters.out.repository;

import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataAtividadeEconomicaCnaeRepository
        extends JpaRepository<AtividadeEconomicaCnaeEntity, Long> {

    Optional<AtividadeEconomicaCnaeEntity> findByCodigo(String codigo);

    List<AtividadeEconomicaCnaeEntity> findByDescricaoContainingIgnoreCaseOrderByCodigo(String termo);
}
