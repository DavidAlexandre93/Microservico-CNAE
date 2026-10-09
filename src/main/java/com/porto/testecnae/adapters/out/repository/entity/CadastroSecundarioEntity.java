package com.porto.testecnae.adapters.out.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Entidade JPA de cadastro secundario com associacao obrigatoria a um CNAE. */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cadastro_secundario")
@Entity
public class CadastroSecundarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nomeFantasia;

    @Column(nullable = false, length = 14)
    private String documento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cnae_id", nullable = false)
    private AtividadeEconomicaCnaeEntity cnae;
}
