package org.secure.security.common.web.repository;

import org.secure.security.common.web.model.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserIdentityRepository extends JpaRepository<UserIdentity, Long> {
    List<UserIdentity> findByProviderUserId(Long providerUserId);

    Optional<UserIdentity> findByProviderUserIdAndProvider(Long id, UserIdentity.AuthProvider authProvider);
}
