package org.springtech.springmarket.service;

import org.springframework.web.multipart.MultipartFile;
import org.springtech.springmarket.domain.User;
import org.springtech.springmarket.dto.UserDTO;
import org.springtech.springmarket.form.UpdateForm;

import java.util.Collection;

public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);
    void sendVerificationCode(UserDTO user);
    UserDTO verifyCode(String email, String code);
    void resetPassword(String email);
    UserDTO verifyPasswordKey(String key);
    void updatePassword(long userId, String password, String confirmPassword);
    UserDTO verifyAccountKey(String key);
    UserDTO updateUserDetails(UpdateForm user);

    UserDTO getUserById(Long userId);
    void updatePassword(Long userId, String currentPassword, String newPassword, String confirmNewPassword);
    void updateUserRole(Long userId, String roleName);
    void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);
    UserDTO toggleMfa(String email);
    void updateImage(UserDTO user, MultipartFile image);

    Collection<User> getUsers(int page, int size);
}
