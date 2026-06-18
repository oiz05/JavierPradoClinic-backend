package com.clinica_javierprado.cjp_backend.repository;

import com.clinica_javierprado.cjp_backend.domain.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
