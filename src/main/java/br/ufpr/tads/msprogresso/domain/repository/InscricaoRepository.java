package br.ufpr.tads.msprogresso.domain.repository;

import br.ufpr.tads.msprogresso.domain.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByFuncionarioId(String funcionarioId);

    List<Inscricao> findByCursoId(String cursoId);

    Optional<Inscricao> findByFuncionarioIdAndCursoId(String funcionarioId, String cursoId);

    @Query("SELECT i FROM Inscricao i WHERE i.funcionarioId = :funcionarioId AND i.status = :status")
    List<Inscricao> findByFuncionarioIdAndStatus(@Param("funcionarioId") String funcionarioId, @Param("status") String status);

    @Query("SELECT COUNT(i) FROM Inscricao i WHERE i.cursoId = :cursoId AND i.status = 'CONCLUIDO'")
    Long countConcluidasByCursoId(@Param("cursoId") String cursoId);

    @Query("SELECT i FROM Inscricao i WHERE i.funcionarioId = :funcionarioId AND i.cursoId = :cursoId")
    Optional<Inscricao> findInscricao(@Param("funcionarioId") String funcionarioId, @Param("cursoId") String cursoId);
}