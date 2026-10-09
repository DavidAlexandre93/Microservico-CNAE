package com.porto.testecnae.adapters.out.repository.dao;

import com.porto.testecnae.adapters.out.repository.SpringDataCadastroSecundarioRepository;
import com.porto.testecnae.adapters.out.repository.entity.CadastroSecundarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CadastroSecundarioDaoImpl implements CadastroSecundarioDao {

    private final SpringDataCadastroSecundarioRepository repository;

    @Override
    public CadastroSecundarioEntity salvar(CadastroSecundarioEntity cadastro) {
        return repository.save(cadastro);
    }

    @Override
    public List<CadastroSecundarioEntity> buscarTodosComCnae() {
        return repository.buscarTodosComCnae();
    }
}
