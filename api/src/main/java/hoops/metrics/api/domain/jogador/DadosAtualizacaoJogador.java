package hoops.metrics.api.domain.jogador;

import hoops.metrics.api.domain.clube.Clube;

public record DadosAtualizacaoJogador(Long id, String nome, float peso, int altura, Clube clube, Posicao posicao) {
}
