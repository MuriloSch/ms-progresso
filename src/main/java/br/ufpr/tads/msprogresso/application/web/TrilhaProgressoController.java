package br.ufpr.tads.msprogresso.application.web;

import br.ufpr.tads.msprogresso.application.dto.TrilhaProgressoDTO;
import br.ufpr.tads.msprogresso.domain.model.TrilhaProgresso;
import br.ufpr.tads.msprogresso.domain.service.TrilhaProgressoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trilhas-progresso")
public class TrilhaProgressoController {

    @Autowired
    private TrilhaProgressoService trilhaProgressoService;

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<TrilhaProgressoDTO>> listarTrilhasProgresso(@PathVariable String funcionarioId) {
        List<TrilhaProgresso> trilhasProgresso = trilhaProgressoService.listarTrilhasProgressoPorFuncionario(funcionarioId);
        List<TrilhaProgressoDTO> trilhasProgressoDTO = trilhasProgresso.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(trilhasProgressoDTO);
    }

    @GetMapping("/funcionario/{funcionarioId}/trilha/{trilhaId}")
    public ResponseEntity<TrilhaProgressoDTO> buscarTrilhaProgresso(@PathVariable String funcionarioId, @PathVariable String trilhaId) {
        return trilhaProgressoService.buscarTrilhaProgresso(funcionarioId, trilhaId)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/funcionario/{funcionarioId}/trilha/{trilhaId}")
    public ResponseEntity<TrilhaProgressoDTO> buscarTrilhaProgressoPorFuncionarioETrilha(
            @PathVariable String funcionarioId, 
            @PathVariable String trilhaId) {
        Optional<TrilhaProgresso> trilhaProgresso = trilhaProgressoService.buscarTrilhaProgressoPorFuncionarioETrilha(funcionarioId, trilhaId);
        return trilhaProgresso.map(t -> ResponseEntity.ok(toDTO(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/funcionario/{funcionarioId}/trilha/{trilhaId}/atualizar")
    public ResponseEntity<TrilhaProgressoDTO> atualizarProgressoTrilha(
            @PathVariable String funcionarioId,
            @PathVariable String trilhaId,
            @RequestParam Integer cursosConcluidos) {
        TrilhaProgresso trilhaProgresso = trilhaProgressoService.atualizarProgressoTrilha(funcionarioId, trilhaId, cursosConcluidos);
        return ResponseEntity.ok(toDTO(trilhaProgresso));
    }

    @DeleteMapping("/{trilhaProgressoId}")
    public ResponseEntity<Void> deletarTrilhaProgresso(@PathVariable Long trilhaProgressoId) {
        trilhaProgressoService.deletarTrilhaProgresso(trilhaProgressoId);
        return ResponseEntity.noContent().build();
    }

    private TrilhaProgressoDTO toDTO(TrilhaProgresso trilhaProgresso) {
        return new TrilhaProgressoDTO(
                trilhaProgresso.getId(),
                trilhaProgresso.getFuncionarioId(),
                trilhaProgresso.getTrilhaId(),
                trilhaProgresso.getCursosConcluidos(),
                trilhaProgresso.getProgressoPercentual(),
                trilhaProgresso.getCreatedAt(),
                trilhaProgresso.getUpdatedAt()
        );
    }
}