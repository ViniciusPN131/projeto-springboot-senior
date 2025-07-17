ALTER TABLE clubes
ADD CONSTRAINT unique_nome UNIQUE (nome),
ADD CONSTRAINT unique_sigla UNIQUE (sigla);
