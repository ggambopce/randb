package com.jinho.randb.domain.profile.api.user;

import com.jinho.randb.domain.profile.application.user.ProfileService;
import com.jinho.randb.domain.profile.dto.user.UserAddRequest;
import com.jinho.randb.global.payload.ControllerApiResponse;
import com.jinho.randb.global.security.oauth2.details.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(value = "/user/profiles")
    public ResponseEntity<?> createProfile(@Valid @RequestBody UserAddRequest userAddRequest, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        profileService.save(userAddRequest, principalDetails.getAccountId());
        return ResponseEntity.ok(new ControllerApiResponse(true,"작성 성공"));
    }
}
