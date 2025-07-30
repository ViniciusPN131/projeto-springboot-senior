package hoops.metrics.api.infra.exception;

public record ErroValidacaoDTO(
        String campo,
        String mensagem
) {}
