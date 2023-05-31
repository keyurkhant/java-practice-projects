import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * WordAgent is the class for handling each row or column having starting grid points and direction. It also contains
 * possible word list which could be available for that word agent.
 * */
public class WordAgent {
    private GridPoint startPoint;
    private int size;
    private Character direction;
    private HashSet<String> potentialWordlist;
    private Character[] value ;

    /**
     * WordAgent constructor is used to create instance of word agent with its required data.
     * @param startPoint: Starting point of the word agent
     * @param direction: Horizontal or vertial direction of the grid
     * */
    public WordAgent(GridPoint startPoint, int size, char direction) {

        this.startPoint = startPoint;
        this.size = size;
        this.direction = direction;
        this.value = new Character[this.size];
        Arrays.fill(value, ' ');
        this.potentialWordlist =  new HashSet<>();
    }

    public GridPoint getStartPoint() {
        return startPoint;
    }

    public int getSize() {
        return size;
    }

    public char getDirection() {
        return direction;
    }

    public Character[] getValue() {
        return value;
    }

    public Set<String> getPotentialWordlist() {
        return potentialWordlist;
    }

    /**
     * This method helps to compare two word agent object
     * @param wordAgent: second word agent object which will be compared
     * */
    public boolean isEqual(WordAgent wordAgent) {
        if (this == wordAgent)
            return true;
        if (wordAgent == null)
            return false;
        if (getClass() != wordAgent.getClass())
            return false;
        GridPoint that = wordAgent.startPoint;
        if (this.startPoint.getX() != that.getX())
            return false;
        return this.startPoint.getY() == that.getY();
    }
}
