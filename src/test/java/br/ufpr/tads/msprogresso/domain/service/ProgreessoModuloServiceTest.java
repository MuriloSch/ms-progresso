package br.ufpr.tads.msprogresso.domain.service;

import br.ufpr.tads.msprogresso.domain.model.Inscricao;
import br.ufpr.tads.msprogresso.domain.model.ProgressoModulo;
import br.ufpr.tads.msprogresso.domain.repository.InscricaoRepository;
import br.ufpr.tads.msprogresso.domain.repository.ProgressoModuloRepository;
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
class ProgressoModuloServiceTest {

    @Mock
    private ProgressoModuloRepository progressoModuloRepository;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @InjectMocks
    private ProgressoModuloService progressoModuloService;

    private Inscricao inscricao;
    private ProgressoModulo progressoModulo;

    @BeforeEach
    void setUp() {
        inscricao = new Inscricao();
        inscricao.setId(1L);
        inscricao.setFuncionarioId("func-123");
        inscricao.setCursoId("curso-456");
        inscricao.setStatus("EM_ANDAMENTO");
        inscricao.setProgressoPercentual(0.0);

        progressoModulo = new ProgressoModulo();
        progressoModulo.setId(1L);
        progressoModulo.setInscricao(inscricao);
        progressoModulo.setModuloId("modulo-789");
        progressoModulo.setDataInicio(LocalDateTime.now().minusHours(1));
    }

    @Test
    void listarProgressoModulosPorInscricao_DeveRetornarLista() {
        // Arrange
        List<ProgressoModulo> modulos = Arrays.asList(progressoModulo);
        when(progressoModuloRepository.findByInscricaoId(1L)).thenReturn(modulos);

        // Act
        List<ProgressoModulo> resultado = progressoModuloService.listarProgressoModulosPorInscricao(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("modulo-789", resultado.get(0).getModuloId());
        verify(progressoModuloRepository, times(1)).findByInscricaoId(1L);
    }

    @Test
    void buscarProgressoModulo_QuandoExistir_DeveRetornarModulo() {
        // Arrange
        when(progressoModuloRepository.findByInscricaoIdAndModuloId(1L, "modulo-789"))
                .thenReturn(Optional.of(progressoModulo));

        // Act
        Optional<ProgressoModulo> resultado = progressoModuloService.buscarProgressoModulo(1L, "modulo-789");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("modulo-789", resultado.get().getModuloId());
    }

    @Test
    void buscarProgressoModulo_QuandoNaoExistir_DeveRetornarVazio() {
        // Arrange
        when(progressoModuloRepository.findByInscricaoIdAndModuloId(1L, "modulo-inexistente"))
                .thenReturn(Optional.empty());

        // Act
        Optional<ProgressoModulo> resultado = progressoModuloService.buscarProgressoModulo(1L, "modulo-inexistente");

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    void iniciarModulo_QuandoInscricaoExistirEModuloNaoIniciado_DeveCriarNovoProgresso() {
        // Arrange
        when(inscricaoRepository.findById(1L)).thenReturn(Optional.of(inscricao));
        when(progressoModuloRepository.findByInscricaoIdAndModuloId(1L, "novo-modulo"))
                .thenReturn(Optional.empty());
        when(progressoModuloRepository.save(any(ProgressoModulo.class))).thenReturn(progressoModulo);

        // Act
        ProgressoModulo resultado = progressoModuloService.iniciarModulo(1L, "novo-modulo");

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getDataInicio());
        verify(progressoModuloRepository, times(1)).save(any(ProgressoModulo.class));
    }

    @Test
    void iniciarModulo_QuandoModuloJaIniciado_DeveRetornarExistente() {
        // Arrange
        when(inscricaoRepository.findById(1L)).thenReturn(Optional.of(inscricao));
        when(progressoModuloRepository.findByInscricaoIdAndModuloId(1L, "modulo-789"))
                .thenReturn(Optional.of(progressoModulo));

        // Act
        ProgressoModulo resultado = progressoModuloService.iniciarModulo(1L, "modulo-789");

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(progressoModuloRepository, never()).save(any(ProgressoModulo.class));
    }

    @Test
    void iniciarModulo_QuandoInscricaoNaoExistir_DeveLancarExcecao() {
        // Arrange
        when(inscricaoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            progressoModuloService.iniciarModulo(999L, "modulo-789");
        });

        assertTrue(exception.getMessage().contains("Inscrição não encontrada"));
    }

