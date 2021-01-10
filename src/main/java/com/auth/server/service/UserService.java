package com.auth.server.service;

import com.auth.server.entity.Otp;
import com.auth.server.entity.User;
import com.auth.server.respository.OtpRepository;
import com.auth.server.respository.UserRepository;
import com.auth.server.util.GenerateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user) {
        Optional<User> o =
                userRepository.findByUsername(user.getUsername());
        if (o.isPresent()) {
            User u = o.get();
            if (passwordEncoder.matches(
                    user.getPassword(),
                    u.getPassword())) {
                renewOtp(u);
            } else {
                throw new BadCredentialsException("Bad credentials.");
            }
        } else {
            throw new BadCredentialsException("Bad credentials.");
        }
    }

    public boolean check(Otp otpToValidate) {
        Optional<Otp> userOtp = otpRepository.findByUsername(otpToValidate.getUsername());
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            return otpToValidate.getCode().equals(otp.getCode());
        }
        return false;
    }

    private void renewOtp(User u) {
        String code = GenerateCodeUtil
                .generateCode();
        Optional<Otp> userOtp = otpRepository.findByUsername(u.getUsername());
        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
        } else {
            Otp otp = new Otp();
            otp.setUsername(u.getUsername());
            otp.setCode(code);

            otpRepository.save(otp);
        }
    }
}
