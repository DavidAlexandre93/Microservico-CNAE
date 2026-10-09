package com.porto.testecnae.adapters.out.repository;

import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataCadastroSecundarioRepository extends JpaRepository<CadastroSecundarioEntity, Long> {

    @Query("select cadastro from CadastroSecundarioEntity cadastro join fetch cadastro.cnae")
    List<CadastroSecundarioEntity> buscarTodosComCnae();
}
