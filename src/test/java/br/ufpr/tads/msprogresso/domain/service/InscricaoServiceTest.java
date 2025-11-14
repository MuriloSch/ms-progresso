package br.ufpr.tads.msprogresso.domain.service;

import br.ufpr.tads.msprogresso.domain.model.Inscricao;
import br.ufpr.tads.msprogresso.domain.repository.InscricaoRepository;
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
class InscricaoServiceTest {

    @Mock
    private InscricaoRepository inscricaoRepository;

    @InjectMocks
    private InscricaoService inscricaoService;

    private Inscricao inscricao;

    @BeforeEach
    void setUp() {
        inscricao = new Inscricao();
        inscricao.setId(1L);
        inscricao.setFuncionarioId("func-123");
        inscricao.setCursoId("curso-456");
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatus("ATIVA");
        inscricao.setProgressoPercentual(0.0);
    }

    @Test
    void listarTodasInscricoes_DeveRetornarLista() {
        // Arrange
        List<Inscricao> inscricoes = Arrays.asList(inscricao);
        when(inscricaoRepository.findAll()).thenReturn(inscricoes);

        // Act
        List<Inscricao> resultado = inscricaoService.listarTodasInscricoes();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inscricaoRepository, times(1)).findAll();
    }

    @Test
    void buscarInscricaoPorId_QuandoExistir_DeveRetornarInscricao() {
        // Arrange
        when(inscricaoRepository.findById(1L)).thenReturn(Optional.of(inscricao));

        // Act
        Optional<Inscricao> resultado = inscricaoService.buscarInscricaoPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("func-123", resultado.get().getFuncionarioId());
    }

    @Test
    void buscarInscricaoPorId_QuandoNaoExistir_DeveRetornarVazio() {
        // Arrange
        when(inscricaoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Inscricao> resultado = inscricaoService.buscarInscricaoPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void salvarInscricao_DeveSalvarERetornarInscricao() {
        // Arrange
        when(inscricaoRepository.save(any(Inscricao.class))).thenReturn(inscricao);

        // Act
        Inscricao resultado = inscricaoService.salvarInscricao(inscricao);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(inscricaoRepository, times(1)).save(inscricao);
    }

    @Test
    void atualizarInscricao_QuandoExistir_DeveAtualizar() {
        // Arrange
        Inscricao inscricaoAtualizada = new Inscricao();
        inscricaoAtualizada.setId(1L);
        inscricaoAtualizada.setFuncionarioId("func-123");
        inscricaoAtualizada.setCursoId("curso-456");
        inscricaoAtualizada.setStatus("CONCLUIDA");
        inscricaoAtualizada.setProgressoPercentual(100.0);

        when(inscricaoRepository.existsById(1L)).thenReturn(true);
        when(inscricaoRepository.save(any(Inscricao.class))).thenReturn(inscricaoAtualizada);

        // Act
        Inscricao resultado = inscricaoService.atualizarInscricao(1L, inscricaoAtualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals("CONCLUIDA", resultado.getStatus());
        assertEquals(100.0, resultado.getProgressoPercentual());
        verify(inscricaoRepository, times(1)).save(inscricaoAtualizada);
    }

    @Test
    void atualizarInscricao_QuandoNaoExistir_DeveLancarExcecao() {
        // Arrange
        when(inscricaoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            inscricaoService.atualizarInscricao(999L, inscricao);
        });

        verify(inscricaoRepository, never()).save(any(Inscricao.class));
    }

    @Test
    void deletarInscricao_QuandoExistir_DeveDeletar() {
        // Arrange
        when(inscricaoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(inscricaoRepository).deleteById(1L);

        // Act
        inscricaoService.deletarInscricao(1L);

        // Assert
        verify(inscricaoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletarInscricao_QuandoNaoExistir_DeveLancarExcecao() {
        // Arrange
        when(inscricaoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            inscricaoService.deletarInscricao(999L);
        });

        verify(inscricaoRepository, never()).deleteById(anyLong());
    }

    @Test
    void listarInscricoesPorFuncionario_DeveRetornarLista() {
        // Arrange
        List<Inscricao> inscricoes = Arrays.asList(inscricao);
        when(inscricaoRepository.findByFuncionarioId("func-123")).thenReturn(inscricoes);

        // Act
        List<Inscricao> resultado = inscricaoService.listarInscricoesPorFuncionario("func-123");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inscricaoRepository, times(1)).findByFuncionarioId("func-123");
    }

    @Test
    void listarInscricoesPorCurso_DeveRetornarLista() {
        // Arrange
        List<Inscricao> inscricoes = Arrays.asList(inscricao);
        when(inscricaoRepository.findByCursoId("curso-456")).thenReturn(inscricoes);

        // Act
        List<Inscricao> resultado = inscricaoService.listarInscricoesPorCurso("curso-456");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inscricaoRepository, times(1)).findByCursoId("curso-456");
    }
}