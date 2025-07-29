package hoops.metrics.api.dto.tecnico;

import hoops.metrics.api.domain.Tecnico;

public record DadosListagemTecnico(Long id, String nome, String cref) {

    public DadosListagemTecnico(Tecnico tecnico){

        this(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getCref()
        );
    }

}
