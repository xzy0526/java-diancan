package com.qcl.study_list;

import java.util.ArrayList;
import java.util.List;

public class ListStudy {
    public static void main(String[] args) {
//        List<Integer> list=new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//        list.add(5);
//        list.add(6);
//        list.add(7);
////        System.out.println(list.size());
////        System.out.println(list.get(6));
//        for(int index=0;index<list.size();index++){
//            System.out.println(list.get(index));
//        }


        //集合里面使用类对象
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("特斯拉", 500000));
        carList.add(new Car("法拉利", 1500000));
        carList.add(new Car("奔驰", 1500000));

        for(int i=0;i<carList.size();i++){
            Car car=carList.get(i);
            System.out.println(car.getPinpai()+","+car.getJiage());
        }

    }
}
