package project.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/*Filter where all requests will be captured to authenticate*/
public class JWTApiAuthenticationFilter extends GenericFilterBean{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	
		/*Establish the authentication to the request*/
		Authentication authentication = new JWTTokenAuthenticationService().
				getAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
		
		/*Puts the authentication process in Spring Security*/
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		/*Continue the process*/
		chain.doFilter(request, response);
		
	}

}
