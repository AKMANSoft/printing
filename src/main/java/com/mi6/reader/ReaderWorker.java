package com.mi6.reader;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReaderWorker extends SwingWorker<Void, Void>{

	private final Logger log = LoggerFactory.getLogger(ReaderWorker.class);
	
	private Reader reader;
	
	public ReaderWorker(Reader reader) {
		this.reader = reader;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		reader.startReading();
		reader.count();
		reader.read();
		return null;
	}
	
	public void cancelReading() {
		reader.cancelReading();
	}
	
	@Override
	protected void done() {
		try {
			get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.done();
	}

}
