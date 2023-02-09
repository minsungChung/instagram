package com.example.instagram_diana.src.user;


import com.example.instagram_diana.src.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    // ※ JPA Query Method
    // findBy 규칙 , Username 문법
    // select * from user where username = ?
    Optional<User> findById(Long id);			// 회원 검색(아이디로)
    User findByEmail(String email);
    User findByUsername(String username);
    User findByPhone(String phone);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);

    User findByPassword(String passsword);
}
