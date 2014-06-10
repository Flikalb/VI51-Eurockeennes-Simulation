package fr.utbm.gi.vi51.project.environment;

import org.janusproject.jaak.envinterface.channel.GridStateChannel;
import org.janusproject.jaak.envinterface.perception.Obstacle;
import org.janusproject.jaak.environment.model.JaakEnvironment;
import org.janusproject.jaak.environment.solver.ActionApplier;
import org.janusproject.jaak.spawner.JaakSpawner;
import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Kernels;

import fr.utbm.gi.vi51.project.agent.Direction;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstaclePumpRoom;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleTree;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleWater;
import fr.utbm.gi.vi51.project.environment.obstacles.ObstacleWaterClosed;
import fr.utbm.gi.vi51.project.gui.FestivalPanel;

public class FestivalSystem {

	//Set a wrapped environment.
	private static final boolean isWrappedEnvironment = false;
	
	//Maximal number of festival goers.
	private static final int FESTIVAL_GOER_COUNT = 20;

	//Width of the spawn area.
	private static final int SPAWN_WIDTH = 10;

	//Height of the spawn area.
	private static final int SPAWN_HEIGHT = 10;
	
	//X coordinate of the spawn area.
	private static final int SPAWN_X = 140;

	//X coordinate of the spawn area.
	private static final int SPAWN_Y = 10;

	//Width of the Jaak grid.
	private static final int WIDTH = 200;

	//Height of the Jaak grid.
	private static final int HEIGHT = 200;
        
        public static FestivalMap _festivalMap;

	public static JaakEnvironment createEnvironment() {
		// Create the Jaak environment with the correct size.
	    JaakEnvironment environment = new JaakEnvironment(WIDTH, HEIGHT);
	    ActionApplier ap = environment.getActionApplier();
            
            
            _festivalMap = new FestivalMap();
            
            
	    Scene GrandeScene = new Scene( 0,  40,  20,  80,  19,  60,20,  ap, Direction.EAST);
	    Scene GreenHouse = new Scene( 60,  5,  80,  35,  79,  20,15,  ap, Direction.EAST);
	    Scene Plage = new Scene( 120,  50,  130,  70,  120,  60,10,  ap, Direction.WEST);
            
            _festivalMap.addConstruction(GrandeScene);
            _festivalMap.addConstruction(GreenHouse);
            _festivalMap.addConstruction(Plage);
        
	    //a manger!
	    PumpRoom pmpRoom = new PumpRoom(70,70,72,72,71,71, ap);
        PumpRoom pmpRoom2 = new PumpRoom(66,70,68,72,67,71, ap);
        PumpRoom pmpRoom3 = new PumpRoom(62,70,64,72,63,71, ap);
        
        PumpRoom pmpRoom4 = new PumpRoom(70,58,72,60,71,58, ap);
        PumpRoom pmpRoom5 = new PumpRoom(74,58,76,60,75,58, ap);
        PumpRoom pmpRoom6 = new PumpRoom(78,58,80,60,79,58, ap);
        
            _festivalMap.addConstruction(pmpRoom);
            _festivalMap.addConstruction(pmpRoom2);
            _festivalMap.addConstruction(pmpRoom3);
            _festivalMap.addConstruction(pmpRoom4);
            _festivalMap.addConstruction(pmpRoom5);
            _festivalMap.addConstruction(pmpRoom6);
        
        //aller aux toilettes!
        WaterClosed WC1 = new WaterClosed(14,5,18,7,16,7, ap);
        WaterClosed WC2 = new WaterClosed(20,5,24,7,22,7, ap);
        WaterClosed WC3 = new WaterClosed(26,5,30,7,28,7, ap);
        
        WaterClosed WC4 = new WaterClosed(148,30,150,34,148,32, ap);
        WaterClosed WC5 = new WaterClosed(148,36,150,40,148,38, ap);
        WaterClosed WC6 = new WaterClosed(148,42,150,46,148,44, ap);
        
            _festivalMap.addConstruction(WC1);
            _festivalMap.addConstruction(WC2);
            _festivalMap.addConstruction(WC3);
            _festivalMap.addConstruction(WC4);
            _festivalMap.addConstruction(WC5);
            _festivalMap.addConstruction(WC6);
        
        //foret
        for(int x =0  ; x <160  ; ++x) 
		{ 
			for(int y = 0 ; y < 5 ; ++y) 
			{ 
				ap.putObject(x, y, new ObstacleTree()); 
			}
		}
        for(int x =0  ; x <10  ; ++x) 
		{ 
			for(int y = 0 ; y < 40 ; ++y) 
			{ 
				ap.putObject(x, y, new ObstacleTree()); 
			}
		}
        for(int x =150  ; x <160  ; ++x) 
		{ 
			for(int y = 20 ; y < 50 ; ++y) 
			{ 
				ap.putObject(x, y, new ObstacleTree()); 
			}
		}
        for(int x =0  ; x <120  ; ++x) 
		{ 
			for(int y = 80 ; y < 100 ; ++y) 
			{ 
				ap.putObject(x, y, new ObstacleTree()); 
			}
		}
        for(int x =70  ; x <90  ; ++x) 
		{ 
			for(int y = 60 ; y < 65 ; ++y) 
			{ 
				ap.putObject(x, y, new ObstacleTree()); 
			}
		}
        
        for(int x =60  ; x <80  ; ++x) 
		{ 
			for(int y = 65 ; y < 70 ; ++y) 
			{ 
				ap.putObject(x, y, new ObstacleTree()); 
			}
		}
           
        //eau
        for(int x =120  ; x <160  ; ++x) 
		{ 
			for(int y = 70 ; y < 100 ; ++y) 
			{ 
				ap.putObject(x, y, new ObstacleWater()); 
			}
		}
        for(int x =130  ; x <160  ; ++x) 
		{ 
			for(int y = 50 ; y < 100 ; ++y) 
			{ 
				ap.putObject(x, y, new ObstacleWater()); 
			}
		}
        
         
	    environment.setWrapped(isWrappedEnvironment);
	    return environment;
	}

	public static JaakSpawner createSpawner(JaakEnvironment environment) {
		return new FestivalSpawner(FESTIVAL_GOER_COUNT,SPAWN_X,SPAWN_Y,SPAWN_WIDTH,SPAWN_HEIGHT, _festivalMap);
	}

	public static FestivalPanel createPanel(AgentAddress kernelAddress, JaakEnvironment environment) {
		GridStateChannel channel = Kernels.get().getChannelManager().getChannel(kernelAddress, GridStateChannel.class);
	    if (channel==null) throw new IllegalStateException();
	    return new FestivalPanel(channel, environment);
	}
}
