package project.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.api.rest.model.System_User;
import project.api.rest.repository.UserRepository;

@RestController/*REST Architecture*/
@RequestMapping(value = "/user")
public class IndexController {
	
	@Autowired /*If was a CDI would be a @Inject*/
	private UserRepository userRepository;
	
	/*RESTful service*/
	@GetMapping(value = "/{id}/salecode/{sale}", produces = "application/pdf")
	public ResponseEntity<System_User> report(@PathVariable(value = "id") Long id
			                                , @PathVariable(value = "sale") Long sale) {
		
		Optional<System_User> user = userRepository.findById(id);
		
		/*The return would be a report/sale report!*/
		return new ResponseEntity<System_User>(user.get(), HttpStatus.OK);
	}	
	
	/*RESTful service*/
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<System_User> init(@PathVariable(value = "id") Long id) {
		
		Optional<System_User> user = userRepository.findById(id);
		
		return new ResponseEntity<System_User>(user.get(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<System_User>> users (){
		
		List<System_User> list = (List<System_User>) userRepository.findAll();
		
		return new ResponseEntity<List<System_User>>(list, HttpStatus.OK);
	}
}





