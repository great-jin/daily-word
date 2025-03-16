package xyz.ibudai.dailyword.basic.consts;

import java.util.regex.Pattern;

/**
 * The type Regex const.
 */
public class RegexConst {

    /**
     * The constant EMAIL_PATTERN.
     */
    public static Pattern PATTERN_EMAIL = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
}