    @Test
    void concluirModulo_QuandoModuloExistir_DeveConcluirEAtualizarProgresso() {
        // Arrange
        ProgressoModulo moduloParaConcluir = new ProgressoModulo();
        moduloParaConcluir.setId(1L);
        moduloParaConcluir.setInscricao(inscricao);
        moduloParaConcluir.setModuloId("modulo-789");
        moduloParaConcluir.setDataInicio(LocalDateTime.now().minusHours(2));

        when(progressoModuloRepository.findByInscricaoIdAndModuloId(1L, "modulo-789"))
                .thenReturn(Optional.of(moduloParaConcluir));
        when(progressoModuloRepository.countModulosConcluidosByInscricaoId(1L)).thenReturn(1L);
        when(progressoModuloRepository.countByInscricaoId(1L)).thenReturn(3L);
        when(inscricaoRepository.findById(1L)).thenReturn(Optional.of(inscricao));
        when(progressoModuloRepository.save(any(ProgressoModulo.class))).thenReturn(moduloParaConcluir);
        when(inscricaoRepository.save(any(Inscricao.class))).thenReturn(inscricao);

        // Act
        ProgressoModulo resultado = progressoModuloService.concluirModulo(1L, "modulo-789");

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getDataConclusao());
        assertTrue(resultado.getTempoGasto() > 0);
        verify(progressoModuloRepository, times(1)).save(moduloParaConcluir);
    }

    @Test
    void concluirModulo_QuandoModuloNaoExistir_DeveLancarExcecao() {
        // Arrange
        when(progressoModuloRepository.findByInscricaoIdAndModuloId(1L, "modulo-inexistente"))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            progressoModuloService.concluirModulo(1L, "modulo-inexistente");
        });

        assertTrue(exception.getMessage().contains("Progresso do módulo não encontrado"));
    }

    @Test
    void concluirModulo_QuandoModuloJaConcluido_DeveLancarExcecao() {
        // Arrange
        ProgressoModulo moduloConcluido = new ProgressoModulo();
        moduloConcluido.setId(1L);
        moduloConcluido.setDataConclusao(LocalDateTime.now().minusHours(1));

        when(progressoModuloRepository.findByInscricaoIdAndModuloId(1L, "modulo-concluido"))
                .thenReturn(Optional.of(moduloConcluido));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            progressoModuloService.concluirModulo(1L, "modulo-concluido");
        });

        assertTrue(exception.getMessage().contains("Módulo já concluído"));
    }

    @Test
    void obterEstatisticasProgresso_DeveRetornarEstatisticasCorretas() {
        // Arrange
        when(progressoModuloRepository.countByInscricaoId(1L)).thenReturn(5L);
        when(progressoModuloRepository.countModulosConcluidosByInscricaoId(1L)).thenReturn(2L);
        when(progressoModuloRepository.findModulosPendentesByInscricaoId(1L))
                .thenReturn(Arrays.asList(new ProgressoModulo(), new ProgressoModulo(), new ProgressoModulo()));

        // Act
        ProgressoModuloService.EstatisticasProgresso estatisticas = 
            progressoModuloService.obterEstatisticasProgresso(1L);

        // Assert
        assertNotNull(estatisticas);
        assertEquals(5L, estatisticas.getTotalModulos());
        assertEquals(2L, estatisticas.getModulosConcluidos());
        assertEquals(3L, estatisticas.getModulosPendentes());
        assertEquals(40.0, estatisticas.getPercentualConcluido());
    }

    @Test
    void obterEstatisticasProgresso_QuandoNenhumModulo_DeveRetornarZeros() {
        // Arrange
        when(progressoModuloRepository.countByInscricaoId(1L)).thenReturn(0L);
        when(progressoModuloRepository.countModulosConcluidosByInscricaoId(1L)).thenReturn(0L);
        when(progressoModuloRepository.findModulosPendentesByInscricaoId(1L))
                .thenReturn(Arrays.asList());

        // Act
        ProgressoModuloService.EstatisticasProgresso estatisticas = 
            progressoModuloService.obterEstatisticasProgresso(1L);

        // Assert
        assertNotNull(estatisticas);
        assertEquals(0L, estatisticas.getTotalModulos());
        assertEquals(0L, estatisticas.getModulosConcluidos());
        assertEquals(0L, estatisticas.getModulosPendentes());
        assertEquals(0.0, estatisticas.getPercentualConcluido());
    }
}