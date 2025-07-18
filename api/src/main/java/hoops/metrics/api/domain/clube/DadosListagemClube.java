package hoops.metrics.api.domain.clube;

import hoops.metrics.api.domain.tecnico.Tecnico;

public record DadosListagemClube(Long id, String nome, String sigla, String cidade, String nomeTecnico) {

    public DadosListagemClube(Clube clube){

        this(clube.getId(), clube.getNome(), clube.getSigla(), clube.getCidade(), clube.getTecnico().getNome());

    }

}
