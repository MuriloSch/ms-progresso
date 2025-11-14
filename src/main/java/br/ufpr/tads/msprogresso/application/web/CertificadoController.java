package br.ufpr.tads.msprogresso.application.web;

import br.ufpr.tads.msprogresso.application.dto.CertificadoDTO;
import br.ufpr.tads.msprogresso.domain.model.Certificado;
import br.ufpr.tads.msprogresso.domain.service.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/certificados")
public class CertificadoController {

    @Autowired
    private CertificadoService certificadoService;

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<CertificadoDTO>> listarCertificadosPorFuncionario(@PathVariable String funcionarioId) {
        List<Certificado> certificados = certificadoService.listarCertificadosPorFuncionario(funcionarioId);
        List<CertificadoDTO> certificadosDTO = certificados.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(certificadosDTO);
    }

    @GetMapping("/codigo/{codigoCertificado}")
    public ResponseEntity<CertificadoDTO> buscarCertificadoPorCodigo(@PathVariable String codigoCertificado) {
        return certificadoService.buscarCertificadoPorCodigo(codigoCertificado)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hash/{hashValidacao}")
    public ResponseEntity<CertificadoDTO> buscarCertificadoPorHash(@PathVariable String hashValidacao) {
        return certificadoService.buscarCertificadoPorHash(hashValidacao)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/funcionario/{funcionarioId}/curso/{cursoId}")
    public ResponseEntity<CertificadoDTO> emitirCertificado(@PathVariable String funcionarioId, @PathVariable String cursoId) {
        Certificado certificado = certificadoService.emitirCertificado(funcionarioId, cursoId);
        return ResponseEntity.ok(toDTO(certificado));
    }

    @DeleteMapping("/{certificadoId}")
    public ResponseEntity<Void> deletarCertificado(@PathVariable Long certificadoId) {
        certificadoService.deletarCertificado(certificadoId);
        return ResponseEntity.noContent().build();
    }

    private CertificadoDTO toDTO(Certificado certificado) {
        return new CertificadoDTO(
                certificado.getId(),
                certificado.getFuncionarioId(),
                certificado.getCursoId(),
                certificado.getCodigoCertificado(),
                certificado.getDataEmissao(),
                certificado.getHashValidacao(),
                certificado.getUrlPdf()
        );
    }
}