package br.ufpr.tads.msprogresso.application.web;

import br.ufpr.tads.msprogresso.application.dto.InscricaoDTO;
import br.ufpr.tads.msprogresso.domain.model.Inscricao;
import br.ufpr.tads.msprogresso.domain.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inscricoes")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoesPorFuncionario(@PathVariable String funcionarioId) {
        List<Inscricao> inscricoes = inscricaoService.listarInscricoesPorFuncionario(funcionarioId);
        List<InscricaoDTO> inscricoesDTO = inscricoes.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(inscricoesDTO);
    }

    @GetMapping("/funcionario/{funcionarioId}/curso/{cursoId}")
    public ResponseEntity<InscricaoDTO> buscarInscricao(@PathVariable String funcionarioId, @PathVariable String cursoId) {
        return inscricaoService.buscarInscricaoPorFuncionarioECurso(funcionarioId, cursoId)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/funcionario/{funcionarioId}/curso/{cursoId}")
    public ResponseEntity<InscricaoDTO> criarInscricao(@PathVariable String funcionarioId, @PathVariable String cursoId) {
        Inscricao inscricao = inscricaoService.criarInscricao(funcionarioId, cursoId);
        return ResponseEntity.ok(toDTO(inscricao));
    }

    @PutMapping("/{inscricaoId}/progresso")
    public ResponseEntity<InscricaoDTO> atualizarProgresso(@PathVariable Long inscricaoId, @RequestParam Double progresso) {
        Inscricao inscricao = inscricaoService.atualizarProgresso(inscricaoId, progresso);
        return ResponseEntity.ok(toDTO(inscricao));
    }

    @PutMapping("/{inscricaoId}/concluir")
    public ResponseEntity<InscricaoDTO> concluirInscricao(@PathVariable Long inscricaoId) {
        Inscricao inscricao = inscricaoService.concluirInscricao(inscricaoId);
        return ResponseEntity.ok(toDTO(inscricao));
    }

    @DeleteMapping("/{inscricaoId}")
    public ResponseEntity<Void> deletarInscricao(@PathVariable Long inscricaoId) {
        inscricaoService.deletarInscricao(inscricaoId);
        return ResponseEntity.noContent().build();
    }

    private InscricaoDTO toDTO(Inscricao inscricao) {
        return new InscricaoDTO(
                inscricao.getId(),
                inscricao.getFuncionarioId(),
                inscricao.getCursoId(),
                inscricao.getDataInscricao(),
                inscricao.getDataInicio(),
                inscricao.getDataConclusao(),
                inscricao.getStatus(),
                inscricao.getProgressoPercentual()
        );
    }
}