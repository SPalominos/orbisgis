package org.orbisgis.core.ui.plugins.orbisgisFrame.configuration;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;

import org.orbisgis.core.Services;
import org.orbisgis.core.configuration.BasicConfiguration;
import org.orbisgis.core.workspace.DefaultWorkspace;
import org.orbisgis.core.workspace.Workspace;

public class WorkspaceConfiguration implements IConfiguration {

	private WorkspacePanel panel;
	private static final String TIMER_PROPERTY = "timer";
	private String timerValue = "1";

	@Override
	public void applyUserInput() {
		if (panel.getAuthCheck().isSelected())
			timerValue = panel.getTimer().getValue();
		else
			timerValue = null;
		apply(timerValue);
		if (timerValue != null)
			changePeriodicSaving(timerValue);
	}

	private void apply(String timerValue) {
		Properties systemSettings = System.getProperties();
		if (timerValue != null)
			systemSettings.put(TIMER_PROPERTY, timerValue);
		else
			systemSettings.remove(TIMER_PROPERTY);
		System.setProperties(systemSettings);
	}

	@Override
	public JComponent getComponent() {
		stopPeriodicSaving();
		panel = new WorkspacePanel(timerValue);
		panel.init();
		return panel;
	}

	@Override
	public void loadAndApply() {
		BasicConfiguration bc = Services.getService(BasicConfiguration.class);
		timerValue = bc.getProperty(TIMER_PROPERTY);
		apply(timerValue);
	}

	@Override
	public void saveApplied() {
		Properties systemSettings = System.getProperties();
		timerValue = systemSettings.getProperty(TIMER_PROPERTY);
		BasicConfiguration bc = Services.getService(BasicConfiguration.class);
		if (timerValue != null)
			bc.setProperty(TIMER_PROPERTY, timerValue);
		else {
			bc.removeProperty(TIMER_PROPERTY);
		}

	}

	private static void changePeriodicSaving(String sTimer) {
		stopPeriodicSaving();
		int iTimer;
		if ((iTimer = convert(sTimer)) != 0)
			startPeriodicSaving(iTimer);
	}

	private static void stopPeriodicSaving() {
		DefaultWorkspace workspace = (DefaultWorkspace) Services
				.getService(Workspace.class);
		workspace.getTimer().stopSaving();
	}

	public static int convert(String sTimer) {
		try {
			return Integer.parseInt(sTimer);
		} catch (NumberFormatException e) {
			Services.getErrorManager().warning("No timer to save workspace");
		}
		return 0;
	}

	public static void startPeriodicSaving(int timer) {
		DefaultWorkspace workspace = (DefaultWorkspace) Services
				.getService(Workspace.class);
		// Apply periodic saving
		workspace.setTimer(new PeriodicSaveWorkspace(workspace));
		workspace.getTimer().setPeriodicTimeToSaveWrksp(
				TimeUnit.MINUTES.toMillis(timer));
		workspace.getTimer().start();
	}

	public static String getTimerProperty() {
		return TIMER_PROPERTY;
	}

	public String validateInput() {
		if (panel.getAuthCheck().isSelected()) {
			String timer = panel.getTimer().getValue();
			if (timer.equals("")) {
				return "You must specify a timer greater than 1.";
			} else {
				try {
					int timerValue = Integer.parseInt(timer);
					if (timerValue < 1 && timerValue > 60) {
						return "You must specify a timer between 1 and 60.";
					}
				} catch (NumberFormatException e) {
					return "Only numeric value allowed.";
				}

			}

		}
		return null;
	}

}
