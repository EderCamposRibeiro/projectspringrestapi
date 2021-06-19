package project.api.rest.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import com.google.gson.Gson;

import project.api.rest.model.System_User;
import project.api.rest.model.UserDTO;
import project.api.rest.repository.TelephoneRepositorty;
import project.api.rest.repository.UserRepository;
import project.api.rest.service.ImplementationUserDetailsService;

@RestController/*REST Architecture*/
@RequestMapping(value = "/users")
public class IndexController {
	
	@Autowired /*If was a CDI would be a @Inject*/
	private UserRepository userRepository;
	
	@Autowired
	private TelephoneRepositorty telephoneRepositorty;
	
	@Autowired
	private ImplementationUserDetailsService implementationUserDetailsService;
	
	/*RESTful service*/
	@GetMapping(value = "/{id}/salecode/{sale}", produces = "application/json")
	public ResponseEntity<System_User> report(@PathVariable(value = "id") Long id
			                                , @PathVariable(value = "sale") Long sale) {
		
		Optional<System_User> user = userRepository.findById(id);
		
		/*The return would be a report/sale report!*/
		return new ResponseEntity<System_User>(user.get(), HttpStatus.OK);
	}
	
	/* RESTful Service */
	@GetMapping(value = "/{id}", produces = "application/json")
	@CacheEvict(value = "cacheuser", allEntries = true)
	@CachePut("cacheuser")
	public ResponseEntity<System_User> init(@PathVariable (value = "id") Long id) {
		
		Optional<System_User> user = userRepository.findById(id);
		
		return new ResponseEntity<System_User>(user.get(), HttpStatus.OK);
	}	
	
	/*RESTful service/*
	/*We are going to suppose that the user charging is a slow process
	 * and we want to control it with cache to make the process faster*/
	/*@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v1")
	@CacheEvict(value = "cacheuserV1", allEntries = true)
	@CachePut("cacheuserV1")		
	public ResponseEntity<UserDTO> initV1(@PathVariable(value = "id") Long id){
		
		Optional<System_User> user = userRepository.findById(id);
		
		System.out.println("Processing version 1");
		return new ResponseEntity<UserDTO>(new UserDTO(user.get()), HttpStatus.OK);
	}*/
	
