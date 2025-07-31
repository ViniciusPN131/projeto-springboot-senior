package hoops.metrics.api.dto.jogador;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Posicao;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoJogador(
        @NotNull Long id,
        String nome,
        float peso,
        int altura,
        Clube clube,
        Posicao posicao) {
}
