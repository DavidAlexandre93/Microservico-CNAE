package com.porto.testecnae.adapters.out.repository.dao;

import com.porto.testecnae.adapters.out.repository.SpringDataCadastroSecundarioRepository;
import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Implementacao do DAO de cadastros secundarios com Spring Data JPA. */
@Repository
@RequiredArgsConstructor
public class CadastroSecundarioDaoImpl implements CadastroSecundarioDao {

    private final SpringDataCadastroSecundarioRepository repository;

    /**
     * Persiste a entidade e retorna a instancia gerenciada pelo repositorio.
     *
     * @param cadastro entidade de cadastro a persistir
     * @return entidade persistida
     */
    @Override
    public CadastroSecundarioEntity salvar(CadastroSecundarioEntity cadastro) {
        return repository.save(cadastro);
    }

    /**
     * Recupera os cadastros com a associacao CNAE carregada pela consulta.
     *
     * @return entidades de cadastro com seus CNAEs
     */
    @Override
    public List<CadastroSecundarioEntity> buscarTodosComCnae() {
        return repository.buscarTodosComCnae();
    }
}
