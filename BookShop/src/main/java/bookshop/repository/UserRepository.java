package bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bookshop.models.User;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
	List<User> findByRoleName(@Param("roleName") String roleName);
}
