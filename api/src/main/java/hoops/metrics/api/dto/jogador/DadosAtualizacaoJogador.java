package hoops.metrics.api.dto.jogador;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Posicao;

public record DadosAtualizacaoJogador(Long id, String nome, float peso, int altura, Clube clube, Posicao posicao) {
}
