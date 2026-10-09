package com.porto.testecnae.adapters.out.repository.dao;

import com.porto.testecnae.adapters.out.repository.SpringDataAtividadeEconomicaCnaeRepository;
import com.porto.testecnae.adapters.out.repository.entity.AtividadeEconomicaCnaeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AtividadeEconomicaCnaeDaoImpl implements AtividadeEconomicaCnaeDao {

    private final SpringDataAtividadeEconomicaCnaeRepository repository;

    @Override
    public List<AtividadeEconomicaCnaeEntity> buscarTodas() {
        return repository.findAll();
    }

    @Override
    public List<AtividadeEconomicaCnaeEntity> buscarPorDescricao(String termo) {
        return repository.findByDescricaoContainingIgnoreCaseOrderByCodigo(termo);
    }

    @Override
    public Optional<AtividadeEconomicaCnaeEntity> buscarPorCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    @Override
    public AtividadeEconomicaCnaeEntity buscarPorId(Long id) {
        return repository.getReferenceById(id);
    }
}
