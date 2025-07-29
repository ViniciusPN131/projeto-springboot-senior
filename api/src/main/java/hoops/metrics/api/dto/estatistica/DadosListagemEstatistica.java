package hoops.metrics.api.dto.estatistica;

import hoops.metrics.api.domain.Estatistica;

public record DadosListagemEstatistica(
        Long id,
        String jogador,
        String partida,
        Integer totalPontos,
        Integer totalFaltas,
        Integer rebotesOfensivos,
        Integer rebotesDefensivos,
        Integer roubosDeBola,
        Integer turnovers
) {

    public DadosListagemEstatistica(Estatistica estatistica) {
        this(
                estatistica.getId(),
                estatistica.getJogador().getNome(),
                "Partida #" + estatistica.getPartida().getId(),
                estatistica.getTotalPontos(),
                estatistica.getTotalFaltas(),
                estatistica.getRebotesOfensivos(),
                estatistica.getRebotesDefensivos(),
                estatistica.getRoubosDeBola(),
                estatistica.getTurnovers()
        );
    }
}
