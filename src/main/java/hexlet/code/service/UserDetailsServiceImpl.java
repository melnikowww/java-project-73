package hexlet.code.service;

import hexlet.code.model.User;

import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).orElseThrow();
        String role = user.getUserRole().name();

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(), user.getPassword(), authorities
        );
    }
}
