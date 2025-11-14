package br.ufpr.tads.msprogresso.domain.service;

import br.ufpr.tads.msprogresso.domain.model.Certificado;
import br.ufpr.tads.msprogresso.domain.repository.CertificadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;

    public List<Certificado> listarCertificadosPorFuncionario(String funcionarioId) {
        return certificadoRepository.findByFuncionarioId(funcionarioId);
    }

    public Optional<Certificado> buscarCertificadoPorCodigo(String codigoCertificado) {
        return certificadoRepository.findByCodigoCertificado(codigoCertificado);
    }

    public Optional<Certificado> buscarCertificadoPorHash(String hashValidacao) {
        return certificadoRepository.findByHashValidacao(hashValidacao);
    }

    @Transactional
    public Certificado emitirCertificado(String funcionarioId, String cursoId) {
        Optional<Certificado> existing = certificadoRepository.findByFuncionarioIdAndCursoId(funcionarioId, cursoId);
        if (existing.isPresent()) {
            return existing.get();
        }

        String codigoCertificado = gerarCodigoCertificado();
        String hashValidacao = gerarHashValidacao();

        Certificado certificado = new Certificado();
        certificado.setFuncionarioId(funcionarioId);
        certificado.setCursoId(cursoId);
        certificado.setCodigoCertificado(codigoCertificado);
        certificado.setHashValidacao(hashValidacao);
        certificado.setDataEmissao(LocalDateTime.now());

        return certificadoRepository.save(certificado);
    }

    private String gerarCodigoCertificado() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String gerarHashValidacao() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public void deletarCertificado(Long certificadoId) {
        if (!certificadoRepository.existsById(certificadoId)) {
            throw new RuntimeException("Certificado não encontrado com ID: " + certificadoId);
        }
        certificadoRepository.deleteById(certificadoId);
    }

    // MÉTODOS NOVOS ADICIONADOS
    public List<Certificado> listarTodosCertificados() {
        return certificadoRepository.findAll();
    }

    public Optional<Certificado> buscarCertificadoPorId(Long id) {
        return certificadoRepository.findById(id);
    }

    public Certificado salvarCertificado(Certificado certificado) {
        return certificadoRepository.save(certificado);
    }

    @Transactional
    public Certificado atualizarCertificado(Long id, Certificado certificadoAtualizado) {
        if (!certificadoRepository.existsById(id)) {
            throw new RuntimeException("Certificado não encontrado com ID: " + id);
        }
        certificadoAtualizado.setId(id);
        return certificadoRepository.save(certificadoAtualizado);
    }

    public Optional<Certificado> buscarCertificadoPorFuncionarioECurso(String funcionarioId, String cursoId) {
        return certificadoRepository.findByFuncionarioIdAndCursoId(funcionarioId, cursoId);
    }
}