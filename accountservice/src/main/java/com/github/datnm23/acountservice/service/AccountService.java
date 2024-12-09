package com.github.datnm23.acountservice.service;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.datnm23.acountservice.entity.User;
import com.github.datnm23.acountservice.exception.ExpiredEmailActivationUrlException;
import com.github.datnm23.acountservice.exception.ExpiredPasswordForgottenUrlException;
import com.github.datnm23.acountservice.exception.ObjectNotFoundException;
import com.github.datnm23.acountservice.exception.PasswordNotMatchedException;
import com.github.datnm23.acountservice.model.request.ForgotPasswordEmailRequest;
import com.github.datnm23.acountservice.model.request.PasswordChangingRequest;
import com.github.datnm23.acountservice.repository.UserRepository;
import com.github.datnm23.acountservice.statics.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountService {

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    final EmailService emailService;

    @Value("${application.account.activation.expiredDurationInMilliseconds}")
    long activationMailExpiredDurationInMilliseconds;

    @Value("${application.account.passwordForgotten.expiredDurationInMilliseconds}")
    long passwordForgottenExpiredDurationInMilliseconds;

    @Value("${application.account.activation.maxResendTimes}")
    int activationMailMaxSentCount;

    public void changePassword(UUID id, PasswordChangingRequest request) throws ObjectNotFoundException, PasswordNotMatchedException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if (!request.getPassword().equals(request.getConfirmedPassword())) {
            throw new PasswordNotMatchedException("Password not matched");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public void activateAccount(UUID userId) throws ObjectNotFoundException, ExpiredEmailActivationUrlException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        // check xem link active het han chua
        LocalDateTime activationMailSentAt = user.getActivationMailSentAt();
        if (activationMailSentAt.plusSeconds(activationMailExpiredDurationInMilliseconds / 1000).isBefore(LocalDateTime.now())) {
            throw new ExpiredEmailActivationUrlException("Activation link expired");
        }
        user.setStatus(UserStatus.ACTIVATED);
        userRepository.save(user);
    }

    public void sendActivationEmail(UUID id) throws MessagingException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getActivationMailSentCount() > activationMailMaxSentCount) {
            throw new MessagingException("Activation email has been sent over " + activationMailMaxSentCount + " times");
        }
        emailService.sendActivationMail(user);
        user.setActivationMailSentCount(user.getActivationMailSentCount() + 1);
        userRepository.save(user);
    }

    public void sendForgotPasswordEmail(@Valid ForgotPasswordEmailRequest request) throws MessagingException {
        // TODO - cần check so lần gửi tối đa trong 1 khoag thơi gian (vi du chi duoc gui toi da 3 mail trong vong 1h)

        // cần check xem email có tồn tại trong hệ thống không
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        // gửi mail
        emailService.sendForgotPasswordMail(user);
        user.setForgotPasswordMailSentAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void changeForgotPassword(UUID userId, PasswordChangingRequest request)
            throws ObjectNotFoundException, ExpiredPasswordForgottenUrlException, PasswordNotMatchedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        // check xem link active het han chua
        LocalDateTime forgotPasswordMailSentAt = user.getForgotPasswordMailSentAt();
        if (forgotPasswordMailSentAt.plusSeconds(passwordForgottenExpiredDurationInMilliseconds / 1000).isBefore(LocalDateTime.now())) {
            throw new ExpiredPasswordForgottenUrlException("Forgot password link expired");
        }

        if (!request.getPassword().equals(request.getConfirmedPassword())) {
            throw new PasswordNotMatchedException("Password not matched");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
