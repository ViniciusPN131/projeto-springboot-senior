package hoops.metrics.api.dto.clube;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Tecnico;

public record DadosListagemClube(Long id, String nome, String sigla, String cidade, Long tecnicoId, String tecnicoNome) {

    public DadosListagemClube(Clube clube){

        this(clube.getId(), clube.getNome(), clube.getSigla(), clube.getCidade(), clube.getTecnico().getId(), clube.getTecnico().getNome());

    }

}
