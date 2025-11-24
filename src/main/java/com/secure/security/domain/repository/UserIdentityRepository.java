package com.secure.security.domain.repository;

import com.secure.security.domain.model.entity.UserIdentity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserIdentityRepository extends JpaRepository<@NonNull UserIdentity, @NonNull Long> {

    List<UserIdentity> findByProviderUserId(Long providerUserId);

    Optional<UserIdentity> findOptionalByProviderUserIdAndProvider(Long id, UserIdentity.AuthProvider authProvider);
}
