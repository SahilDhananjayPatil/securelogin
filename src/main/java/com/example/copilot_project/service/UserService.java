package com.example.copilot_project.service;

import com.example.copilot_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * UserService interface - defines user related operations.
 *
 * Implementation is provided by UserServiceImpl which handles password
 * encoding and secure password matching.
 */
public interface UserService {

    User registerUser(User user);

    User authenticateUser(String emailOrUsername, String plainPassword);

    User findByEmail(String email);

    User findByUsername(String username);

    User findById(Long id);

    User updateProfile(Long userId, String bio, String website, String gender);

    User updateProfilePicture(Long userId, String pictureUrl, String picturePath);

    Page<User> searchUsers(String query, Pageable pageable);

    Page<User> getPublicProfiles(Pageable pageable);

    void togglePrivacy(Long userId);

    List<User> getNewestUsers();

}

