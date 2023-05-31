public class Conversion {
    /**
     * Conversion class to create conversion metric from conversion file and getter setter of each unit and its quantity.
     **/
    private String firstUnit;

    private float firstValue;

    private String secondUnit;

    private float secondValue;

    public String getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(String firstUnit) {
        this.firstUnit = firstUnit;
    }

    public float getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(float firstValue) {
        this.firstValue = firstValue;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public void setSecondUnit(String secondUnit) {
        this.secondUnit = secondUnit;
    }

    public float getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(float secondValue) {
        this.secondValue = secondValue;
    }


    /**
     * Read each line of conversion file and validate its value (quantity and value)
     * @param line: conversion metric line
     * @return Conversion object: if validated successfully and converted into fraction or decimal
     *         null object: if validation fails.
     **/
    public static Conversion createConversion(String line){
        if(Utils.validateNonEmptyString(line)){

            String[] conversionList = line.split(" ");

            if(conversionList.length != 4){
                return null;
            }

            Conversion conversion = new Conversion();
            conversion.setFirstUnit(conversionList[1]);
            conversion.setSecondUnit(conversionList[3]);

            Float firstValue = Utils.fractionToDecimal(conversionList[0]);
            Float secondValue = Utils.fractionToDecimal(conversionList[2]);
            if(firstValue == null || secondValue == null){
                return null;
            }
            conversion.setFirstValue(firstValue);
            conversion.setSecondValue(secondValue);

            return conversion;
        }
        return null;
    }
}
