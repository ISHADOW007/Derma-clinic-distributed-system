package userservice.com.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import userservice.com.user.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {



    Page<User> findByAgeBetween(
            Integer minAge,
            Integer maxAge,
            Pageable pageable
    );

    Page<User> findByAgeGreaterThanEqual(
            Integer minAge,
            Pageable pageable
    );

    Page<User> findByAgeLessThanEqual(
            Integer maxAge,
            Pageable pageable
    );

    Optional<User> findByEmail(String username);

    Optional<User> findByEmailIgnoreCase(String email);
}








