package hoops.metrics.api.domain.clube;

import hoops.metrics.api.domain.tecnico.Tecnico;

public record DadosDetalhamentoClube(Long id, String nome, String sigla, String cidade, String estado, Tecnico tecnico) {

    public DadosDetalhamentoClube(Clube clube) {

        this(
                clube.getId(),
                clube.getNome(),
                clube.getSigla(),
                clube.getCidade(),
                clube.getEstado(),
                clube.getTecnico()
        );

    }

}
