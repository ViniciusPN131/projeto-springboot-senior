package hoops.metrics.api.dto.clube;

import hoops.metrics.api.domain.Tecnico;
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
