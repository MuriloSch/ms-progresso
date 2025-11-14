package br.ufpr.tads.msprogresso.domain.service;

import br.ufpr.tads.msprogresso.domain.model.TrilhaProgresso;
import br.ufpr.tads.msprogresso.domain.repository.TrilhaProgressoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrilhaProgressoServiceTest {

    @Mock
    private TrilhaProgressoRepository trilhaProgressoRepository;

    @InjectMocks
    private TrilhaProgressoService trilhaProgressoService;

    private TrilhaProgresso trilhaProgresso;

    @BeforeEach
    void setUp() {
        trilhaProgresso = new TrilhaProgresso();
        trilhaProgresso.setId(1L);
        trilhaProgresso.setFuncionarioId("func-123");
        trilhaProgresso.setTrilhaId("trilha-456");
        trilhaProgresso.setCursosConcluidos(2);
        trilhaProgresso.setProgressoPercentual(50.0);
    }

    @Test
    void listarTodasTrilhasProgresso_DeveRetornarLista() {
        // Arrange
        List<TrilhaProgresso> trilhas = Arrays.asList(trilhaProgresso);
        when(trilhaProgressoRepository.findAll()).thenReturn(trilhas);

        // Act
        List<TrilhaProgresso> resultado = trilhaProgressoService.listarTodasTrilhasProgresso();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(trilhaProgressoRepository, times(1)).findAll();
    }

    @Test
    void buscarTrilhaProgressoPorId_QuandoExistir_DeveRetornarTrilha() {
        // Arrange
        when(trilhaProgressoRepository.findById(1L)).thenReturn(Optional.of(trilhaProgresso));

        // Act
        Optional<TrilhaProgresso> resultado = trilhaProgressoService.buscarTrilhaProgressoPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("trilha-456", resultado.get().getTrilhaId());
    }

    @Test
    void buscarTrilhaProgressoPorId_QuandoNaoExistir_DeveRetornarVazio() {
        // Arrange
        when(trilhaProgressoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<TrilhaProgresso> resultado = trilhaProgressoService.buscarTrilhaProgressoPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void salvarTrilhaProgresso_DeveSalvarERetornarTrilha() {
        // Arrange
        when(trilhaProgressoRepository.save(any(TrilhaProgresso.class))).thenReturn(trilhaProgresso);

        // Act
        TrilhaProgresso resultado = trilhaProgressoService.salvarTrilhaProgresso(trilhaProgresso);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(trilhaProgressoRepository, times(1)).save(trilhaProgresso);
    }

    @Test
    void atualizarTrilhaProgresso_QuandoExistir_DeveAtualizar() {
        // Arrange
        TrilhaProgresso trilhaAtualizada = new TrilhaProgresso();
        trilhaAtualizada.setId(1L);
        trilhaAtualizada.setFuncionarioId("func-123");
        trilhaAtualizada.setTrilhaId("trilha-456");
        trilhaAtualizada.setCursosConcluidos(3);
        trilhaAtualizada.setProgressoPercentual(75.0);

        when(trilhaProgressoRepository.existsById(1L)).thenReturn(true);
        when(trilhaProgressoRepository.save(any(TrilhaProgresso.class))).thenReturn(trilhaAtualizada);

        // Act
        TrilhaProgresso resultado = trilhaProgressoService.atualizarTrilhaProgresso(1L, trilhaAtualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.getCursosConcluidos());
        assertEquals(75.0, resultado.getProgressoPercentual());
        verify(trilhaProgressoRepository, times(1)).save(trilhaAtualizada);
    }

    @Test
    void atualizarTrilhaProgresso_QuandoNaoExistir_DeveLancarExcecao() {
        // Arrange
        when(trilhaProgressoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            trilhaProgressoService.atualizarTrilhaProgresso(999L, trilhaProgresso);
        });

        verify(trilhaProgressoRepository, never()).save(any(TrilhaProgresso.class));
    }

    @Test
    void deletarTrilhaProgresso_QuandoExistir_DeveDeletar() {
        // Arrange
        when(trilhaProgressoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(trilhaProgressoRepository).deleteById(1L);

        // Act
        trilhaProgressoService.deletarTrilhaProgresso(1L);

        // Assert
        verify(trilhaProgressoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletarTrilhaProgresso_QuandoNaoExistir_DeveLancarExcecao() {
        // Arrange
        when(trilhaProgressoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            trilhaProgressoService.deletarTrilhaProgresso(999L);
        });

        verify(trilhaProgressoRepository, never()).deleteById(anyLong());
    }

    @Test
    void listarTrilhasProgressoPorFuncionario_DeveRetornarLista() {
        // Arrange
        List<TrilhaProgresso> trilhas = Arrays.asList(trilhaProgresso);
        when(trilhaProgressoRepository.findByFuncionarioId("func-123")).thenReturn(trilhas);

        // Act
        List<TrilhaProgresso> resultado = trilhaProgressoService.listarTrilhasProgressoPorFuncionario("func-123");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(trilhaProgressoRepository, times(1)).findByFuncionarioId("func-123");
    }

    @Test
    void buscarTrilhaProgressoPorFuncionarioETrilha_QuandoExistir_DeveRetornarTrilha() {
        // Arrange
        when(trilhaProgressoRepository.findByFuncionarioIdAndTrilhaId("func-123", "trilha-456"))
                .thenReturn(Optional.of(trilhaProgresso));

        // Act
        Optional<TrilhaProgresso> resultado = trilhaProgressoService.buscarTrilhaProgressoPorFuncionarioETrilha("func-123", "trilha-456");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("func-123", resultado.get().getFuncionarioId());
        assertEquals("trilha-456", resultado.get().getTrilhaId());
        verify(trilhaProgressoRepository, times(1)).findByFuncionarioIdAndTrilhaId("func-123", "trilha-456");
    }

    @Test
    void buscarTrilhaProgressoPorFuncionarioETrilha_QuandoNaoExistir_DeveRetornarVazio() {
        // Arrange
        when(trilhaProgressoRepository.findByFuncionarioIdAndTrilhaId("func-123", "trilha-inexistente"))
                .thenReturn(Optional.empty());

        // Act
        Optional<TrilhaProgresso> resultado = trilhaProgressoService.buscarTrilhaProgressoPorFuncionarioETrilha("func-123", "trilha-inexistente");

        // Assert
        assertFalse(resultado.isPresent());
        verify(trilhaProgressoRepository, times(1)).findByFuncionarioIdAndTrilhaId("func-123", "trilha-inexistente");
    }
}