package br.ufpr.tads.msprogresso.application.web;

import br.ufpr.tads.msprogresso.application.dto.ProgressoModuloDTO;
import br.ufpr.tads.msprogresso.domain.model.ProgressoModulo;
import br.ufpr.tads.msprogresso.domain.service.ProgressoModuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/progresso-modulos")
public class ProgressoModuloController {

    @Autowired
    private ProgressoModuloService progressoModuloService;

    @GetMapping("/inscricao/{inscricaoId}")
    public ResponseEntity<List<ProgressoModuloDTO>> listarProgressoModulos(@PathVariable Long inscricaoId) {
        List<ProgressoModulo> progressoModulos = progressoModuloService.listarProgressoModulosPorInscricao(inscricaoId);
        List<ProgressoModuloDTO> progressoModulosDTO = progressoModulos.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(progressoModulosDTO);
    }

    @GetMapping("/inscricao/{inscricaoId}/modulo/{moduloId}")
    public ResponseEntity<ProgressoModuloDTO> buscarProgressoModulo(@PathVariable Long inscricaoId, @PathVariable String moduloId) {
        return progressoModuloService.buscarProgressoModulo(inscricaoId, moduloId)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/inscricao/{inscricaoId}/modulo/{moduloId}/iniciar")
    public ResponseEntity<ProgressoModuloDTO> iniciarModulo(@PathVariable Long inscricaoId, @PathVariable String moduloId) {
        ProgressoModulo progressoModulo = progressoModuloService.iniciarModulo(inscricaoId, moduloId);
        return ResponseEntity.ok(toDTO(progressoModulo));
    }

    @PutMapping("/inscricao/{inscricaoId}/modulo/{moduloId}/concluir")
    public ResponseEntity<ProgressoModuloDTO> concluirModulo(@PathVariable Long inscricaoId, @PathVariable String moduloId) {
        ProgressoModulo progressoModulo = progressoModuloService.concluirModulo(inscricaoId, moduloId);
        return ResponseEntity.ok(toDTO(progressoModulo));
    }

    private ProgressoModuloDTO toDTO(ProgressoModulo progressoModulo) {
        return new ProgressoModuloDTO(
                progressoModulo.getId(),
                progressoModulo.getInscricao().getId(),
                progressoModulo.getModuloId(),
                progressoModulo.getDataInicio(),
                progressoModulo.getDataConclusao(),
                progressoModulo.getTempoGasto()
        );
    }
}