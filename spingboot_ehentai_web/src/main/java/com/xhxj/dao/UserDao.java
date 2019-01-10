package com.xhxj.dao;

import com.xhxj.daomain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDao  extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
}
