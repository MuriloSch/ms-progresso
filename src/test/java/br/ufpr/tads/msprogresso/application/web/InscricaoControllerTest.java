package br.ufpr.tads.msprogresso.application.web;

import br.ufpr.tads.msprogresso.application.dto.InscricaoDTO;
import br.ufpr.tads.msprogresso.domain.model.Inscricao;
import br.ufpr.tads.msprogresso.domain.service.InscricaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscricaoController.class)
@ExtendWith(MockitoExtension.class)
class InscricaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InscricaoService inscricaoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Inscricao inscricao;
    private InscricaoDTO inscricaoDTO;

    @BeforeEach
    void setUp() {
        inscricao = new Inscricao();
        inscricao.setId(1L);
        inscricao.setFuncionarioId("func-123");
        inscricao.setCursoId("curso-456");
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatus("ATIVA");
        inscricao.setProgressoPercentual(0.0);

        inscricaoDTO = new InscricaoDTO();
        inscricaoDTO.setId(1L);
        inscricaoDTO.setFuncionarioId("func-123");
        inscricaoDTO.setCursoId("curso-456");
        inscricaoDTO.setDataInscricao(LocalDateTime.now());
        inscricaoDTO.setStatus("ATIVA");
        inscricaoDTO.setProgressoPercentual(0.0);
    }

    @Test
    void listarTodas_DeveRetornarLista() throws Exception {
        // Arrange
        List<Inscricao> inscricoes = Arrays.asList(inscricao);
        when(inscricaoService.listarTodasInscricoes()).thenReturn(inscricoes);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].funcionarioId", is("func-123")));

        verify(inscricaoService, times(1)).listarTodasInscricoes();
    }

    @Test
    void buscarPorId_QuandoExistir_DeveRetornarInscricao() throws Exception {
        // Arrange
        when(inscricaoService.buscarInscricaoPorId(1L)).thenReturn(Optional.of(inscricao));

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.funcionarioId", is("func-123")));

        verify(inscricaoService, times(1)).buscarInscricaoPorId(1L);
    }

    @Test
    void buscarPorId_QuandoNaoExistir_DeveRetornarNotFound() throws Exception {
        // Arrange
        when(inscricaoService.buscarInscricaoPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inscricaoService, times(1)).buscarInscricaoPorId(999L);
    }

    @Test
    void criarInscricao_DeveRetornarInscricaoCriada() throws Exception {
        // Arrange
        when(inscricaoService.salvarInscricao(any(Inscricao.class))).thenReturn(inscricao);

        // Act & Assert
        mockMvc.perform(post("/api/progresso/inscricoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inscricaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.funcionarioId", is("func-123")));

        verify(inscricaoService, times(1)).salvarInscricao(any(Inscricao.class));
    }

    @Test
    void atualizarInscricao_QuandoExistir_DeveRetornarInscricaoAtualizada() throws Exception {
        // Arrange
        when(inscricaoService.atualizarInscricao(anyLong(), any(Inscricao.class))).thenReturn(inscricao);

        // Act & Assert
        mockMvc.perform(put("/api/progresso/inscricoes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inscricaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.funcionarioId", is("func-123")));

        verify(inscricaoService, times(1)).atualizarInscricao(anyLong(), any(Inscricao.class));
    }

    @Test
    void atualizarInscricao_QuandoNaoExistir_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(inscricaoService.atualizarInscricao(anyLong(), any(Inscricao.class)))
                .thenThrow(new RuntimeException("Inscrição não encontrada"));

        // Act & Assert
        mockMvc.perform(put("/api/progresso/inscricoes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inscricaoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Inscrição não encontrada")));

        verify(inscricaoService, times(1)).atualizarInscricao(anyLong(), any(Inscricao.class));
    }

    @Test
    void deletarInscricao_QuandoExistir_DeveRetornarNoContent() throws Exception {
        // Arrange
        doNothing().when(inscricaoService).deletarInscricao(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/progresso/inscricoes/1"))
                .andExpect(status().isNoContent());

        verify(inscricaoService, times(1)).deletarInscricao(1L);
    }

    @Test
    void deletarInscricao_QuandoNaoExistir_DeveRetornarBadRequest() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Inscrição não encontrada"))
                .when(inscricaoService).deletarInscricao(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/progresso/inscricoes/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Inscrição não encontrada")));

        verify(inscricaoService, times(1)).deletarInscricao(999L);
    }

    @Test
    void listarInscricoesPorFuncionario_DeveRetornarLista() throws Exception {
        // Arrange
        List<Inscricao> inscricoes = Arrays.asList(inscricao);
        when(inscricaoService.listarInscricoesPorFuncionario("func-123")).thenReturn(inscricoes);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes/funcionario/func-123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].funcionarioId", is("func-123")));

        verify(inscricaoService, times(1)).listarInscricoesPorFuncionario("func-123");
    }

    @Test
    void listarInscricoesPorCurso_DeveRetornarLista() throws Exception {
        // Arrange
        List<Inscricao> inscricoes = Arrays.asList(inscricao);
        when(inscricaoService.listarInscricoesPorCurso("curso-456")).thenReturn(inscricoes);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/inscricoes/curso/curso-456")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].cursoId", is("curso-456")));

        verify(inscricaoService, times(1)).listarInscricoesPorCurso("curso-456");
    }
}