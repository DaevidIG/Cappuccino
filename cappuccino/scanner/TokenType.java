package cappuccino.scanner;

public enum TokenType {
    // Generic
    IdentifierToken("identifier"),

    // Keyword:
    LetKeywordToken("let"),

    // Symbols:
    AtSymbolToken("@"),
    LowLineSymbolToken("_"),
    TildeSymbolToken("~"),
    QuotationSymbolToken("\""),
    ApostropheSymbolToken("'"),

    EqualSymbolOperatorToken("="),
    AmpersandSymbolArithmeticalOperatorToken("&"),
    PipeSymbolArithmeticalOperatorToken("|"),
    ExclamationSymbolOperatorToken("!"),
    QuestionSymbolOperatorToken("?"),
    LessThanSymbolOperatorToken("<"),
    GreaterThanSymbolOperatorToken(">"),

    PlusSymbolArithmeticalOperatorToken("+"),
    MinusSymbolArithmeticalOperatorToken("-"),
    AsteriskSymbolArithmeticalOperatorToken("*"),
    SlashSymbolArithmeticalOperatorToken("/"),
    PercentSymbolArithmeticalOperatorToken("%"),
    CaretSymbolArithmeticalOperatorToken("^"),

    SemicolonSymbolDelimiterOperatorToken(";"),
    ColonSymbolDelimiterOperatorToken(":"),
    CommaSymbolDelimiterOperatorToken(","),
    DotSymbolDelimiterOperatorToken("."),

    ParenthesisLeftSymbolDelimiterSeparatorOperatorToken("("),
    ParenthesisRightSymbolDelimiterSeparatorOperatorToken(")"),
    SquareLeftSymbolDelimiterSeparatorOperatorToken("["),
    SquareRightSymbolDelimiterSeparatorOperatorToken("]"),
    CurlyLeftSymbolDelimiterSeparatorOperatorToken("{"),
    CurlyRightSymbolDelimiterSeparatorOperatorToken("}"),

    // Literal Tokens:
    DigitLiteralToken("digit"),
    NumberLiteralToken("number"),
    ByteLiteralToken("byte"),
    ShortLiteralToken("short"),
    IntegerLiteralToken("integer"),
    LongLiteralToken("long"),
    FloatLiteralToken("float"),
    DoubleLiteralToken("double"),

    StringLiteralToken("string"),

    TrueLiteralToken("true"),
    FalseLiteralToken("false"),
    IndeterminateLiteralToken("indeterminate"),

    NullLiteralToken("null"),
    UndefinedLiteralToken("undefined"),

    // Lietral Keyword:
    NeutralLiteralKeywordToken("0"),

    // Special Tokens:
    EndOfInputFileToken("\0"),
    SOIF("\1");

    public final String symbol;

    TokenType(String symbol) {
        this.symbol = symbol;
    }
}
