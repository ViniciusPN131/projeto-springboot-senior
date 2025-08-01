package hoops.metrics.api.dto.partida;

import hoops.metrics.api.domain.Partida;

import java.time.LocalDateTime;


public record DadosDetalhamentoPartida(
        Long id,
        Long clubeDaCasaId,
        String clubeCasa,
        Long clubeVisitanteId,
        String clubeVisitante,
        LocalDateTime dataHora,
        String local
) {
    public DadosDetalhamentoPartida(Partida partida) {
        this(
                partida.getId(),
                partida.getClubeDaCasa().getId(),
                partida.getClubeDaCasa().getNome(),
                partida.getClubeVisitante().getId(),
                partida.getClubeVisitante().getNome(),
                partida.getDataHora(),
                partida.getLocal()
        );
    }
}
