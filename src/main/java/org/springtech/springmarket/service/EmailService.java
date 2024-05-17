package org.springtech.springmarket.service;

import org.springtech.springmarket.enumeration.VerificationType;

public interface EmailService {
    void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType);
}
