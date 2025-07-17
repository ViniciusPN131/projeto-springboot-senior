package hoops.metrics.api.domain.partida;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DadosAtualizacaoPartida(
        @NotNull Long id,
        Long timeCasaId,
        Long timeVisitanteId,
        LocalDateTime dataHora,
        String local
) {}
