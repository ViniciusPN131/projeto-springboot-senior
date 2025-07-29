package hoops.metrics.api.dto.estatistica;

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
