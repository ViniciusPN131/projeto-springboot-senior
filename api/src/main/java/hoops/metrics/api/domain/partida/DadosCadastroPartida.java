package hoops.metrics.api.domain.partida;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DadosCadastroPartida(
        @NotNull Long timeCasaId,
        @NotNull Long timeVisitanteId,
        @NotNull LocalDateTime dataHora,
        @NotNull String local
) {}
