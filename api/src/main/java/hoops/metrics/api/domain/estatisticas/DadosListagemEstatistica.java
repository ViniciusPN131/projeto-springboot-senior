package hoops.metrics.api.domain.estatisticas;

public record DadosListagemEstatistica(
        Long id,
        String jogador,
        String partida,
        int totalPontos,
        int totalFaltas
) {
    public DadosListagemEstatistica(hoops.metrics.api.domain.estatisticas.Estatistica estatistica) {
        this(
                estatistica.getId(),
                estatistica.getJogador().getNome(),
                "Partida #" + estatistica.getPartida().getId(),
                estatistica.getTotalPontos(),
                estatistica.getTotalFaltas()
        );
    }
}
