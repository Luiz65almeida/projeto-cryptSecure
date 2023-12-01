package com.almeida.criptografiasha512.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.almeida.criptografiasha512.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
