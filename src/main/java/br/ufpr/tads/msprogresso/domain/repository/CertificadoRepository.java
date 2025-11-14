package br.ufpr.tads.msprogresso.domain.repository;

import br.ufpr.tads.msprogresso.domain.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    List<Certificado> findByFuncionarioId(String funcionarioId);

    Optional<Certificado> findByCodigoCertificado(String codigoCertificado);

    Optional<Certificado> findByHashValidacao(String hashValidacao);

    @Query("SELECT c FROM Certificado c WHERE c.funcionarioId = :funcionarioId AND c.cursoId = :cursoId")
    Optional<Certificado> findByFuncionarioIdAndCursoId(@Param("funcionarioId") String funcionarioId, @Param("cursoId") String cursoId);
}