package com.example.oechapp.Security;

import com.example.oechapp.Entity.User;
import com.example.oechapp.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetailsService userDetailsService() {
        return this::getbyEmail;
    }
    public UserDetailsImpl getbyEmail(String email)
    {
        Optional<User> founded = userRepository.findByEmail(email);
        if (founded.isEmpty())
        {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
        return new UserDetailsImpl(founded.get());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(UserDetailsImpl::new).orElseThrow(() -> new UsernameNotFoundException("Cant find user with email "+ email));
    }
}
