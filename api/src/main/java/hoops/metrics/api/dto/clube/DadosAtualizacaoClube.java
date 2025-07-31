package hoops.metrics.api.dto.clube;

import hoops.metrics.api.domain.Tecnico;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoClube(
        @NotNull Long id,
        String nome,
        String sigla,
        String cidade,
        String estado,
        Tecnico tecnico) {
}
