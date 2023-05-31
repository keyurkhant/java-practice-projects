import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

/**
 * This is the handler class for cross word puzzle. This will find the intersection between two word agents and also
 * handles all grid values.
 * */

public class PuzzleHandler {
    private Character[][] fillInPuzzle;
    private List<WordAgent> listOfWordAgents;
    private Set<Intersection> setOfIntersections;
    private int rowSize;
    private int colSize;

    /**
     * PuzzleHandler constructor will initialize object will empty matrix.
     * */
    public PuzzleHandler(int rowSize, int colSize, List<WordAgent> listOfWordAgents) {
        this.rowSize = rowSize;
        this.colSize = colSize;
        this.listOfWordAgents = listOfWordAgents;
        this.fillInPuzzle = new Character[this.rowSize][this.colSize];
        for (Character[] row : fillInPuzzle) {
            Arrays.fill(row, 'X');
        }
        this.setOfIntersections = findIntersection(listOfWordAgents);
    }

    public Character[][] getFillInPuzzle() {
        return fillInPuzzle;
    }

    public void setFillInPuzzle(Character[][] fillInPuzzle) {
        this.fillInPuzzle = fillInPuzzle;
    }

    public List<WordAgent> getListOfWordAgents() {
        return listOfWordAgents;
    }

    public Set<Intersection> getSetOfIntersections() {
        return setOfIntersections;
    }

    /**
     * This method will find intersection point (In sets) from given list of word agents
     * @param listOfWordAgents: List of available word agents
     * @return Set of intersections
     * */
    public Set<Intersection> findIntersection(List<WordAgent> listOfWordAgents) {

        Set<Intersection> setOfIntersections = new HashSet<>();

        // Iterating through each word agents
        for (int i = 0; i < listOfWordAgents.size() - 1; i++) {
            WordAgent first = listOfWordAgents.get(i);

            Set<GridPoint> firstGridSet = getAllGridPoints(first);
            Set<GridPoint> tempGridSet = new HashSet<>();

            for (int j = i + 1; j < listOfWordAgents.size(); j++) {
                tempGridSet.addAll(firstGridSet);
                WordAgent second = listOfWordAgents.get(j);

                Set<GridPoint> secondGridSet = getAllGridPoints(second);

                tempGridSet.retainAll(secondGridSet);

                if (!tempGridSet.isEmpty()) {

                    GridPoint intersectionPoint = tempGridSet.iterator().next();
                    setOfIntersections.add(new Intersection(first, second, intersectionPoint));
                    setOfIntersections.add(new Intersection(second, first, intersectionPoint));
                }
            }
        }
        return setOfIntersections;
    }

    /**
     * This method will get all the grid point from given
     * @param wordAgent: Word Agent
     * @return Set of Grid points
     * */
    public Set<GridPoint> getAllGridPoints(WordAgent wordAgent) {
        Set<GridPoint> gridPoints = new HashSet<>();
        int rowIdx = wordAgent.getStartPoint().getX();
        int colIdx = wordAgent.getStartPoint().getY();

        // Check for direction
        if (wordAgent.getDirection() == 'h') {
            for (int i = colIdx; i < (wordAgent.getSize() + colIdx); i++) {
                gridPoints.add(new GridPoint(rowIdx, i));
                fillInPuzzle[rowIdx][i] = '.';      // Here, giving period(.) initially
            }
        } else if (wordAgent.getDirection() == 'v') {
            for (int i = rowIdx; i >= (rowIdx - wordAgent.getSize() + 1); i--) {
                gridPoints.add(new GridPoint(i, colIdx));
                fillInPuzzle[i][colIdx] = '.';      // Here, giving period(.) initially
            }
        }
        return gridPoints;
    }
}
