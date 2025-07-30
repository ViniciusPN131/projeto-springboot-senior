package hoops.metrics.api.dto.estatistica;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroEstatistica(
        @NotNull Long jogadorId,
        @NotNull Long partidaId,
        @NotNull @Size(min = 0) int totalPontos,
        @NotNull @Size(min = 0) int totalFaltas,
        @NotNull @Size(min = 0) int rebotesOfensivos,
        @NotNull @Size(min = 0) int rebotesDefensivos,
        @NotNull @Size(min = 0) int roubosDeBola,
        @NotNull @Size(min = 0) int turnovers
) {
}
