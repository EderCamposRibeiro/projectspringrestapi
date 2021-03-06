package project.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.api.rest.model.System_User;

@RestController/*REST Architecture*/
@RequestMapping(value = "/user")
public class IndexController {
	
	/*RESTful service*/
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<System_User> init() {
		
		System_User sysuser = new System_User();
		sysuser.setId(50L);
		sysuser.setLogin("edercribeiro@gmail.com");
		sysuser.setName("Eder Campos Ribeiro Mazzoccante");
		sysuser.setPassword("ABcd1234");
		
		System_User sysuser2 = new System_User();
		sysuser2.setId(51L);
		sysuser2.setLogin("ederteste@gmail.com");
		sysuser2.setName("Eder Campos Ribeiro");
		sysuser2.setPassword("00223200");		
		
		List<System_User> users = new ArrayList<System_User>();
		users.add(sysuser);
		users.add(sysuser2);
		
		return new ResponseEntity(users, HttpStatus.OK);
	}
}
