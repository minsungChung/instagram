package com.example.instagram_diana.src.member.repository;


import com.example.instagram_diana.src.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member,Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String userName);

    Member findByEmail(String loginInput);

    Member findByUsername(String loginInput);

    Member findByPhone(String loginInput);
}
