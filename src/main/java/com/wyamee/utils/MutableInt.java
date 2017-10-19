package com.wyamee.utils;

import java.util.Map;

public class MutableInt {

	int value = 0;
	
	public void increment () { 
		++value;      
	}
	
	public void add(int value) {
		this.value += value; 
	}
	
	public int get() { 
		return value; 
	}
	
	public static void incrementMap(Map<String, MutableInt> counter, String key) {
		MutableInt count = counter.get(key);
		if (count == null) {
			MutableInt mutableInt = new MutableInt();
			mutableInt.increment();
			counter.put(key, mutableInt);
		}
		else {
		    count.increment();
		}
	}
	
	public static void incrementMap(Map<String, MutableInt> counter, String key, int size) {
		MutableInt count = counter.get(key);
		if (count == null) {
			MutableInt mutableInt = new MutableInt();
			mutableInt.add(size);
			counter.put(key, mutableInt);
		}
		else {
		    count.add(size);
		}
	}
}
