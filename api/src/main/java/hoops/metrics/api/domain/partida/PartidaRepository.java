package hoops.metrics.api.domain.partida;

import hoops.metrics.api.domain.clube.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    Page<Partida> findAllByAtivoTrue(Pageable paginacao);
}
