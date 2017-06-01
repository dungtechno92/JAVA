package com.dungdv.java;

/**
 * Created by DUNGDV on 5/17/2017.
 */
public class StaticNestedClass {
    public int field;
    static class Nested_Demo {
        public void my_method() {
            System.out.println("This is my nested class");
        }
    }

    public static void main(String args[]) {
        StaticNestedClass.Nested_Demo nested = new StaticNestedClass.Nested_Demo();
        nested.my_method();
    }
}
