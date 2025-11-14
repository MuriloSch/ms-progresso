package br.ufpr.tads.msprogresso.domain.service;

import br.ufpr.tads.msprogresso.domain.model.Certificado;
import br.ufpr.tads.msprogresso.domain.repository.CertificadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificadoServiceTest {

    @Mock
    private CertificadoRepository certificadoRepository;

    @InjectMocks
    private CertificadoService certificadoService;

    private Certificado certificado;

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
    }

    @Test
    void listarTodosCertificados_DeveRetornarLista() {
        // Arrange
        List<Certificado> certificados = Arrays.asList(certificado);
        when(certificadoRepository.findAll()).thenReturn(certificados);

        // Act
        List<Certificado> resultado = certificadoService.listarTodosCertificados();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(certificadoRepository, times(1)).findAll();
    }

    @Test
    void buscarCertificadoPorId_QuandoExistir_DeveRetornarCertificado() {
        // Arrange
        when(certificadoRepository.findById(1L)).thenReturn(Optional.of(certificado));

        // Act
        Optional<Certificado> resultado = certificadoService.buscarCertificadoPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("CERT-123456", resultado.get().getCodigoCertificado());
    }

    @Test
    void buscarCertificadoPorId_QuandoNaoExistir_DeveRetornarVazio() {
        // Arrange
        when(certificadoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Certificado> resultado = certificadoService.buscarCertificadoPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void salvarCertificado_DeveSalvarERetornarCertificado() {
        // Arrange
        when(certificadoRepository.save(any(Certificado.class))).thenReturn(certificado);

        // Act
        Certificado resultado = certificadoService.salvarCertificado(certificado);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(certificadoRepository, times(1)).save(certificado);
    }

    @Test
    void atualizarCertificado_QuandoExistir_DeveAtualizar() {
        // Arrange
        Certificado certificadoAtualizado = new Certificado();
        certificadoAtualizado.setId(1L);
        certificadoAtualizado.setFuncionarioId("func-123");
        certificadoAtualizado.setCursoId("curso-456");
        certificadoAtualizado.setCodigoCertificado("CERT-123456");
        certificadoAtualizado.setDataEmissao(LocalDateTime.now());
        certificadoAtualizado.setHashValidacao("hash-123456-updated");
        certificadoAtualizado.setUrlPdf("http://example.com/certificado-updated.pdf");

        when(certificadoRepository.existsById(1L)).thenReturn(true);
        when(certificadoRepository.save(any(Certificado.class))).thenReturn(certificadoAtualizado);

        // Act
        Certificado resultado = certificadoService.atualizarCertificado(1L, certificadoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("hash-123456-updated", resultado.getHashValidacao());
        verify(certificadoRepository, times(1)).save(certificadoAtualizado);
    }

    @Test
    void atualizarCertificado_QuandoNaoExistir_DeveLancarExcecao() {
        // Arrange
        when(certificadoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            certificadoService.atualizarCertificado(999L, certificado);
        });

        verify(certificadoRepository, never()).save(any(Certificado.class));
    }

    @Test
    void deletarCertificado_QuandoExistir_DeveDeletar() {
        // Arrange
        when(certificadoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(certificadoRepository).deleteById(1L);

        // Act
        certificadoService.deletarCertificado(1L);

        // Assert
        verify(certificadoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletarCertificado_QuandoNaoExistir_DeveLancarExcecao() {
        // Arrange
        when(certificadoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            certificadoService.deletarCertificado(999L);
        });

        verify(certificadoRepository, never()).deleteById(anyLong());
    }

    @Test
    void listarCertificadosPorFuncionario_DeveRetornarLista() {
        // Arrange
        List<Certificado> certificados = Arrays.asList(certificado);
        when(certificadoRepository.findByFuncionarioId("func-123")).thenReturn(certificados);

        // Act
        List<Certificado> resultado = certificadoService.listarCertificadosPorFuncionario("func-123");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(certificadoRepository, times(1)).findByFuncionarioId("func-123");
    }

    @Test
    void buscarCertificadoPorHash_QuandoExistir_DeveRetornarCertificado() {
        // Arrange
        when(certificadoRepository.findByHashValidacao("hash-123456")).thenReturn(Optional.of(certificado));

        // Act
        Optional<Certificado> resultado = certificadoService.buscarCertificadoPorHash("hash-123456");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("hash-123456", resultado.get().getHashValidacao());
    }

    @Test
    void buscarCertificadoPorHash_QuandoNaoExistir_DeveRetornarVazio() {
        // Arrange
        when(certificadoRepository.findByHashValidacao("hash-inexistente")).thenReturn(Optional.empty());

        // Act
        Optional<Certificado> resultado = certificadoService.buscarCertificadoPorHash("hash-inexistente");

        // Assert
        assertFalse(resultado.isPresent());
    }
}