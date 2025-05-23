package com.basic.myspringboot.service;

import com.basic.myspringboot.controller.dto.UserDTO;
import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 모드 (성능 최적화)
public class UserService {
    private final UserRepository userRepository;

    // 등록
    @Transactional
    public UserDTO.UserResponse createUser(UserDTO.UserCreateRequest request) {
        // Email 중복 검사
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new BusinessException(
                            "User with this Email already exist", HttpStatus.CONFLICT);
                });
        User user = request.toEntity();
        User saveUser = userRepository.save(user);
        return new UserDTO.UserResponse(saveUser);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 수정
    @Transactional
    public UserDTO.UserResponse updateUserByEmail(String email, UserDTO.UserUpdateRequest userDetail) {
        User user = getUserByEmail(email);

        user.setName(userDetail.getName());
//        return userRepository.save(user);
        return new UserDTO.UserResponse(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
    }

    // 삭제
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }


}