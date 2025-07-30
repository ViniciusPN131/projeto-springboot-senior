package hoops.metrics.api.dto.tecnico;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoTecnico(
        @NotNull
        Long id,
        String nome,
        String cref
) {
}
