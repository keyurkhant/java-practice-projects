

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Ingredient {
    /**
     * Ingredient class to validate each ingredient line and store into ingredient list
     **/
    private String unit;
    private Float quantity;
    private String ingredientName;

    public static float THRESHOLD = 5;

    public Ingredient(String unit, Float quantity, String ingredientName){
        this.unit = unit;
        this.quantity = quantity;
        this.ingredientName = ingredientName;
    }

    /**
     * validateIngredientLine method validate each line of ingredient lines.
     * @param ingredientLine: String line of each ingredient line
     * @param measurementSystem: available measurement system object
     * @return Ingredient Object: If validated and extracted successfully
     *         null Object: validation fails
     **/
    public static Ingredient validateIngredientLine(String ingredientLine, MeasurementSystem measurementSystem) {
        String quantity = null;
        Float handledQuantity = null;
        List<String> ingredientLineList;
        String unit = null;
        String ingredientName = null;
        Float newQuantity = null;
        try{
            if(Utils.validateNonEmptyString(ingredientLine)) {
                ingredientLineList = new ArrayList<>(Arrays.asList(ingredientLine.split("  ")));
                if(ingredientLineList.size() == 3) {
                    // Quantity
                    quantity = ingredientLineList.get(0);
                    handledQuantity = Utils.fractionToDecimal(quantity);

                    quantity = Utils.getValueByRoundingRules(handledQuantity, measurementSystem);

                    newQuantity = Utils.fractionToDecimal(quantity);

                    if(newQuantity != null) {
                        if(Math.abs(handledQuantity - newQuantity) / handledQuantity * 100 > THRESHOLD) {
                            return null;
                        }
                    }

                    if(handledQuantity == null) return null;
                    // Unit
                    unit = ingredientLineList.get(1);
                    if (!Utils.validateNonEmptyString(unit)) {
                        return null;
                    }
                    // Ingredient Name
                    ingredientName = ingredientLineList.get(2);
                    if (!Utils.validateNonEmptyString(ingredientName)) {
                        return null;
                    }
                }
                else return null;
            }
        } catch (Exception e) {
            return null;
        }
        return new Ingredient(unit, newQuantity, ingredientName);
    }

    public String getName() {
        return ingredientName;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
