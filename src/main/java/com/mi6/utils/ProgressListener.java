package com.mi6.utils;

public interface ProgressListener {
	
	void progressChanged(String comment, int value);
	void maxValueChanged(int maxValue);
	
}
