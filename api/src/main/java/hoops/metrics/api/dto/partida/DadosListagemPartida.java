package hoops.metrics.api.dto.partida;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Partida;

import java.time.LocalDateTime;

public record DadosListagemPartida(
        Long id,
        Long timeCasa,
        String timeCasaNome,
        String timeCasaSigla,
        Long timeVisitante,
        String timeVisitanteNome,
        String timeVisitanteSigla,
        LocalDateTime dataHora,
        String local
) {
    public DadosListagemPartida(Partida partida) {
        this(
                partida.getId(),
                partida.getClubeDaCasa().getId(),
                partida.getClubeDaCasa().getNome(),
                partida.getClubeDaCasa().getSigla(),
                partida.getClubeVisitante().getId(),
                partida.getClubeVisitante().getNome(),
                partida.getClubeVisitante().getSigla(),
                partida.getDataHora(),
                partida.getLocal()
        );
    }
}
