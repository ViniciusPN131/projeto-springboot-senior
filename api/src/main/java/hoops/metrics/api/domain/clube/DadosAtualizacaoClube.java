package hoops.metrics.api.domain.clube;

import hoops.metrics.api.domain.tecnico.Tecnico;

public record DadosAtualizacaoClube(Long id, String nome, String sigla, String cidade, String estado, Tecnico tecnico) {
}
