package hoops.metrics.api.repository;

import hoops.metrics.api.dto.estatistica.DadosGeraisEstatistica;
import hoops.metrics.api.domain.Estatistica;
import hoops.metrics.api.domain.Jogador;
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


    @Query(value = """
                WITH pontos_por_clube AS (
                    SELECT\s
                        p.id AS partida_id,
                        j.clube_id,
                        SUM(e.total_pontos) AS pontos
                    FROM estatisticas e
                    JOIN jogadores j ON e.jogador_id = j.id
                    JOIN partidas p ON e.partida_id = p.id
                    GROUP BY p.id, j.clube_id
                ),
                vencedores AS (
                    SELECT\s
                        partida_id,
                        clube_id AS clube_vencedor
                    FROM (
                        SELECT\s
                            partida_id,
                            clube_id,
                            pontos,
                            RANK() OVER (PARTITION BY partida_id ORDER BY pontos DESC) AS rank
                        FROM pontos_por_clube
                    ) ranked
                    WHERE rank = 1
                ),
                vitorias_jogador AS (
                    SELECT DISTINCT p.id AS partida_id
                    FROM estatisticas e
                    JOIN jogadores j ON e.jogador_id = j.id
                    JOIN partidas p ON e.partida_id = p.id
                    JOIN vencedores v ON p.id = v.partida_id
                    WHERE j.id = :jogadorId
                      AND j.clube_id = v.clube_vencedor
                )
                SELECT COUNT(*) AS quantidade_vitorias
                FROM vitorias_jogador;
            
            """, nativeQuery = true)
    int quantidadeDeVitoriasDoJogador(@Param("jogadorId") Long jogadorId);

}
