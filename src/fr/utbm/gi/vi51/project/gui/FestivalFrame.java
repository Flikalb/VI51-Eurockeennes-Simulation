package fr.utbm.gi.vi51.project.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.janusproject.jaak.envinterface.channel.GridStateChannel;
import org.janusproject.kernel.agent.Kernels;

public class FestivalFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2415712154712596814L;
	
	public FestivalFrame(FestivalPanel panel) {
		GridStateChannel channel = panel.getChannel();
		
		setTitle("Eurockeennes Simulation");
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		addWindowListener(new Closer());
		
		JScrollPane scrollPane = new JScrollPane(panel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		setPreferredSize(new Dimension(10+FestivalPanel.CELL_SIZE*(channel.getGridWidth()), 
									   10+FestivalPanel.CELL_SIZE*(channel.getGridHeight())));
		pack();
	}
	
	private static class Closer extends WindowAdapter {

		public Closer() {}

		@Override
		public void windowClosing(WindowEvent event) {
			Kernels.killAll();
			System.exit(0);
		}
	}

}
