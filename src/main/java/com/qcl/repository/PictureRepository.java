package com.qcl.repository;

import com.qcl.bean.PictureInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<PictureInfo, Integer> {
    PictureInfo findByPicId(Integer picId);
}
