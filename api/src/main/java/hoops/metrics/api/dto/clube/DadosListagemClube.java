package hoops.metrics.api.dto.clube;

import hoops.metrics.api.domain.Clube;

public record DadosListagemClube(Long id, String nome, String sigla, String cidade, String nomeTecnico) {

    public DadosListagemClube(Clube clube){

        this(clube.getId(), clube.getNome(), clube.getSigla(), clube.getCidade(), clube.getTecnico().getNome());

    }

}
