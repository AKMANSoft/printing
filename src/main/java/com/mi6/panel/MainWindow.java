package com.mi6.panel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi6.control.CSVFilter;
import com.mi6.controller.PrintController;

public class MainWindow extends JFrame implements ActionListener, MouseListener, DropTargetListener, PropertyChangeListener {

	private final Logger log = LoggerFactory.getLogger(MainWindow.class);
	private static final long serialVersionUID = 1L;

	private JLabel heading;
	private JTextField field;
	private JButton  print;
	private JProgressBar progress;
	private PrintController controller;

	public MainWindow() {
		setTitle("Priniting Demo Version 0.0.1");
		initComponent();
		setLayout();
		initListener();
		setSize(450, 350);
		setUndecorated(false);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public void initComponent() {

		heading = new JLabel("Printing Application");
		heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
		
		field = new JTextField();
		//field.setInputPrompt("Add Files or Drag and Drop");
		
		print = new JButton("PRINT");
		progress = new JProgressBar();
		progress.setStringPainted(true);
		
		controller = new PrintController();
		
		field.setEnabled(false);
		
	}

	public void setLayout() {
		setLayout(new GridBagLayout());

		GridBagConstraints gc = getConstrain();

		add(heading, gc);

		gc.gridy++;
		gc.weighty = 0;
		gc.insets = new Insets(20, 10, 0, 10);
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(new JLabel("Click to Add File or Drag and Drop"), gc);
		
		gc.gridy++;
		gc.ipady = 20;
		
		gc.insets = new Insets(0, 10, 10, 10);
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(field, gc);
		
		gc.gridy++;
		gc.ipady = 10;
		gc.weighty = 0;
		gc.insets = new Insets(30, 10, 0, 10);
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(progress, gc);

		gc.gridy++;
		gc.ipady = 10;
		gc.insets = new Insets(0, 9, 10, 9);
		gc.fill = GridBagConstraints.HORIZONTAL;
		add(print, gc);

	}

	public void initListener() {
		print.addActionListener(this);
		field.addMouseListener(this);
		field.setDropTarget(new DropTarget(field, this));
		controller.getCSVReader().addPropertyChange(this);
	}

	private GridBagConstraints getConstrain() {

		GridBagConstraints gc = new GridBagConstraints();

		gc.gridx = 0;
		gc.gridy = 0;

		gc.gridwidth = 1;
		gc.gridheight = 1;

		gc.weightx = 1;
		gc.weighty = 1;

		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.NONE;

		gc.insets = new Insets(20, 0, 0, 0);
		gc.ipadx = 0;
		gc.ipady = 0;

		return gc;

	}
	
	private void PrintAction() {
		
		log.info("Print Button Action Arise. ");
		String csv = field.getText();
		
		if(csv == null || csv.isEmpty()) {
			
			log.error("No File Selected");
			
			String title = "No File Selected";
			String message = "Please Select The File First";
			JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		print.setText("CANCEL");
		controller.printAction(csv);
	}
	
	private void CancelAction() {
		
		log.info("Cancel Action Performed");
		controller.cancelAction();
		print.setText("PRINT");
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == print) {

			switch (print.getText()) {
			case "PRINT":
				PrintAction();
				break;
				
			case "CANCEL":
				CancelAction();
				break;

			default:
				break;
			}

		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(e.getClickCount() == 1 && e.getSource() == field) {
			
			log.info("File Choose Event Arise. ");
			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new CSVFilter());
			fileChooser.setMultiSelectionEnabled ( true );
            final int result = fileChooser.showOpenDialog ( this );
            if(result == JFileChooser.CANCEL_OPTION) return;
           
            File file = fileChooser.getSelectedFile();
            field.setText(file.getAbsolutePath());

		}
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public void drop(DropTargetDropEvent dtde) {
		
		log.info("Drop File Event Arise. ");
		// Accept the drop first, important!
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        
        // Get the files that are dropped as java.util.List
        List<File> list;
		try {
			list = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			
			// Now get the first file from the list,
	        File file = (File)list.get(0);
	        
	        CSVFilter filter = new CSVFilter();
	        boolean value = filter.accept(file);
	        
	        if(value) field.setText(file.getAbsolutePath());
	        
		} catch (UnsupportedFlavorException | IOException e) {
			log.error("Drag and Drop Error: ", e.getMessage());
			log.trace("", e);
		}
        
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		switch (evt.getPropertyName()) {
		case "CountStart":
			progress.setIndeterminate(true);
			break;
		case "count":
			int count = (Integer) evt.getNewValue();
			progress.setString("Counting Record in CSV Found: "+ count);
			break;
		case "CountEnd":
			int n = (Integer) evt.getNewValue();
			progress.setMaximum(n);
			progress.setIndeterminate(false);
			break;
		case "StartReading":
			progress.setValue(1);
			progress.setString("Printing Report  " + 1 + " of "+ progress.getMaximum());
			break;
		case "print":
			n = (Integer) evt.getNewValue();
			progress.setValue(n);
			progress.setString("Printing Report  " + n + " of "+ progress.getMaximum());
			break;
		case "cancel":
			progress.setString("Cancel Printing");
			break;
		case "done":
			print.setText("PRINT");
			progress.setString("Finish Printing");
			break;

		default:
			break;
		}
		
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void dragEnter(DropTargetDragEvent dtde) {}
	public void dragOver(DropTargetDragEvent dtde) {}
	public void dropActionChanged(DropTargetDragEvent dtde) {}
	public void dragExit(DropTargetEvent dte) {}
	
	public static void main(String[] args) {

	}

}
