package hoops.metrics.api.domain.estatisticas;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoEstatistica(
        @NotNull Long id,
        Integer totalPontos,
        Integer totalFaltas,
        Integer rebotesOfensivos,
        Integer rebotesDefensivos,
        Integer roubosDeBola,
        Integer turnovers
) {}
