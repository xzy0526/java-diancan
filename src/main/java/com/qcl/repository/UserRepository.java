package com.qcl.repository;

import com.qcl.bean.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserInfo, String> {
    UserInfo findByOpenid(String openid);
}
