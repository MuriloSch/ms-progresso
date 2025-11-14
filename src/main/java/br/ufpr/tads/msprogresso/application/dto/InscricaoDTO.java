package br.ufpr.tads.msprogresso.application.dto;

import java.time.LocalDateTime;

public class InscricaoDTO {

    private Long id;
    private String funcionarioId;
    private String cursoId;
    private LocalDateTime dataInscricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    private String status;
    private Double progressoPercentual;

    // Construtores
    public InscricaoDTO() {
    }

    public InscricaoDTO(Long id, String funcionarioId, String cursoId, LocalDateTime dataInscricao, 
                       LocalDateTime dataInicio, LocalDateTime dataConclusao, String status, Double progressoPercentual) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.cursoId = cursoId;
        this.dataInscricao = dataInscricao;
        this.dataInicio = dataInicio;
        this.dataConclusao = dataConclusao;
        this.status = status;
        this.progressoPercentual = progressoPercentual;
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

    public String getCursoId() {
        return cursoId;
    }

    public void setCursoId(String cursoId) {
        this.cursoId = cursoId;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getProgressoPercentual() {
        return progressoPercentual;
    }

    public void setProgressoPercentual(Double progressoPercentual) {
        this.progressoPercentual = progressoPercentual;
    }
}