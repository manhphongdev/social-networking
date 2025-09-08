package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.socialnet.model.AbstractEntity;
import vn.socialnet.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository<L extends Number, P extends AbstractEntity> extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
