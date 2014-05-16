package fr.utbm.gi.vi51.project.environment;

import org.janusproject.jaak.envinterface.channel.GridStateChannel;
import org.janusproject.jaak.environment.model.JaakEnvironment;
import org.janusproject.jaak.spawner.JaakSpawner;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Kernels;

import fr.utbm.gi.vi51.project.gui.FestivalPanel;

public class FestivalSystem {

	//Set a wrapped environment.
	private static final boolean isWrappedEnvironment = false;
	
	//Maximal number of festival goers.
	private static final int FESTIVAL_GOER_COUNT = 1;

	//Width of the spawn area.
	private static final int SPAWN_WIDTH = 10;

	//Height of the spawn area.
	private static final int SPAWN_HEIGHT = 10;
	
	//X coordinate of the spawn area.
	private static final int SPAWN_X = 0;

	//X coordinate of the spawn area.
	private static final int SPAWN_Y = 0;

	//Width of the Jaak grid.
	private static final int WIDTH = 200;

	//Height of the Jaak grid.
	private static final int HEIGHT = 200;

	public static JaakEnvironment createEnvironment() {
		// Create the Jaak environment with the correct size.
	    JaakEnvironment environment = new JaakEnvironment(WIDTH, HEIGHT);
	    environment.setWrapped(isWrappedEnvironment);
	    return environment;
	}

	public static JaakSpawner createSpwaner(JaakEnvironment environment) {
		return new FestivalSpawner(FESTIVAL_GOER_COUNT,SPAWN_X,SPAWN_Y,SPAWN_WIDTH,SPAWN_HEIGHT);
	}

	public static FestivalPanel createPanel(AgentAddress kernelAddress) {
		GridStateChannel channel = Kernels.get().getChannelManager().getChannel(kernelAddress, GridStateChannel.class);
	    if (channel==null) throw new IllegalStateException();
	    return new FestivalPanel(channel);
	}	
}
