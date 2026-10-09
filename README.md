# Teste Tecnico - Microservico CNAE

Projeto Spring Boot criado para avaliacao de candidatos a vagas de Lider Tecnico e Analista.

## Stack

- Java 25
- Spring Boot 4.1.0
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Maven
- OpenAPI 3.0.3
- OpenAPI Generator 7.26.0
- Springdoc OpenAPI 3.1.1
- JUnit 6 e Mockito
- ArchUnit 1.5.1
- JaCoCo 0.8.15

## Arquitetura hexagonal

O projeto separa regras de negocio de detalhes de framework e infraestrutura:

```text
src/main/java/com/porto/testecnae
|-- adapters
|   |-- in/controller       # Controllers, mapeadores HTTP e tratamento de erros
|   `-- out
|       |-- repository      # Spring Data JPA e entidades de persistencia
|       `-- *Adapter.java   # Implementacoes das portas de saida
|-- application
|   |-- core
|   |   |-- domain          # Modelos de dominio sem dependencia de framework
|   |   |-- exception       # Excecoes da aplicacao
|   |   `-- usecase         # Regras e orquestracao dos casos de uso
|   `-- ports
|       |-- in              # Contratos expostos aos adaptadores de entrada
|       `-- out             # Contratos exigidos da infraestrutura
|-- config                  # Composicao dos casos de uso como beans
`-- TesteCnaeApplication.java
```

O fluxo de dependencias aponta para o centro da aplicacao:

```text
HTTP -> porta de entrada -> caso de uso -> porta de saida -> JPA/H2
```

As regras de arquitetura sao executaveis: o ArchUnit impede que a camada de aplicacao
dependa de Spring, JPA, configuracao ou adapters, bloqueia dependencias entre adapters de
entrada e saida e detecta ciclos entre os modulos.

## Principios aplicados

- **SOLID:** controllers tratam HTTP, casos de uso concentram a orquestracao, portas definem
  dependencias e adapters implementam detalhes externos. As dependencias sao recebidas por
  construtor e apontam para interfaces da aplicacao.
- **DRY:** o contrato OpenAPI gera uma unica vez interfaces, modelos HTTP, validacoes e
  documentacao; os mapeamentos repetidos ficam centralizados no `CnaeApiMapper`.
- **KISS:** cada caso de uso possui somente o fluxo exigido pelo desafio e o H2 e inicializado
  por um unico `data.sql`.
- **YAGNI:** nao foram adicionados cache, mensageria, autenticacao, paginacao ou validacao de
  CNPJ, pois esses requisitos nao fazem parte do comportamento solicitado.
- **Clean Code:** nomes expressam a intencao, entidades JPA nao vazam para o dominio, erros
  HTTP seguem um modelo unico e testes sao organizados pelo componente que protegem.

## API-first com OpenAPI

O contrato [cnae-api.yaml](src/main/openapi/cnae-api.yaml) e a fonte de verdade da API HTTP.
Durante a fase `generate-sources`, o OpenAPI Generator cria as interfaces Spring e os modelos
de transporte em `target/generated-sources/openapi`. Os controllers implementam as interfaces
geradas e convertem os modelos HTTP para as portas da aplicacao.

```text
cnae-api.yaml
    -> CnaesApi / CadastrosSecundariosApi (gerados)
    -> controllers (implementacao manual)
    -> portas de entrada
    -> casos de uso
```

Para validar o contrato e gerar novamente as interfaces:

```powershell
mvn clean generate-sources
```

Os arquivos dentro de `target/generated-sources` nao devem ser editados manualmente.

## Preparacao e execucao local

Os comandos desta secao consideram Windows com PowerShell. O projeto nao possui Maven
Wrapper, portanto Java e Maven precisam estar instalados na maquina.

### 1. Acessar o projeto

```powershell
Set-Location "C:\Users\Pichau\Desktop\Desafio Porto\Microservico-CNAE"
```

### 2. Verificar os pre-requisitos

```powershell
java -version
mvn -version
```

Versoes esperadas:

- Java 25
- Maven 3.9 ou superior

Os dois comandos devem reconhecer o mesmo Java 25. Caso `java` ou `mvn` nao seja
reconhecido, instale a ferramenta correspondente e verifique as variaveis `JAVA_HOME`
e `PATH` antes de continuar.

### 3. Baixar as dependencias

Para baixar antecipadamente as dependencias e plugins utilizados pelo build:

```powershell
mvn dependency:go-offline
```

Esse passo e opcional. Qualquer comando Maven posterior tambem baixa automaticamente o
que estiver faltando.

### 4. Gerar e verificar a API OpenAPI

O contrato versionado fica em `src/main/openapi/cnae-api.yaml`. Para limpar geracoes
anteriores, validar o contrato e executar o codegen:

```powershell
mvn clean generate-sources
```

Para conferir os arquivos gerados:

```powershell
Get-ChildItem `
  "target\generated-sources\openapi\src\main\java\com\porto\testecnae\adapters\in\api" `
  -Recurse
```

