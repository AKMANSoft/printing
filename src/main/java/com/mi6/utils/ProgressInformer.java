package com.mi6.utils;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProgressInformer {
	
	private int minValue;
	private int maxValue;
	private String operation;
	private ConcurrentLinkedQueue<ProgressListener> listeners = new ConcurrentLinkedQueue<>();
	
	public ProgressInformer(String operation, int minValue, int maxValue) {
		this.operation = operation;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public String getOperation() {
		return operation;
	}
	
	public int getMinValue() {
		return minValue;
	}
	
	public int getMaxValue() {
		return maxValue;
	}
	
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		fireMaxValueChanged(maxValue);
	}
	
	public void addListener(ProgressListener listener) {
		if (listeners.contains(listener)) return;
		listeners.add(listener);
	}
	
	public void removeListener(ProgressListener listener) {
		listeners.remove(listener);
	}
	
	public void fireProgressChange(String comment, int value) {
		for (ProgressListener l : listeners)
			l.progressChanged(comment, value);
	}
	
	public void fireMaxValueChanged(int maxValue) {
		for (ProgressListener l : listeners)
			l.maxValueChanged(maxValue);
	}

}
