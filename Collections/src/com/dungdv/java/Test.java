package com.dungdv.java;

import java.util.Date;

/**
 * Created by DUNGDV on 5/17/2017.
 */
public class Test {

    public static void changeDate(Date date) {
        date.setYear(30);
    }

    public static void changeCount(Long count) {
        count = 20L;
    }
    public static void main(String[] args) {
        Date start = new Date();
        Test.changeDate(start);

        Long count = 10L;
        Test.changeCount(count);

        System.out.print(start);
    }
}
