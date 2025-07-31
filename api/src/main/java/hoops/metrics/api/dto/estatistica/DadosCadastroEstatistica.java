package hoops.metrics.api.dto.estatistica;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroEstatistica(
        @NotNull Long jogadorId,
        @NotNull Long partidaId,
        @NotNull int totalPontos,
        @NotNull int assistencias,
        @NotNull int totalFaltas,
        @NotNull int rebotesOfensivos,
        @NotNull int rebotesDefensivos,
        @NotNull int roubosDeBola,
        @NotNull int turnovers
) {
}
