package ma.cigma.springsecurity.configuration;

import lombok.AllArgsConstructor;
import ma.cigma.springsecurity.service.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private PasswordEncoder passwordEncoder;
    private IUserService userService;
    private UnauthorizedHandler unauthorizedHandler;
    static final List<String> PUBLIC_API = List.of("/h2/**", "/static/**", "/css/**", "/js/**", "/images/**");
    static final List<String> API_FOR_ALL_USERS = List.of("/", "/login", "/h2/**", "/h2/login.do**");

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PUBLIC_API.toArray(String[]::new));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(API_FOR_ALL_USERS.toArray(String[]::new)).permitAll();
            auth.requestMatchers("/admin/**").hasAuthority("ADMIN");
            auth.requestMatchers("/client/**").hasAuthority("CLIENT");
            auth.anyRequest().authenticated();
        });

        http.formLogin(form -> {
            form.loginPage("/login");
            form.failureUrl("/login?error=true");
            form.defaultSuccessUrl("/welcome");
            form.usernameParameter("username");
            form.passwordParameter("password");
        });

        http.logout(logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
            logout.logoutSuccessUrl("/");
        });

        http.exceptionHandling((exception) -> exception.authenticationEntryPoint(unauthorizedHandler).
                accessDeniedPage("/access-denied"));

        return http.build();
    }
}
