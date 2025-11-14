package br.ufpr.tads.msprogresso.application.web;

import br.ufpr.tads.msprogresso.application.dto.TrilhaProgressoDTO;
import br.ufpr.tads.msprogresso.domain.model.TrilhaProgresso;
import br.ufpr.tads.msprogresso.domain.service.TrilhaProgressoService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrilhaProgressoController.class)
@ExtendWith(MockitoExtension.class)
class TrilhaProgressoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrilhaProgressoService trilhaProgressoService;

    @Autowired
    private ObjectMapper objectMapper;

    private TrilhaProgresso trilhaProgresso;
    private TrilhaProgressoDTO trilhaProgressoDTO;

    @BeforeEach
    void setUp() {
        trilhaProgresso = new TrilhaProgresso();
        trilhaProgresso.setId(1L);
        trilhaProgresso.setFuncionarioId("func-123");
        trilhaProgresso.setTrilhaId("trilha-456");
        trilhaProgresso.setCursosConcluidos(2);
        trilhaProgresso.setProgressoPercentual(50.0);

        trilhaProgressoDTO = new TrilhaProgressoDTO();
        trilhaProgressoDTO.setId(1L);
        trilhaProgressoDTO.setFuncionarioId("func-123");
        trilhaProgressoDTO.setTrilhaId("trilha-456");
        trilhaProgressoDTO.setCursosConcluidos(2);
        trilhaProgressoDTO.setProgressoPercentual(50.0);
    }

    @Test
    void listarTodas_DeveRetornarLista() throws Exception {
        // Arrange
        List<TrilhaProgresso> trilhas = Arrays.asList(trilhaProgresso);
        when(trilhaProgressoService.listarTodasTrilhasProgresso()).thenReturn(trilhas);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/trilhas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].trilhaId", is("trilha-456")));

        verify(trilhaProgressoService, times(1)).listarTodasTrilhasProgresso();
    }

    @Test
    void buscarPorId_QuandoExistir_DeveRetornarTrilha() throws Exception {
        // Arrange
        when(trilhaProgressoService.buscarTrilhaProgressoPorId(1L)).thenReturn(Optional.of(trilhaProgresso));

        // Act & Assert
        mockMvc.perform(get("/api/progresso/trilhas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.trilhaId", is("trilha-456")));

        verify(trilhaProgressoService, times(1)).buscarTrilhaProgressoPorId(1L);
    }

    @Test
    void buscarPorId_QuandoNaoExistir_DeveRetornarNotFound() throws Exception {
        // Arrange
        when(trilhaProgressoService.buscarTrilhaProgressoPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/progresso/trilhas/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(trilhaProgressoService, times(1)).buscarTrilhaProgressoPorId(999L);
    }

    @Test
    void criarTrilhaProgresso_DeveRetornarTrilhaCriada() throws Exception {
        // Arrange
        when(trilhaProgressoService.salvarTrilhaProgresso(any(TrilhaProgresso.class))).thenReturn(trilhaProgresso);

        // Act & Assert
        mockMvc.perform(post("/api/progresso/trilhas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trilhaProgressoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.trilhaId", is("trilha-456")));

        verify(trilhaProgressoService, times(1)).salvarTrilhaProgresso(any(TrilhaProgresso.class));
    }

    @Test
    void atualizarTrilhaProgresso_QuandoExistir_DeveRetornarTrilhaAtualizada() throws Exception {
        // Arrange
        when(trilhaProgressoService.atualizarTrilhaProgresso(anyLong(), any(TrilhaProgresso.class))).thenReturn(trilhaProgresso);

        // Act & Assert
        mockMvc.perform(put("/api/progresso/trilhas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trilhaProgressoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.trilhaId", is("trilha-456")));

        verify(trilhaProgressoService, times(1)).atualizarTrilhaProgresso(anyLong(), any(TrilhaProgresso.class));
    }

    @Test
    void atualizarTrilhaProgresso_QuandoNaoExistir_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(trilhaProgressoService.atualizarTrilhaProgresso(anyLong(), any(TrilhaProgresso.class)))
                .thenThrow(new RuntimeException("Trilha de progresso não encontrada"));

        // Act & Assert
        mockMvc.perform(put("/api/progresso/trilhas/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trilhaProgressoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Trilha de progresso não encontrada")));

        verify(trilhaProgressoService, times(1)).atualizarTrilhaProgresso(anyLong(), any(TrilhaProgresso.class));
    }

    @Test
    void deletarTrilhaProgresso_QuandoExistir_DeveRetornarNoContent() throws Exception {
        // Arrange
        doNothing().when(trilhaProgressoService).deletarTrilhaProgresso(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/progresso/trilhas/1"))
                .andExpect(status().isNoContent());

        verify(trilhaProgressoService, times(1)).deletarTrilhaProgresso(1L);
    }

    @Test
    void deletarTrilhaProgresso_QuandoNaoExistir_DeveRetornarBadRequest() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Trilha de progresso não encontrada"))
                .when(trilhaProgressoService).deletarTrilhaProgresso(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/progresso/trilhas/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Trilha de progresso não encontrada")));

        verify(trilhaProgressoService, times(1)).deletarTrilhaProgresso(999L);
    }

    @Test
    void listarTrilhasProgressoPorFuncionario_DeveRetornarLista() throws Exception {
        // Arrange
        List<TrilhaProgresso> trilhas = Arrays.asList(trilhaProgresso);
        when(trilhaProgressoService.listarTrilhasProgressoPorFuncionario("func-123")).thenReturn(trilhas);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/trilhas/funcionario/func-123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].funcionarioId", is("func-123")));

        verify(trilhaProgressoService, times(1)).listarTrilhasProgressoPorFuncionario("func-123");
    }

    @Test
    void buscarTrilhaProgressoPorFuncionarioETrilha_QuandoExistir_DeveRetornarTrilha() throws Exception {
        // Arrange
        when(trilhaProgressoService.buscarTrilhaProgressoPorFuncionarioETrilha("func-123", "trilha-456"))
                .thenReturn(Optional.of(trilhaProgresso));

        // Act & Assert
        mockMvc.perform(get("/api/progresso/trilhas/funcionario/func-123/trilha/trilha-456")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.funcionarioId", is("func-123")))
                .andExpect(jsonPath("$.trilhaId", is("trilha-456")));

        verify(trilhaProgressoService, times(1)).buscarTrilhaProgressoPorFuncionarioETrilha("func-123", "trilha-456");
    }

    @Test
    void buscarTrilhaProgressoPorFuncionarioETrilha_QuandoNaoExistir_DeveRetornarNotFound() throws Exception {
        // Arrange
        when(trilhaProgressoService.buscarTrilhaProgressoPorFuncionarioETrilha("func-123", "trilha-inexistente"))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/progresso/trilhas/funcionario/func-123/trilha/trilha-inexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(trilhaProgressoService, times(1)).buscarTrilhaProgressoPorFuncionarioETrilha("func-123", "trilha-inexistente");
    }
}