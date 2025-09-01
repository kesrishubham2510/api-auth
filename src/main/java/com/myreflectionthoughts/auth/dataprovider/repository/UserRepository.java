package com.myreflectionthoughts.auth.dataprovider.repository;

import com.myreflectionthoughts.auth.datamodel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

     User findByUsername(String userName);
     User findByEmail(String email);
}
