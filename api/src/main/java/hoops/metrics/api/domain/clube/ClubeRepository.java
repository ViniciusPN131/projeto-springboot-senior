package hoops.metrics.api.domain.clube;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubeRepository extends JpaRepository<Clube, Long> {
    Page<Clube> findAllByAtivoTrue(Pageable paginacao);

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

}
