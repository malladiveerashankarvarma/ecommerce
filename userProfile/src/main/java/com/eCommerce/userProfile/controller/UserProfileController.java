package com.eCommerce.userProfile.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.eCommerce.userProfile.dto.UserProfileRequest;
import com.eCommerce.userProfile.dto.UserProfileResponse;
import com.eCommerce.userProfile.entity.UserProfile;
import com.eCommerce.userProfile.repository.UserProfileRepository;
import com.eCommerce.userProfile.security.JwtUtils;

import java.util.Optional;
@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserProfileRepository repo;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(@RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        Optional<UserProfile> profile = repo.findById(userId);
        return profile.map(p -> ResponseEntity.ok(toDto(p))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me")
    public ResponseEntity<?> createOrUpdateProfile(@RequestHeader("Authorization") String authHeader,
                                                   @RequestBody UserProfileRequest req) {
        Long userId = extractUserIdFromToken(authHeader);
        UserProfile profile = new UserProfile();
        profile.setId(userId);
        profile.setFullName(req.getFullName());
        profile.setAddress(req.getAddress());
        profile.setPhoneNumber(req.getPhoneNumber());
        repo.save(profile);
        return ResponseEntity.ok("Profile updated");
    }

    private Long extractUserIdFromToken(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtils.getUserIdFromJwtToken(token); // extract from claim
    }

    private UserProfileResponse toDto(UserProfile p) {
        UserProfileResponse dto = new UserProfileResponse();
        dto.setId(p.getId());
        dto.setFullName(p.getFullName());
        dto.setAddress(p.getAddress());
        dto.setPhoneNumber(p.getPhoneNumber());
        return dto;
    }

    @GetMapping("/test")
    public String testPublic() { return "public"; }
}
