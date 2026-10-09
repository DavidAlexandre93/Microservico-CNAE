package com.porto.testecnae.unit.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(
        packages = "com.porto.testecnae",
        importOptions = ImportOption.DoNotIncludeTests.class
)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule APPLICATION_NAO_DEPENDE_DE_ADAPTERS_OU_FRAMEWORKS = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "..adapters..",
                    "..config..",
                    "org.springframework..",
                    "jakarta.persistence..",
                    "lombok.."
            );

    @ArchTest
    static final ArchRule ADAPTERS_DE_ENTRADA_NAO_DEPENDEM_DE_SAIDA = noClasses()
            .that().resideInAPackage("..adapters.in..")
            .should().dependOnClassesThat().resideInAPackage("..adapters.out..");

    @ArchTest
    static final ArchRule ADAPTERS_DE_SAIDA_NAO_DEPENDEM_DE_ENTRADA = noClasses()
            .that().resideInAPackage("..adapters.out..")
            .should().dependOnClassesThat().resideInAPackage("..adapters.in..");

    @ArchTest
    static final ArchRule DOMINIO_PERMANECE_ISOLADO = classes()
            .that().resideInAPackage("..application.core.domain..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                    "java..",
                    "..application.core.domain.."
            );

    @ArchTest
    static final ArchRule MODULOS_NAO_POSSUEM_CICLOS = slices()
            .matching("com.porto.testecnae.(*)..")
            .should().beFreeOfCycles();
}
