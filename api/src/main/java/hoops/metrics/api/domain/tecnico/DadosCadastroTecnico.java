package hoops.metrics.api.domain.tecnico;

import hoops.metrics.api.domain.clube.Clube;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroTecnico(

        @NotBlank
        String nome
//        @NotNull @Valid
//        Clube clube

) {
}
