package hoops.metrics.api.domain.clube;

public record DadosListagemClube(Long id, String nome, String sigla, String cidade) {

    public DadosListagemClube(Clube clube){

        this(clube.getId(), clube.getNome(), clube.getSigla(), clube.getCidade());

    }

}
