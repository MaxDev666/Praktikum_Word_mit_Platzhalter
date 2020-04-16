package main.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import main.datenbankmodel.Textbaustein;

@Repository
public interface TextbausteinRepository extends CrudRepository<Textbaustein, String>{

}
