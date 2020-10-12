package com.mi6.prinitng;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

public class Report implements PropertyChangeListener{

	private final Logger log = LoggerFactory.getLogger(Report.class);
	
	private Object[] array;

	public Report() {
		array = new Object[18];
	}

	public void print() {

		log.info("Start Generating Report");

		try {

			String RName = "/com/mi6/template.jasper";

			JasperReport jr = (JasperReport) JRLoader.loadObject(Report.class.getResourceAsStream(RName));

			log.info("Filling Report");
			Map<String, Object> map = new HashMap<>();
			JRMapArrayDataSource source = new JRMapArrayDataSource(array);
			JasperPrint jp = JasperFillManager.fillReport(jr, map, source);

			log.info("Printing Report");

			//JasperViewer.viewReport(jp, false);
			//JasperExportManager.exportReportToPdfFile(jp, "report.pdf");

			log.info("Completed");
			
			printReport(jp);

		} catch (JRException e) {

			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);

			log.error("Error Prinitng Report", e.getMessage());
			log.trace("", e);
		}
	}

	public void addRow(int row, String line) {
		Map<String, Object> map = new HashMap<>();
		log.info("Adding Row: "+ line);
		int column = 0;
		StringTokenizer tokens = new StringTokenizer(line, ",");
		while(tokens.hasMoreElements()) {
			String v = (String)tokens.nextElement();
			log.info("COLUMN_"+column + " Value: "+ v);
			map.put("COLUMN_"+column++, Integer.parseInt(v));
		}
		array[row-1] = map;
	}
	
	public void printReport(JasperPrint jasperPrint) throws JRException {
		
		PrintService selectedService = 
                PrintServiceLookup.lookupDefaultPrintService();
		
		PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();

		PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
		printServiceAttributeSet.add(new PrinterName(selectedService.getName(), null));

		JRPrintServiceExporter exporter = new JRPrintServiceExporter();
		SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
		configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
		configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
		configuration.setDisplayPageDialog(false);
		configuration.setDisplayPrintDialog(false);

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setConfiguration(configuration);

		System.err.println("Selected Printer Name: "+ selectedService.getName());

		exporter.exportReport();
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("row")) {
			int row = (Integer) evt.getOldValue();
			String line = (String) evt.getNewValue();
			addRow(row, line);
		}
		
		if(evt.getPropertyName().equals("print")) {
			print();
		}
		
	}
	
}
