package com.example.security_app.repository;
import com.example.security_app.model.u;
import com.example.security_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRoleRepository extends JpaRepository<u, Long> {
    List<u> findByUser(User user);
}
