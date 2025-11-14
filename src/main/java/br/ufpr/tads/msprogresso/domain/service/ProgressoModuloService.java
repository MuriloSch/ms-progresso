package br.ufpr.tads.msprogresso.domain.service;

import br.ufpr.tads.msprogresso.domain.model.Inscricao;
import br.ufpr.tads.msprogresso.domain.model.ProgressoModulo;
import br.ufpr.tads.msprogresso.domain.repository.InscricaoRepository;
import br.ufpr.tads.msprogresso.domain.repository.ProgressoModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressoModuloService {

    @Autowired
    private ProgressoModuloRepository progressoModuloRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    public List<ProgressoModulo> listarProgressoModulosPorInscricao(Long inscricaoId) {
        return progressoModuloRepository.findByInscricaoId(inscricaoId);
    }

    public Optional<ProgressoModulo> buscarProgressoModulo(Long inscricaoId, String moduloId) {
        return progressoModuloRepository.findByInscricaoIdAndModuloId(inscricaoId, moduloId);
    }

    @Transactional
    public ProgressoModulo iniciarModulo(Long inscricaoId, String moduloId) {
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada com ID: " + inscricaoId));

        Optional<ProgressoModulo> existing = progressoModuloRepository.findByInscricaoIdAndModuloId(inscricaoId, moduloId);
        if (existing.isPresent()) {
            ProgressoModulo progressoExistente = existing.get();
            if (progressoExistente.getDataConclusao() != null) {
                throw new RuntimeException("Módulo já concluído: " + moduloId);
            }
            return progressoExistente;
        }

        ProgressoModulo progressoModulo = new ProgressoModulo();
        progressoModulo.setInscricao(inscricao);
        progressoModulo.setModuloId(moduloId);
        progressoModulo.setDataInicio(LocalDateTime.now());

        return progressoModuloRepository.save(progressoModulo);
    }

    @Transactional
    public ProgressoModulo concluirModulo(Long inscricaoId, String moduloId) {
        ProgressoModulo progressoModulo = progressoModuloRepository.findByInscricaoIdAndModuloId(inscricaoId, moduloId)
                .orElseThrow(() -> new RuntimeException("Progresso do módulo não encontrado para inscrição: " + inscricaoId + " e módulo: " + moduloId));

        if (progressoModulo.getDataConclusao() != null) {
            throw new RuntimeException("Módulo já concluído: " + moduloId);
        }

        progressoModulo.setDataConclusao(LocalDateTime.now());

        if (progressoModulo.getDataInicio() != null) {
            Duration duration = Duration.between(progressoModulo.getDataInicio(), progressoModulo.getDataConclusao());
            progressoModulo.setTempoGasto((int) duration.toMinutes());
        }

        ProgressoModulo moduloSalvo = progressoModuloRepository.save(progressoModulo);
        atualizarProgressoInscricao(inscricaoId);

        return moduloSalvo;
    }

    private void atualizarProgressoInscricao(Long inscricaoId) {
        try {
            Long modulosConcluidos = progressoModuloRepository.countModulosConcluidosByInscricaoId(inscricaoId);
            Long totalModulos = progressoModuloRepository.countByInscricaoId(inscricaoId);

            if (totalModulos > 0) {
                double progresso = (modulosConcluidos.doubleValue() / totalModulos.doubleValue()) * 100.0;
                
                Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
                        .orElseThrow(() -> new RuntimeException("Inscrição não encontrada: " + inscricaoId));
                
                inscricao.setProgressoPercentual(Math.round(progresso * 100.0) / 100.0);
                
                if (modulosConcluidos.equals(totalModulos)) {
                    inscricao.setStatus("CONCLUIDO");
                    inscricao.setDataConclusao(LocalDateTime.now());
                }
                
                inscricaoRepository.save(inscricao);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar progresso da inscrição: " + e.getMessage(), e);
        }
    }

    // MÉTODO NOVO ADICIONADO
    public EstatisticasProgresso obterEstatisticasProgresso(Long inscricaoId) {
        Long totalModulos = progressoModuloRepository.countByInscricaoId(inscricaoId);
        Long modulosConcluidos = progressoModuloRepository.countModulosConcluidosByInscricaoId(inscricaoId);
        List<ProgressoModulo> modulosPendentes = progressoModuloRepository.findModulosPendentesByInscricaoId(inscricaoId);

        return new EstatisticasProgresso(totalModulos, modulosConcluidos, (long) modulosPendentes.size());
    }

    // Classe interna para estatísticas
    public static class EstatisticasProgresso {
        private final Long totalModulos;
        private final Long modulosConcluidos;
        private final Long modulosPendentes;
        private final Double percentualConcluido;

        public EstatisticasProgresso(Long totalModulos, Long modulosConcluidos, Long modulosPendentes) {
            this.totalModulos = totalModulos;
            this.modulosConcluidos = modulosConcluidos;
            this.modulosPendentes = modulosPendentes;
            this.percentualConcluido = totalModulos > 0 ? 
                Math.round((modulosConcluidos.doubleValue() / totalModulos.doubleValue()) * 10000.0) / 100.0 : 0.0;
        }

        // Getters
        public Long getTotalModulos() { return totalModulos; }
        public Long getModulosConcluidos() { return modulosConcluidos; }
        public Long getModulosPendentes() { return modulosPendentes; }
        public Double getPercentualConcluido() { return percentualConcluido; }
    }
}