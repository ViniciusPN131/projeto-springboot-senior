package hoops.metrics.api.domain.jogador;

import hoops.metrics.api.domain.clube.Clube;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDate;

@Table(name = "jogadores")
@Entity(name = "Jogador")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Jogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Posicao posicao;

    private int altura;

    private float peso;

    private LocalDate data_nascimento;

    @ManyToOne
    @JoinColumn(name = "clube_id")
    private Clube clube;

    private Boolean ativo;

    public Jogador(@Valid DadosCadastroJogador dados) {

        this.ativo = true;
        this.nome = dados.nome();
        this.peso = dados.peso();
        this.altura = dados.altura();
        this.data_nascimento = dados.data_nascimento();
        this.posicao = dados.posicao();
        this.clube = dados.clube();

    }

    public void excluir() {

        this.ativo = false;

    }

    public void atualizarInformacoes(DadosAtualizacaoJogador dados) {
        if (dados.nome() != null && !dados.nome().isBlank()) {
            this.nome = dados.nome();
        }

        if (dados.altura() > 0) {
            this.altura = dados.altura();
        }

        if (dados.peso() > 0) {
            this.peso = dados.peso();
        }

        if (dados.posicao() != null) {
            this.posicao = dados.posicao();
        }

        if (dados.clube() != null) {
            this.clube = dados.clube();
        }
    }
}
