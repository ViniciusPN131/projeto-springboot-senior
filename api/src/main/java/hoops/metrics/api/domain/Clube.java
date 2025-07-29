package hoops.metrics.api.domain;

import hoops.metrics.api.dto.clube.DadosAtualizacaoClube;
import hoops.metrics.api.dto.clube.DadosCadastroClube;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Table(name = "clubes")
@Entity(name = "Clube")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String sigla;

    private String cidade;

    private String estado;

    private Boolean ativo;

    @OneToOne
    @JoinColumn(name = "tecnico_id", unique = true)
    @Setter
    private Tecnico tecnico;

    public Clube(@Valid DadosCadastroClube dados) {

        this.ativo = true;
        this.nome = dados.nome();
        this.sigla = dados.sigla();
        this.cidade = dados.cidade();
        this.estado = dados.estado();
        this.tecnico = dados.tecnico();

    }

    public Clube(@NotNull Long aLong) {
    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoClube dados) {

        if (dados.nome() != null && !dados.nome().isBlank()) {
            this.nome = dados.nome();
        }

        if (dados.sigla() != null && !dados.sigla().isBlank()) {
            this.sigla = dados.sigla();
        }

        if (dados.cidade() != null && !dados.cidade().isBlank()) {
            this.cidade = dados.cidade();
        }

        if (dados.estado() != null && !dados.estado().isBlank()) {
            this.estado = dados.estado();
        }

        if (dados.tecnico()!=null){
            this.tecnico = dados.tecnico();
        }


    }

    public void excluir() {

        this.ativo = false;

    }
}
