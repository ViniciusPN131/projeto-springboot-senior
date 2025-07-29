package hoops.metrics.api.dto.tecnico;

import hoops.metrics.api.domain.Tecnico;

public record DadosDetalhamentoTecnico(Long id, String nome, String cref) {

    public  DadosDetalhamentoTecnico(Tecnico tecnico){

        this(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getCref()
        );

    }

}
