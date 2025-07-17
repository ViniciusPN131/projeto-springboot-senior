package hoops.metrics.api.domain.jogador;

import hoops.metrics.api.domain.clube.Clube;

public record DadosListagemJogador(Long id, String nome, float peso, int altura, Clube clube) {

    public DadosListagemJogador(Jogador jogador){
        this(jogador.getId(), jogador.getNome(), jogador.getPeso(), jogador.getAltura(), jogador.getClube());
    }

}
