package br.ufpr.tads.msprogresso.application.web;

import br.ufpr.tads.msprogresso.application.dto.CertificadoDTO;
import br.ufpr.tads.msprogresso.domain.model.Certificado;
import br.ufpr.tads.msprogresso.domain.service.CertificadoService;
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

@WebMvcTest(CertificadoController.class)
@ExtendWith(MockitoExtension.class)
class CertificadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CertificadoService certificadoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Certificado certificado;
    private CertificadoDTO certificadoDTO;

    @BeforeEach
    void setUp() {
        certificado = new Certificado();
        certificado.setId(1L);
        certificado.setFuncionarioId("func-123");
        certificado.setCursoId("curso-456");
        certificado.setCodigoCertificado("CERT-123456");
        certificado.setDataEmissao(LocalDateTime.now());
        certificado.setHashValidacao("hash-123456");
        certificado.setUrlPdf("http://example.com/certificado.pdf");

        certificadoDTO = new CertificadoDTO();
        certificadoDTO.setId(1L);
        certificadoDTO.setFuncionarioId("func-123");
        certificadoDTO.setCursoId("curso-456");
        certificadoDTO.setCodigoCertificado("CERT-123456");
        certificadoDTO.setDataEmissao(LocalDateTime.now());
        certificadoDTO.setHashValidacao("hash-123456");
        certificadoDTO.setUrlPdf("http://example.com/certificado.pdf");
    }

    @Test
    void listarTodos_DeveRetornarLista() throws Exception {
        // Arrange
        List<Certificado> certificados = Arrays.asList(certificado);
        when(certificadoService.listarTodosCertificados()).thenReturn(certificados);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/certificados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].codigoCertificado", is("CERT-123456")));

        verify(certificadoService, times(1)).listarTodosCertificados();
    }

    @Test
    void buscarPorId_QuandoExistir_DeveRetornarCertificado() throws Exception {
        // Arrange
        when(certificadoService.buscarCertificadoPorId(1L)).thenReturn(Optional.of(certificado));

        // Act & Assert
        mockMvc.perform(get("/api/progresso/certificados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigoCertificado", is("CERT-123456")));

        verify(certificadoService, times(1)).buscarCertificadoPorId(1L);
    }

    @Test
    void buscarPorId_QuandoNaoExistir_DeveRetornarNotFound() throws Exception {
        // Arrange
        when(certificadoService.buscarCertificadoPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/progresso/certificados/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(certificadoService, times(1)).buscarCertificadoPorId(999L);
    }

    @Test
    void criarCertificado_DeveRetornarCertificadoCriado() throws Exception {
        // Arrange
        when(certificadoService.salvarCertificado(any(Certificado.class))).thenReturn(certificado);

        // Act & Assert
        mockMvc.perform(post("/api/progresso/certificados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificadoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigoCertificado", is("CERT-123456")));

        verify(certificadoService, times(1)).salvarCertificado(any(Certificado.class));
    }

    @Test
    void atualizarCertificado_QuandoExistir_DeveRetornarCertificadoAtualizado() throws Exception {
        // Arrange
        when(certificadoService.atualizarCertificado(anyLong(), any(Certificado.class))).thenReturn(certificado);

        // Act & Assert
        mockMvc.perform(put("/api/progresso/certificados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificadoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigoCertificado", is("CERT-123456")));

        verify(certificadoService, times(1)).atualizarCertificado(anyLong(), any(Certificado.class));
    }

    @Test
    void atualizarCertificado_QuandoNaoExistir_DeveRetornarBadRequest() throws Exception {
        // Arrange
        when(certificadoService.atualizarCertificado(anyLong(), any(Certificado.class)))
                .thenThrow(new RuntimeException("Certificado não encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api/progresso/certificados/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(certificadoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Certificado não encontrado")));

        verify(certificadoService, times(1)).atualizarCertificado(anyLong(), any(Certificado.class));
    }

    @Test
    void deletarCertificado_QuandoExistir_DeveRetornarNoContent() throws Exception {
        // Arrange
        doNothing().when(certificadoService).deletarCertificado(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/progresso/certificados/1"))
                .andExpect(status().isNoContent());

        verify(certificadoService, times(1)).deletarCertificado(1L);
    }

    @Test
    void deletarCertificado_QuandoNaoExistir_DeveRetornarBadRequest() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Certificado não encontrado"))
                .when(certificadoService).deletarCertificado(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/progresso/certificados/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Certificado não encontrado")));

        verify(certificadoService, times(1)).deletarCertificado(999L);
    }

    @Test
    void listarCertificadosPorFuncionario_DeveRetornarLista() throws Exception {
        // Arrange
        List<Certificado> certificados = Arrays.asList(certificado);
        when(certificadoService.listarCertificadosPorFuncionario("func-123")).thenReturn(certificados);

        // Act & Assert
        mockMvc.perform(get("/api/progresso/certificados/funcionario/func-123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].funcionarioId", is("func-123")));

        verify(certificadoService, times(1)).listarCertificadosPorFuncionario("func-123");
    }

    @Test
    void buscarCertificadoPorHash_QuandoExistir_DeveRetornarCertificado() throws Exception {
        // Arrange
        when(certificadoService.buscarCertificadoPorHash("hash-123456")).thenReturn(Optional.of(certificado));

        // Act & Assert
        mockMvc.perform(get("/api/progresso/certificados/hash/hash-123456")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.hashValidacao", is("hash-123456")));

        verify(certificadoService, times(1)).buscarCertificadoPorHash("hash-123456");
    }

    @Test
    void buscarCertificadoPorHash_QuandoNaoExistir_DeveRetornarNotFound() throws Exception {
        // Arrange
        when(certificadoService.buscarCertificadoPorHash("hash-inexistente")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/progresso/certificados/hash/hash-inexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(certificadoService, times(1)).buscarCertificadoPorHash("hash-inexistente");
    }
}