import java.util.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * FillInPuzzle class contains mainly 3 methods: load, solve and print to handle various operation for solving the cross
 * word puzzle.
 * */
public class FillInPuzzle {
    private static String PUZZLE_DIMENSION_REGEX = "([\\d]+[\\s]*){3}";     // Regular expression for first line

    private static String DIRECTION_LINE_REGEX = "([\\d]+\\s){3}[hv]{1}";     // Regular expression for each direction
    private PuzzleHandler puzzle;
    private int rowSize;
    private int colSize = 0;
    private int wordCount;
    private Set<String> visitedWord = new HashSet<>();
    private int choices;
    private Set<WordAgent> visitedWordAgent = new HashSet<>();

    public FillInPuzzle() {
    }

    /**
     * loadPuzzle method will load whole input file and required data for crossword puzzle.
     * @param stream: BufferedReader file of input data for crossword puzzle
     * @return true: Data loaded successfully
     *         false: Occurs any error
     * */
    public Boolean loadPuzzle(BufferedReader stream) {
        String inputLines;
        boolean success = false;
        Set<Integer> wordLengths = new HashSet<>();
        ArrayList<WordAgent> listOfWordAgents = new ArrayList<>();
        try {
            // Read first line of input matrix for getting row and column size to design grid
            inputLines = stream.readLine().trim();
            if (inputLines.matches(PUZZLE_DIMENSION_REGEX)) {
                String[] dimensions = inputLines.split("[\\s]+");

                rowSize = Integer.parseInt(dimensions[1]);
                colSize = Integer.parseInt(dimensions[0]);
                wordCount = Integer.parseInt(dimensions[2]);
                success = true;
            }

            // After successfully getting matrix dimension
            if (success) {
                for (int i = 0; i < wordCount; i++) {
                    inputLines = stream.readLine().trim();
                    if(!inputLines.matches(DIRECTION_LINE_REGEX)) return false;
                    String[] puzzleDetails = inputLines.split("[\\s]+");

                    int xCoord = Integer.parseInt(puzzleDetails[1]);
                    int yCoord = Integer.parseInt(puzzleDetails[0]);

                    // Getting start point of given x,y co-ordinates in GridPoint object
                    GridPoint startPoint = new GridPoint(xCoord, yCoord);

                    int size = Integer.parseInt(puzzleDetails[2]);
                    wordLengths.add(size);

                    // Getting direction for given start point word agent (horizontal or vertical)
                    char direction = puzzleDetails[3].trim().toLowerCase().charAt(0);
                    // Verification of direction in puzzle
                    if(!verifyDirectionInPuzzle(direction, startPoint, size)) return false;

                    // Handling word agent by direction and start point
                    WordAgent wordAgent = new WordAgent(startPoint, size, direction);
                    listOfWordAgents.add(wordAgent);
                }

                // Validating each words given in the input list
                for (int i = 0; i < wordCount; i++) {
                    inputLines = stream.readLine().trim().toLowerCase();
                    if (!(wordLengths.contains(inputLines.length()) && inputLines.matches("[a-zA-Z]+"))) return false;

                    for (WordAgent tempAgent : listOfWordAgents) {
                        if (tempAgent.getSize() == inputLines.length()) {
                            tempAgent.getPotentialWordlist().add(inputLines);
                        }
                    }
                }

                // Implement puzzle matrix from puzzle handler
                puzzle = new PuzzleHandler(rowSize, colSize, listOfWordAgents);
            }
        } catch (Exception e) {
            return false;
        }
        return success;
    }

    /**
     * verifyDirectionInPuzzle method is used for checking horizontal or vertical direction of the word
     * @param direction: Direction of word vertical as 'v' and horizontal as 'h'
     * @param startPoint: Start point of the word
     * @param size: Length of the word
     * @return true: verified successfully
     *         false: verification failed
     * */
    private boolean verifyDirectionInPuzzle(Character direction, GridPoint startPoint, int size) {
        if (direction == 'h') {
            return  ((startPoint.getY() + size) <= colSize);
        } else if (direction == 'v') {
            return (((startPoint.getX() + 1) - size) >= 0);
        }
        return false;
    }

    /**
     * solve method is used to solve whole crossword puzzle with given grid dimensions.
     * @return true: Whole crossword puzzle solved successfully and stored in given grid
     *         false: There is any error or issue to fit word or whole puzzle into grid
     * */
    public boolean solve() {
        try {

            List<WordAgent> listOfWordAgents = puzzle.getListOfWordAgents();
            Character[][] solvedPuzzleMatrix = solvePuzzle(puzzle.getFillInPuzzle(), listOfWordAgents);

            if(solvedPuzzleMatrix == null) return false;
            puzzle.setFillInPuzzle(solvedPuzzleMatrix);
        }
        catch(Exception e) {
            return false;
        }
        return true;
    }

