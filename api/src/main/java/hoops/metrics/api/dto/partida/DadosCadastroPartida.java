package hoops.metrics.api.dto.partida;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;

public record DadosCadastroPartida(
        @NotNull Long timeCasaId,
        @NotNull Long timeVisitanteId,
        @NotNull @Past LocalDateTime dataHora,
        @NotNull String local
) {}
