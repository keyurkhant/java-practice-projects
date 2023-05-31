

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeBook {
    /**
     * RecipeBook is the prime class for recipe conversion and have various methods for conversion, recipe extracting and
     * unit conversion
    **/
    public static float THRESHOLD = 5;  // rounding tolerance threshold

    // Conversion map to store key as concatenation of both units and Conversion object as value
    Map<String, Conversion> conversionMap = new HashMap<String, Conversion>();

    // Recipe map to store key as name of recipe and Recipe object as value
    Map<String, Recipe> recipeMap = new HashMap<String, Recipe>();

    List<MeasurementSystem> measurementSystems = new ArrayList<>();

    RecipeBook() {
    }

    /**
     * unitConversion method validate whole conversion unit file (measurement units and conversion metric) and stores
     * both measurement systems into ArrayList if validated successfully.
     * @param unitMatches: conversion file content
     * @return true: validation successfully and stores in ArrayList;
     *         false: validation fails
     **/
    public Boolean unitConversion(BufferedReader unitMatches) {
        try {
            String line = null;
            while (measurementSystems.size() < 2 && (line = unitMatches.readLine()) != null) {
                MeasurementSystem mSystem = MeasurementSystem.extractMeasurements(line);

                if (mSystem != null) {

                    if (mSystem.getMinWeight() < 0) {
                        return false;
                    }
                    measurementSystems.add(mSystem);
                }
                else break;
            }

            if (line == null) {
                return false;
            }

            if (measurementSystems.size() < 2) {
                return false;
            }

            if (measurementSystems.get(0).getMeasurementName().equals(measurementSystems.get(1).getMeasurementName())) {
                return false;
            }

            while((line = unitMatches.readLine()) != null){
                if(Utils.validateNonEmptyString(line)){
                    Conversion conversion = Conversion.createConversion(line);
                    if(conversion == null) return false;
                    String conversionKey = conversion.getFirstUnit() + conversion.getSecondUnit();

                    if(conversionMap.containsKey(conversionKey)){
                        Conversion prevConversion = conversionMap.get(conversionKey);
                        float est = conversion.getFirstValue() * prevConversion.getSecondValue() / conversion.getSecondValue();

                        // Handle threshold for handling rounding tolerance
                        if((prevConversion.getFirstValue() - est) / prevConversion.getFirstValue() * 100 <= THRESHOLD){
                            conversionMap.replace(conversionKey, conversion);
                        }
                    } else {
                        conversionMap.put(conversionKey, conversion);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Data is not integer parsable");
        }
        return true;
    }

    /**
     * recipe method will validate the recipe file content and generate RecipeMap using HashMap.
     * @param originalUnits: original unit of measurement
     * @param recipeContent: recipe content of file
     * @return true: validation successfully and stores in HashMap;
     *         false: validation fails
     **/
    public Boolean recipe(String originalUnits, BufferedReader recipeContent){
        if(!Utils.validateNonEmptyString(originalUnits)) return false;
        Recipe recipeContentObj = Recipe.extractRecipe(recipeContent, measurementSystems.get(0));
        recipeMap.put(recipeContentObj.getRecipeTitle(), recipeContentObj);
        if(recipeContentObj == null) return false;
        return true;
    }

    /**
     * convert method will take recipeName and convert its unit to corresponding measurement system according to given
     * rounding rules and scaling factor.
     * @param recipeName: name of the recipe
     * @param targetUnits: Unit of measurement in which you want to target
     * @param scaleFactor: Number in which you want to scale the current recipe
     * @param convertedRecipe: PrintWriter object to add converted recipe into output file
     * @return 0: If recipe is converted successfully and stored into output_recipe.txt
     *         1: If recipe is converted but some ingredients has rounding variance more than 5%.
     *         2: If there is any issue with conversion which cause error. No conversion happens. output_recipe.txt
     *            will be empty.
     **/
    public int convert( String recipeName, String targetUnits, double scaleFactor, PrintWriter convertedRecipe){
        Recipe recipe = recipeMap.get(recipeName);
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        int outputFlag = 0;

        try {
            // Looping through all ingredients (acquired from recipe object)
            for(Ingredient ingredient: recipe.getIngredients()){
                float finalValue = 0;
                String unit = null;
                // Looping through all conversion entry (acquired from conversion map)
                for (Conversion entry : conversionMap.values()) {
                    // If first unit will be matched
                    if(entry.getFirstUnit().equals(ingredient.getUnit())){
                        // Handling scaling factor with mathematical operations
                        float value = (float) ((ingredient.getQuantity() * entry.getSecondValue() * scaleFactor) /
                                entry.getFirstValue());
                        if(unit == null){
                            unit = entry.getSecondUnit();
                            finalValue = value;
                        }
                        else{
                            if((int) value == 0) {
                                // Only fraction value is exist
                                if(finalValue < value) {
                                    finalValue = value;
                                    unit = entry.getSecondUnit();
                                }
                            } else if(finalValue > value) {
                                finalValue = value;
                                unit = entry.getSecondUnit();
                            }
                        }
                    }
                    // If second unit will be matched
                    else if(entry.getSecondUnit().equals(ingredient.getUnit())){
                        // Handling scaling factor with mathematical operations
                        float value = (float) ((ingredient.getQuantity() * entry.getFirstValue() * scaleFactor) /
                                entry.getSecondValue());
                        if(unit == null){
                            unit = entry.getFirstUnit();
                            finalValue = value;
                        }
                        else{
                            if((int) value == 0) {
                                if(finalValue < value) {
                                    finalValue = value;
                                    unit = entry.getFirstUnit();
                                }
                            } else if(finalValue > value) {
                                finalValue = value;
                                unit = entry.getFirstUnit();
                            }
                        }
                    }
                }
                // Add ingredient object to ArrayList after each conversion looping
                Ingredient ing = new Ingredient(unit, finalValue, ingredient.getName());
                ingredients.add(ing);
            }

            // Adding each successful output to output_txt file using PrintWriter
            convertedRecipe.println(recipeName);    // Adding recipe name
            convertedRecipe.println();  // Adding blank line

            // For each ingredient, looping through ingredients list
            for(Ingredient ingredient: ingredients){
                String convertedValue = Utils.getValueByRoundingRules(ingredient.getQuantity(), measurementSystems.get(1));

                Float roundedValue = Utils.fractionToDecimal(convertedValue);

                if(roundedValue != null) {
                    if(Math.abs(ingredient.getQuantity() - roundedValue) / ingredient.getQuantity() * 100 > THRESHOLD) {
                        outputFlag = 1;
                    }
                    convertedRecipe.println(convertedValue + " " + ingredient.getUnit() + " " + ingredient.getName());
                }
            }
            convertedRecipe.println();
            for(String instructionLine: recipe.getInstructions()) {
                convertedRecipe.println(instructionLine);
            }
            convertedRecipe.close();
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED: " + e);
            outputFlag = 2;
        }
        return outputFlag;
    }
}
