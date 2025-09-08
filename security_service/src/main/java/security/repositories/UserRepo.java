package security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import security.models.auth.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    User getUserByEmail(String email);
}
