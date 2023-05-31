package Utils;

import sun.font.StandardGlyphVector;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    /**
     * Checks if a given String is valid, meaning it is not null and has a length greater than zero.
     * @param value The String to be checked. May be null.
     * @return true if the String is not null and has a length greater than zero, false otherwise.
     * */
    public static boolean isValidString(String value) {
        if(value != null && !value.isEmpty()) return true;
        return false;
    }

    /**
     * Converts a String containing multiple values separated by a specified separator into a Set of Strings.
     * @param value The String to be converted. May be null or empty.
     * @param separator The separator used to separate values in the String. Must not be null.
     * @return A Set of Strings containing the individual values from the original String, or an empty Set
     * */
    public static Set<String> getSetFromString(String value, String separator) {
        String[] array = value.split(separator);
        return new HashSet<String>(Arrays.asList(array));
    }

    /**
     * Converts a Set of Strings into a single String, with the individual values separated by a specified separator.
     * @param stringSet The Set of Strings to be converted. May be null or empty.
     * @param separator The separator to use between individual values. Must not be null.
     * @return A String containing the individual values from the original Set, separated by the specified separator
     * */
    public static String getStringFromSet(Set<String> stringSet, String separator) {
        return String.join(separator, stringSet);
    }

    /**
     * Checks whether the specified String is a valid file name on the current operating system.
     * @param fileName The String to check. Must not be null.
     * @return True if the String is a valid file name, false otherwise.
     * */
    public static boolean isValidFileName(String fileName) {
        if(!isValidString(fileName)) return false;
        return fileName.endsWith(".txt");
    }

    /**
     * Generates an abbreviated author string from a full author string, using the first letter of each author's first name,
     * followed by their full last name. Multiple authors are separated by a comma and a space.
     * @param authorString The full author string to abbreviate. Must not be null or empty.
     * @return An abbreviated author string generated from the input, or an empty String if the input was null or empty.
     * */
    public static String getAbbreviatedAuthorsString(String authorString) {
        String[] authorList = authorString.trim().split(",");
        StringBuilder result = new StringBuilder();
        for(String author: authorList) {
            String[] authorNameParts = author.trim().split(" ");
            if(authorList.length > 6) {
                result.append(authorNameParts[0].trim().charAt(0) + ". " + authorNameParts[authorNameParts.length - 1] + " et al. ");
                break;
            }
            result.append(authorNameParts[0].trim().charAt(0) + ". " + authorNameParts[authorNameParts.length - 1]);
            if(authorList.length == 2 && Arrays.asList(authorList).indexOf(author) != authorList.length - 1) {
                result.append(" and ");
            } else {
                result.append(", ");
            }
        }
        return result.toString();
    }
}
