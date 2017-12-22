package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

}
