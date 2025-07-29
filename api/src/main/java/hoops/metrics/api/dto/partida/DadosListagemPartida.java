package hoops.metrics.api.dto.partida;

import hoops.metrics.api.domain.Partida;

import java.time.LocalDateTime;

public record DadosListagemPartida(
        Long id,
        String timeCasa,
        String timeVisitante,
        LocalDateTime dataHora,
        String local
) {
    public DadosListagemPartida(Partida partida) {
        this(
                partida.getId(),
                partida.getClubeDaCasa().getNome(),
                partida.getClubeVisitante().getNome(),
                partida.getDataHora(),
                partida.getLocal()
        );
    }
}
