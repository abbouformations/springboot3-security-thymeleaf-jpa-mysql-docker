package ma.cigma.springsecurity.dao;

import ma.cigma.springsecurity.service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(String role);

}