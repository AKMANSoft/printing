package com.mi6.prinitng;

public class Box {

	private double x;
	private double y;

	public Box(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return (float) (x * (72 / 2.54));
	}
	
	public float getY() {
		return (float) (y * (72 / 2.54));
	}

	
}
