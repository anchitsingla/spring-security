package com.auth.server.respository;

import com.auth.server.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface OtpRepository extends JpaRepository<Otp, String> {

    Optional<Otp> findByUsername(String username);
}
