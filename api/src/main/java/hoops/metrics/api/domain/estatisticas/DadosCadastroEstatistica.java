package hoops.metrics.api.domain.estatisticas;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroEstatistica(
        @NotNull Long jogadorId,
        @NotNull Long partidaId,
        @NotNull int totalPontos,
        @NotNull int totalFaltas,
        @NotNull int rebotesOfensivos,
        @NotNull int rebotesDefensivos,
        @NotNull int roubosDeBola,
        @NotNull int turnovers
) {}
