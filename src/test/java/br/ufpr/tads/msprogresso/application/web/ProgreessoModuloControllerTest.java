package br.ufpr.tads.msprogresso.application.web;

import br.ufpr.tads.msprogresso.application.dto.ProgressoModuloDTO;
import br.ufpr.tads.msprogresso.domain.model.Inscricao;
import br.ufpr.tads.msprogresso.domain.model.ProgressoModulo;
import br.ufpr.tads.msprogresso.domain.service.ProgressoModuloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProgressoModuloController.class)
@ExtendWith(MockitoExtension.class)
class ProgressoModuloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressoModuloService progressoModuloService;

    private ProgressoModulo progressoModulo;
    private ProgressoModuloDTO progressoModuloDTO;
    private Inscricao inscricao;

    @BeforeEach
    void setUp() {
        inscricao = new Inscricao();
        inscricao.setId(1L);
        inscricao.setFuncionarioId("func-123");
        inscricao.setCursoId("curso-456");

        progressoModulo = new ProgressoModulo();
        progressoModulo.setId(1L);
        progressoModulo.setInscricao(inscricao);
        progressoModulo.setModuloId("modulo-789");
        progressoModulo.setDataInicio(LocalDateTime.now().minusHours(1));
        progressoModulo.setTempoGasto(60);

        progressoModuloDTO = new ProgressoModuloDTO();
        progressoModuloDTO.setId(1L);
        progressoModuloDTO.setInscricaoId(1L);
        progressoModuloDTO.setModuloId("modulo-789");
        progressoModuloDTO.setDataInicio(LocalDateTime.now().minusHours(1));
        progressoModuloDTO.setTempoGasto(60);
    }

    @Test
    void listarProgressoModulos_DeveRetornarLista() throws Exception {
        // Arrange
        List<ProgressoModulo> modulos = Arrays.asList(progressoModulo);
        when(progressoModuloService.listarProgressoModulosPorInscricao(1L)).thenReturn(modulos);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes/1/modulos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].moduloId", is("modulo-789")));

        verify(progressoModuloService, times(1)).listarProgressoModulosPorInscricao(1L);
    }

    @Test
    void buscarProgressoModulo_QuandoExistir_DeveRetornarModulo() throws Exception {
        // Arrange
        when(progressoModuloService.buscarProgressoModulo(1L, "modulo-789"))
                .thenReturn(java.util.Optional.of(progressoModulo));

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes/1/modulos/modulo-789")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.moduloId", is("modulo-789")));

        verify(progressoModuloService, times(1)).buscarProgressoModulo(1L, "modulo-789");
    }

    @Test
    void buscarProgressoModulo_QuandoNaoExistir_DeveRetornarNotFound() throws Exception {
        // Arrange
        when(progressoModuloService.buscarProgressoModulo(1L, "modulo-inexistente"))
                .thenReturn(java.util.Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes/1/modulos/modulo-inexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(progressoModuloService, times(1)).buscarProgressoModulo(1L, "modulo-inexistente");
    }

    @Test
    void iniciarModulo_DeveRetornarModuloCriado() throws Exception {
        // Arrange
        when(progressoModuloService.iniciarModulo(1L, "novo-modulo")).thenReturn(progressoModulo);

        // Act & Assert
        mockMvc.perform(post("/api/progresso/inscricoes/1/modulos/novo-modulo/iniciar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.moduloId", is("modulo-789")));

        verify(progressoModuloService, times(1)).iniciarModulo(1L, "novo-modulo");
    }

    @Test
    void iniciarModulo_QuandoInscricaoNaoExistir_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(progressoModuloService.iniciarModulo(999L, "novo-modulo"))
                .thenThrow(new RuntimeException("Inscrição não encontrada"));

        // Act & Assert
        mockMvc.perform(post("/api/progresso/inscricoes/999/modulos/novo-modulo/iniciar")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Inscrição não encontrada")));

        verify(progressoModuloService, times(1)).iniciarModulo(999L, "novo-modulo");
    }

    @Test
    void concluirModulo_DeveRetornarModuloConcluido() throws Exception {
        // Arrange
        ProgressoModulo moduloConcluido = new ProgressoModulo();
        moduloConcluido.setId(1L);
        moduloConcluido.setInscricao(inscricao);
        moduloConcluido.setModuloId("modulo-789");
        moduloConcluido.setDataInicio(LocalDateTime.now().minusHours(2));
        moduloConcluido.setDataConclusao(LocalDateTime.now());
        moduloConcluido.setTempoGasto(120);

        when(progressoModuloService.concluirModulo(1L, "modulo-789")).thenReturn(moduloConcluido);

        // Act & Assert
        mockMvc.perform(put("/api/progresso/inscricoes/1/modulos/modulo-789/concluir")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.moduloId", is("modulo-789")))
                .andExpect(jsonPath("$.tempoGasto", is(120)));

        verify(progressoModuloService, times(1)).concluirModulo(1L, "modulo-789");
    }

    @Test
    void concluirModulo_QuandoModuloNaoExistir_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(progressoModuloService.concluirModulo(1L, "modulo-inexistente"))
                .thenThrow(new RuntimeException("Progresso do módulo não encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api/progresso/inscricoes/1/modulos/modulo-inexistente/concluir")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Progresso do módulo não encontrado")));

        verify(progressoModuloService, times(1)).concluirModulo(1L, "modulo-inexistente");
    }

    @Test
    void obterEstatisticasProgresso_DeveRetornarEstatisticas() throws Exception {
        // Arrange
        ProgressoModuloService.EstatisticasProgresso estatisticas = 
            new ProgressoModuloService.EstatisticasProgresso(5L, 2L, 3L);

        when(progressoModuloService.obterEstatisticasProgresso(1L)).thenReturn(estatisticas);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes/1/modulos/estatisticas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalModulos", is(5)))
                .andExpect(jsonPath("$.modulosConcluidos", is(2)))
                .andExpect(jsonPath("$.modulosPendentes", is(3)))
                .andExpect(jsonPath("$.percentualConcluido", is(40.0)));

        verify(progressoModuloService, times(1)).obterEstatisticasProgresso(1L);
    }

    @Test
    void healthCheck_DeveRetornarStatusOK() throws Exception {
        mockMvc.perform(get("/api/progresso/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("MS Progresso")));
    }
}