package io.catalyte.training.health.domains.user;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

/**
 * User repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

}
