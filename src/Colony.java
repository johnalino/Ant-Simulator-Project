import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Colony {
	
	// attributes
	private Node[][] nodes;
	private ColonyView colonyView;
	private boolean finished;
	private static Random random = new Random();
	int columns, rows;
	
	// constructor
	Colony(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		
		nodes = new Node[columns][rows];
		colonyView = new ColonyView(columns, rows);
	}
	
	// setup methods
	public void normalSetup() {
		finished = false;
		for (int col = 0; col < nodes.length; col++) {
			for (int row = 0; row < nodes[col].length; row++) {
				nodes[col][row] = new Node(col, row);
				colonyView.addColonyNodeView(nodes[col][row].getColonyNodeView(), col, row);
				nodes[col][row].resetNode();
			}
		}
	}
	
	public void resetNodes() {
		for (Node[] col : nodes) {
			for (Node node : col) {
				node.resetNode();
			}
		}
	}
	
	// turn taking methods
	public void takeTurn(int currentTurn) {
		// spawn Bala ants
		spawnBalaAnts();
		
		// take turn for Queen Ant
		takeTurnQueenAnt(currentTurn);
		
		// take turn for all other ants
		for (Node[] col : nodes) {
			for (Node node : col) {
				takeTurnAnts(node.getBalaAnts(), node);
				takeTurnAnts(node.getSoldierAnts(), node);
				takeTurnAnts(node.getScoutAnts(), node);
				takeTurnAnts(node.getForagerAnts(), node);
			}
		}
		
		// reset turns for all other ants
		for (Node[] col : nodes) {
			for (Node node : col) {
				for (BalaAnt b : node.getBalaAnts())
					b.setTurnFinished(false);
				for (ForagerAnt f : node.getForagerAnts())
					f.setTurnFinished(false);
				for (ScoutAnt s : node.getScoutAnts())
					s.setTurnFinished(false);
				for (SoldierAnt sl : node.getSoldierAnts())
					sl.setTurnFinished(false);
				
				// decrease pheromone
				if (currentTurn % 10 == 0) {
					node.setPheromone(node.getPheromone() / 2);
					node.getColonyNodeView().setPheromoneLevel(node.getPheromone());
				}
			}
		}
	}
	
	private void spawnBalaAnts() {
		if (random.nextInt(100) < 3) {
			if (random.nextBoolean()) {
				int col = random.nextInt(27);
				int row = random.nextBoolean() ? 0 : 26;
				nodes[col][row].addBalaAnt(new BalaAnt());
				updateBalaAnts(nodes[col][row]);
			}
			else {
				int row = random.nextInt(27);
				int col = random.nextBoolean() ? 0 : 26;
				nodes[col][row].addBalaAnt(new BalaAnt());
				updateBalaAnts(nodes[col][row]);
			}
		}
	}
	
	private void takeTurnQueenAnt(int currentTurn) {
		// Queen turn
		nodes[13][13].getQueenAnt().decreaseLifespan();
		if (nodes[13][13].getQueenAnt().getLifespan() <= 0) {
			finished = true;
			nodes[13][13].getColonyNodeView().hideQueenIcon();
		}
		else {
			nodes[13][13].setFood(nodes[13][13].getFood() - 1);
			nodes[13][13].getColonyNodeView().setFoodAmount(nodes[13][13].getFood());
		}
		
		// Queen hatch
		if ((currentTurn + 1) % 10 == 0) {
			int number = random.nextInt(4);
			if (number == 0) {
				nodes[13][13].addSoldierAnt(new SoldierAnt());
				updateSoldierAnts(nodes[13][13]);
			}
			else if (number == 1) {
				nodes[13][13].addScoutAnt(new ScoutAnt());
				updateScoutAnts(nodes[13][13]);
			}
			else {
				nodes[13][13].addForagerAnt(new ForagerAnt());
				updateForagerAnts(nodes[13][13]);
			}
		}
	}
	
	private <E extends Ant> void takeTurnAnts(ArrayList<E> ants, Node node) {
		Iterator<E> itr = ants.iterator();
		while (itr.hasNext()) {
			E ant = (E) itr.next();
			if (!ant.isTurnFinished()) {
				ant.decreaseLifespan();
				if (ant.getLifespan() <= 0) {
					itr.remove();
					if (ant instanceof BalaAnt)
						updateBalaAnts(node);
					else if (ant instanceof SoldierAnt)
						updateSoldierAnts(node);
					else if (ant instanceof ScoutAnt)
						updateScoutAnts(node);
					else if (ant instanceof ForagerAnt) {
						node.setFood(node.getFood() + ((ForagerAnt) ant).getFoodHeld());
						updateForagerAnts(node);
					}
				}
				else {
					if (ant instanceof BalaAnt) {
						if (takeTurnBalaAnts((BalaAnt) ant, node)) {
							itr.remove();
							updateBalaAnts(node);
						}
					}
					else if (ant instanceof SoldierAnt) {
						takeTurnSoldierAnts((SoldierAnt) ant, node);
						itr.remove();
						updateSoldierAnts(node);
					}
					else if (ant instanceof ScoutAnt) {
						takeTurnScoutAnts((ScoutAnt) ant, node);
						itr.remove();
						updateScoutAnts(node);
					}
					else if (ant instanceof ForagerAnt) {
						takeTurnForagerAnts((ForagerAnt) ant, node);
						itr.remove();
						updateForagerAnts(node);
					}
				}
			}
		}
	}
	
	private boolean takeTurnBalaAnts(BalaAnt b, Node node) {
		b.setTurnFinished(true);
		
		// Attack : Queen > Soldier > Scout > Forager
		if (node.getQueenAnt() != null) {
			if (random.nextBoolean()) {
				node.getColonyNodeView().hideQueenIcon();
				finished = true;
			}
			return false;
		}
		else if (node.getSoldierAnts().size() > 0) {
			if (random.nextBoolean()) {
				node.removeSoldierAnt();
				updateSoldierAnts(node);
			}
			return false;
		}
		else if (node.getScoutAnts().size() > 0) {
			if (random.nextBoolean()) {
				node.removeScoutAnt();
				updateScoutAnts(node);
			}
			return false;
		}
		else if (node.getForagerAnts().size() > 0) {
			if (random.nextBoolean()) {
				node.setFood(node.getFood() + node.getForagerAnts().get(0).getFoodHeld());
				node.removeForagerAnt();
				updateForagerAnts(node);
			}
			return false;
		}
		// Move towards adjacent non-Bala ants or randomly
		else {
			ArrayList<Node> availableNodes = getAvailableNodes(node, b.willAllowUnexploredNodes());
			Node newNode = null;
			for (Node n : availableNodes) {
				if (n.getForagerAnts().size() > 0 || n.getScoutAnts().size() > 0 || n.getSoldierAnts().size() > 0 || n.getForagerAnts().size() > 0) {
					newNode = n;
					break;
				}
			}
			if (newNode == null)
				newNode = availableNodes.get(random.nextInt(availableNodes.size()));
			
			newNode.addBalaAnt(b);
			updateBalaAnts(newNode);
			return true;
		}
	}
	
	private boolean takeTurnSoldierAnts(SoldierAnt sl, Node node) {
		sl.setTurnFinished(true);
		
		// Attack
		if (node.getBalaAnts().size() > 0) {
			if (random.nextBoolean()) {
				node.removeBalaAnt();
				updateBalaAnts(node);
			}
			return false;
		}
		// Move towards adjacent Bala ants or randomly
		else {
			ArrayList<Node> availableNodes = getAvailableNodes(node, sl.willAllowUnexploredNodes());
			Node newNode = null;
			for (Node n : availableNodes) {
				if (n.getBalaAnts().size() > 0) {
					newNode = n;
					break;
				}
			}
			if (newNode == null)
				newNode = availableNodes.get(random.nextInt(availableNodes.size()));
			
			newNode.addSoldierAnt(sl);
			updateSoldierAnts(newNode);
			return true;
		}
	}
	
	private void takeTurnScoutAnts(ScoutAnt s, Node node) {
		s.setTurnFinished(true);
		
		// Move randomly
		ArrayList<Node> availableNodes = getAvailableNodes(node, s.willAllowUnexploredNodes());
		Node newNode = availableNodes.get(random.nextInt(availableNodes.size()));
		newNode.addScoutAnt(s);
		updateScoutAnts(newNode);
	}
	
	private void takeTurnForagerAnts(ForagerAnt f, Node node) {
		f.setTurnFinished(true);
		
		Node newNode = null;
		if (f.getPath().isEmpty()) f.addToPath(nodes[13][13]);
		
		// Forage mode
		if (f.getFoodHeld() <= 0) {
			ArrayList<Node> availableNodes = getAvailableNodes(node, f.willAllowUnexploredNodes());
			if (availableNodes.contains(nodes[13][13])) availableNodes.remove(nodes[13][13]);
			if (availableNodes.size() > 1 && f.getPath().size() > 1) availableNodes.remove(nodes[f.previousNode().getCol()][f.previousNode().getRow()]);
			
			int maxPheromone = 0;
			for (Node n : availableNodes) {
				if (n.getPheromone() > maxPheromone) {
					maxPheromone = n.getPheromone();
					newNode = n;
				}
			}
			
			if (newNode == null) {
				if (!availableNodes.isEmpty()) newNode = availableNodes.get(random.nextInt(availableNodes.size()));
				else newNode = f.previousNode();
			}
			
			if (newNode.getFood() > 0) {
				f.setFoodHeld(1);
				newNode.setFood(newNode.getFood() - 1);
				if (newNode.getPheromone() < 1000) newNode.setPheromone(newNode.getPheromone() + 10);
			}
			else
				f.addToPath(newNode);
			
			newNode.addForagerAnt(f);
			updateForagerAnts(newNode);
		}
		// Return-to-nest mode
		else {
			newNode = f.popFromPath();
			newNode.addForagerAnt(f);
			if (f.getPath().isEmpty()) {
				newNode.setFood(newNode.getFood() + 1);
				f.setFoodHeld(0);
			}
			else {
				if (newNode.getPheromone() < 1000) newNode.setPheromone(newNode.getPheromone() + 10);
			}
			updateForagerAnts(newNode);
		}
	}
	
	// update colony view methods
	private void updateBalaAnts(Node node) {
		node.getColonyNodeView().setBalaCount(node.getBalaAnts().size());
		if (node.getBalaAnts().size() > 0)
			node.getColonyNodeView().showBalaIcon();
		else
			node.getColonyNodeView().hideBalaIcon();
	}
	
	private void updateForagerAnts(Node node) {
		node.getColonyNodeView().setFoodAmount(node.getFood());
		node.getColonyNodeView().setPheromoneLevel(node.getPheromone());
		node.getColonyNodeView().setForagerCount(node.getForagerAnts().size());
		if (node.getForagerAnts().size() > 0)
			node.getColonyNodeView().showForagerIcon();
		else
			node.getColonyNodeView().hideForagerIcon();
	}
	
	private void updateScoutAnts(Node node) {
		if (!node.isExplored()) {
			node.setExplored(true);
			if (random.nextInt(4) == 0) {
				node.setFood(random.nextInt(501) + 500);
				node.getColonyNodeView().setFoodAmount(node.getFood());
			}
			node.getColonyNodeView().showNode();
		}
		node.getColonyNodeView().setScoutCount(node.getScoutAnts().size());
		if (node.getScoutAnts().size() > 0)
			node.getColonyNodeView().showScoutIcon();
		else
			node.getColonyNodeView().hideScoutIcon();
	}
	
	private void updateSoldierAnts(Node node) {
		node.getColonyNodeView().setSoldierCount(node.getSoldierAnts().size());
		if (node.getSoldierAnts().size() > 0)
			node.getColonyNodeView().showSoldierIcon();
		else
			node.getColonyNodeView().hideSoldierIcon();
	}
	
	// getter methods
	private ArrayList<Node> getAvailableNodes(Node currentNode, boolean allowUnexploredNodes) {
		ArrayList<Node> possibleMoves = new ArrayList<Node>();
		
		// check left, left-up, left-down
		if (currentNode.getCol() != 0) {
			if (allowUnexploredNodes || nodes[currentNode.getCol() - 1][currentNode.getRow()].isExplored())
				possibleMoves.add(nodes[currentNode.getCol() - 1][currentNode.getRow()]);
			if (currentNode.getRow() != 0)
				if (allowUnexploredNodes || nodes[currentNode.getCol() - 1][currentNode.getRow() - 1].isExplored())
					possibleMoves.add(nodes[currentNode.getCol() - 1][currentNode.getRow() - 1]);
			if (currentNode.getRow() != 26)
				if (allowUnexploredNodes || nodes[currentNode.getCol() - 1][currentNode.getRow() + 1].isExplored())
					possibleMoves.add(nodes[currentNode.getCol() - 1][currentNode.getRow() + 1]);
		}
		// check right, right-up, right-down
		if (currentNode.getCol() != 26) {
			if (allowUnexploredNodes || nodes[currentNode.getCol() + 1][currentNode.getRow()].isExplored())
				possibleMoves.add(nodes[currentNode.getCol() + 1][currentNode.getRow()]);
			if (currentNode.getRow() != 0)
				if (allowUnexploredNodes || nodes[currentNode.getCol() + 1][currentNode.getRow() - 1].isExplored())
					possibleMoves.add(nodes[currentNode.getCol() + 1][currentNode.getRow() - 1]);
			if (currentNode.getRow() != 26)
				if (allowUnexploredNodes || nodes[currentNode.getCol() + 1][currentNode.getRow() + 1].isExplored())
					possibleMoves.add(nodes[currentNode.getCol() + 1][currentNode.getRow() + 1]);
		}
		// check up
		if (currentNode.getRow() != 0)
			if (allowUnexploredNodes || nodes[currentNode.getCol()][currentNode.getRow() - 1].isExplored())
				possibleMoves.add(nodes[currentNode.getCol()][currentNode.getRow() - 1]);
		// check down
		if (currentNode.getRow() != 26)
			if (allowUnexploredNodes || nodes[currentNode.getCol()][currentNode.getRow() + 1].isExplored())
				possibleMoves.add(nodes[currentNode.getCol()][currentNode.getRow() + 1]);
		
		return possibleMoves;
	}
	
	public ColonyView getColonyView() {
		return colonyView;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
}
