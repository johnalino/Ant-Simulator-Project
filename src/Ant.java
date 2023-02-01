import java.util.Stack;

public abstract class Ant {
	
	// constants
	protected static final int YEAR = 3650;
	
	// attributes
	private int lifespan;
	private boolean turnFinished;
	private boolean allowUnexploredNodes;
	private int id;
	private static int idCounter;
	
	// constructor
	Ant(int lifespan, boolean allowUnexploredNodes) {
		this.lifespan = lifespan;
		this.allowUnexploredNodes = allowUnexploredNodes;
		this.id = idCounter;
		idCounter++;
	}
	
	// methods
	public int getLifespan() {
		return lifespan;
	}
	
	public void decreaseLifespan() {
		lifespan--;
	}
	
	public boolean isTurnFinished() {
		return turnFinished;
	}
	
	public void setTurnFinished(boolean turnFinished) {
		this.turnFinished = turnFinished;
	}
	
	public boolean willAllowUnexploredNodes() {
		return allowUnexploredNodes;
	}
	
	public int getId() {
		return id;
	}
	
}

class QueenAnt extends Ant {
	
	QueenAnt() {
		super(20 * YEAR, false);
	}
	
}

class ForagerAnt extends Ant {
	
	// attributes
	private int foodHeld = 0;
	private Stack<Node> path = new Stack<Node>();
	
	// constructor
	ForagerAnt() {
		super(YEAR, false);
	}
	
	// methods
	public int getFoodHeld() {
		return foodHeld;
	}
	
	public void setFoodHeld(int food) {
		foodHeld = food;
	}
	
	public Stack<Node> getPath() {
		return path;
	}
	
	public void addToPath(Node node) {
		path.push(node);
	}
	
	public Node popFromPath() {
		return path.pop();
	}
	
	public Node previousNode() {
		return path.get(path.size() - 2);
	}
}

class ScoutAnt extends Ant {
	
	ScoutAnt() {
		super(YEAR, true);
	}
	
}

class SoldierAnt extends Ant {
	
	SoldierAnt() {
		super(YEAR, false);
	}
	
}

class BalaAnt extends Ant {
	
	BalaAnt() {
		super(YEAR, true);
	}
	
}
