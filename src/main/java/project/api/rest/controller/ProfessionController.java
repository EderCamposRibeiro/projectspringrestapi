package project.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.api.rest.model.Profession;
import project.api.rest.repository.ProfessionRepository;

@RestController
@RequestMapping(value = "/profession")
public class ProfessionController {

	@Autowired
	private ProfessionRepository professionRepository;

	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Profession>> professions() {

		List<Profession> list = professionRepository.findAll();

		return new ResponseEntity<List<Profession>>(list, HttpStatus.OK);
	}

}
