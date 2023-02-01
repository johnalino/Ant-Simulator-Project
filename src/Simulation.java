import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Simulation implements SimulationEventListener, ActionListener {
	
	private int turnCounter;
	private int dayCounter;
	private AntSimGUI gui;
	private Timer swingTimer;
	private Colony colony;
	private boolean running;
	private boolean isSetup;
	
	public static void main(String[] args) {
		AntSimGUI gui = new AntSimGUI();
		Simulation sim = new Simulation(gui);
	}
	
	Simulation(AntSimGUI gui) {
		this.gui = gui;
		this.turnCounter = 0;
		this.dayCounter = 0;
		this.swingTimer = new Timer(1000, this);
		
		gui.addSimulationEventListener(this);
		colony = new Colony(27, 27);
		gui.initGUI(colony.getColonyView());
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (colony.isFinished()) {
			JOptionPane.showMessageDialog(null, "The simulation has ended", "Finished", JOptionPane.INFORMATION_MESSAGE);
			running = false;
			swingTimer.stop();
		}
		else {
			gui.setTime("Day " + dayCounter + " Turn " + turnCounter++);
			colony.takeTurn(turnCounter);
			if (turnCounter >= 10) {
				turnCounter = 0;
				dayCounter++;
			}
		}
	}
	
	@Override
	public void simulationEventOccurred(SimulationEvent simEvent) {
		if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT) {
			// set up initial state of the simulation
			if (running) {
				running = false;
				swingTimer.stop();
			}
			
			if (isSetup)
				colony.resetNodes();
			else {
				colony.normalSetup();
				isSetup = true;
			}
			turnCounter = 0;
			dayCounter = 0;
			gui.setTime("Day " + dayCounter + " Turn " + turnCounter++);
		}
		else if (simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT) {
			// set up antSim.simulation for testing the queen ant
			JOptionPane.showMessageDialog(null, "Queen Test Event", "Queen Test", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT) {
			// set up antSim.simulation for testing the scout ant
			JOptionPane.showMessageDialog(null, "Scout Test Event", "Scout Test", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT) {
			// set up antSim.simulation for testing the forager ant
			JOptionPane.showMessageDialog(null, "Forager Test Event", "Forager Test", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT) {
			// set up antSim.simulation for testing the soldier ant
			JOptionPane.showMessageDialog(null, "Soldier Test Event", "Soldier Test", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (simEvent.getEventType() == SimulationEvent.RUN_EVENT) {
			// if already running, stop the timer; if not running start the timer
			if (!isSetup)
				JOptionPane.showMessageDialog(null, "Colony hasn't been set up yet!", "Setup Error", JOptionPane.INFORMATION_MESSAGE);
			else {
				if (colony.isFinished()) {
					JOptionPane.showMessageDialog(null, "The simulation has finished", "Finished", JOptionPane.INFORMATION_MESSAGE);
					swingTimer.stop();
				}
				else {
					if (!running) {
						swingTimer.start();
						running = true;
					}
					else {
						running = false;
						swingTimer.stop();
					}
				}
			}
		}
		else if (simEvent.getEventType() == SimulationEvent.STEP_EVENT) {
			// if already running stop the timer; increment turn counter
			if (!isSetup)
				JOptionPane.showMessageDialog(null, "Colony hasn't been set up yet!", "Setup Error", JOptionPane.INFORMATION_MESSAGE);
			else {
				if (running) {
					running = false;
					swingTimer.stop();
				}
				
				if (colony.isFinished()) {
					JOptionPane.showMessageDialog(null, "The simulation has finished", "Finished", JOptionPane.INFORMATION_MESSAGE);
					swingTimer.stop();
				}
				else {
					gui.setTime("Day " + dayCounter + " Turn " + turnCounter++);
					colony.takeTurn(turnCounter);
					if (turnCounter >= 10) {
						turnCounter = 0;
						dayCounter++;
					}
				}
			}
		}
		else {
			// invalid event occurred
		}
	}
}
