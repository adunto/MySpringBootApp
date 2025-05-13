package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@Controller + @ResponseBody
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Profile("test")
public class UserRestController {
    // UserRepository 가져오기 (의존성)
//    @Autowired
    private final UserRepository userRepository;

    @PostMapping
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.map(ResponseEntity::ok)
                .orElse(new ResponseEntity("User Not Found", HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/email/{email}/")
    public User getUserByEmail(@PathVariable String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User existUser =
                optionalUser.orElseThrow(() -> new BusinessException("User Not Found",HttpStatus.NOT_FOUND));
        return existUser;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetail) {
        Optional<User> optionalUser = userRepository.findById(id);
        User existUser = optionalUser.orElseThrow(() ->
                new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
        existUser.setName(userDetail.getName());
        existUser.setEmail(userDetail.getEmail());
        User updatedUser = userRepository.save(existUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
        userRepository.delete(user);
        //return ResponseEntity.ok(user);
        return ResponseEntity.ok("User 삭제됨.");
    }
}
