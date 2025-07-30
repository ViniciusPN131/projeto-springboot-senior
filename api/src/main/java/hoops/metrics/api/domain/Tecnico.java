package hoops.metrics.api.domain;

import hoops.metrics.api.dto.tecnico.DadosAtualizacaoTecnico;
import hoops.metrics.api.dto.tecnico.DadosCadastroTecnico;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    private String cref;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo;

    public Tecnico(DadosCadastroTecnico dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Dados de cadastro não podem ser nulos");
        }
        this.ativo = true;
        this.nome = dados.nome();
        this.cref = dados.cref();
    }

    public void atualizarInformacoes(DadosAtualizacaoTecnico dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Dados de atualização não podem ser nulos");
        }

        if (dados.nome() != null && !dados.nome().isBlank()) {
            this.nome = dados.nome();
        }

        if (dados.cref() != null && !dados.cref().isBlank()) {
            this.cref = dados.cref();
        }
    }

    public void excluir() {
        this.ativo = false;
    }

}