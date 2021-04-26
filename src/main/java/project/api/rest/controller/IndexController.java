package project.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.api.rest.model.System_User;
import project.api.rest.repository.UserRepository;

@RestController/*REST Architecture*/
@RequestMapping(value = "/users")
public class IndexController {
	
	@Autowired /*If was a CDI would be a @Inject*/
	private UserRepository userRepository;
	
	/*RESTful service*/
	@GetMapping(value = "/{id}/salecode/{sale}", produces = "application/jsonp")
	public ResponseEntity<System_User> report(@PathVariable(value = "id") Long id
			                                , @PathVariable(value = "sale") Long sale) {
		
		Optional<System_User> user = userRepository.findById(id);
		
		/*The return would be a report/sale report!*/
		return new ResponseEntity<System_User>(user.get(), HttpStatus.OK);
	}	
	
	/*RESTful service*/
	/*We are going to suppose that the user charging is a slow process
	 * and we want to control it with cache to make the process faster*/
	@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v1")
	@CacheEvict(value = "cacheuserV1", allEntries = true)
	@CachePut("cacheuserV1")		
	public ResponseEntity<System_User> initV1(@PathVariable(value = "id") Long id){
		
		Optional<System_User> user = userRepository.findById(id);
		
		System.out.println("Processing version 1");
		return new ResponseEntity<System_User>(user.get(), HttpStatus.OK);
	}
	
	/*RESTful service*/
	@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v2")
	@CacheEvict(value = "cacheuserV2", allEntries = true)
	@CachePut("cacheuserV2")	
	public ResponseEntity<System_User> initV2(@PathVariable(value = "id") Long id) {
		
		Optional<System_User> user = userRepository.findById(id);
		System.out.println("Processing version 2");		
		return new ResponseEntity<System_User>(user.get(), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable("id") Long id) {
		
		userRepository.deleteById(id);
		
		return "OK";
	}
	
	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value = "cacheusers", allEntries = true)
	@CachePut("cacheusers")
	public ResponseEntity<List<System_User>> users () throws InterruptedException{
		
		List<System_User> list = (List<System_User>) userRepository.findAll();
		
		//Thread.sleep(6000); /*It hold on a process for about 6 seconds to simulate a slow execution*/
		
		return new ResponseEntity<List<System_User>>(list, HttpStatus.OK);
	}
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<System_User> register(@RequestBody System_User system_user){
		
		for (int pos = 0; pos < system_user.getTelephones().size(); pos++) {
			system_user.getTelephones().get(pos).setUser(system_user);
		}
		
		String criptoPassword = new BCryptPasswordEncoder().encode(system_user.getPassword());
		system_user.setPassword(criptoPassword);
		System_User saveduser = userRepository.save(system_user);
		
		return new ResponseEntity<System_User>(saveduser, HttpStatus.OK);
		
	}
	
	@PostMapping(value = "/{iduser}/idsell/{idsell}", produces = "application/json")
	public ResponseEntity<System_User> registerSell(@PathVariable Long iduser,
			                                        @PathVariable Long idsell){
		/*Here would be the sell process*/
		//System_User saveduser = userRepository.save(system_user);
		
		return new ResponseEntity("Id user: " + iduser + " Id sell: " + idsell, HttpStatus.OK);
	}
	
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<System_User> update(@RequestBody System_User system_user){
		
		for (int pos = 0; pos < system_user.getTelephones().size(); pos++) {
			system_user.getTelephones().get(pos).setUser(system_user);
		}
		
		System_User userTemp = userRepository.findUserByLogin(system_user.getLogin());
		
		if (!userTemp.getPassword().equals(system_user.getPassword())) { /*Different passwords*/
			String criptoPassword = new BCryptPasswordEncoder().encode(system_user.getPassword());
			system_user.setPassword(criptoPassword);
		}
		
		System_User saveduser = userRepository.save(system_user);
		
		return new ResponseEntity<System_User>(saveduser, HttpStatus.OK);
		
	}	

	
	
}











