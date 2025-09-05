public final class ConsoleColors {
    private ConsoleColors() {}

    public static boolean ENABLED = true;

    static {
        String noColor = System.getenv("NO_COLOR");
        if (noColor != null) {
            ENABLED = false;
        }
        String prop = System.getProperty("ansi.enabled");
        if (prop != null) {
            ENABLED = Boolean.parseBoolean(prop);
        }
    }

    public static void setEnabled(boolean enabled) {
        ENABLED = enabled;
    }

    public static String clean(String text) {
        if (ENABLED) return text;
        return text == null ? null : text.replaceAll("\\u001B\\[[;\\d]*m", "");
    }

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String DIM = "\u001B[2m";

    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BRIGHT_BLACK = "\u001B[90m";
    public static final String BRIGHT_RED = "\u001B[91m";
    public static final String BRIGHT_GREEN = "\u001B[92m";
    public static final String BRIGHT_YELLOW = "\u001B[93m";
    public static final String BRIGHT_BLUE = "\u001B[94m";
    public static final String BRIGHT_MAGENTA = "\u001B[95m";
    public static final String BRIGHT_CYAN = "\u001B[96m";
    public static final String BRIGHT_WHITE = "\u001B[97m";
}