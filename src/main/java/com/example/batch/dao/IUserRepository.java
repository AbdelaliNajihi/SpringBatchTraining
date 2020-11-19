package com.example.batch.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.batch.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
}
