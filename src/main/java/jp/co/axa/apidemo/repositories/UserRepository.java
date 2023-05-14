package jp.co.axa.apidemo.repositories;


import jp.co.axa.apidemo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}