	/*RESTful service*/
	/*@GetMapping(value = "/{id}", produces = "application/json", headers = "X-API-Version=v2")
	@CacheEvict(value = "cacheuserV2", allEntries = true)
	@CachePut("cacheuserV2")	
	public ResponseEntity<System_User> initV2(@PathVariable(value = "id") Long id) {
		
		Optional<System_User> user = userRepository.findById(id);
		System.out.println("Processing version 2");		
		return new ResponseEntity<System_User>(user.get(), HttpStatus.OK);
	}*/
	
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable("id") Long id) {
		
		userRepository.deleteById(id);
		
		return "OK";
	}

	@DeleteMapping(value = "/{id}/sell", produces = "application/text")
	public String deleteSell(@PathVariable("id") Long id) {
		
		userRepository.deleteById(id);
		
		return "OK";
	}	
	
	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value = "cacheusers", allEntries = true)
	@CachePut("cacheusers")
	public ResponseEntity<Page<System_User>> users () throws InterruptedException{
		
		PageRequest page = PageRequest.of(0, 5, Sort.by("name"));
		
		Page<System_User> list = userRepository.findAll(page);
		
		//List<System_User> list = (List<System_User>) userRepository.findAll();
		
		//Thread.sleep(6000); /*It hold on a process for about 6 seconds to simulate a slow execution*/
		
		return new ResponseEntity<Page<System_User>>(list, HttpStatus.OK);
	}
	
	@GetMapping(value = "/page/{page}", produces = "application/json")
	@CacheEvict(value = "cacheusers", allEntries = true)
	@CachePut("cacheusers")
	public ResponseEntity<Page<System_User>> usersPage (@PathVariable("page") int page) throws InterruptedException{
		
		PageRequest pagination = PageRequest.of(page, 5, Sort.by("name"));
		
		Page<System_User> list = userRepository.findAll(pagination);
		
		return new ResponseEntity<Page<System_User>>(list, HttpStatus.OK);
	}
	

	/*END-POINT find user by name*/
	@GetMapping(value = "/userByName/{name}", produces = "application/json")
	//@CacheEvict(value = "cacheusers", allEntries = true)
	@CachePut("cacheusers")
	public ResponseEntity<Page<System_User>> userByName (@PathVariable("name") String name) throws InterruptedException{
		
		PageRequest pageRequest = null;
		Page<System_User> list = null;
		
		if (name == null || (name != null && name.trim().isEmpty()) || name.equalsIgnoreCase("undefined")) { /* Name not informed*/
			pageRequest = PageRequest.of(0, 5, Sort.by("name"));
			list = userRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(0, 5, Sort.by("name"));
			list = userRepository.findUserByNamePage(name, pageRequest);
		}
		
		
		return new ResponseEntity<Page<System_User>>(list, HttpStatus.OK);
	}

	/*END-POINT find user by name*/
	@GetMapping(value = "/userByName/{name}/page/{page}", produces = "application/json")
	//@CacheEvict(value = "cacheusers", allEntries = true)
	@CachePut("cacheusers")
	public ResponseEntity<Page<System_User>> userByNamePage (@PathVariable("name") String name, @PathVariable("page") int page) throws InterruptedException{
		
		PageRequest pageRequest = null;
		Page<System_User> list = null;
		
		if (name == null || (name != null && name.trim().isEmpty()) || name.equalsIgnoreCase("undefined")) { /* Name not informed*/
			pageRequest = PageRequest.of(page, 5, Sort.by("name"));
			list = userRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(page, 5, Sort.by("name"));
			list = userRepository.findUserByNamePage(name, pageRequest);
		}
		
		
		return new ResponseEntity<Page<System_User>>(list, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<System_User> register(@RequestBody @Valid System_User system_user) throws Exception{
		
		for (int pos = 0; pos < system_user.getTelephones().size(); pos++) {
			system_user.getTelephones().get(pos).setUser(system_user);
		}
		
		if (system_user.getCep() != null) {
			/*Consuming externa public API => Start*/
			URL url = new URL("https://viacep.com.br/ws/" + system_user.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			System.out.println(jsonCep.toString());
			System_User userAux = new Gson().fromJson(jsonCep.toString(), System_User.class);
			system_user.setCep(userAux.getCep());
			system_user.setLogradouro(userAux.getLogradouro());
			system_user.setComplemento(userAux.getComplemento());
			system_user.setBairro(userAux.getBairro());
			system_user.setLocalidade(userAux.getLocalidade());
			system_user.setUf(userAux.getUf());
		}
		
				
		/*Consuming externa public API => End*/
		
		String criptoPassword = new BCryptPasswordEncoder().encode(system_user.getPassword());
		system_user.setPassword(criptoPassword);
		System_User saveduser = userRepository.save(system_user);
		
		implementationUserDetailsService.insertsStandardAccess(saveduser.getId());
		
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
		
		System_User userTemp = userRepository.findById(system_user.getId()).get();
		
		if (!userTemp.getPassword().equals(system_user.getPassword())) { /*Different passwords*/
			String criptoPassword = new BCryptPasswordEncoder().encode(system_user.getPassword());
			system_user.setPassword(criptoPassword);
		}
		
		System_User saveduser = userRepository.save(system_user);
		
		return new ResponseEntity<System_User>(saveduser, HttpStatus.OK);
		
	}	
	
	@DeleteMapping(value = "/deleteTelephone/{id}", produces = "application/text")
	public String removeTelephone(@PathVariable("id") Long id) {
		telephoneRepositorty.deleteById(id);
		return "ok";
	}

	
	
}











