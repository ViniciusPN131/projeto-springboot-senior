package hoops.metrics.api.dto.clube;

import hoops.metrics.api.domain.Clube;
import hoops.metrics.api.domain.Tecnico;

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
