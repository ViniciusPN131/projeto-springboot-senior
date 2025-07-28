package hoops.metrics.api.domain.jogador;

import hoops.metrics.api.domain.estatisticas.DadosGeraisEstatistica;
import hoops.metrics.api.domain.estatisticas.Estatistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {

    @Query("""
                SELECT e FROM Estatistica e
                WHERE e.partida.id = :partidaId
                AND e.totalPontos = (
                    SELECT MAX(e2.totalPontos) FROM Estatistica e2
                    WHERE e2.partida.id = :partidaId
                )
            """)
    List<Estatistica> buscarMvpDaPartida(@Param("partidaId") Long partidaId);

    @Query(value = """
            
                SELECT
                        e.jogador_id,
                        SUM(e.total_pontos) AS totalPontos,
                        SUM(e.total_faltas) AS totalFaltas,
                        SUM(e.rebotes_ofensivos) AS rebotes_ofensivos,
                        SUM(e.rebotes_defensivos) AS rebotes_defensivos,\s
                        SUM(e.roubos_de_bola) AS roubos_de_bola,
                        SUM(e.turnovers) AS turnovers
                    FROM estatisticas e
                    WHERE e.jogador_id = :jogadorId
                    group by e.jogador_id
            """, nativeQuery = true)
    DadosGeraisEstatistica estatisticasGeraisPorJogador(@Param("jogadorId") Long jogadorId);
}
