package hoops.metrics.api.domain.clube;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroClube(
        @NotBlank String nome,
        @NotBlank String sigla,
        @NotBlank String cidade,
        @NotBlank String estado
) {
}
