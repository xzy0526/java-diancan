package com.qcl.repository;

import com.qcl.bean.Leimu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LeiMuRepository extends JpaRepository<Leimu, Integer> {

    List<Leimu> findByLeimuTypeIn(List<Integer> categoryTypeList);

    List<Leimu> findByLeimuType(Integer categoryType);

}
