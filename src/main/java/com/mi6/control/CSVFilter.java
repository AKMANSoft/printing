package com.mi6.control;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CSVFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		return f.getName().toLowerCase().contains("csv");
	}

	@Override
	public String getDescription() {
		return "Select CSV File";
	}

}
