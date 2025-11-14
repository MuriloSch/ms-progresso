-- Tabelas do MS Progresso
CREATE TABLE IF NOT EXISTS inscricoes (
    id BIGSERIAL PRIMARY KEY,
    funcionario_id VARCHAR(255) NOT NULL,
    curso_id VARCHAR(255) NOT NULL,
    data_inscricao TIMESTAMP NOT NULL DEFAULT NOW(),
    data_inicio TIMESTAMP,
    data_conclusao TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    progresso_percentual DECIMAL(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS progresso_modulos (
    id BIGSERIAL PRIMARY KEY,
    inscricao_id BIGINT NOT NULL REFERENCES inscricoes(id),
    modulo_id VARCHAR(255) NOT NULL,
    data_inicio TIMESTAMP,
    data_conclusao TIMESTAMP,
    tempo_gasto INTEGER, -- em minutos
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS certificados (
    id BIGSERIAL PRIMARY KEY,
    funcionario_id VARCHAR(255) NOT NULL,
    curso_id VARCHAR(255) NOT NULL,
    codigo_certificado VARCHAR(255) UNIQUE NOT NULL,
    data_emissao TIMESTAMP NOT NULL DEFAULT NOW(),
    hash_validacao VARCHAR(255) UNIQUE NOT NULL,
    url_pdf VARCHAR(500),
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS trilhas_progresso (
    id BIGSERIAL PRIMARY KEY,
    funcionario_id VARCHAR(255) NOT NULL,
    trilha_id VARCHAR(255) NOT NULL,
    cursos_concluidos INTEGER DEFAULT 0,
    progresso_percentual DECIMAL(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(funcionario_id, trilha_id)
);

-- Índices para performance
CREATE INDEX idx_inscricoes_funcionario ON inscricoes(funcionario_id);
CREATE INDEX idx_inscricoes_curso ON inscricoes(curso_id);
CREATE INDEX idx_inscricoes_status ON inscricoes(status);
CREATE INDEX idx_progresso_inscricao ON progresso_modulos(inscricao_id);
CREATE INDEX idx_certificados_funcionario ON certificados(funcionario_id);
CREATE INDEX idx_certificados_hash ON certificados(hash_validacao);