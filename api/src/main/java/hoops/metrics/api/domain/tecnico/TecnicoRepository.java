package hoops.metrics.api.domain.tecnico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    Page<Tecnico> findAllByAtivoTrue(Pageable paginacao);
}
