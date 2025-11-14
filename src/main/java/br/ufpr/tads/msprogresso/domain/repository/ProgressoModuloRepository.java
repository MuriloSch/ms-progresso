package br.ufpr.tads.msprogresso.domain.repository;

import br.ufpr.tads.msprogresso.domain.model.ProgressoModulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressoModuloRepository extends JpaRepository<ProgressoModulo, Long> {

    List<ProgressoModulo> findByInscricaoId(Long inscricaoId);

    Optional<ProgressoModulo> findByInscricaoIdAndModuloId(Long inscricaoId, String moduloId);

    @Query("SELECT COUNT(pm) FROM ProgressoModulo pm WHERE pm.inscricao.id = :inscricaoId AND pm.dataConclusao IS NOT NULL")
    Long countModulosConcluidosByInscricaoId(@Param("inscricaoId") Long inscricaoId);

    // MÉTODO ADICIONADO AQUI ↓
    @Query("SELECT COUNT(pm) FROM ProgressoModulo pm WHERE pm.inscricao.id = :inscricaoId")
    Long countByInscricaoId(@Param("inscricaoId") Long inscricaoId);

    @Query("SELECT pm FROM ProgressoModulo pm WHERE pm.inscricao.id = :inscricaoId AND pm.dataConclusao IS NULL")
    List<ProgressoModulo> findModulosPendentesByInscricaoId(@Param("inscricaoId") Long inscricaoId);

    @Query("SELECT pm FROM ProgressoModulo pm WHERE pm.inscricao.id = :inscricaoId AND pm.dataConclusao IS NOT NULL")
    List<ProgressoModulo> findModulosConcluidosByInscricaoId(@Param("inscricaoId") Long inscricaoId);
}