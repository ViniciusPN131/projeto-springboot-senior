package hoops.metrics.api.dto.jogador;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Jogador;
import hoops.metrics.api.domain.Posicao;

import java.time.LocalDate;

public record DadosDetalhamentoJogador(Long id, String nome, float peso, int altura, Posicao posicao, LocalDate data_nascimento, Long clubeId, String clubeNome) {

    public  DadosDetalhamentoJogador(Jogador jogador){

        this(
                jogador.getId(),
                jogador.getNome(),
                jogador.getPeso(),
                jogador.getAltura(),
                jogador.getPosicao(),
                jogador.getData_nascimento(),
                jogador.getClube().getId(),
                jogador.getClube().getNome()
        );

    }

}
