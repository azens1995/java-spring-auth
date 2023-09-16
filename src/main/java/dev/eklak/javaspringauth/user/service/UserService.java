package dev.eklak.javaspringauth.user.service;

import dev.eklak.javaspringauth.user.models.Otp;
import dev.eklak.javaspringauth.user.models.User;
import dev.eklak.javaspringauth.user.repository.OtpRepository;
import dev.eklak.javaspringauth.user.repository.UserRepository;
import dev.eklak.javaspringauth.user.utils.GenerateCodeUtil;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user) {
        Optional<User> retrievedUser = userRepository.findUserByUsername(user.getUsername());
        if (retrievedUser.isPresent()) {
            User u = retrievedUser.get();
            if (passwordEncoder.matches(user.getPassword(), u.getPassword())) {
                // If the password is correct, generate a new OTP
                renewOtp(u);
            } else {
                throw new BadCredentialsException("Bad Credentials");
            }
        } else {
            throw new BadCredentialsException("Bad Credentials");
        }

    }

    private void renewOtp(User u) {
        // Generate a random value for otp
        String code = GenerateCodeUtil.generateCode();
        // Search the OTP by username
        Optional<Otp> retrievedOtp = otpRepository.findOtpByUsername(u.getUsername());
        if (retrievedOtp.isPresent()) {
            Otp otp = retrievedOtp.get();
            // Update code if otp exists
            otp.setCode(code);
        } else {
            // If the OTP does not exist for the user, create one with the generated code
            Otp otp = new Otp();
            otp.setUsername(u.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
    }

    public boolean check(Otp otpToValidate) {
        Optional<Otp> retrievedOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());
        if (retrievedOtp.isPresent()){
            Otp otp = retrievedOtp.get();
            // Check the otp present in DB and received to validate
            if (otpToValidate.getCode().equals(otp.getCode())) {
                return true;
            }
        }
        return false;
    }
}
