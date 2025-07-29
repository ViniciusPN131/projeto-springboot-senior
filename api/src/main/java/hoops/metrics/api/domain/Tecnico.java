package hoops.metrics.api.domain;

import hoops.metrics.api.dto.tecnico.DadosAtualizacaoTecnico;
import hoops.metrics.api.dto.tecnico.DadosCadastroTecnico;
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

    @Column(unique = true)
    private String cref;

    private Boolean ativo;

    public Tecnico(@Valid DadosCadastroTecnico dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.cref = dados.cref();
    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoTecnico dados) {
        if (dados.nome() == null || dados.nome().isBlank()) {
            return;
        }
        if (dados.cref() == null || dados.cref().isBlank()){
            return;
        }
        this.nome = dados.nome();
        this.cref = dados.cref();
    }

    public void excluir() {
        this.ativo = false;
    }
}
