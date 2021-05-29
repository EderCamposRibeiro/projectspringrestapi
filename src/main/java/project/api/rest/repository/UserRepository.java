package project.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import project.api.rest.model.System_User;

@Repository
public interface UserRepository extends CrudRepository<System_User, Long> {
	
	@Query("select u from System_User u where u.login = ?1")
	System_User findUserByLogin(String login);
	
	@Query("select u from System_User u where u.name like %?1%")
	List<System_User> findUserByName(String name);	
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "update System_User set token = ?1 where login = ?2")
	void updateTokeUser(String token, String login );
	
	@Query(value = "select constraint_name from information_schema.constraint_column_usage where table_name = 'users_role' and   column_name = 'role_id' and   constraint_name <> 'unique_role_user';", nativeQuery = true)
	String queryConstraintRole();
	
	/*@Transactional
	@Modifying
	@Query(value="alter table users_role DROP CONSTRAINT ?1;", nativeQuery= true)
	void removeConstraintRole(String constraint);*/
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO users_role (user_id, role_id) VALUES(?1, (select ID from role where name_role = 'ROLE_USER'));", nativeQuery=true)
	void insertAccessRoleStandard(Long idUser);

}
