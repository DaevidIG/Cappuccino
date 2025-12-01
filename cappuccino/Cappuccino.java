package cappuccino;

import java.util.HashMap;

public class Cappuccino {
    public static final String Name = "Cappuccino";
    public static final String Version = "0.0.1";
    
    public static class General {
        public static final String Author = "MDDT (GHRMMDDTT)";
        public static final String Year = "2025";
        public static final String License = "MIT";
        public static final String Description = "A Java-based programming language.";
    }

    public static class Internal {
        public static final String[] Sections = {
            "Scanner",
            "Parser",
            "Abstract Syntax Tree",
            "Bytecode Generator",
            "Virtual Machine"
        };

        public static final String[] SectionsError = {
            "Error",
            "Constructor"
        };

        public static final String SectionsType = "Syntax";
    }

    public static class Error {
        public static final HashMap<String, String[]> sections = new HashMap<>() {{
            put("Scanner", new String[] {
                "General Matcher Mismatch"
            });
            put("Parser", new String[] {
                "Symbol Mismatch"
            });
            put("Abstract Syntax Tree", new String[] {
                "General Mismatch Type",
                "General Mismatch Range Digit"
            });
        }};
    }

    public static void printError(int section, int error, int subError, int line, int[] column, String message) {
        System.err.println('(' + Cappuccino.Name + ' ' + Cappuccino.Internal.Sections[section] + ')' + ' ' + Cappuccino.Error.sections.get(Cappuccino.Internal.Sections[section])[error] + " (" + Cappuccino.Internal.SectionsType + ' ' + Cappuccino.Internal.SectionsError[subError] + " - {" + line + ":" + join(column) + "}): " + message);
        System.exit(1);
    }

    private static String join(int[] elements) {
        String delim = "-";
        String[] elems = new String[elements.length];

		for(int i = 0; i < elements.length; ++i) {
			int element = elements[i];
            elems[i] = String.valueOf(element);
        }

        return String.join(delim, elems);
    }
}
