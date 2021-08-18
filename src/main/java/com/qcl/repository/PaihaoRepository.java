package com.qcl.repository;

import com.qcl.bean.Paihao;
import com.qcl.bean.PictureInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 排号相关Repository
 */
public interface PaihaoRepository extends JpaRepository<Paihao, Integer> {
    List<Paihao> findByDayAndType(String day, Integer type);

    List<Paihao> findByOpenidAndDay(String openid,String day);

    //查询当天已经就位的客户，并按照号码排序
    List<Paihao> findByDayAndRuzuoAndTypeOrderByNum(String day, boolean ruzuo, Integer type);
}
