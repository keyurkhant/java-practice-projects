import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            FillInPuzzle puzzle = new FillInPuzzle();

            // File reader
            FileReader fileReader = new FileReader("input.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // File writer
            PrintWriter printWriter = new PrintWriter("output.txt");

            puzzle.loadPuzzle(bufferedReader);
            System.out.println(puzzle.solve());
            puzzle.print(printWriter);

            System.out.println(puzzle.choices());
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED: " + e);
        }
    }
}
