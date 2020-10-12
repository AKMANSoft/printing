package com.mi6.controller;
import com.mi6.prinitng.Report;
import com.mi6.reader.CSVReader;
import com.mi6.reader.ReaderWorker;

public class PrintController {

	private CSVReader reader;
	private ReaderWorker worker;
	private Report report;
	
	public PrintController() {
		report = new Report();
		reader = new CSVReader();
		reader.addPropertyChange(report);
		
	}
	
	public void printAction(final String path) {
		reader.setPath(path);
		worker = new ReaderWorker(reader);
		worker.execute();
		
	}
	
	public void cancelAction() {
		reader.cancelReading();
	}
	
	public CSVReader getCSVReader() {
		return reader;
	}
}
