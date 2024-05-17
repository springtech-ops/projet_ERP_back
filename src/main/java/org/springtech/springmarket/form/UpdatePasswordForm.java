package org.springtech.springmarket.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordForm {
    @NotEmpty(message = "current Password cannot be empty")
    private String currentPassword;
    @NotEmpty(message = "New Password cannot be empty")
    private String newPassword;
    @NotEmpty(message = "Confirm Password cannot be empty")
    private String confirmNewPassword;
}
