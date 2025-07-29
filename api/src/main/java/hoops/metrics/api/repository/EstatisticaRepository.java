package hoops.metrics.api.repository;

import hoops.metrics.api.domain.Estatistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstatisticaRepository extends JpaRepository<Estatistica, Long> {

    @Query(value = """
    SELECT e.*
    FROM estatisticas e
    JOIN partidas p ON e.partida_id = p.id
    WHERE e.jogador_id = :jogadorId
      AND e.total_pontos = (
        SELECT MAX(e2.total_pontos)
        FROM estatisticas e2
        WHERE e2.partida_id = p.id
      )
    """, nativeQuery = true)
    List<Estatistica> buscarEstatisticasOndeJogadorFoiMvp(@Param("jogadorId") Long jogadorId);


}
