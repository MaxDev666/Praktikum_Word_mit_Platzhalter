package main.datenbankmodel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import main.dao.Vorlagenschrank;

@Repository
public interface VorlagenschrankRepository extends CrudRepository<Vorlagenschrank, String>{

}
