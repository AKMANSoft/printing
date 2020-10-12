package com.mi6.prinitng;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 412 X 764
 * 
 * @author Mi6
 *
 */
public class Template implements PropertyChangeListener{
	
	private final Logger log = LoggerFactory.getLogger(Template.class);
	
	private static final int BOX_IN_X_AXIS = 3;
	
	private static final int BOX_IN_Y_AXIS = 6;
	
	private static final double PADDING_X = 0;
	
	private static final double PADDING_Y = 1.6;
	
	private static final double WIDTH = 3.2;
	
	private static final double HEIGHT = 2.5;
	
	private BigBox[][] boxes;
	
	private Printer printer;
	
	public Template() {
		printer = new Printer();
		boxes = new BigBox[BOX_IN_X_AXIS][BOX_IN_Y_AXIS];
	}
	
	public void initBoxes() {
		
		log.info("Creating Test Page");
		
		for(int x = 0; x < BOX_IN_X_AXIS; x++) {
			
			for(int y = 0; y < BOX_IN_Y_AXIS; y++) {
				
				double p = getX(x);
				
				double q = getY(y);
				
				boxes[x][y] = new BigBox(p , q);
				
				boxes[x][y].initBoxes();
				
				printBox(boxes[x][y]);
				
			}
			
		}
		
		printer.close();
		
	}
	
	private double getX(int x) {
		return PADDING_X + (WIDTH * x);
	}
	
	private double getY(int y) {
		return PADDING_Y + (HEIGHT * y);
	}
	
	public void printBox(BigBox bigBox) {
		
		for(int x = 0; x < 8; x++)
			for(int y = 0; y < 6; y++)
				printer.addX(bigBox.getBox(x, y));
			
	}
	
	public int getValue(String s) {
		return Integer.valueOf(s);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		
		if(evt.getPropertyName().equals("columns")) {
			String[] columns = (String[]) evt.getNewValue();
			String s = (String) evt.getOldValue();
			
			String[] value = s.split(";");
			
			int row = getValue(value[0]);
			int col = getValue(value[1]);
			
			double x = getX(row);
			double y = getY(col); 

			for(String v: columns) {
				printer.addX(new BigBox(x, y).getBox(getValue(v)));
			}
		}
		
		if(evt.getPropertyName().equals("done")) {
			printer.close();
		}
		
	}
	
	public static void main(String[] args) {
		Template box = new Template();
		box.initBoxes();
	}
	
}
