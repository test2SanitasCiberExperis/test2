package com.mycorp.constant;

import java.util.regex.Pattern;

public class Constants {

    // HTML CONSTANTS
    public static final String ESCAPED_LINE_SEPARATOR = "\\n";
    public static final String ESCAPE_ER = "\\";
    public static final String HTML_BR = "<br/>";

    // CONTENT TYPE CONSTANTS
    public static final String JSON = "application/json; charset=UTF-8";

    // REGEX CONSTANTS
    public static final Pattern RESTRICTED_PATTERN = Pattern.compile("%2B", Pattern.LITERAL);

    // LITERAL CONSTANTS
    public static final String LITERAL_TICKET = "idTicket";

    private Constants() {

    }

}