O resultado deve incluir:

```text
CnaesApi.java
CadastrosSecundariosApi.java
model/ApiErrorResponse.java
model/AtividadeEconomicaCnaeResponse.java
model/CadastroSecundarioRequest.java
model/CadastroSecundarioResponse.java
```

As interfaces geradas contem os mapeamentos HTTP e as validacoes definidas no contrato.
Os controllers em `adapters/in/controller` implementam essas interfaces e fazem a
conversao para as portas da aplicacao.

### 5. Executar build e testes

O comando recomendado para validar toda a aplicacao e:

```powershell
mvn clean verify
```

Ele executa, nesta ordem:

1. Limpeza do diretorio `target`.
2. Validacao do arquivo OpenAPI.
3. Geracao das interfaces e modelos HTTP.
4. Compilacao da aplicacao.
5. Testes unitarios das regras dos casos de uso e do fallback HTTP 500.
6. Testes integrados com servidor HTTP real, OpenAPI, controllers, adapters JPA e H2 em memoria.
7. Regras de arquitetura hexagonal com ArchUnit, mantidas no grupo rapido `unit`.
8. Empacotamento do JAR, relatorio JaCoCo e verificacao de cobertura minima de 85% das linhas.

Resultado esperado:

```text
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
All coverage checks have been met.
BUILD SUCCESS
```

O relatorio navegavel de cobertura e criado em:

```text
target/site/jacoco/index.html
```

O workflow `.github/workflows/ci.yml` executa o mesmo comando em Java 25 para toda
Pull Request direcionada a `main`.

Para executar apenas os testes sem empacotar nem gerar o relatorio final:

```powershell
mvn test
```

### 6. Subir a aplicacao

```powershell
mvn spring-boot:run
```

A aplicacao sera iniciada em `http://localhost:8080`.

Se a porta `8080` ja estiver ocupada, utilize outra porta sem alterar o projeto:

```powershell
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18080"
```

Nesse caso, substitua `8080` por `18080` nas URLs dos exemplos.

### 7. Verificar OpenAPI e Swagger UI

Com a aplicacao em execucao, acesse:

```text
Contrato OpenAPI: http://localhost:8080/openapi/cnae-api.yaml
Swagger UI:       http://localhost:8080/swagger-ui.html
```

A Swagger UI esta configurada para carregar diretamente o contrato versionado em
`src/main/openapi/cnae-api.yaml`, mantendo o fluxo API-first.

### 8. Acessar o H2

```text
Console:  http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:cnaedb
User:     sa
Password: deixar vazio
```

## Endpoints

```http
GET /api/cnaes
GET /api/cnaes/buscar?termo=programas
GET /api/cnaes/codigo?codigo=6201-5/01
GET /api/cadastros-secundarios
GET /api/cadastros-secundarios/validar-cnae?codigoCnae=6201-5/01
POST /api/cadastros-secundarios
```

Comportamento esperado:

- `GET /api/cnaes` deve retornar todas as atividades cadastradas.
- `GET /api/cnaes/buscar?termo={texto}` deve buscar CNAEs que contenham o texto informado em qualquer parte da descricao, ignorando maiusculas e minusculas.
- `GET /api/cnaes/codigo?codigo={codigo}` deve retornar o CNAE do codigo informado.
- Codigos CNAE inexistentes devem retornar uma resposta HTTP adequada para recurso nao encontrado.
- `POST /api/cadastros-secundarios` deve criar um cadastro vinculado a um CNAE existente.
- `GET /api/cadastros-secundarios/validar-cnae?codigoCnae={codigo}` deve validar se o CNAE informado pode ser usado no cadastro.
- Cadastros secundários com CNAE inexistente nao devem ser criados.

Exemplos no PowerShell:

```powershell
curl.exe "http://localhost:8080/api/cnaes"
curl.exe "http://localhost:8080/api/cnaes/buscar?termo=programas"
curl.exe "http://localhost:8080/api/cnaes/codigo?codigo=6201-5%2F01"

curl.exe -X POST "http://localhost:8080/api/cadastros-secundarios" `
  -H "Content-Type: application/json" `
  --data-raw '{"nomeFantasia":"Tech Porto","documento":"12345678000199","codigoCnae":"6201-5/01"}'
```

## Estrategia de testes

Todos os testes ficam exclusivamente nos grupos `unit` e `integration`:

```text
src/test/java/com/porto/testecnae
|-- unit
|   |-- application/core/usecase # Regras de negocio com portas simuladas
|   |-- adapters/in/controller   # Somente o fallback HTTP 500 nao induzivel pela API
|   `-- architecture             # Regras hexagonais rapidas com ArchUnit
`-- integration                  # HTTP + OpenAPI + Spring + controllers + JPA + H2
```

Nao ha testes unitarios para records, getters, mapeamentos triviais, controllers ou simples
delegacoes de repository. Esses componentes sao exercitados de forma mais representativa pelo
teste integrado, evitando testes acoplados a detalhes de implementacao e manutencao duplicada.

