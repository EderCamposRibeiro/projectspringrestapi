package project.api.rest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class System_User implements UserDetails{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String login;
	
	private String password;
	
	private String name;
	
	@CPF(message = "Invalid CPF!")
	private String cpf = "";
	
	private String token = "";
	
	private String cep;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String uf;
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Telephone> telephones = new ArrayList<Telephone>();
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_role", uniqueConstraints = @UniqueConstraint (
			   columnNames = {"user_id","role_id"}, name = "unique_role_user"),
	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", table = "user", unique = false,
	foreignKey = @ForeignKey (name = "user_fk", value = ConstraintMode.CONSTRAINT)),
			
	inverseJoinColumns = @JoinColumn (name = "role_id", referencedColumnName = "id", table = "role", unique = false, updatable = false,
	foreignKey = @ForeignKey (name="role_fk", value = ConstraintMode.CONSTRAINT)))
	private List<Role> roles = new ArrayList<Role>(); /*The roles or access*/
	
	public List<Telephone> getTelephones() {
		return telephones;
	}
		
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setTelephones(List<Telephone> telephones) {
		this.telephones = telephones;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		System_User other = (System_User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*There are the user's access: ROLE_ADMIN or ROLE_VISITOR*/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}
	
	/*@Override -> 
	public String getPassword() {
		return this.password;
	}*/
	
	@JsonIgnore
	@Override
	public String getUsername() {
		return this.login;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}
