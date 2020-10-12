package com.mi6;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mi6.panel.MainWindow;
import com.mi6.utils.Settings;
import com.mi6.utils.Util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;

public class Main {

	private final static Logger log = LoggerFactory.getLogger(Main.class);

	private static StringBuilder logForDialog = new StringBuilder();
	public static final int MAX_VIEW_LOGS = 1000_000_0; // 10Mb
	public static final int VIEW_LOGS_RESERVE = 1000_000; // 1Mb
	public static final int MAX_LOG_SIZE = 1_000_000_000;
	public static String logpath;

	public static void main(final String[] args) {

		boolean trace = false;
		try {
			Settings sets = Settings.load();
			trace = sets.isTrace();
		} catch (Exception ignore) {
		}

		configureLogger(trace);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				log.info("Application started. Priniting, v 0.0.1");

				for (String string : args) {
					log.info("Value of args is " + string);
				}

				log.info("Setting Windows Look and Feel.");
				try {
					// Set System L&F
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (UnsupportedLookAndFeelException e) {
					// handle exception
				} catch (ClassNotFoundException e) {
					// handle exception
				} catch (InstantiationException e) {
					// handle exception
				} catch (IllegalAccessException e) {
					// handle exception
				}

				log.info("Initilizing MainWindow");
				MainWindow main = new MainWindow();
				main.setVisible(true);


			}
		});
	}

	private static void configureLogger(boolean trace) {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(Logger.ROOT_LOGGER_NAME);
		LoggerContext ctx = root.getLoggerContext();
		ctx.reset();
		if (trace)
			root.setLevel(Level.TRACE);
		// root.setLevel(Level.INFO);
		root.setLevel(Level.TRACE);
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(ctx);
		encoder.setPattern("%date %-5level %C{30}: %message%n");
		encoder.start();

		ConsoleAppender<ILoggingEvent> con = new MyConsoleAppender<>(logForDialog);
		con.setContext(ctx);
		con.setEncoder(encoder);
		con.start();

		encoder = new PatternLayoutEncoder();
		encoder.setContext(ctx);
		encoder.setPattern("%date %-5level %C{30}: %message%n");
		encoder.start();

		FileAppender<ILoggingEvent> fil = new FileAppender<>();
		fil.setContext(ctx);
		fil.setEncoder(encoder);
		File logDir = new File("./", "Debug Logs");
		logDir.mkdirs();
		logpath = new File(logDir,
				"debug_log " + (new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new Date())) + ".log")
						.getAbsolutePath();
		fil.setFile(logpath);
		fil.setAppend(true);
		fil.start();

		root.addAppender(con);
		root.addAppender(fil);
	}

	public static StringBuilder getLogForDialog() {
		return logForDialog;
	}
}

class MyConsoleAppender<E> extends ConsoleAppender<E> {
	StringBuilder sb;

	public MyConsoleAppender(StringBuilder sb) {
		super();
		this.sb = sb;
	}

	public void writeOut(E ev) throws IOException {
		super.writeOut(ev);
		try {
			ILoggingEvent event = (ILoggingEvent) ev;
			if (!event.getLevel().equals(Level.DEBUG)) {
				sb.append(Util.getCommonDateTimeFormat().format(new Date(event.getTimeStamp())) + " "
						+ event.getMessage() + "\n");
				if (sb.length() > Main.MAX_VIEW_LOGS) {
					sb.delete(0, sb.length() - Main.MAX_VIEW_LOGS + Main.VIEW_LOGS_RESERVE);
				}
			}
		} catch (Exception e) {

		}
	}
}
