package hoops.metrics.api.dto.estatistica;

import hoops.metrics.api.domain.Estatistica;

public record DadosDetalhamentoEstatistica(
        Long id,
        Long jogadorId,
        Long partidaId,
        int totalPontos,
        int assistencias,
        int totalFaltas,
        int rebotesOfensivos,
        int rebotesDefensivos,
        int roubosDeBola,
        int turnovers
) {
    public DadosDetalhamentoEstatistica(Estatistica estatistica) {
        this(
                estatistica.getId(),
                estatistica.getJogador().getId(),
                estatistica.getPartida().getId(),
                estatistica.getTotalPontos(),
                estatistica.getAssistencias(),
                estatistica.getTotalFaltas(),
                estatistica.getRebotesOfensivos(),
                estatistica.getRebotesDefensivos(),
                estatistica.getRoubosDeBola(),
                estatistica.getTurnovers()
        );
    }
}
