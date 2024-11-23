package ma.cigma.springsecurity.service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String role;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users;

    public Role(String role) {
        this.role = role;
    }
}