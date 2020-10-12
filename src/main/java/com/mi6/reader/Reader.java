package com.mi6.reader;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Reader {
	
	protected PropertyChangeSupport support;
	
	protected String path;
	
	protected AtomicBoolean cancel;
	
	public Reader() {
		support = new PropertyChangeSupport(this);
		cancel = new AtomicBoolean(false);
	}
	
	public abstract int count();
	public abstract void read();
	
	public void addPropertyChange(PropertyChangeListener l) {
		support.addPropertyChangeListener(l);
	}

	public void removePropertyChange(PropertyChangeListener l) {
		support.removePropertyChangeListener(l);
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void startReading() {
		cancel.set(false);
	}
	
	public void cancelReading() {
		cancel.set(true);
	}
	
	protected void publish(String name) {
		support.firePropertyChange(name,null,null);
	}
	
	protected void publish(String name, Object value, Object value1) {
		support.firePropertyChange(name, value, value1);
	}
	
	protected void publish(String name, Object value) {
		support.firePropertyChange(name, null, value);
	}
	
	protected void row(String row) {
		support.firePropertyChange("row", null, row);
	}
	
	protected void print() {
		support.firePropertyChange("print", null, null);
	}
	
	protected void done() {
		support.firePropertyChange("done", null, null);
	}

}