    /**
     * solvePuzzle is the core algorithm to solve the fill-in puzzle. This method will be called int recursive manner
     * to get each words and compare with Grid matrix
     * @param matrix: filled puzzle
     * @param wordAgent: Specific element for given word in matrix
     * @return 2D matrix: solving whole input matrix and return solved crossword puzzle
     * */
    private Character[][] solvePuzzle(Character[][] matrix, List<WordAgent> wordAgent) {
        if (wordAgent.isEmpty()) return matrix;

        // taking a word agent from the end, remove it and add to visited list.
        WordAgent lastWordAgent = wordAgent.get(wordAgent.size() - 1);
        wordAgent.remove(lastWordAgent);
        visitedWordAgent.add(lastWordAgent);

        // Looping through whole intersection
        for (Intersection intersection : puzzle.getSetOfIntersections()) {
            // comparing the intersection's start point with last word agent
            if (intersection.getFirst().isEqual(lastWordAgent)) {
                WordAgent secondWordAgent = intersection.getSecond();
                if (!visitedWordAgent.contains(secondWordAgent)) {
                    wordAgent.remove(secondWordAgent);
                    wordAgent.add(secondWordAgent);
                }
            }
        }

        // Iterating of each word in the given possible word list
        for (String word : lastWordAgent.getPotentialWordlist()) {
            if (!visitedWord.contains(word)) {
                // Mapping potential word puzzle by filling matrix
                Character[][] potentialWordPuzzle = fillMatrix(matrix, word, lastWordAgent);

                if (potentialWordPuzzle != null) {
                    visitedWord.add(word);
                    // Recursive call if word is not visited yet
                    Character[][] returnedMatrix = solvePuzzle(potentialWordPuzzle, wordAgent);

                    // Recursive call until newly returned matrix is null
                    if (returnedMatrix != null) return returnedMatrix;

                    // Handling choices if it requires to do undo
                    choices++;
                    visitedWord.remove(word);
                }
            }
        }
        visitedWordAgent.remove(lastWordAgent);
        return null;
    }

    /**
     * fillMatrix will simply getting each word and matrix and will fill data according to word agent and direction
     * @param matrix: filled puzzle
     * @param word: Word which will be filled
     * @param wordAgent: Specific element for given word in matrix
     * */
    private Character[][] fillMatrix(Character[][] matrix, String word, WordAgent wordAgent) {
        if(matrix == null) return null;

        // Initializing word puzzle matrix
        Character[][] wordPuzzle = new Character[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            wordPuzzle[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }

        // Getting co-ordinates of word agent
        int xPoint = wordAgent.getStartPoint().getX();
        int yPoint = wordAgent.getStartPoint().getY();
        int size = wordAgent.getSize();

        // Handling vertical and horizontal scenario
        if(wordAgent.getDirection() == 'v') {
            for(int i = xPoint; i > (xPoint - size); i--) {
                if (wordPuzzle[i][yPoint] != '.' && wordPuzzle[i][yPoint] != word.charAt(xPoint - i)) {
                    return null;
                }
            }
            for (int i = xPoint; i > xPoint - size; i--) {
                wordPuzzle[i][yPoint] = word.charAt(xPoint - i);
            }
        } else {
            for (int i = yPoint; i < (yPoint + size); i++) {
                if (wordPuzzle[xPoint][i] != '.' && wordPuzzle[xPoint][i] != word.charAt(i - yPoint)) {
                    return null;
                }
            }
            for (int i = yPoint; i < yPoint + size; i++) {
                wordPuzzle[xPoint][i] = word.charAt(i - yPoint);
            }
        }
        return wordPuzzle;
    }

    /**
     * print will get whole filledIn puzzle and print into new file according to rows and columns
     * @param outStream: PrintWriter object
     * */
    public void print(PrintWriter outStream) {
        try {
            Character[][] fillInPuzzle = puzzle.getFillInPuzzle();
            for(int row = rowSize - 1; row >= 0; row--) {
                for(int col = 0; col < colSize; col++) {
                    if(fillInPuzzle[row][col] == 'X') {
                        outStream.print(" ");
                    } else {
                        outStream.print(fillInPuzzle[row][col]);
                    }
                }
                outStream.print("\n");
            }
            outStream.close();
        } catch (Exception e) {
            System.err.print("ERROR OCCURRED: " + e);
            e.printStackTrace();
        }
    }

    /**
     * choices method will return choices which will be handled while solving the puzzle.
     * @return number of choices
     * */
    public int choices() {
        return choices;
    }
}

