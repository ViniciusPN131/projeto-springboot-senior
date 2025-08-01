package hoops.metrics.api.dto.jogador;

import hoops.metrics.api.domain.Jogador;

public record DadosListagemJogador(Long id, String nome, float peso, int altura, Long clubeId, String clubeNome) {

    public DadosListagemJogador(Jogador jogador){
        this(jogador.getId(), jogador.getNome(), jogador.getPeso(), jogador.getAltura(), jogador.getClube().getId(), jogador.getClube().getNome());
    }

}
