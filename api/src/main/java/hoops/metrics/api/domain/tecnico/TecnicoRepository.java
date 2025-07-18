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
    SELECT COUNT(*) 
    FROM partidas p
    JOIN clubes c ON c.id = p.clube_mandante_id OR c.id = p.clube_visitante_id
    WHERE c.tecnico_id = :id
    AND (
        (
            SELECT SUM(e1.total_pontos)
            FROM estatisticas e1
            JOIN jogadores j1 ON e1.jogador_id = j1.id
            WHERE j1.clube_id = c.id AND e1.partida_id = p.id
        ) >
        (
            SELECT SUM(e2.total_pontos)
            FROM estatisticas e2
            JOIN jogadores j2 ON e2.jogador_id = j2.id
            WHERE j2.clube_id != c.id AND e2.partida_id = p.id
        )
    )
""", nativeQuery = true)
    Long vitoriasDoTecnico(@Param("id") Long id);



}
