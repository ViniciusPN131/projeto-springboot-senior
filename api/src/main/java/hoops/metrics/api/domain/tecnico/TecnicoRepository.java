package hoops.metrics.api.domain.tecnico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    Page<Tecnico> findAllByAtivoTrue(Pageable paginacao);

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

}
