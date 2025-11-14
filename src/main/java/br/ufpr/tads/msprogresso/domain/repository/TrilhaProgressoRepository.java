package br.ufpr.tads.msprogresso.domain.repository;

import br.ufpr.tads.msprogresso.domain.model.TrilhaProgresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrilhaProgressoRepository extends JpaRepository<TrilhaProgresso, Long> {

    List<TrilhaProgresso> findByFuncionarioId(String funcionarioId);

    Optional<TrilhaProgresso> findByFuncionarioIdAndTrilhaId(String funcionarioId, String trilhaId);

    @Query("SELECT tp FROM TrilhaProgresso tp WHERE tp.funcionarioId = :funcionarioId AND tp.trilhaId = :trilhaId")
    Optional<TrilhaProgresso> findTrilhaProgresso(@Param("funcionarioId") String funcionarioId, @Param("trilhaId") String trilhaId);
}