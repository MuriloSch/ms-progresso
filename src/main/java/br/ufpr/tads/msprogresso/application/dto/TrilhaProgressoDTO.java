package br.ufpr.tads.msprogresso.application.dto;

import java.time.LocalDateTime;

public class TrilhaProgressoDTO {

    private Long id;
    private String funcionarioId;
    private String trilhaId;
    private Integer cursosConcluidos;
    private Double progressoPercentual;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Construtores
    public TrilhaProgressoDTO() {
    }

    public TrilhaProgressoDTO(Long id, String funcionarioId, String trilhaId, Integer cursosConcluidos, 
                             Double progressoPercentual, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.trilhaId = trilhaId;
        this.cursosConcluidos = cursosConcluidos;
        this.progressoPercentual = progressoPercentual;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
}