import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * Utils class contains all static methods which are used throughout the program. This contains commonly used methods.
     **/

    Utils() {

    }

    // is give string value empty of nullified
    public static Boolean validateNonEmptyString(String value) {
        value = value.trim();
        if(value == null || value.isEmpty()) {
            return false;
        }
        return true;
    }
    // Used to validate any regex with given pattern
    public static Boolean isRegexValidate(String pattern, String value) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher  = regex.matcher(value);
        return  matcher.matches();
    }

    // Get each file line and stored into List
    public static List<String> getFileContentList(BufferedReader file) {
        List<String> fileContentList = new ArrayList<>();

        if(file == null) return null;
        String line;
        while (true) {
            try {
                if((line = file.readLine()) == null) {
                    break;
                }
                fileContentList.add(line);
            } catch (IOException e) {
                System.out.println("Unable to load the file!");
                e.printStackTrace();
            }
        }
        return fileContentList;
    }

    // Is given string value is float parsable
    public static boolean isFloatParsable(String value){
        try {
            float q = Float.parseFloat(value);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }

    /**
     * Convert String value from fraction to Decimal
     * @param value: String value (12 3/4)
     * @return float value: 12.75
     **/
    public static Float fractionToDecimal(String value) {
        float q;
        if(isFloatParsable(value) && (q = Float.parseFloat(value)) != 0){
            if(q < 0) return null;
            return q;
        }
        else if(Utils.isRegexValidate("^(\\d+/\\d+)$", value)) {
            String[] denoNume = value.split("/");
            int denominator = Integer.parseInt(denoNume[1]);
            int numerator = Integer.parseInt(denoNume[0]);

            if(denominator == 0){
                return null;
            }
            return (float) (numerator * 1.0 / denominator);
        } else if(Utils.isRegexValidate("^(\\d*)(\\s)*(\\d+/\\d+)$", value)) {
            String[] valueList = value.split(" ");
            int intNumber = Integer.parseInt(valueList[0]);

            String[] denoNume = valueList[1].split("/");
            int denominator = Integer.parseInt(denoNume[1]);
            int numerator = Integer.parseInt(denoNume[0]);

            return (float) (intNumber  + numerator * 1.0 / denominator);
        }
        return null;
    }

    /**
     * Convert value to fractional value by given specific fraction value
     * @param value: String value (12.75)
     * @param fractionValue: Integer value (8)
     * @return String value: 12 6/8
     **/
    public static String decimalToFraction(float value, int fractionValue) {
        BigDecimal decimalValue = new BigDecimal(value);
        int wholeNumber = decimalValue.intValue();
        BigDecimal fraction = decimalValue.subtract(new BigDecimal(wholeNumber));
        int numerator = fraction.multiply(new BigDecimal(fractionValue)).intValue();
        int denominator = fractionValue;
        int gcd = findGCD(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        if(numerator == 0 || denominator == 0) {
            return Integer.toString(wholeNumber);
        } else {
            return (wholeNumber != 0 ? wholeNumber + " " : "") + numerator + "/" + denominator;
        }
    }

    // Used to find the Greatest common Divisor between two value
    private static int findGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return findGCD(b, a % b);
    }

    // This will handle rounding rules for given value by fractional and integral rounding rules.
    private static String handleRoundingRules(float value, int fraction, int integer) {
        String output = "";
        if(integer != 0) {
            int module = (int) value % integer;

            if(module == 0) {
                output += Integer.toString((int) value);
            }
            else if(module >= integer / 2) {
                output += Integer.toString((int)value + (integer - module));
            }
            else if(module < integer / 2) {
                output += Integer.toString(((int) value - module) == 0? integer: (int) value - module);
            }
        }

        if(fraction == 0) {
            return output;
        }
        else {
            int value1 = (int) value;
            float remaining = value - value1;
            float decimalFraction = (float) 1 / fraction;
            float temp = decimalFraction;
            do {
                if(remaining == decimalFraction) {
                    return output.equals("") ? Utils.decimalToFraction(value1 + remaining, fraction) :
                            output + " " + Utils.decimalToFraction(value1 + remaining, fraction);
                }
                else if(Math.abs(remaining - temp) < decimalFraction) {

                    if (Math.abs(remaining - temp) < (decimalFraction / 2)) {
                        return output.equals("") ?
                                Utils.decimalToFraction(value1 + ((temp == 0)? fraction : temp), fraction) :
                                output + " " + Utils.decimalToFraction(value1 + ((temp == 0)?fraction:temp), fraction);

                    } else if (Math.abs(remaining - temp) >= (decimalFraction / 2)) {
                        return output.equals("") ?
                                Utils.decimalToFraction(value1 + temp + decimalFraction, fraction) :
                                output + " " + Utils.decimalToFraction(value1 + temp + decimalFraction, fraction);
                    }
                }
                temp += decimalFraction;
            }while((temp <= remaining));
        }
        return "";
    }

    // Get value by rounding rules (internal use)
    public static String getValueByRoundingRules(float value, MeasurementSystem measurementSystem) {
        List<IntegerFraction> rules = measurementSystem.getRoundingRules();
        if (measurementSystem.getMinWeight() != 0) {
            if (value <= measurementSystem.getMinWeight()) {
                return handleRoundingRules(value, (int) rules.get(0).getFragment(), (int) rules.get(0).getInteger());
            } else {
                return handleRoundingRules(value, (int) rules.get(1).getFragment(), (int) rules.get(1).getInteger());
            }
        } else {
            return handleRoundingRules(value, (int) rules.get(0).getFragment(), (int) rules.get(0).getInteger());
        }
    }
}
