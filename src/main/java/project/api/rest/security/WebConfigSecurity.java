package project.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import project.api.rest.service.ImplementationUserDetailsService;

/*Mapping URLs, address, allow or deny the access to URL*/
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private ImplementationUserDetailsService implementationUserDetailsService;
	
	/*It configures the access requests by Http*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*Activating the protection against users that are not validated by TOKEN*/
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*Activating the access permission to the first page of the system. For example: system.com.br/index.html/*/
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		/*Logout URL - Will redirect after the user logout*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*Mapping the Logout URL and invalidate the user*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/*It filters the login requests to authenticate */
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), 
												UsernamePasswordAuthenticationFilter.class)
		
		/*It filters other requests to verify the JWT TOKEN in the HTTP HEADER*/
		.addFilterBefore(new JWTApiAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			
		/*Service that will consult the user in the database*/
		auth.userDetailsService(implementationUserDetailsService)
		/*Pattern of password's codification*/
		.passwordEncoder(new BCryptPasswordEncoder());
			
	}
	
}
