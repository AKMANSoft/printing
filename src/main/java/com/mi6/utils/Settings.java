package com.mi6.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

public class Settings implements Cloneable {

	private transient static final Logger log = LoggerFactory.getLogger(Settings.class);
	private transient static String CURRENT_FILE = "settings.yaml";

	private transient static File settingsFile = new File(CURRENT_FILE);
	private transient static Settings loadedSettings = null;

	private boolean trace = false;

	public Settings() {
	}

	private synchronized static void loadSettings() {
		loadedSettings = Settings.load();
	}

	private synchronized static void setLoadedSettings(Settings sets) {
		try {
			loadedSettings = (Settings) sets.clone();
		} catch (CloneNotSupportedException e) {
			log.error("Can't update current setting", e);
		}
	}

	public static Settings getLoadedSettings() {
		if (loadedSettings == null)
			loadSettings();
		return loadedSettings;
	}

	public static Settings loadFrom(File source) throws Exception {
		Settings result = null;
		try {
			Representer rp = new Representer();
			rp.getPropertyUtils().setSkipMissingProperties(true);
			result = (Settings) new Yaml(new Constructor(Settings.class), rp).load(new FileReader(source));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw ex;
		}
		return result;
	}

	public static Settings load() {
		Settings s = null;
		if (settingsFile.exists()) {
			try {
				Representer rp = new Representer();
				rp.getPropertyUtils().setSkipMissingProperties(true);
				s = (Settings) new Yaml(new Constructor(Settings.class), rp).load(new FileReader(settingsFile));
				if (s != null) {
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}
		if (s == null) {
			s = new Settings();
			s.initDefault();
			try {
				s.save(new FileWriter(settingsFile));
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		}

		setLoadedSettings(s);
		return s;
	}

	public void save() {
		try {
			save(new FileWriter(settingsFile));
			setLoadedSettings(this);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			String title = "Unable to Save Settings";
			String message = "Error saving the analysis settings file, "
					+ "please check application is running as admin and not write-blocked";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
		}
	}

	public void saveAs(File dest) throws Exception {
		try {
			save(new FileWriter(dest));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			String title = "Unable to Save Settings";
			String message = "Error saving the analysis settings file, "
					+ "please check application is running as admin and not write-blocked";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
			throw ex;
		}
	}

	public void save(Writer writer) {
		Yaml yaml = new Yaml();
		yaml.dump(this, writer);
		try {
			writer.close();
		} catch (Exception ignore) {
		}
	}

	private void initDefault() {
		trace = false;
	}

	public boolean isTrace() {
		return trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}

}
