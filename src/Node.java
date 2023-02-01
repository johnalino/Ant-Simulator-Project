import java.util.ArrayList;

public class Node {
	
	// attributes
	private int col, row;
	private boolean explored;
	private int food;
	private int pheromone;
	private QueenAnt queenAnt;
	private ArrayList<ForagerAnt> foragerAnts = new ArrayList<ForagerAnt>();
	private ArrayList<ScoutAnt> scoutAnts = new ArrayList<ScoutAnt>();
	private ArrayList<SoldierAnt> soldierAnts = new ArrayList<SoldierAnt>();
	private ArrayList<BalaAnt> balaAnts = new ArrayList<BalaAnt>();
	private ColonyNodeView colonyNodeView;
	
	// constructor
	Node(int col, int row) {
		this.col = col;
		this.row = row;
		
		colonyNodeView = new ColonyNodeView();
		colonyNodeView.setID("(" + col + ", " + row + ")");
		resetNode();
		if (col == 13 && row == 13) setQueenNode();
	}
	
	// methods
	public void resetNode() {
		if ((col >= 12 && col <= 14) && (row >= 12 && row <= 14)) {
			colonyNodeView.showNode();
			explored = true;
		}
		else {
			colonyNodeView.hideNode();
			explored = false;
		}
		
		foragerAnts = new ArrayList<ForagerAnt>();
		scoutAnts = new ArrayList<ScoutAnt>();
		soldierAnts = new ArrayList<SoldierAnt>();
		balaAnts = new ArrayList<BalaAnt>();
		
		colonyNodeView.setForagerCount(0);
		colonyNodeView.setScoutCount(0);
		colonyNodeView.setSoldierCount(0);
		colonyNodeView.setBalaCount(0);
		
		colonyNodeView.hideForagerIcon();
		colonyNodeView.hideScoutIcon();
		colonyNodeView.hideSoldierIcon();
		colonyNodeView.hideBalaIcon();
		
		food = 0;
		pheromone = 0;
		colonyNodeView.setFoodAmount(0);
		colonyNodeView.setPheromoneLevel(0);
		
		if (col == 13 && row == 13) setQueenNode();
	}
	
	public void setQueenNode() {
		colonyNodeView.setQueen(true);
		queenAnt = new QueenAnt();
		colonyNodeView.showQueenIcon();
		
		for (int i = 0; i < 4; i++) scoutAnts.add(new ScoutAnt());
		colonyNodeView.setScoutCount(scoutAnts.size());
		colonyNodeView.showScoutIcon();
		
		for (int j = 0; j < 50; j++) foragerAnts.add(new ForagerAnt());
		colonyNodeView.setForagerCount(foragerAnts.size());
		colonyNodeView.showForagerIcon();
		
		for (int k = 0; k < 10; k++) soldierAnts.add(new SoldierAnt());
		colonyNodeView.setSoldierCount(soldierAnts.size());
		colonyNodeView.showSoldierIcon();
		
		food = 1000;
		colonyNodeView.setFoodAmount(food);
	}
	
	// get ArrayLists
	public ArrayList<ForagerAnt> getForagerAnts() {
		return foragerAnts;
	}
	
	public ArrayList<ScoutAnt> getScoutAnts() {
		return scoutAnts;
	}
	
	public ArrayList<SoldierAnt> getSoldierAnts() {
		return soldierAnts;
	}
	
	public ArrayList<BalaAnt> getBalaAnts() {
		return balaAnts;
	}
	
	// add or remove Ants
	public void addForagerAnt(ForagerAnt ant) {
		foragerAnts.add(ant);
	}
	
	public ForagerAnt removeForagerAnt() {
		return foragerAnts.remove(0);
	}
	
	public void addScoutAnt(ScoutAnt ant) {
		scoutAnts.add(ant);
	}
	
	public ScoutAnt removeScoutAnt() {
		return scoutAnts.remove(0);
	}
	
	public void addSoldierAnt(SoldierAnt ant) {
		soldierAnts.add(ant);
	}
	
	public SoldierAnt removeSoldierAnt() {
		return soldierAnts.remove(0);
	}
	
	public void addBalaAnt(BalaAnt ant) {
		balaAnts.add(ant);
	}
	
	public BalaAnt removeBalaAnt() {
		return balaAnts.remove(0);
	}
	
	// getter only methods
	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	
	public ColonyNodeView getColonyNodeView() {
		return colonyNodeView;
	}
	
	public QueenAnt getQueenAnt() {
		return queenAnt;
	}
	
	// getter and setter methods
	public boolean isExplored() {
		return explored;
	}
	
	public void setExplored(boolean explored) {
		this.explored = explored;
	}
	
	public int getFood() {
		return food;
	}
	
	public void setFood(int food) {
		this.food = food;
	}
	
	public int getPheromone() {
		return pheromone;
	}
	
	public void setPheromone(int pheromone) {
		this.pheromone = pheromone;
	}
}
