import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class A1 {
    public static void main(String[] args) throws FileNotFoundException {
        /**
         * The main is the calling method for whole RecipeBook program. It will call RecipeBook object and perform
         * various operations.
        **/
        RecipeBook recipeBook = new RecipeBook();
        BufferedReader conversionUnit = new BufferedReader(new FileReader("conversion_unit.txt"));
        BufferedReader inputRecipe = new BufferedReader(new FileReader(("input_recipe.txt")));
        PrintWriter outputRecipe = new PrintWriter("output_recipe.txt");

        try {
            recipeBook.unitConversion(conversionUnit);
            recipeBook.recipe("metric", inputRecipe);
            int outputCode = recipeBook.convert("Palak Paneer","imperial", 2, outputRecipe);
            if(outputCode == 0) {
                System.out.println("Conversion successfully.\nFollow output_recipe.txt for converted recipe.");
            } else if (outputCode == 1) {
                System.out.println("Conversion exist with more than 5% tolerance for any ingredients." +
                        "\nFollow output_recipe.txt for converted recipe.");
            } else if (outputCode == 2) {
                System.out.println("Conversion failed due to some exception in conversion system.");
            }
        } catch (Exception e) {
            System.out.println("Conversion system is causing issue..." + e);
        }
    }
}