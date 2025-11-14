package br.ufpr.tads.msprogresso.application.dto;

import java.time.LocalDateTime;

public class CertificadoDTO {

    private Long id;
    private String funcionarioId;
    private String cursoId;
    private String codigoCertificado;
    private LocalDateTime dataEmissao;
    private String hashValidacao;
    private String urlPdf;

    // Construtores
    public CertificadoDTO() {
    }

    public CertificadoDTO(Long id, String funcionarioId, String cursoId, String codigoCertificado, 
                         LocalDateTime dataEmissao, String hashValidacao, String urlPdf) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.cursoId = cursoId;
        this.codigoCertificado = codigoCertificado;
        this.dataEmissao = dataEmissao;
        this.hashValidacao = hashValidacao;
        this.urlPdf = urlPdf;
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

    public String getCodigoCertificado() {
        return codigoCertificado;
    }

    public void setCodigoCertificado(String codigoCertificado) {
        this.codigoCertificado = codigoCertificado;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getHashValidacao() {
        return hashValidacao;
    }

    public void setHashValidacao(String hashValidacao) {
        this.hashValidacao = hashValidacao;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }
}