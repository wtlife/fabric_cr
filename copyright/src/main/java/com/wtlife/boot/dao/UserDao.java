package com.wtlife.boot.dao;

import com.wtlife.boot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDao extends JpaRepository<User, Long> {
    List<User> findByName(String name);

    List<User> findByNameAndPassword(String name, String password);

    @Query(value = "select u from User u where u.name=:name")
    User findUser(@Param("name") String name);

}
