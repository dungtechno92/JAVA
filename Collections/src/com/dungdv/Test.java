package com.dungdv;

import java.util.*;
import java.util.HashMap;
import java.util.TreeMap;

public class Test {
	public static void main(String[] args) {
		TreeMap<String, String> maps = new TreeMap<String, String>();
		maps.put("1", "dung1");
		maps.put("2", "dung2");

		Map.Entry<String, String> entry = maps.ceilingEntry("1");
		System.out.print("----");

		String key = maps.ceilingKey("1");
		System.out.print("----");

		String key3 = maps.remove("1");

		String key2 = maps.get("1");
		System.out.print("----");

	}
}
