package hoops.metrics.api.domain.tecnico;

import hoops.metrics.api.domain.clube.Clube;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Boolean ativo;

//    @OneToOne(mappedBy = "tecnico")
//    private Clube clube;

    public Tecnico(@Valid DadosCadastroTecnico dados) {
        this.ativo = true;
        this.nome = dados.nome();
    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoTecnico dados) {
        if (dados.nome() != null && !dados.nome().isBlank()) {
            this.nome = dados.nome();
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
