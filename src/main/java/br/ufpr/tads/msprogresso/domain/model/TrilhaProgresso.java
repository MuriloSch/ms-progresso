package br.ufpr.tads.msprogresso.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trilhas_progresso")
public class TrilhaProgresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "funcionario_id", nullable = false)
    private String funcionarioId;

    @Column(name = "trilha_id", nullable = false)
    private String trilhaId;

    @Column(name = "cursos_concluidos")
    private Integer cursosConcluidos = 0;

    @Column(name = "progresso_percentual")
    private Double progressoPercentual = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtor
    public TrilhaProgresso() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public TrilhaProgresso(String funcionarioId, String trilhaId) {
        this();
        this.funcionarioId = funcionarioId;
        this.trilhaId = trilhaId;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(String funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getTrilhaId() {
        return trilhaId;
    }

    public void setTrilhaId(String trilhaId) {
        this.trilhaId = trilhaId;
    }

    public Integer getCursosConcluidos() {
        return cursosConcluidos;
    }

    public void setCursosConcluidos(Integer cursosConcluidos) {
        this.cursosConcluidos = cursosConcluidos;
    }

    public Double getProgressoPercentual() {
        return progressoPercentual;
    }

    public void setProgressoPercentual(Double progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}