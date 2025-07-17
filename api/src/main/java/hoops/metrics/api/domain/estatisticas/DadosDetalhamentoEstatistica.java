package hoops.metrics.api.domain.estatisticas;

public record DadosDetalhamentoEstatistica(
        Long id,
        String jogador,
        Long partidaId,
        int totalPontos,
        int totalFaltas,
        int rebotesOfensivos,
        int rebotesDefensivos,
        int roubosDeBola,
        int turnovers
) {
    public DadosDetalhamentoEstatistica(hoops.metrics.api.domain.estatisticas.Estatistica estatistica) {
        this(
                estatistica.getId(),
                estatistica.getJogador().getNome(),
                estatistica.getPartida().getId(),
                estatistica.getTotalPontos(),
                estatistica.getTotalFaltas(),
                estatistica.getRebotesOfensivos(),
                estatistica.getRebotesDefensivos(),
                estatistica.getRoubosDeBola(),
                estatistica.getTurnovers()
        );
    }
}
