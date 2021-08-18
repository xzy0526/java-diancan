package com.qcl.repository;

import com.qcl.bean.WxOrderRoot;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRootRepository extends JpaRepository<WxOrderRoot, Integer> {


    List<WxOrderRoot> findByBuyerOpenidAndOrderStatus(String buyerOpenid, Integer orderStatus, Sort updateTime);

    List<WxOrderRoot> findAll(Specification specification);
}
