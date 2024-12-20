package com.example.training_manager.Security;

import com.example.training_manager.Model.RoleEntity;
import com.example.training_manager.Model.UserEntity;
import com.example.training_manager.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchUserDetails implements UserDetailsService {
    UserRepository userRepository;

    @Autowired
    public FetchUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.
                            findByUsername(username).
                            orElseThrow(()-> new UsernameNotFoundException
                            ("Usuário não encontrado"));
        return new User(user.getUsername(), user.getPassword(), mappingRolesToAuthorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> mappingRolesToAuthorities(List<RoleEntity> roles){
        return roles
                .stream()
                .map(
                roleEntity -> new SimpleGrantedAuthority
                (roleEntity.getRole()))
                .collect(Collectors.toList());
    }
}
