package cappuccino.scanner;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.HashMap;

import cappuccino.Cappuccino;

import static cappuccino.scanner.TokenType.*;

public class CappuccinoScanner {
	public static final Token EOIF = new Token("\0", TokenType.EndOfInputFileToken, 1);
	public static final Token SOIF = new Token("\1", TokenType.SOIF, -1);

	private static final HashMap<Character, TokenType> symbols = new HashMap<>() {{
		put('(', ParenthesisLeftSymbolDelimiterSeparatorOperatorToken);
		put(')', ParenthesisRightSymbolDelimiterSeparatorOperatorToken);
		put('[', SquareLeftSymbolDelimiterSeparatorOperatorToken);
		put(']', SquareRightSymbolDelimiterSeparatorOperatorToken);
		put('{', CurlyLeftSymbolDelimiterSeparatorOperatorToken);
		put('}', CurlyRightSymbolDelimiterSeparatorOperatorToken);
		put(':', ColonSymbolDelimiterOperatorToken);
		put(';', SemicolonSymbolDelimiterOperatorToken);
		put(',', CommaSymbolDelimiterOperatorToken);
		put('.', DotSymbolDelimiterOperatorToken);
		put('=', EqualSymbolOperatorToken);
		put('!', ExclamationSymbolOperatorToken);
		put('+', PlusSymbolArithmeticalOperatorToken);
		put('-', MinusSymbolArithmeticalOperatorToken);
		put('*', AsteriskSymbolArithmeticalOperatorToken);
		put('/', SlashSymbolArithmeticalOperatorToken);
		put('%', PercentSymbolArithmeticalOperatorToken);
		put('^', CaretSymbolArithmeticalOperatorToken);
		put('&', AmpersandSymbolArithmeticalOperatorToken);
		put('|', PipeSymbolArithmeticalOperatorToken);
		put('<', LessThanSymbolOperatorToken);
		put('>', GreaterThanSymbolOperatorToken);
	}};

	private final char[] target;

	private int position;
	private int line;
	
	private Token currentToken = SOIF;

	public CappuccinoScanner(char[] target) {
		this.target = target;
		this.position = 0;
		this.line = 1;
		this.currentToken = SOIF;
	}

	public CappuccinoScanner(String target) {
		this.target = target.toCharArray();
		this.position = 0;
		this.line = 1;
		this.currentToken = SOIF;
	}

	public CappuccinoScanner(File file) {
		this.target = this.readFile(file);
		this.position = 0;
		this.line = 1;
		this.currentToken = SOIF;
	}

