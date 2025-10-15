package vn.socialnet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.socialnet.enums.AuthProvider;
import vn.socialnet.model.AccountProvider;
import vn.socialnet.model.User;

import java.util.Optional;

@Repository
public interface AccountProviderRepository extends JpaRepository<AccountProvider, Long> {

    Optional<AccountProvider> findByUserAndProvider(User user, AuthProvider provider);
}
