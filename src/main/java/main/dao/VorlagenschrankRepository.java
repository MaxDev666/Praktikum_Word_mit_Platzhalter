package main.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import main.datenbankmodel.Vorlagenschrank;

@Repository
public interface VorlagenschrankRepository extends CrudRepository<Vorlagenschrank, String>{

}
