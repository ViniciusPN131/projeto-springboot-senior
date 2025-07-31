package hoops.metrics.api.repository;

import hoops.metrics.api.dto.jogador.DadosGeraisJogador;
import hoops.metrics.api.domain.Jogador;
import hoops.metrics.api.dto.partida.DadosMvpPartida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {

    @Query(value = """
            SELECT
                        e.jogador_id AS jogadorId,
                        j.nome AS nomeJogador,
                        j.clube_id AS clubeId,
                        c.nome AS clubeJogador,
                        e.total_pontos AS totalPontos,
                        e.assistencias AS assistencias,
                        e.total_faltas AS totalFaltas,
                        e.rebotes_ofensivos AS rebotesOfensivos,
                        e.rebotes_defensivos AS rebotesDefensivos,
                        e.roubos_de_bola AS roubosDeBola,
                        e.turnovers AS turnovers
            
                    FROM estatisticas e
                    JOIN jogadores j ON j.id = e.jogador_id
                    JOIN clubes c ON c.id = j.clube_id
                    WHERE e.partida_id = :partidaId
                    AND e.total_pontos = (
                        SELECT MAX(e2.total_pontos)\s
                        FROM estatisticas e2
                        WHERE e2.partida_id = :partidaId
                    )
        """, nativeQuery = true)
    List<DadosMvpPartida> buscarMvpDaPartida(@Param("partidaId") Long partidaId);

    @Query(value = """
            
                SELECT
                        e.jogador_id,
                        SUM(e.total_pontos) AS totalPontos,
                        SUM(e.assistencias) AS assistencias,
                        SUM(e.total_faltas) AS totalFaltas,
                        SUM(e.rebotes_ofensivos) AS rebotes_ofensivos,
                        SUM(e.rebotes_defensivos) AS rebotes_defensivos,\s
                        SUM(e.roubos_de_bola) AS roubos_de_bola,
                        SUM(e.turnovers) AS turnovers
                    FROM estatisticas e
                    WHERE e.jogador_id = :jogadorId
                    group by e.jogador_id
            """, nativeQuery = true)
    DadosGeraisJogador estatisticasGeraisPorJogador(@Param("jogadorId") Long jogadorId);


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

    boolean existsByCpf(String cpf);
}
