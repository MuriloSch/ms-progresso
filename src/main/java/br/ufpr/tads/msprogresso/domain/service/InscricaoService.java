package br.ufpr.tads.msprogresso.domain.service;

import br.ufpr.tads.msprogresso.domain.model.Inscricao;
import br.ufpr.tads.msprogresso.domain.repository.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    // MÉTODOS EXISTENTES
    public List<Inscricao> listarInscricoesPorFuncionario(String funcionarioId) {
        return inscricaoRepository.findByFuncionarioId(funcionarioId);
    }

    public Optional<Inscricao> buscarInscricaoPorFuncionarioECurso(String funcionarioId, String cursoId) {
        return inscricaoRepository.findByFuncionarioIdAndCursoId(funcionarioId, cursoId);
    }

    public Inscricao criarInscricao(String funcionarioId, String cursoId) {
        Inscricao inscricao = new Inscricao(funcionarioId, cursoId, "EM_ANDAMENTO");
        return inscricaoRepository.save(inscricao);
    }

    @Transactional
    public Inscricao atualizarProgresso(Long inscricaoId, Double progressoPercentual) {
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        inscricao.setProgressoPercentual(progressoPercentual);
        return inscricaoRepository.save(inscricao);
    }

    @Transactional
    public Inscricao concluirInscricao(Long inscricaoId) {
        Inscricao inscricao = inscricaoRepository.findById(inscricaoId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada"));
        inscricao.setStatus("CONCLUIDO");
        inscricao.setDataConclusao(LocalDateTime.now());
        inscricao.setProgressoPercentual(100.0);
        return inscricaoRepository.save(inscricao);
    }

    public void deletarInscricao(Long inscricaoId) {
        if (!inscricaoRepository.existsById(inscricaoId)) {
            throw new RuntimeException("Inscrição não encontrada com ID: " + inscricaoId);
        }
        inscricaoRepository.deleteById(inscricaoId);
    }

    // MÉTODOS NOVOS ADICIONADOS
    public List<Inscricao> listarTodasInscricoes() {
        return inscricaoRepository.findAll();
    }

    public Optional<Inscricao> buscarInscricaoPorId(Long id) {
        return inscricaoRepository.findById(id);
    }

    public Inscricao salvarInscricao(Inscricao inscricao) {
        return inscricaoRepository.save(inscricao);
    }

    @Transactional
    public Inscricao atualizarInscricao(Long id, Inscricao inscricaoAtualizada) {
        if (!inscricaoRepository.existsById(id)) {
            throw new RuntimeException("Inscrição não encontrada com ID: " + id);
        }
        inscricaoAtualizada.setId(id);
        return inscricaoRepository.save(inscricaoAtualizada);
    }

    public List<Inscricao> listarInscricoesPorCurso(String cursoId) {
        return inscricaoRepository.findByCursoId(cursoId);
    }
}