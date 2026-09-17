package com.henrique.cadastroprodutos.web;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.henrique.cadastroprodutos.repository.ProdutoRepository;

/**
 * Teste de integração da API: sobe o contexto completo (controller, service,
 * JPA, Flyway) sobre um H2 em memória em modo PostgreSQL.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutoApiTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    @Autowired
    ProdutoRepository repository;

    @BeforeEach
    void limpaBanco() {
        repository.deleteAll();
    }

    @Test
    void fluxoCompletoDeCrud() throws Exception {
        // create
        MvcResult criado = mvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"FISICO","nome":"Teclado","preco":349.90,
                                 "pesoKg":0.85,"dimensoesCm":44.0}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tipo").value("FISICO"))
                .andExpect(jsonPath("$.pesoKg").value(0.85))
                .andExpect(jsonPath("$.tamanhoArquivoMb").doesNotExist())
                .andReturn();
        long id = json.readTree(criado.getResponse().getContentAsString()).get("id").asLong();

        // read
        mvc.perform(get("/api/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Teclado"));

        // update
        mvc.perform(put("/api/produtos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"FISICO","nome":"Teclado Mecânico","preco":399.00,
                                 "pesoKg":0.9,"dimensoesCm":44.0}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Teclado Mecânico"))
                .andExpect(jsonPath("$.preco").value(399.00));

        // delete
        mvc.perform(delete("/api/produtos/{id}", id))
                .andExpect(status().isNoContent());
        mvc.perform(get("/api/produtos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"));
    }

    @Test
    void listagemFiltraPorTipoENome() throws Exception {
        criar("""
                {"tipo":"FISICO","nome":"Monitor 27","preco":1500,"pesoKg":5,"dimensoesCm":61}
                """);
        criar("""
                {"tipo":"DIGITAL","nome":"Curso de Java","preco":199.9,"tamanhoArquivoMb":2048}
                """);
        criar("""
                {"tipo":"DIGITAL","nome":"E-book Angular","preco":39.9,"tamanhoArquivoMb":8}
                """);

        mvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].nome").value("Curso de Java")); // ordenado por nome

        mvc.perform(get("/api/produtos").param("tipo", "DIGITAL"))
                .andExpect(jsonPath("$.totalElements").value(2));

        mvc.perform(get("/api/produtos").param("nome", "angular"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].tipo").value("DIGITAL"));

        mvc.perform(get("/api/produtos").param("size", "2"))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void validacaoDeBeanRetorna400ComListaDeCampos() throws Exception {
        mvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"FISICO","nome":"","preco":-1,"pesoKg":1,"dimensoesCm":1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Dados inválidos"))
                .andExpect(jsonPath("$.campos", hasSize(2)));
    }

    @Test
    void regraDeDominioRetorna400() throws Exception {
        // pesoKg ausente para produto físico: passa no Bean Validation,
        // mas é barrado pela regra do domínio.
        mvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"tipo":"FISICO","nome":"Cadeira","preco":800,"dimensoesCm":120}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Regra de negócio violada"))
                .andExpect(jsonPath("$.detail").value("O campo 'pesoKg' deve ser preenchido."));
    }

    @Test
    void corpoMalformadoRetorna400() throws Exception {
        mvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipo\":\"INEXISTENTE\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Requisição malformada"));
    }

    private void criar(String corpo) throws Exception {
        mvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isCreated());
    }
}
