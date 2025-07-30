package hoops.metrics.api.dto.clube;

import hoops.metrics.api.domain.Tecnico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroClube(

        @NotBlank
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank
        @Size(min = 3, max = 5, message = "Sigla deve ter entre 3 e 5 caracteres")
        String sigla,

        @NotBlank
        String cidade,

        @NotBlank String estado,

        @NotNull Long tecnico_id,

        Tecnico tecnico

        ) {
}
