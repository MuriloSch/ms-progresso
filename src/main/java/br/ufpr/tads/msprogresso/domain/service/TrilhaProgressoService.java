package br.ufpr.tads.msprogresso.domain.service;

import br.ufpr.tads.msprogresso.domain.model.TrilhaProgresso;
import br.ufpr.tads.msprogresso.domain.repository.TrilhaProgressoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrilhaProgressoService {

    @Autowired
    private TrilhaProgressoRepository trilhaProgressoRepository;

    public List<TrilhaProgresso> listarTrilhasProgressoPorFuncionario(String funcionarioId) {
        return trilhaProgressoRepository.findByFuncionarioId(funcionarioId);
    }

    public Optional<TrilhaProgresso> buscarTrilhaProgresso(String funcionarioId, String trilhaId) {
        return trilhaProgressoRepository.findByFuncionarioIdAndTrilhaId(funcionarioId, trilhaId);
    }

    // MÉTODO ADICIONADO - ALIAS para buscarTrilhaProgresso
    public Optional<TrilhaProgresso> buscarTrilhaProgressoPorFuncionarioETrilha(String funcionarioId, String trilhaId) {
        return buscarTrilhaProgresso(funcionarioId, trilhaId);
    }

    public TrilhaProgresso criarOuAtualizarTrilhaProgresso(String funcionarioId, String trilhaId, Integer cursosConcluidos, Double progressoPercentual) {
        Optional<TrilhaProgresso> existing = trilhaProgressoRepository.findByFuncionarioIdAndTrilhaId(funcionarioId, trilhaId);
        TrilhaProgresso trilhaProgresso;

        if (existing.isPresent()) {
            trilhaProgresso = existing.get();
            trilhaProgresso.setCursosConcluidos(cursosConcluidos);
            trilhaProgresso.setProgressoPercentual(progressoPercentual);
        } else {
            trilhaProgresso = new TrilhaProgresso(funcionarioId, trilhaId);
            trilhaProgresso.setCursosConcluidos(cursosConcluidos);
            trilhaProgresso.setProgressoPercentual(progressoPercentual);
        }

        return trilhaProgressoRepository.save(trilhaProgresso);
    }

    @Transactional
    public TrilhaProgresso atualizarProgressoTrilha(String funcionarioId, String trilhaId, Integer cursosConcluidos) {
        int totalCursosTrilha = 5; // Valor padrão, deve vir de serviço externo
        double progressoPercentual = (cursosConcluidos.doubleValue() / totalCursosTrilha) * 100.0;

        return criarOuAtualizarTrilhaProgresso(funcionarioId, trilhaId, cursosConcluidos, progressoPercentual);
    }

    @Transactional
    public TrilhaProgresso atualizarProgressoTrilha(String funcionarioId, String trilhaId, Integer cursosConcluidos, Integer totalCursosTrilha) {
        double progressoPercentual = (cursosConcluidos.doubleValue() / totalCursosTrilha.doubleValue()) * 100.0;
        return criarOuAtualizarTrilhaProgresso(funcionarioId, trilhaId, cursosConcluidos, progressoPercentual);
    }

    public void deletarTrilhaProgresso(Long trilhaProgressoId) {
        if (!trilhaProgressoRepository.existsById(trilhaProgressoId)) {
            throw new RuntimeException("Trilha de progresso não encontrada com ID: " + trilhaProgressoId);
        }
        trilhaProgressoRepository.deleteById(trilhaProgressoId);
    }

    // MÉTODOS ADICIONAIS
    public List<TrilhaProgresso> listarTodasTrilhasProgresso() {
        return trilhaProgressoRepository.findAll();
    }

    public Optional<TrilhaProgresso> buscarTrilhaProgressoPorId(Long id) {
        return trilhaProgressoRepository.findById(id);
    }

    public TrilhaProgresso salvarTrilhaProgresso(TrilhaProgresso trilhaProgresso) {
        return trilhaProgressoRepository.save(trilhaProgresso);
    }

    @Transactional
    public TrilhaProgresso atualizarTrilhaProgresso(Long id, TrilhaProgresso trilhaAtualizada) {
        if (!trilhaProgressoRepository.existsById(id)) {
            throw new RuntimeException("Trilha de progresso não encontrada com ID: " + id);
        }
        trilhaAtualizada.setId(id);
        return trilhaProgressoRepository.save(trilhaAtualizada);
    }
}