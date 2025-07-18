package hoops.metrics.api.domain.clube;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubeRepository extends JpaRepository<Clube, Long> {
    Page<Clube> findAllByAtivoTrue(Pageable paginacao);
}
