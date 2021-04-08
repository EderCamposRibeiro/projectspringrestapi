package project.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import project.api.rest.ApplicationContextLoad;
import project.api.rest.model.System_User;
import project.api.rest.repository.UserRepository;

@Service
@Component
public class JWTTokenAuthenticationService {
	
	/*Token validation time - 2 days*/
	private static final long EXPIRATION_TIME = 172800000;
	
	/*Only one password to compose the authentication and help the security*/
	private static final String SECRET = "SenhaExtremamenteSecreta";
	
	/*Pattern prefix token*/
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	/*Generating authentication token and add to the head the Http response*/
	public void addAuthentication(HttpServletResponse response, String username) throws IOException{
		
		/*Token assembly*/
		String JWT = Jwts.builder() /*Call the token generator*/
				         .setSubject(username) /*Add the user*/
				         .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Expiration time*/
				         .signWith(SignatureAlgorithm.HS512, SECRET).compact(); /*Compression and password generation algorithm*/
		
		/*Join the token with the prefix*/
		String token = TOKEN_PREFIX + " " + JWT; /*Bearer 998dssf989sfs98s9f8sfs98s9...*/
		
		/*Add in the http header*/
		response.addHeader(HEADER_STRING, token); /*Authorization: Bearer 998dssf989sfs98s9f8sfs98s9...*/
		
		/*Write the token as a response in the http body*/
		response.getWriter().write("{\"Authorization\": \""+token+"\"}");
	}
	
	/*Return validated user with token or in case it isn't valid return null*/
	public Authentication getAuthentication(HttpServletRequest request) {
		
		/*Get the token sent in the http header*/
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			
			/*Validates the user's token in the request*/
			String user = Jwts.parser().setSigningKey(SECRET) /*-> Bearer 998dssf989sfs98s9f8sfs98s9...*/
					          .parseClaimsJws(token.replace(TOKEN_PREFIX, "")) /*-> 998dssf989sfs98s9f8sfs98s9...*/
					          .getBody().getSubject(); /*John Smith*/
			
			if (user != null) {
				
				System_User suser = ApplicationContextLoad.getApplicationContext()
						          .getBean(UserRepository.class).findUserByLogin(user);
				
				if (suser != null) {
					return new UsernamePasswordAuthenticationToken(
							suser.getLogin(),
							suser.getPassword(),
							suser.getAuthorities());
				}
			}
		} 
		return null; /*Not authorized*/
		
	}
	

}






