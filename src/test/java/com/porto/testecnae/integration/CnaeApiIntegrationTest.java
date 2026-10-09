package com.porto.testecnae.integration;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.jpa.show-sql=false",
                "logging.level.org.hibernate.SQL=OFF"
        }
)
class CnaeApiIntegrationTest {

    private static final String CNAE_EXISTENTE = "6201-5/01";
    private static final String CNAE_INEXISTENTE = "0000-0/00";

    @Value("${local.server.port}")
    private int serverPort;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    @Test
    void deveCarregarDadosIniciaisEListarTodosOsCnaes() throws Exception {
        var response = get("/api/cnaes");

        assertJsonResponse(response, 200);
        DocumentContext json = JsonPath.parse(response.body());
        List<String> codigos = json.read("$[*].codigo");
        assertEquals(8, codigos.size());
        assertTrue(codigos.contains(CNAE_EXISTENTE));
        assertTrue(codigos.contains("7311-4/00"));
    }

    @Test
    void deveBuscarCnaesPorDescricaoIgnorandoMaiusculasEMinusculas() throws Exception {
        var response = get("/api/cnaes/buscar?termo=PROGRAMAS");

        assertJsonResponse(response, 200);
        DocumentContext json = JsonPath.parse(response.body());
        List<String> codigos = json.read("$[*].codigo");
        assertEquals(List.of("6201-5/01", "6202-3/00"), codigos);
    }

    @Test
    void deveBuscarCnaePorCodigo() throws Exception {
        var response = get("/api/cnaes/codigo?codigo=6201-5%2F01");

        assertJsonResponse(response, 200);
        var json = JsonPath.parse(response.body());
        assertAll(
                () -> assertEquals(CNAE_EXISTENTE, json.read("$.codigo")),
                () -> assertEquals(
                        "Desenvolvimento de programas de computador sob encomenda",
                        json.read("$.descricao")
                ),
                () -> assertEquals("Tecnologia", json.read("$.secao"))
        );
    }

    @Test
    void deveRetornar404PadronizadoQuandoCnaeNaoExistir() throws Exception {
        var response = get("/api/cnaes/codigo?codigo=0000-0%2F00");

        assertJsonResponse(response, 404);
        assertError(
                response,
                404,
                "Not Found",
                "CNAE nao encontrado para o codigo: " + CNAE_INEXISTENTE
        );
    }

    @ParameterizedTest(name = "requisicao GET invalida: {0}")
    @MethodSource("requisicoesGetInvalidas")
    void deveValidarParametrosGetGeradosPeloContrato(String path) throws Exception {
        var response = get(path);

        assertJsonResponse(response, 400);
        assertError(
                response,
                400,
                "Bad Request",
                "Parametros ou corpo da requisicao invalidos"
        );
    }

    @Test
    void deveValidarCnaeExistenteParaCadastro() throws Exception {
        var response = get("/api/cadastros-secundarios/validar-cnae?codigoCnae=6201-5%2F01");

        assertJsonResponse(response, 200);
        assertEquals(CNAE_EXISTENTE, JsonPath.parse(response.body()).read("$.codigo"));
    }

    @Test
    void deveRetornar404AoValidarCnaeInexistente() throws Exception {
        var response = get("/api/cadastros-secundarios/validar-cnae?codigoCnae=0000-0%2F00");

        assertJsonResponse(response, 404);
        assertError(
                response,
                404,
                "Not Found",
                "CNAE nao encontrado para o codigo: " + CNAE_INEXISTENTE
        );
    }

    @Test
    void deveRetornar404ParaRotaInexistente() throws Exception {
        var response = get("/rota-inexistente");

        assertJsonResponse(response, 404);
        assertError(response, 404, "Not Found", "Recurso nao encontrado");
    }

    @Test
    void deveCadastrarEListarCadastroSecundario() throws Exception {
        var antes = getCadastros();
        var response = post(
                "/api/cadastros-secundarios",
                """
                        {
                          "nomeFantasia": "API First Porto",
                          "documento": "12345678000199",
                          "codigoCnae": "6201-5/01"
                        }
                        """
        );

        assertJsonResponse(response, 201);
        var json = JsonPath.parse(response.body());
        Number id = json.read("$.id");
        assertAll(
                () -> assertTrue(id.longValue() > 0),
                () -> assertEquals("API First Porto", json.read("$.nomeFantasia")),
                () -> assertEquals("12345678000199", json.read("$.documento")),
                () -> assertEquals(CNAE_EXISTENTE, json.read("$.cnae.codigo"))
        );

        var depois = getCadastros();
        assertEquals(antes.size() + 1, depois.size());
        assertTrue(depois.contains("API First Porto"));
    }

