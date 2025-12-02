package cappuccino.scanner;

import java.util.Arrays;

public record Token(String value, TokenType type, int line) {

    @Override
    public String toString() {
        return "Token{" + "value='" + value + '\'' + ", type=" + type + ", line=" + line + '}';
    }
}
