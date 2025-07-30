package hoops.metrics.api.dto.tecnico;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroTecnico(

        @NotBlank
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank
        @Size(min = 6, max = 20, message = "CREF deve ter entre 6 e 20 caracteres")
        String cref

) {
}