	private char[] readFile(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			FileChannel channel = fis.getChannel();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

			Charset charset = StandardCharsets.UTF_8;
			CharBuffer charBuffer = charset.decode(buffer);

			char[] result = new char[charBuffer.remaining()];
			charBuffer.get(result);
			return result;
		} catch (IOException e) {
			return new char[0];
		}
	}

	private Token getIdentifier$KeywordToken() {
		int startPost = this.position;

		while ((this.position < this.target.length) && ((this.target[this.position] >= 'a' && this.target[this.position] <= 'z') || (this.target[this.position] >= 'A' && this.target[this.position] <= 'Z') || (this.target[this.position] >= '0' && this.target[this.position] <= '9') || (this.target[this.position] == '_') || (this.target[this.position] == '$'))) {
			this.position++;
		}

		String identifier = new String(this.target, startPost, this.position - startPost);

		return new Token(
				identifier, switch (identifier) {
					case "false" -> FalseLiteralToken;
					case "indeterminate" -> IndeterminateLiteralToken;
					case "let" -> LetKeywordToken;
					case "neutral" -> NeutralLiteralKeywordToken;
					case "true" -> TrueLiteralToken;
					default -> IdentifierToken;
				}, this.line
		);
	}

	private Token getNumericToken() {
		int startPost = this.position;
		boolean isDecimalPoint = false;

		do {
			this.position++;
		} while ((this.position < this.target.length) && ((this.target[this.position] >= '0' && this.target[this.position] <= '9') || this.target[this.position] == '_'));

		if (this.position < this.target.length) {
			if (this.target[this.position] == '.') {
				do {
					this.position++;
				} while ((this.position < this.target.length) && ((this.target[this.position] >= '0' && this.target[this.position] <= '9') || this.target[this.position] == '_'));
				isDecimalPoint = true;
			}

			if (this.target[this.position] == 'e' || this.target[this.position] == 'E') {
				this.position++;

				if (this.target[this.position] == '+' || this.target[this.position] == '-') {
					this.position++;
				}

				while ((this.position < this.target.length) && ((this.target[this.position] >= '0' && this.target[this.position] <= '9') || this.target[this.position] == '_')) {
					this.position++;
				}
			}
		}

		if (this.position < this.target.length) {
			switch (this.target[this.position]) {
				case 'b', 'B' -> { if (!isDecimalPoint) return new Token(new String(this.target, startPost, ++this.position - startPost), ByteLiteralToken, this.line); }
				case 's', 'S' -> { if (!isDecimalPoint) return new Token(new String(this.target, startPost, ++this.position - startPost), ShortLiteralToken, this.line); }
				case 'i', 'I' -> { if (!isDecimalPoint) return new Token(new String(this.target, startPost, ++this.position - startPost), IntegerLiteralToken, this.line); }
				case 'l', 'L' -> { if (!isDecimalPoint) return new Token(new String(this.target, startPost, ++this.position - startPost), LongLiteralToken, this.line); }
				case 'f', 'F' -> { return new Token(new String(this.target, startPost, ++this.position - startPost), FloatLiteralToken, this.line); }
				case 'd', 'D' -> { return new Token(new String(this.target, startPost, ++this.position - startPost), DoubleLiteralToken, this.line); }
				case 'n', 'N' -> { return new Token(new String(this.target, startPost, ++this.position - startPost), NumberLiteralToken, this.line); }
			}
		}
		return new Token(new String(this.target, startPost, this.position - startPost), DigitLiteralToken, this.line);
	}

	private Token getNotationScientificToken() {
		int startPost = this.position;

		this.position++;

		if (this.position < this.target.length) {
			TokenType type = switch (this.target[this.position]) {
				case 'n', 'N' -> NumberLiteralToken;
				case 'b', 'B' -> ByteLiteralToken;
				case 's', 'S' -> ShortLiteralToken;
				case 'i', 'I' -> IntegerLiteralToken;
				case 'l', 'L' -> LongLiteralToken;
				case 'f', 'F' -> FloatLiteralToken;
				case 'd', 'D' -> DoubleLiteralToken;
				default -> DigitLiteralToken;
			};

			if (type != DigitLiteralToken) this.position++;

			if (this.target[this.position] == 'x' || this.target[this.position] == 'X') {
				do {
					this.position++;
				} while (this.position < this.target.length && ((this.target[this.position] >= '0' && this.target[this.position] <= '9') || (this.target[this.position] >= 'a' && this.target[this.position] <= 'f') || (this.target[this.position] >= 'A' && this.target[this.position] <= 'F')));

				if (this.position < this.target.length && this.target[this.position] == '.') {
					do {
						this.position++;
					} while (this.position < this.target.length && ((this.target[this.position] >= '0' && this.target[this.position] <= '9') || (this.target[this.position] >= 'a' && this.target[this.position] <= 'f') || (this.target[this.position] >= 'A' && this.target[this.position] <= 'F')));
				}

				if (this.position < this.target.length && (this.target[this.position] == 'p' || this.target[this.position] == 'P')) {
					do {
						this.position++;
					} while (this.position < this.target.length && ((this.target[this.position] >= '0' && this.target[this.position] <= '9') || (this.target[this.position] >= 'a' && this.target[this.position] <= 'f') || (this.target[this.position] >= 'A' && this.target[this.position] <= 'F')));
				}

				return new Token(new String(this.target, startPost, this.position - startPost), type, this.line);
			} else if (this.target[this.position] == 'o' || this.target[this.position] == 'O') {
				do {
					this.position++;
				} while (this.position < this.target.length && ((this.target[this.position] >= '0' && this.target[this.position] <= '7')));

				if (this.position < this.target.length && this.target[this.position] == '.') {
					do {
						this.position++;
					} while (this.position < this.target.length && ((this.target[this.position] >= '0' && this.target[this.position] <= '7')));
				}

				if (this.position < this.target.length && (this.target[this.position] == 'p' || this.target[this.position] == 'P')) {
					do {
						this.position++;
					} while (this.position < this.target.length && ((this.target[this.position] >= '0' && this.target[this.position] <= '7')));
				}

				return new Token(new String(this.target, startPost, this.position - startPost), type, this.line);
			} else if (type == ByteLiteralToken) {
				if (this.target[this.position] == 'b' || this.target[this.position] == 'B') {
					this.position++;
				} else {
					type = DigitLiteralToken;
				}

				while (this.position < this.target.length && ((this.target[this.position] == '0') || (this.target[this.position] == '1'))) {
					this.position++;
				}

				if (this.position < this.target.length && this.target[this.position] == '.') {
					do {
						this.position++;
					} while (this.position < this.target.length && ((this.target[this.position] == '0') || (this.target[this.position] == '1')));
				}

				if (this.position < this.target.length && (this.target[this.position] == 'p' || this.target[this.position] == 'P')) {
					do {
						this.position++;
					} while (this.position < this.target.length && ((this.target[this.position] == '0') || (this.target[this.position] == '1')));
				}

				return new Token(new String(this.target, startPost, this.position - startPost), type, this.line);
			}
		}

		return new Token("0", NeutralLiteralKeywordToken, this.line);
	}

	public void getNextToken() {
		if (this.position >= this.target.length) {
			this.currentToken = EOIF;
			return;
		}

		if (this.target[this.position] == ' ' || this.target[this.position] == '\r' || this.target[this.position] == '\t' || this.target[this.position] == '\f' || this.target[this.position] == '\b' || this.target[this.position] == '\n') {
			do {
				if (this.target[this.position] == '\n') {
					this.line++;
				}
				this.position++;
			} while (this.position < this.target.length && (this.target[this.position] == ' ' || this.target[this.position] == '\r' || this.target[this.position] == '\t' || this.target[this.position] == '\f' || this.target[this.position] == '\b' || this.target[this.position] == '\n'));
		}

		if ((this.target[this.position] >= 'a' && this.target[this.position] <= 'z') || (this.target[this.position] >= 'A' && this.target[this.position] <= 'Z')) {
			this.currentToken = this.getIdentifier$KeywordToken();
			return;
		} else if (this.target[this.position] >= '1' && this.target[this.position] <= '9') {
			this.currentToken = this.getNumericToken();
			return;
		} else if (this.target[this.position] == '0') {
			this.currentToken = this.getNotationScientificToken();
			return;
		} else if (CappuccinoScanner.symbols.containsKey(this.target[this.position])) {
			this.currentToken = new Token(String.valueOf(this.target[this.position]), CappuccinoScanner.symbols.get(this.target[this.position]), this.line);
			this.position++;
			return;
		}

		Cappuccino.printError(/* section */0, /* error */0, /* subError */0, this.line, "Could not find a suitable place for character: '" + this.target[this.position] + "';");
	}

	public Token getCurrentToken() {
		return this.currentToken;
	}
}
