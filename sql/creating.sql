DROP TRIGGER IF EXISTS trigger_integridade_registro ON Registro;
DROP FUNCTION IF EXISTS verifica_integridade_registro();
DROP TABLE IF EXISTS Registro;
DROP TABLE IF EXISTS Usuario;

CREATE TABLE Usuario(
    username VARCHAR(256),
    nome VARCHAR(256) NOT NULL,
    senha TEXT NOT NULL,
    CONSTRAINT PK_USUARIO PRIMARY KEY (username)
);

CREATE TABLE Registro(
    usuario VARCHAR(256),
    data DATE,
    hora TIME,
    tipo VARCHAR(16) NOT NULL,
    CONSTRAINT PK_REGISTRO PRIMARY KEY (usuario, data, hora),
    CONSTRAINT FK_REGISTRO FOREIGN KEY (usuario) REFERENCES Usuario(username),
    CONSTRAINT CK_TIPO CHECK (tipo IN ('ENTRADA', 'SAIDA'))
);

CREATE OR REPLACE FUNCTION verifica_integridade_registro() RETURNS TRIGGER AS $$
DECLARE 
    ultimo_tipo VARCHAR(16);
BEGIN
    SELECT tipo INTO ultimo_tipo
    FROM REGISTRO
    WHERE usuario = NEW.usuario
    ORDER BY (data,hora) DESC
    LIMIT 1;

    IF ultimo_tipo = NEW.tipo THEN
        RAISE EXCEPTION 'Não é permitido registrar duas entradas ou duas saídas consecutivas.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_integridade_registro
BEFORE INSERT ON Registro
FOR EACH ROW
EXECUTE FUNCTION verifica_integridade_registro();