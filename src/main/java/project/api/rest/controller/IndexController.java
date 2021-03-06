package project.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController/*REST Architecture*/
@RequestMapping(value = "/user")
public class IndexController {
	
	/*RESTful service*/
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity init(@RequestParam (value = "name", required = true, defaultValue = "The name is required!") String name, 
			                   @RequestParam("income")  Long income) {
		System.out.println("Received parameter " + name + " / " + income);
		return new ResponseEntity("Hello User REST Spring Boot, your name is: " + name + " Income: R$: " + income, HttpStatus.OK);
	}
}
