

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
    /**
     * Recipe class used to set accessor and mutator for Recipe and also used for extracting recipe from recipe file content.
     **/
    private String recipeTitle;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> instructions;

    public Recipe(String recipeTitle, ArrayList<Ingredient> ingredients, ArrayList<String> instructions) {
        this.recipeTitle = recipeTitle;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    /**
     * extractRecipe method used for validating recipe content according to measurement unit and return recipe object.
     * @param recipeContent: recipe content file
     * @param measurementSystem: Measurement System object
     * @return Recipe object: extraction and validation is proper
     *         null object: validation fails
     **/
    public static Recipe extractRecipe(BufferedReader recipeContent, MeasurementSystem measurementSystem) {
        if(recipeContent == null) return null;

        List<String> recipeContentList = Utils.getFileContentList(recipeContent);
        String recipeTitle;
        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
        ArrayList<String> instructionLine = new ArrayList<String>();
        int secondSpaceIdx = -1;

        if(recipeContentList.size() >= 5) {
            recipeTitle = recipeContentList.get(0);     // Recipe title
            if(recipeTitle.length() < 2) return null;

            if(!recipeContentList.get(1).isEmpty()) return null;    // First blank line

            // Looping whole recipe Content
            for(int i=2; i<recipeContentList.size(); i++) {
                if(!Utils.validateNonEmptyString(recipeContentList.get(i))) {
                    if(ingredientList.isEmpty()) return null;
                    secondSpaceIdx = i;
                    break;
                }
                ingredientList.add(Ingredient.validateIngredientLine(recipeContentList.get(i), measurementSystem));
            }

            // Second blank line
            if(secondSpaceIdx != -1) {
                for(int i=secondSpaceIdx+1; i<recipeContentList.size(); i++) {
                    instructionLine.add(recipeContentList.get(i));
                }
            }
            // Instruction line
            if(instructionLine.size() == 0) return null;
            return new Recipe(recipeTitle, ingredientList, instructionLine);
        }
        return null;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }
}
