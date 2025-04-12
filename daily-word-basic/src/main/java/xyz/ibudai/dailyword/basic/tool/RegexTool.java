package xyz.ibudai.dailyword.basic.tool;

public class RegexTool {

    /**
     * Is alpha numeric.
     *
     * @param input the input
     * @return the boolean
     */
    public static boolean isAlphaNumeric(String input) {
        return input != null && input.matches("^[a-zA-Z0-9]+$");
    }
}
