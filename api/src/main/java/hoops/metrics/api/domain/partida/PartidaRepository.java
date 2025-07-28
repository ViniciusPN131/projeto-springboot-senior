package hoops.metrics.api.domain.partida;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    Page<Partida> findAllByAtivoTrue(Pageable paginacao);

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
