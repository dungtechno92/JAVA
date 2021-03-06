package com.dungdv;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ArrayList<E> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final Object[] EMPTY_ELEMENTDATA = {};
	private transient Object[] elementData;
	private int size;
	
	public ArrayList() {
		elementData = new Object[DEFAULT_CAPACITY];
	}
	
	public E get(int index){
		checkRange(index);
		return (E) elementData[index];
	}
	
	public void add(Object object){
		if(size == elementData.length) {
			grow();
		}
		elementData[size++] = object;
	}
	
	public void add(int index, Object object){
		
	}
	
	public void set(int index, Object object){
		checkRange(index);
		elementData[index] = object;
	}
	
	public void remove(int index){
		checkRange(index);
		
		Object[] newElementData = new Object[elementData.length];
		int count = 0;
		for (int i = 0; i < size; i++) {
			if(index == i){
				newElementData[count] = elementData[i];
				count ++;
			}
		}
		
		elementData = newElementData;
	}
	
	public int size(){
		return size;
	}
	
	private void grow(){
		int oldCapacity = elementData.length;
		int newCapacity = oldCapacity + oldCapacity >> 1;
		
		elementData = Arrays.copyOf(elementData, newCapacity);
	}
	
	private void checkRange(int index){
		if(index >= size){
			throw new IndexOutOfBoundsException(outOfBoundsMessage(index));
		}
	}
	
	private String outOfBoundsMessage(int index){
		return "index " + index + "of size " + size;
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> xxx = new ArrayList<Integer>();
		System.out.println(xxx.size());
	}
}
