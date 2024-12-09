package com.github.datnm23.acountservice.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.github.datnm23.acountservice.security.CustomUserDetails;
import com.github.datnm23.acountservice.statics.Constant;

import java.util.Optional;

@Component
public class JpaAuditingConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(Constant.DEFAULT_CREATOR);
        } else {
            try {
                Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails userDetails = (CustomUserDetails) userAuthentication.getPrincipal();
                return Optional.of(userDetails.getUsername());
            } catch (Exception e) {
                return Optional.of(Constant.DEFAULT_CREATOR);
            }
        }
    }

}
