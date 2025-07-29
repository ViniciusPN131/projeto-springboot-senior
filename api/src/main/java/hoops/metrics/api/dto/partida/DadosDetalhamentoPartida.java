package hoops.metrics.api.dto.partida;

import hoops.metrics.api.domain.Partida;

import java.time.LocalDateTime;

public record DadosDetalhamentoPartida(
        Long id,
        String timeCasa,
        String timeVisitante,
        LocalDateTime dataHora,
        String local,
        boolean ativo
) {
    public DadosDetalhamentoPartida(Partida partida) {
        this(
                partida.getId(),
                partida.getClubeDaCasa().getNome(),
                partida.getClubeVisitante().getNome(),
                partida.getDataHora(),
                partida.getLocal(),
                partida.isAtivo()
        );
    }
}
