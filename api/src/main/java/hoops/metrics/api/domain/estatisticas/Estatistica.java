package hoops.metrics.api.domain.estatisticas;

import hoops.metrics.api.domain.jogador.Jogador;
import hoops.metrics.api.domain.partida.Partida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "estatisticas")
@Entity(name = "Estatistica")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Estatistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogador_id")
    private Jogador jogador;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Partida partida;

    private int totalPontos;
    private int totalFaltas;
    private int rebotesOfensivos;
    private int rebotesDefensivos;
    private int roubosDeBola;
    private int turnovers;

    public Estatistica(DadosCadastroEstatistica dados, Jogador jogador, Partida partida) {
        this.jogador = jogador;
        this.partida = partida;
        this.totalPontos = dados.totalPontos();
        this.totalFaltas = dados.totalFaltas();
        this.rebotesOfensivos = dados.rebotesOfensivos();
        this.rebotesDefensivos = dados.rebotesDefensivos();
        this.roubosDeBola = dados.roubosDeBola();
        this.turnovers = dados.turnovers();
    }

    public void atualizarInformacoes(DadosAtualizacaoEstatistica dados) {
        if (dados.totalPontos() != null) this.totalPontos = dados.totalPontos();
        if (dados.totalFaltas() != null) this.totalFaltas = dados.totalFaltas();
        if (dados.rebotesOfensivos() != null) this.rebotesOfensivos = dados.rebotesOfensivos();
        if (dados.rebotesDefensivos() != null) this.rebotesDefensivos = dados.rebotesDefensivos();
        if (dados.roubosDeBola() != null) this.roubosDeBola = dados.roubosDeBola();
        if (dados.turnovers() != null) this.turnovers = dados.turnovers();
    }
}
