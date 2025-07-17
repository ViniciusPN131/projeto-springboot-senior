package hoops.metrics.api.domain.clube;

public record DadosDetalhamentoClube(Long id, String nome, String sigla, String cidade, String estado) {

    public DadosDetalhamentoClube(Clube clube) {

        this(
                clube.getId(),
                clube.getNome(),
                clube.getSigla(),
                clube.getCidade(),
                clube.getEstado()
        );

    }

}
