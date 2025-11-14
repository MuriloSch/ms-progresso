package br.ufpr.tads.msprogresso.application.dto;

import java.time.LocalDateTime;

public class ProgressoModuloDTO {

    private Long id;
    private Long inscricaoId;
    private String moduloId;
    private LocalDateTime dataInicio;
    private LocalDateTime dataConclusao;
    private Integer tempoGasto;

    // Construtores
    public ProgressoModuloDTO() {
    }

    public ProgressoModuloDTO(Long id, Long inscricaoId, String moduloId, LocalDateTime dataInicio, 
                             LocalDateTime dataConclusao, Integer tempoGasto) {
        this.id = id;
        this.inscricaoId = inscricaoId;
        this.moduloId = moduloId;
        this.dataInicio = dataInicio;
        this.dataConclusao = dataConclusao;
        this.tempoGasto = tempoGasto;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInscricaoId() {
        return inscricaoId;
    }

    public void setInscricaoId(Long inscricaoId) {
        this.inscricaoId = inscricaoId;
    }

    public String getModuloId() {
        return moduloId;
    }

    public void setModuloId(String moduloId) {
        this.moduloId = moduloId;
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

    public Integer getTempoGasto() {
        return tempoGasto;
    }

    public void setTempoGasto(Integer tempoGasto) {
        this.tempoGasto = tempoGasto;
    }
}