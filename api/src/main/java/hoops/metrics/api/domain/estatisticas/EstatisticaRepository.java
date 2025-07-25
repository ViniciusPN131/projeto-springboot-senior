package hoops.metrics.api.domain.estatisticas;

import hoops.metrics.api.domain.partida.DadosResultadoPartida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstatisticaRepository extends JpaRepository<Estatistica, Long> {

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
                SELECT COUNT(*) FROM partidas p
                JOIN clubes cm ON cm.id = p.clube_mandante_id
                JOIN clubes cv ON cv.id = p.clube_visitante_id
                WHERE (
                    cm.tecnico_id = :tecnicoId AND
                    (
                        (SELECT COALESCE(SUM(e1.total_pontos), 0)
                         FROM estatisticas e1
                         JOIN jogadores j1 ON j1.id = e1.jogador_id
                         WHERE e1.partida_id = p.id AND j1.clube_id = cm.id)
                        >
                        (SELECT COALESCE(SUM(e2.total_pontos), 0)
                         FROM estatisticas e2
                         JOIN jogadores j2 ON j2.id = e2.jogador_id
                         WHERE e2.partida_id = p.id AND j2.clube_id = cv.id)
                    )
                )
                OR (
                    cv.tecnico_id = :tecnicoId AND
                    (
                        (SELECT COALESCE(SUM(e1.total_pontos), 0)
                         FROM estatisticas e1
                         JOIN jogadores j1 ON j1.id = e1.jogador_id
                         WHERE e1.partida_id = p.id AND j1.clube_id = cv.id)
                        >
                        (SELECT COALESCE(SUM(e2.total_pontos), 0)
                         FROM estatisticas e2
                         JOIN jogadores j2 ON j2.id = e2.jogador_id
                         WHERE e2.partida_id = p.id AND j2.clube_id = cm.id)
                    )
                )
            """, nativeQuery = true)
    Long contarVitoriasPorTecnico(@Param("tecnicoId") Long tecnicoId);

    @Query(value = """
                SELECT COUNT(*) FROM partidas p
                WHERE (
                    p.clube_mandante_id = :clubeId AND
                    (
                        (SELECT COALESCE(SUM(e1.total_pontos), 0)
                         FROM estatisticas e1
                         JOIN jogadores j1 ON j1.id = e1.jogador_id
                         WHERE e1.partida_id = p.id AND j1.clube_id = p.clube_mandante_id)
                        >
                        (SELECT COALESCE(SUM(e2.total_pontos), 0)
                         FROM estatisticas e2
                         JOIN jogadores j2 ON j2.id = e2.jogador_id
                         WHERE e2.partida_id = p.id AND j2.clube_id = p.clube_visitante_id)
                    )
                )
                OR (
                    p.clube_visitante_id = :clubeId AND
                    (
                        (SELECT COALESCE(SUM(e1.total_pontos), 0)
                         FROM estatisticas e1
                         JOIN jogadores j1 ON j1.id = e1.jogador_id
                         WHERE e1.partida_id = p.id AND j1.clube_id = p.clube_visitante_id)
                        >
                        (SELECT COALESCE(SUM(e2.total_pontos), 0)
                         FROM estatisticas e2
                         JOIN jogadores j2 ON j2.id = e2.jogador_id
                         WHERE e2.partida_id = p.id AND j2.clube_id = p.clube_mandante_id)
                    )
                )
            """, nativeQuery = true)
    Long contarVitoriasPorClube(@Param("clubeId") Long clubeId);

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
            SELECT 
                p.id AS partida_id,
                cm.nome AS time_mandante,
                COALESCE(SUM(CASE WHEN j.clube_id = p.clube_mandante_id THEN e.total_pontos ELSE 0 END), 0) AS pontos_mandante,
                cv.nome AS time_visitante,
                COALESCE(SUM(CASE WHEN j.clube_id = p.clube_visitante_id THEN e.total_pontos ELSE 0 END), 0) AS pontos_visitante
            FROM partidas p
            JOIN clubes cm ON p.clube_mandante_id = cm.id
            JOIN clubes cv ON p.clube_visitante_id = cv.id
            LEFT JOIN estatisticas e ON e.partida_id = p.id
            LEFT JOIN jogadores j ON e.jogador_id = j.id
            WHERE p.id = :partidaId
            GROUP BY p.id, cm.nome, cv.nome
            ORDER BY p.id
            LIMIT 1
            """, nativeQuery = true)
    DadosResultadoPartida buscarResultadosPartidas(@Param("partidaId") Long partidaId);


}
