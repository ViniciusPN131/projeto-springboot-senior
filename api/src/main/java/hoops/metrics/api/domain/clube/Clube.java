package hoops.metrics.api.domain.clube;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "clubes")
@Entity(name = "Clube")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Clube {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String sigla;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    public Clube(@Valid DadosCadastroClube dados) {

        this.nome = dados.nome();
        this.sigla = dados.sigla();
        this.cidade = dados.cidade();
        this.estado = dados.estado();

    }
}
