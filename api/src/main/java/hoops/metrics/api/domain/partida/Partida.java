package hoops.metrics.api.domain.partida;

import hoops.metrics.api.domain.clube.Clube;
import hoops.metrics.api.domain.partida.DadosAtualizacaoPartida;
import hoops.metrics.api.domain.partida.DadosCadastroPartida;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "partidas")
@Entity(name = "Partida")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String local;

    private LocalDateTime dataHora;

    private boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "clube_mandante_id")
    private Clube clubeDaCasa;

    @ManyToOne
    @JoinColumn(name = "clube_visitante_id")
    private Clube clubeVisitante;

    public Partida(DadosCadastroPartida dados, Clube clubeDaCasa, Clube clubeVisitante) {
        this.local = dados.local();
        this.dataHora = dados.dataHora();
        this.clubeDaCasa = clubeDaCasa;
        this.clubeVisitante = clubeVisitante;
        this.ativo = true;
    }


    public void atualizarInformacoes(DadosAtualizacaoPartida dados) {
        if (dados.local() != null) {
            this.local = dados.local();
        }
        if (dados.dataHora() != null) {
            this.dataHora = dados.dataHora();
        }
        if (dados.timeCasaId() != null) {
            this.clubeDaCasa = new Clube(dados.timeCasaId());
        }
        if (dados.timeVisitanteId() != null) {
            this.clubeVisitante = new Clube(dados.timeVisitanteId());
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
