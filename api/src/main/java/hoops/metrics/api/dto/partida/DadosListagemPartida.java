package hoops.metrics.api.dto.partida;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Partida;

import java.time.LocalDateTime;

public record DadosListagemPartida(
        Long id,
        Clube timeCasa,
        Clube timeVisitante,
        LocalDateTime dataHora,
        String local
) {
    public DadosListagemPartida(Partida partida) {
        this(
                partida.getId(),
                partida.getClubeDaCasa(),
                partida.getClubeVisitante(),
                partida.getDataHora(),
                partida.getLocal()
        );
    }
}
