package com.dungdv.java;

/**
 * Created by DUNGDV on 5/17/2017.
 */
public class InnerClass {
    int num;

    // inner class
    private class Inner_Demo {
        public void print() {
            System.out.println("This is an inner class");
        }
    }

    // Accessing he inner class from the method within
    void display_Inner() {
        Inner_Demo inner = new Inner_Demo();
        inner.print();
    }
}

class My_class {

    public static void main(String args[]) {
        // Instantiating the outer class
        InnerClass outer = new InnerClass();

        // Accessing the display_Inner() method.
        outer.display_Inner();
    }
}
