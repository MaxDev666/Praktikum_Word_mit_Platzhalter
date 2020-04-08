package datenbank;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VorlagenschrankRepository extends CrudRepository<Vorlagenschrank, String>{

}
