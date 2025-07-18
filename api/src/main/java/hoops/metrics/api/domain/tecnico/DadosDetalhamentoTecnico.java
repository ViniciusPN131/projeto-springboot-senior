package hoops.metrics.api.domain.tecnico;

import hoops.metrics.api.domain.clube.Clube;
import hoops.metrics.api.domain.jogador.Jogador;

public record DadosDetalhamentoTecnico(Long id, String nome) {

    public  DadosDetalhamentoTecnico(Tecnico tecnico){

        this(
                tecnico.getId(),
                tecnico.getNome()
//                tecnico.getClube()
        );

    }

}
