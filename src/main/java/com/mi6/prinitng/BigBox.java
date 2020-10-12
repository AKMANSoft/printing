package com.mi6.prinitng;

/**
 * 1 inch = 2.54 cm = 72 pc;
 * 
 * @author Mi6
 *
 */
public class BigBox {

	private static final int BOX_IN_X_AXIS = 8;

	private static final int BOX_IN_Y_AXIS = 6;

	private static final double MARGIN_X = 0.08;

	private static final double MARGIN_Y = 0.08;

	private static final double WIDTH = 0.3;

	private static final double HEIGHT = 0.3;

	private double paddingX = 0.5;

	private double paddingY = 2.1;

	private Box[][] boxes;

	public BigBox(double paddingX, double paddingY) {
		
		this.paddingX = paddingX;
		
		this.paddingY = paddingY;
		
		boxes = new Box[BOX_IN_X_AXIS][BOX_IN_Y_AXIS];
		
	}

	public void initBoxes() {

		for (int x = 0; x < BOX_IN_X_AXIS; x++) {

			for (int y = 0; y < BOX_IN_Y_AXIS; y++) {

				// 0.5 + 0.1 + ( (0.4 + 0.1) * 0)
				// 0.5 + 0.1 + ( (0.4 + 0.1) * 1)
				double p = (paddingX + MARGIN_X) + ((WIDTH + MARGIN_X) * x);

				double q = (paddingY + MARGIN_Y) + ((HEIGHT + MARGIN_Y) * y);

				boxes[x][y] = new Box(p, q);

			}
		}

	}
	
	public Box getBox(int position) {
		 int row = (int) Math.floor(position / BOX_IN_X_AXIS);
		 int column = position - (BOX_IN_X_AXIS * row);
		 System.out.println("Position: " + position +" Row: "+ row + " Column: "+ column);
		 return getBox(row, column);
	}
	
	public Box getBox(int x, int y){
		double p = getX(x);
		double q = getY(y);
		return new Box(p, q);
	}
	
	private double getX(int x) {
		return (paddingX + MARGIN_X) + ((WIDTH + MARGIN_X) * x);
	}
	
	private double getY(int y) {
		return (paddingY + MARGIN_Y) + ((HEIGHT + MARGIN_Y) * y);
	}

}
