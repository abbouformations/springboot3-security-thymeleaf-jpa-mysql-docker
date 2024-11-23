package ma.cigma.springsecurity.service.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotEmpty;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@NoArgsConstructor
public class User  {
	@Id
	@GeneratedValue
	private Long id;

	@Length(min = 5, message = "*Your username must have at least 15 characters")
	@NotEmpty(message = "*Please provide an user name")
	@Column(unique=true)
	private String username;

	@Length(min = 5, message = "*Your password must have at least 15 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;

	private boolean enabled;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean accountNonLocked;
	private String email;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Role> authorities = new ArrayList<Role>();
}