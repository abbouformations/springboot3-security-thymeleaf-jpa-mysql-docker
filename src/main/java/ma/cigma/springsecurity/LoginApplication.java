package ma.cigma.springsecurity;

import lombok.AllArgsConstructor;
import ma.cigma.springsecurity.domaine.RoleVo;
import ma.cigma.springsecurity.domaine.UserVo;
import ma.cigma.springsecurity.service.IUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class LoginApplication  {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(IUserService userService) throws Exception {
		return (args) -> {
			userService.cleanDataBase();
			userService.save(RoleVo.builder().role("ADMIN").build());
			userService.save(RoleVo.builder().role("CLIENT").build());

			RoleVo roleAdmin = userService.getRoleByName("ADMIN");
			RoleVo roleClient = userService.getRoleByName("CLIENT");


			UserVo admin1= UserVo.builder().
					username("admin1").
					password("admin1").
					authorities(List.of(roleAdmin)).
					enabled(true).
					accountNonExpired(true).
					credentialsNonExpired(true).
					accountNonLocked(true).
					build();


			UserVo client1= UserVo.builder().
					username("client1").
					password("client1").
					authorities(List.of(roleClient)).
					enabled(true).
					accountNonExpired(true).
					credentialsNonExpired(true).
					accountNonLocked(true).
					build();

			UserVo sa= UserVo.builder().
					username("superadmin").
					password("superadmin").
					authorities(List.of(roleAdmin,roleClient)).
					enabled(true).
					accountNonExpired(true).
					credentialsNonExpired(true).
					accountNonLocked(true).
					build();

			userService.save(admin1);
			userService.save(client1);
			userService.save(sa);
		};
	}
}