package project.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
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

}










