package hoops.metrics.api.domain.tecnico;

import hoops.metrics.api.domain.clube.Clube;
import hoops.metrics.api.domain.jogador.Jogador;

public record DadosListagemTecnico(Long id, String nome) {

    public DadosListagemTecnico(Tecnico tecnico){
        this(tecnico.getId(), tecnico.getNome());
    }

}
