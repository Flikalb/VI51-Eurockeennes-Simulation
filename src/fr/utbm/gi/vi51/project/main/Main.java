package fr.utbm.gi.vi51.project.main;

import java.util.logging.Level;

import javax.swing.JFrame;

import org.janusproject.jaak.environment.model.JaakEnvironment;
import org.janusproject.jaak.kernel.JaakKernel;
import org.janusproject.jaak.kernel.JaakKernelController;
import org.janusproject.jaak.spawner.JaakSpawner;
import org.janusproject.kernel.logger.LoggerUtil;

import fr.utbm.gi.vi51.project.environment.FestivalSystem;
import fr.utbm.gi.vi51.project.gui.FestivalFrame;
import fr.utbm.gi.vi51.project.gui.FestivalPanel;

public class Main {

	public static void main(String[] args) {
		
		// Change the level of the messages displayed on the console
	    LoggerUtil.setGlobalLevel(Level.WARNING);
	 
	    // Step 1: create the Jaak grid environment.
	    JaakEnvironment environment = FestivalSystem.createEnvironment();
	 
	    // Step 2: create the spawner, i.e. the entry of the festival.
	    JaakSpawner[] spawner = new JaakSpawner[1];
	    spawner[0] = FestivalSystem.createSpwaner(environment);
	 
	    // Step 3: initialize the Jaak kernel.
	    JaakKernelController controller = JaakKernel.initializeKernel(environment, spawner);
	    controller.getTimeManager().setWaitingDuration(500);
	 
	    // Step 4: create the UI.
	    FestivalPanel panel = FestivalSystem.createPanel(controller.getKernelAddress());
	    JFrame frame = new FestivalFrame(panel);
	    frame.setVisible(true);
	}

}
