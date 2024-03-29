package com.example.oechapp.Repository;

import com.example.oechapp.Entity.User;
import com.example.oechapp.Entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);

    public Optional<User> findById(long id);


    public List<User> findByRole(UserRoles role);
}
