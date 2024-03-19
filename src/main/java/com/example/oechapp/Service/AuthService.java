package com.example.oechapp.Service;

import com.example.oechapp.Entity.ResponseDTO.JWTAuthResponse;
import com.example.oechapp.Entity.User;
import com.example.oechapp.Security.UserDetailsServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final RecoveryCodeService recoveryCodeService;
    private final UserService userService;


    public JWTAuthResponse signIn(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                password
        ));

        var user = userDetailsService.loadUserByUsername(username);

        var jwt = jwtService.generateToken(user);
        return new JWTAuthResponse(jwt);
    }

    public void sendResetCode(String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isEmpty())
        {
            throw new NotFoundException("user with email - " + email + " nott found");
        }

        // Generate and save reset code
        String code = recoveryCodeService.getResetCode(email);
        // Save code and associate it with email
        // For example: passwordResetService.saveResetCode(email, code);

        // Send code via email
        String subject = "Password Reset Code";
        String text = "Your password reset code is: " + code;
        try {
            emailService.sendEmail(email, subject, text);
        } catch (MessagingException e) {
            // Handle email sending failure
            e.printStackTrace();
        }
    }

    public void resetPassword(String email, String code, String newPassword) {
        // Validate code
        boolean isValidCode = recoveryCodeService.verifyResetCode(email, code);
        if (isValidCode) {
            // Reset password
            userService.resetPassword(email, newPassword);
        } else {
            throw new RuntimeException("Invalid reset code");
        }
    }

}
