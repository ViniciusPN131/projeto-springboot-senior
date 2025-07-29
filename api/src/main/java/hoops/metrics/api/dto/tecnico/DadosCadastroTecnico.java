package hoops.metrics.api.dto.tecnico;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroTecnico(

        @NotBlank
        String nome,

        @NotBlank
        String cref

) {
}
