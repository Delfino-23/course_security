package com.course.security.security;

import com.course.security.models.Users;
import com.course.security.repository.IUsers;
import com.course.security.repository.IInstructors;
import com.course.security.repository.IAdministrators;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço de detalhes do usuário que carrega informações de usuários, administradores e instrutores
 * para autenticação e autorização no sistema.
 */
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final IUsers usersRepository;
    private final IAdministrators administratorsRepository;
    private final IInstructors instructorsRepository;

    // Construtor para injeção de dependências
    public JpaUserDetailsService(IUsers usersRepository,
                                 IAdministrators administratorsRepository,
                                 IInstructors instructorsRepository) {
        this.usersRepository = usersRepository;
        this.administratorsRepository = administratorsRepository;
        this.instructorsRepository = instructorsRepository;
    }

    // Método para carregar os detalhes do usuário com base no nome de usuário
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Primeiro, tente buscar nas users (credenciais primárias)
        var maybeUser = usersRepository.findByEmail(username);
        if (maybeUser.isPresent()) {
            Users user = maybeUser.get();
            String role = "ROLE_USER";
            if (administratorsRepository.findByEmail(username).isPresent()) {
                role = "ROLE_ADMIN";
            } else if (instructorsRepository.findByEmail(username).isPresent()) {
                role = "ROLE_INSTRUCTOR";
            }
            // define regra de autorização com base no tipo de usuário
            GrantedAuthority authority = new SimpleGrantedAuthority(role);
            return User.withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities(List.of(authority))
                    .build();
        }

        // Se não existir em Users, tente Administrators
        var maybeAdmin = administratorsRepository.findByEmail(username);
        if (maybeAdmin.isPresent()) {
            var admin = maybeAdmin.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
            return User.withUsername(admin.getEmail())
                    .password(admin.getPassword())
                    .authorities(List.of(authority))
                    .build();
        }

        // Se não existir em Administrators, tente Instructors
        var maybeInstructor = instructorsRepository.findByEmail(username);
        if (maybeInstructor.isPresent()) {
            var instr = maybeInstructor.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_INSTRUCTOR");
            return User.withUsername(instr.getEmail())
                    .password(instr.getPassword())
                    .authorities(List.of(authority))
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}