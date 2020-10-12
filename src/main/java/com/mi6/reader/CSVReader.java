package com.mi6.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVReader extends Reader{

	private final Logger log = LoggerFactory.getLogger(CSVReader.class);
	private int counter;

	public CSVReader() {
		
	}

	public int count() {

		log.info("Counting CSV File");
		publish("CountStart");
		counter = 0;
		int count = 0;

		// create a reader
		try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {

			while (br.readLine() != null) {
				
				if(cancel.get()) {
					publish("cancel");
					return counter;
				}

				if (++count % 18 == 0)
					publish("count", counter++);
			}
			
			log.info("Number of Records Found: "+ counter);
			publish("CountEnd", counter);
			
		} catch (IOException ex) {
			log.error("Error Reading CSV File. ", ex.getMessage());
			log.trace("", ex);
		}

		log.info("Number of Records Found: "+ counter);
		return counter;
	}

	public void read(){

		if(cancel.get()) {
			publish("cancel");
			return;
		}
			
		
		publish("StartReading");
		log.info("Reading CSV File");

		if (path == null || path.isEmpty()) {
			log.error("Cannot Read CSV, CSV Path not set. ");
			return;
		}

		// create a reader
		try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {

			// read the file line by line
			String line;
			int row = 1;
			int n = 1;
			while ((line = br.readLine()) != null) {
				
				if(cancel.get()) {
					publish("cancel");
					return;
				}

				publish("row", row, line);

				if (row++ % 18 == 0) {
					publish("print", n++);
					row = 1;
				}
			}

			publish("done");
			log.info("Reading CSV File Completeds.");

		} catch (IOException ex) {
			log.error("Error Reading CSV File. ", ex.getMessage());
			log.trace("", ex);
		}
	}

}
