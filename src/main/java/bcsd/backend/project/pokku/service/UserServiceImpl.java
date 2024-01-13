package bcsd.backend.project.pokku.service;

import bcsd.backend.project.pokku.dao.UserInfoRepository;
import bcsd.backend.project.pokku.domain.Authority;
import bcsd.backend.project.pokku.domain.UserInfo;
import bcsd.backend.project.pokku.dto.UserRequest;
import bcsd.backend.project.pokku.dto.UserResponse;
import bcsd.backend.project.pokku.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserResponse findUsers(UserRequest request) throws Exception{
        UserInfo userInfo = userInfoRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadCredentialsException("잘못된 계정 정보 입니다."));
        if(!passwordEncoder.matches(request.getUserPassword(), userInfo.getUserPassword())){
            throw new BadCredentialsException("잘못된 계정 정보 입니다.");
        }
        if(!jwtProvider.validateToken(request.getToken())) {
            throw new BadCredentialsException("유효하지 않은 토큰 입니다.");
        }
        return UserResponse.builder()
                .userId(userInfo.getUserId())
                .userPassword(userInfo.getUserPassword())
                .userName(userInfo.getUserName())
                .userTel(userInfo.getUserTel())
                .userEducation(userInfo.getUserEducation())
                .userNickname(userInfo.getUserNickname())
                .userBirth(userInfo.getUserBirth())
                .userEmail(userInfo.getUserEmail())
                .build();

    }

    @Override
    public boolean DeleteUsers(UserRequest request) throws Exception{
        UserInfo user = userInfoRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadCredentialsException("잘못된 계정 정보 입니다."));
        if(!passwordEncoder.matches(request.getUserPassword(), user.getUserPassword())){
            throw new BadCredentialsException("잘못된 계정 정보 입니다.");
        }
        if(!jwtProvider.validateToken(request.getToken())) {
            throw new BadCredentialsException("유효하지 않은 토큰 입니다.");
        }
        try {
            UserInfo userInfo = UserInfo.builder()
                    .userId(request.getUserId())
                    .userPassword(passwordEncoder.encode(request.getUserPassword()))
                    .userName(request.getUserName())
                    .userNickname(request.getUserNickname())
                    .userBirth(request.getUserBirth())
                    .userEmail(request.getUserEmail())
                    .userTel(request.getUserTel())
                    .userEducation(request.getUserEducation())
                    .build();

            userInfo.setRoles(Collections.singletonList(Authority.builder()
                    .AuthName("ROLE_User")
                    .build()));

            userInfoRepository.delete(userInfo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    @Override
    public boolean UpdateUsers(UserRequest request) throws Exception{
        UserInfo user = userInfoRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadCredentialsException("잘못된 계정 정보 입니다."));
        if(!passwordEncoder.matches(request.getUserPassword(), user.getUserPassword())){
            throw new BadCredentialsException("잘못된 계정 정보 입니다.");
        }
        if(!jwtProvider.validateToken(request.getToken())) {
            throw new BadCredentialsException("유효하지 않은 토큰 입니다.");
        }
        try {
            UserInfo userInfo = UserInfo.builder()
                    .userId(request.getUserId())
                    .userPassword(passwordEncoder.encode(request.getUserPassword()))
                    .userName(request.getUserName())
                    .userNickname(request.getUserNickname())
                    .userBirth(request.getUserBirth())
                    .userEmail(request.getUserEmail())
                    .userTel(request.getUserTel())
                    .userEducation(request.getUserEducation())
                    .build();

            userInfo.setRoles(Collections.singletonList(Authority.builder()
                    .AuthName("ROLE_User")
                    .build()));

            userInfoRepository.save(userInfo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }
}
