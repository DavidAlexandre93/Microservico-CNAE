package com.porto.testecnae.adapters.out.repository;

import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/** Repositorio Spring Data para operacoes JPA sobre cadastros secundarios. */
public interface SpringDataCadastroSecundarioRepository extends JpaRepository<CadastroSecundarioEntity, Long> {

    /**
     * Busca cadastros e carrega a associacao CNAE na mesma consulta.
     *
     * @return entidades de cadastro com seus CNAEs carregados
     */
    @Query("select cadastro from CadastroSecundarioEntity cadastro join fetch cadastro.cnae")
    List<CadastroSecundarioEntity> buscarTodosComCnae();
}
