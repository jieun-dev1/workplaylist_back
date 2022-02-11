package com.coding.yo.controller;

import com.coding.yo.entity.Member;
import com.coding.yo.security.message.request.RegisterInfo;
import com.coding.yo.security.message.response.MemberInfo;
import com.coding.yo.security.service.UserDetailsServiceImpl;
import com.coding.yo.util.RequestUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class MemberController {
    private final FirebaseAuth firebaseAuth;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    //회원가입
    @PostMapping("")
//    public MemberInfo register(@RequestHeader("Authorization") String authorization, @RequestBody RegisterInfo registerInfo) {
    public MemberInfo register(@RequestHeader("Authorization") String authorization) {
//
            //Token 가져온다
        FirebaseToken decodedToken;
        try {
            String token = RequestUtil.getAuthorizationToken(authorization);
            decodedToken = firebaseAuth.verifyIdToken(token);

        } catch (IllegalArgumentException |FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "{\"code\":\"INVALID_TOKEN\", \"message\":\"" + e.getMessage() + "\"}");
        }
        log.info("decoded: {} {} {} {}", decodedToken.getUid(), decodedToken.getEmail(),decodedToken.getName(),decodedToken.getPicture());
        //사용자를 등록한다.
        Member registeredUser = userDetailsServiceImpl.register(decodedToken.getUid(), decodedToken.getEmail(),decodedToken.getName(),decodedToken.getPicture());
        return new MemberInfo(registeredUser);
//        return null;
        }

    //로그인
    @GetMapping("/me")
    public MemberInfo login(Authentication authentication) {
        Member member = ((Member) authentication.getPrincipal());
//        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
//        return new MemberInfo(userDetailsImpl);
        log.info("member - 로그인 성공" + member.getUsername());
        return null;
    }
    }
