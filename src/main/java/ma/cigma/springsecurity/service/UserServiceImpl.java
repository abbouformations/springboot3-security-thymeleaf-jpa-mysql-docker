package ma.cigma.springsecurity.service;

import lombok.AllArgsConstructor;
import ma.cigma.springsecurity.dao.RoleRepository;
import ma.cigma.springsecurity.dao.UserRepository;
import ma.cigma.springsecurity.domaine.RoleVo;
import ma.cigma.springsecurity.domaine.UserVo;
import ma.cigma.springsecurity.service.model.Role;
import ma.cigma.springsecurity.service.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return modelMapper.map(userRepository.findByUsername(username), UserVo.class);
    }

    @Override
    public void save(UserVo userVo) {
        User user = modelMapper.map(userVo, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> rolesPersist = new ArrayList<>();
        for (Role role : user.getAuthorities()) {
            Role userRole = roleRepository.findByRole(role.getRole()).
                    orElseThrow(() -> new RuntimeException("No Role exist"));
            rolesPersist.add(userRole);
        }
        user.setAuthorities(rolesPersist);
        userRepository.save(user);
    }

    @Override
    public void save(RoleVo roleVo) {

        roleRepository.save(modelMapper.map(roleVo, Role.class));
    }

    @Override
    public List<UserVo> getAllUsers() {

        return userRepository.
                findAll().
                stream().
                map(bo -> modelMapper.map(bo, UserVo.class)).
                collect(Collectors.toList());
    }

    @Override
    public List<RoleVo> getAllRoles() {

        return roleRepository.findAll().
                stream().
                map(role -> modelMapper.map(role, RoleVo.class)).
                collect(Collectors.toList());
    }

    @Override
    public RoleVo getRoleByName(String role) {
        return modelMapper.map(
                roleRepository.findByRole(role).orElseThrow(() -> new RuntimeException("No Role exist")),
                RoleVo.class);
    }

    @Override
    public void cleanDataBase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
