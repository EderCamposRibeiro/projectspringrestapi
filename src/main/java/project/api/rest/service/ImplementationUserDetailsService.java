package project.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.api.rest.model.System_User;
import project.api.rest.repository.UserRepository;

@Service
public class ImplementationUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		/* First step: Find the user in the database*/
		
		System_User user = userRepository.findUserByLogin(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}
		
		return new User(user.getLogin()
				      , user.getPassword()
				      , user.getAuthorities());
	}

	public void insertsStandardAccess(Long id) {
		
		//Step 1 => Find out the restriction constraint
		String constraint = userRepository.queryConstraintRole();
		
		if (constraint != null ) {
			//Step 2 => Remove constraint
			jdbcTemplate.execute(" alter table users_role DROP CONSTRAINT " + constraint);
			//userRepository.removeConstraintRole(constraint);
		}
		
		//Step 3 => Insert the standard Access
		userRepository.insertAccessRoleStandard(id);
		
	}

}










