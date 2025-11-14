package br.ufpr.tads.msprogresso.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificados")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "funcionario_id", nullable = false)
    private String funcionarioId;

    @Column(name = "curso_id", nullable = false)
    private String cursoId;

    @Column(name = "codigo_certificado", nullable = false, unique = true)
    private String codigoCertificado;

    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime dataEmissao;

    @Column(name = "hash_validacao", nullable = false, unique = true)
    private String hashValidacao;

    @Column(name = "url_pdf")
    private String urlPdf;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Construtor
    public Certificado() {
        this.createdAt = LocalDateTime.now();
    }

    public Certificado(String funcionarioId, String cursoId, String codigoCertificado, String hashValidacao) {
        this();
        this.funcionarioId = funcionarioId;
        this.cursoId = cursoId;
        this.codigoCertificado = codigoCertificado;
        this.hashValidacao = hashValidacao;
        this.dataEmissao = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}