package hoops.metrics.api.dto.partida;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Estatistica;

public record DadosMvpPartida(
        Long jogadorId,
        String nomeJogador,
        Long clubeId,
        String clubeJogador,
        int totalPontos,
        int assistencias,
        int totalFaltas,
        int rebotesOfensivos,
        int rebotesDefensivos,
        int roubosDeBola,
        int turnovers

) {
}
