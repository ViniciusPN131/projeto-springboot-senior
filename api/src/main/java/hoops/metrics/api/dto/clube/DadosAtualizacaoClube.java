package hoops.metrics.api.dto.clube;

import hoops.metrics.api.domain.Tecnico;

public record DadosAtualizacaoClube(Long id, String nome, String sigla, String cidade, String estado, Tecnico tecnico) {
}
