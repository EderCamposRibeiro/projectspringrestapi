package project.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.api.rest.model.System_User;

/*Establish our Token Manager*/
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{
	
	/*Configuring the authentication Manager*/
	protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		
		/*We are obliged to authenticate the URL*/
		super(new AntPathRequestMatcher(url));
		
		/*Authentication Manager*/
		setAuthenticationManager(authenticationManager);
	}
	
	/*Returns the user when processing authentication*/
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		/*Is getting the token to validate*/
		System_User system_user = new ObjectMapper().
				readValue(request.getInputStream(), System_User.class);
		
		/*Returns the user's login, password and access*/
		return getAuthenticationManager().
				authenticate(new UsernamePasswordAuthenticationToken(system_user.getLogin(), system_user.getPassword()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		new JWTTokenAuthenticationService().addAuthentication(response, authResult.getName());
		
	}

}
