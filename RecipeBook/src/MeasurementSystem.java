import java.util.ArrayList;
import java.util.List;

public class MeasurementSystem {
    /**
     * MeasurementSystem is the prime class for extracting the measurement systems from conversion file and validate it.
     * Accessor and Mutators of MeasurementSystem
     **/
    private String measurementName;
    private int minWeight;
    private List<IntegerFraction> roundingRules;    // List of rounding rules with type of IntegerFraction

    public MeasurementSystem() {
        this.measurementName = null;
        this.minWeight = 0;
        this.roundingRules = new ArrayList<IntegerFraction>();
    }

    public MeasurementSystem(String name, int minWeight, List<IntegerFraction> roundingRules){
        this.measurementName = name;
        this.minWeight = minWeight;
        this.roundingRules = roundingRules;
    }

    /**
     * extractMeasurements method validate each line of conversion file and extract two possible measurement systems.
     * @param line: String lines of each conversion file.
     * @return Measurement Object: If validated and extracted successfully
     *         null Object: validation fails
     **/
    public static MeasurementSystem extractMeasurements(String line){
        if(line == null) return null;
        String[] arr = line.split(" ");     // Each line with space separated

        List<IntegerFraction> roundingRules = new ArrayList<IntegerFraction>();
        if(arr.length >= 3) {
            String measurementName = arr[0];
            int minWeight = Integer.parseInt(arr[1]);

            for (int i = 2; i < arr.length - 1; i += 2) {
                int fraction = Integer.parseInt(arr[i]);
                int integer = Integer.parseInt(arr[i + 1]);

                if(integer < 0 || fraction < 0) return null;

                roundingRules.add(new IntegerFraction(integer, fraction));
            }

            if(roundingRules.size() == 0) return null;

            if(minWeight != 0 && roundingRules.size() == 1){
                return null;
            }

            if(minWeight > 0 && roundingRules.size() != 2){
                return null;
            }
            return new MeasurementSystem(measurementName, minWeight, roundingRules);
        }
        return null;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public int getMinWeight() {
        return minWeight;
    }

    public List<IntegerFraction> getRoundingRules() {
        return roundingRules;
    }
}
