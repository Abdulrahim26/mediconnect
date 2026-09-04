package com.mediconnect.mediconnectapi.repository;

import com.mediconnect.mediconnectapi.entity.PasswordResetToken;
import com.mediconnect.mediconnectapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);
}