package hoops.metrics.api.domain.clube;

import hoops.metrics.api.domain.tecnico.Tecnico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroClube(
        @NotBlank String nome,
        @NotBlank String sigla,
        @NotBlank String cidade,
        @NotBlank String estado,
        @NotNull Long tecnico_id,
        Tecnico tecnico
        ) {
}
