package hoops.metrics.api.domain.tecnico;

import hoops.metrics.api.domain.clube.Clube;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "tecnicos")
@Entity(name = "Tecnico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tecnico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

//    @ManyToOne
//    @JoinColumn(name = "clube_id")
//    private Clube clube;

    private Boolean ativo;

    public Tecnico(@Valid DadosCadastroTecnico dados) {

        this.ativo = true;
        this.nome = dados.nome();
//        this.clube = dados.clube();

    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoTecnico dados) {

        if (dados.nome() != null && !dados.nome().isBlank()) {
            this.nome = dados.nome();
        }

//        if (dados.clube() != null) {
//            this.clube = dados.clube();
//        }


    }

    public void excluir() {

        this.ativo = false;

    }
}
