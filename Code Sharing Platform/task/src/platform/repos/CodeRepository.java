package platform.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import platform.types.Code;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CodeRepository extends CrudRepository<Code, Integer> {
    List<Code> findAll();

    Optional<Code> findByuuid(UUID id);

}