    @Test
    void naoDevePersistirCadastroQuandoCnaeNaoExistir() throws Exception {
        var quantidadeAntes = getCadastros().size();
        var response = post(
                "/api/cadastros-secundarios",
                """
                        {
                          "nomeFantasia": "Cadastro invalido",
                          "documento": "12345678000199",
                          "codigoCnae": "0000-0/00"
                        }
                        """
        );

        assertJsonResponse(response, 404);
        assertEquals(quantidadeAntes, getCadastros().size());
        assertError(
                response,
                404,
                "Not Found",
                "CNAE nao encontrado para o codigo: " + CNAE_INEXISTENTE
        );
    }

    @ParameterizedTest(name = "corpo POST invalido #{index}")
    @MethodSource("corposPostInvalidos")
    void deveValidarCorpoPostConformeContrato(String body) throws Exception {
        var response = post("/api/cadastros-secundarios", body);

        assertJsonResponse(response, 400);
        assertError(
                response,
                400,
                "Bad Request",
                "Parametros ou corpo da requisicao invalidos"
        );
    }

    @Test
    void devePublicarContratoOpenApiOriginalESwaggerUi() throws Exception {
        var contratoResponse = get("/openapi/cnae-api.yaml");
        var swaggerResponse = get("/swagger-ui.html");

        assertEquals(200, contratoResponse.statusCode());
        assertTrue(contratoResponse.body().contains("openapi: 3.0.3"));
        assertTrue(contratoResponse.body().contains("/api/cnaes:"));
        assertTrue(contratoResponse.body().contains("/api/cadastros-secundarios:"));
        assertEquals(200, swaggerResponse.statusCode());
        assertTrue(swaggerResponse.body().contains("Swagger UI"));
    }

    private static Stream<String> requisicoesGetInvalidas() {
        return Stream.of(
                "/api/cnaes/codigo",
                "/api/cnaes/codigo?codigo=invalido",
                "/api/cnaes/buscar",
                "/api/cnaes/buscar?termo=",
                "/api/cnaes/buscar?termo=%20%20%20",
                "/api/cadastros-secundarios/validar-cnae",
                "/api/cadastros-secundarios/validar-cnae?codigoCnae=invalido"
        );
    }

    private static Stream<String> corposPostInvalidos() {
        return Stream.of(
                """
                        {"nomeFantasia":"","documento":"12345678000199","codigoCnae":"6201-5/01"}
                        """,
                """
                        {"nomeFantasia":"   ","documento":"12345678000199","codigoCnae":"6201-5/01"}
                        """,
                """
                        {"nomeFantasia":"Tech Porto","codigoCnae":"6201-5/01"}
                        """,
                """
                        {"nomeFantasia":"Tech Porto","documento":"123","codigoCnae":"6201-5/01"}
                        """,
                """
                        {"nomeFantasia":"Tech Porto","documento":"12345678000199","codigoCnae":"invalido"}
                        """,
                """
                        {"nomeFantasia":"Tech Porto","documento":"12345678000199","codigoCnae":"6201-5/01","campoDesconhecido":true}
                        """,
                """
                        {"nomeFantasia":"Tech Porto","documento":"12345678000199","codigoCnae":"6201-5/01"
                        """
        );
    }

    private List<String> getCadastros() throws Exception {
        var response = get("/api/cadastros-secundarios");
        assertJsonResponse(response, 200);
        return JsonPath.parse(response.body()).read("$[*].nomeFantasia");
    }

    private static void assertJsonResponse(HttpResponse<String> response, int expectedStatus) {
        assertEquals(expectedStatus, response.statusCode(), response.body());
        var contentType = response.headers().firstValue("Content-Type").orElse("");
        assertTrue(contentType.startsWith("application/json"), contentType);
    }

    private static void assertError(
            HttpResponse<String> response,
            int status,
            String erro,
            String mensagem
    ) {
        var json = JsonPath.parse(response.body());
        Number actualStatus = json.read("$.status");
        assertAll(
                () -> assertEquals(status, actualStatus.intValue()),
                () -> assertEquals(erro, json.read("$.erro")),
                () -> assertEquals(mensagem, json.read("$.mensagem"))
        );
    }

    private HttpResponse<String> get(String path) throws Exception {
        var request = HttpRequest.newBuilder(uri(path)).GET().build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> post(String path, String body) throws Exception {
        var request = HttpRequest.newBuilder(uri(path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String path) {
        assertNotNull(path);
        return URI.create("http://localhost:" + serverPort + path);
    }
}
