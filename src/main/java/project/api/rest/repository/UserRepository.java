package project.api.rest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import project.api.rest.model.System_User;

@Repository
public interface UserRepository extends CrudRepository<System_User, Long> {
	
	@Query("select u from System_User u where u.login = ?1")
	System_User findUserByLogin(String login);

}