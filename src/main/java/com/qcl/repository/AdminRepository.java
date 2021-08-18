package com.qcl.repository;

import com.qcl.bean.AdminInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminInfo, Integer> {
    //    List<AdminInfo> findByPhoneOrUsername(String phone);
//    void findByUsername();
    AdminInfo findByPhoneOrUsername(String phone, String userName);

    AdminInfo findByAdminId(Integer adminId);
}
