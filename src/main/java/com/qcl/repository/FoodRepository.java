package com.qcl.repository;

import com.qcl.bean.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 菜品repository
 */
public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findByFoodStockLessThan(int num);//查询库存少于num的菜品

    List<Food> findByFoodStatusAndFoodNameContaining(Integer foodStatus, String name);

    List<Food> findByFoodStatus(Integer foodStatus);

}