A suite cobre:

- fluxos de sucesso e CNAE inexistente;
- todos os seis endpoints;
- busca case-insensitive;
- persistencia e leitura do relacionamento entre cadastro e CNAE;
- parametros ausentes, formatos invalidos, valores em branco e JSON malformado;
- rejeicao de propriedades JSON nao declaradas pelo contrato;
- respostas padronizadas para HTTP 400, 404 e 500;
- publicacao do contrato OpenAPI original e da Swagger UI;
- isolamento da aplicacao em relacao a frameworks e adapters.

## Relatorio tecnico

### Problemas identificados e causa raiz

| Problema | Causa raiz | Correcao aplicada |
| --- | --- | --- |
| Risco de acoplamento entre negocio, HTTP e JPA | A separacao de pacotes, isoladamente, nao impede uma dependencia futura apontar para fora do nucleo | Portas de entrada e saida, composicao em `BeanConfiguration` e regras ArchUnit executadas no build |
| Possivel divergencia entre documentacao e controllers | Endpoints, modelos e validacoes mantidos manualmente em mais de um lugar tendem a evoluir de forma diferente | OpenAPI como fonte de verdade e OpenAPI Generator criando interfaces implementadas pelos controllers |
| Campos contendo apenas espacos eram validos | `minLength: 1` conta espacos como caracteres | Restricao `pattern` no contrato para exigir ao menos um caractere nao branco |
| JSON aceitava propriedades fora do contrato | O comportamento padrao do Jackson ignora propriedades desconhecidas, mesmo com `additionalProperties: false` no OpenAPI | `fail-on-unknown-properties` habilitado e coberto por teste integrado |
| Respostas 500 podiam fugir do modelo documentado | Existiam handlers apenas para validacao e CNAE inexistente | Handler de erro inesperado com log interno e resposta generica sem exposicao de detalhes |
| Suite inicial misturava responsabilidades e cobria poucos cenarios | Um unico teste de contexto nao isolava regras nem protegia os limites arquiteturais | Suite separada somente em `unit` e `integration`, com ArchUnit no grupo rapido e gate JaCoCo |

### Decisoes tecnicas

- O dominio usa `record` sem anotacoes de framework; as entidades JPA permanecem no adapter
  de saida. Isso evita que persistencia dite o modelo central.
- O caso de uso valida o CNAE antes de criar o cadastro. O adapter de persistencia confirma o
  registro ao montar o relacionamento JPA, mantendo integridade mesmo se for chamado por outra
  composicao.
- A consulta de cadastros usa `join fetch` para carregar o CNAE dentro da transacao e evitar
  acesso lazy fora do adapter.
- Validacoes de transporte pertencem ao OpenAPI e sao aplicadas nas interfaces geradas. A regra
  de existencia do CNAE permanece no caso de uso.
- O codigo gerado em `target` nao entra na metrica JaCoCo; ele e validado pela geracao durante o
  build, pela compilacao dos controllers contra as interfaces e pelos testes HTTP integrados.
- A cobertura minima foi fixada em 85% para impedir regressoes sem transformar a metrica em
  objetivo artificial. Na verificacao desta entrega, o codigo autoral atingiu 100% das linhas.

## Evidencias da entrega

Verificacao executada em 09/10/2026 com Java 25 e Maven 3.9.11:

```text
mvn clean verify
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
All coverage checks have been met.
BUILD SUCCESS
```

Validacao local da aplicacao iniciada em uma porta alternativa:

```text
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18080"

GET  /api/cnaes                                            -> 200
GET  /api/cnaes/buscar?termo=programas                     -> 200
GET  /api/cnaes/codigo?codigo=6201-5%2F01                  -> 200
GET  /api/cadastros-secundarios                            -> 200
GET  /api/cadastros-secundarios/validar-cnae?...           -> 200
POST /api/cadastros-secundarios                            -> 201
GET  /api/cnaes/codigo?codigo=0000-0%2F00                  -> 404
```

Exemplo confirmado no POST:

```json
{
  "id": 1,
  "nomeFantasia": "Evidencia Local",
  "documento": "12345678000199",
  "cnae": {
    "id": 5,
    "codigo": "6201-5/01",
    "descricao": "Desenvolvimento de programas de computador sob encomenda",
    "secao": "Tecnologia"
  }
}
```

## Desafio para o candidato

Objetivo:

1. Fazer a aplicacao subir corretamente.
2. Validar os endpoints disponiveis.
3. Identificar e corrigir problemas encontrados durante a execucao.
4. Explicar as causas dos problemas e as decisoes tomadas.
5. Adicionar ou ajustar testes, quando fizer sentido.

## Entrega esperada

- Codigo corrigido em um branch ou pull request.
- Breve explicacao tecnica das alteracoes.
- Evidencias de execucao, como comandos usados, respostas dos endpoints ou testes.
