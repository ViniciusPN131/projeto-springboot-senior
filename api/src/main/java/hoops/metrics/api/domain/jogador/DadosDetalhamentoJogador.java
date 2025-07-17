package hoops.metrics.api.domain.jogador;

import hoops.metrics.api.domain.clube.Clube;

import java.time.LocalDate;

public record DadosDetalhamentoJogador(Long id, String nome, float peso, int altura, Posicao posicao, LocalDate data_nascimento, Clube clube) {

    public  DadosDetalhamentoJogador(Jogador jogador){

        this(
                jogador.getId(),
                jogador.getNome(),
                jogador.getPeso(),
                jogador.getAltura(),
                jogador.getPosicao(),
                jogador.getData_nascimento(),
                jogador.getClube()
        );

    }

}
