package com.example.copilot_project.service;

import com.example.copilot_project.entity.User;
import com.example.copilot_project.entity.UserProfile;
import com.example.copilot_project.exception.InvalidCredentialsException;
import com.example.copilot_project.exception.ResourceNotFoundException;
import com.example.copilot_project.exception.UserAlreadyExistsException;
import com.example.copilot_project.repository.UserProfileRepository;
import com.example.copilot_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * UserServiceImpl - concrete implementation of UserService.
 *
 * Ensures passwords are encoded at registration and verifies passwords
 * using PasswordEncoder.matches(...) during authentication. Also contains
 * basic checks for duplicate email/username.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(User user) {
        log.info("Attempting to register user with email: {}", user.getEmail());

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        // Prevent duplicate email/username
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Registration failed: Email already exists: {}", user.getEmail());
            throw new UserAlreadyExistsException("Email already registered. Please use a different email.");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("Registration failed: Username already exists: {}", user.getUsername());
            throw new UserAlreadyExistsException("Username already taken. Please choose a different username.");
        }

        // Encode password before saving
        String raw = user.getPassword();
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        String encoded = passwordEncoder.encode(raw);
        user.setPassword(encoded);

        // Persist user
        User savedUser = userRepository.save(user);

        // Create a profile record for the user if profile repo exists
        try {
            UserProfile profile = UserProfile.builder().user(savedUser).build();
            userProfileRepository.save(profile);
        } catch (Exception e) {
            // Non-fatal: Log and continue; the user record is still valid
            log.debug("Could not create UserProfile for user {}: {}", savedUser.getId(), e.getMessage());
        }

        log.info("User registered successfully: {}", savedUser.getEmail());
        return savedUser;
    }

    @Override
    public User authenticateUser(String emailOrUsername, String plainPassword) {
        log.info("Attempting to authenticate user: {}", emailOrUsername);

        if (emailOrUsername == null || emailOrUsername.isBlank()) {
            throw new InvalidCredentialsException("Invalid email/username or password.");
        }

        // Find by email first, then username
        User user = userRepository.findByEmail(emailOrUsername)
                .orElseGet(() -> userRepository.findByUsername(emailOrUsername).orElse(null));

        if (user == null) {
            log.warn("Authentication failed: User not found: {}", emailOrUsername);
            throw new InvalidCredentialsException("Invalid email/username or password.");
        }

        // Use PasswordEncoder.matches to compare raw password with encoded hash
        if (!passwordEncoder.matches(plainPassword, user.getPassword())) {
            log.warn("Authentication failed: Invalid password for: {}", emailOrUsername);
            throw new InvalidCredentialsException("Invalid email/username or password.");
        }

        // Update last login timestamp
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("User authenticated successfully: {}", emailOrUsername);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional
    public User updateProfile(Long userId, String bio, String website, String gender) {
        User user = findById(userId);
        user.setBio(bio);
        user.setWebsite(website);
        user.setGender(gender);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateProfilePicture(Long userId, String pictureUrl, String picturePath) {
        User user = findById(userId);
        user.setProfilePictureUrl(pictureUrl);
        user.setProfilePicturePath(picturePath);
        return userRepository.save(user);
    }

    @Override
    public Page<User> searchUsers(String query, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                query, query, query, pageable);
    }

    @Override
    public Page<User> getPublicProfiles(Pageable pageable) {
        return userRepository.findPublicProfiles(pageable);
    }

    @Override
    @Transactional
    public void togglePrivacy(Long userId) {
        User user = findById(userId);
        user.setIsPrivate(!user.getIsPrivate());
        userRepository.save(user);
    }

    @Override
    public java.util.List<User> getNewestUsers() {
        return userRepository.findTop10ByOrderByCreatedAtDesc();
    }

}

