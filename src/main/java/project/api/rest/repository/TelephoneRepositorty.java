package project.api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import project.api.rest.model.Telephone;

@Repository
public interface TelephoneRepositorty extends CrudRepository<Telephone, Long> {
	
	
	
}
