package cappuccino.utils;

import cappuccino.Cappuccino;
import cappuccino.nodes.CappuccinoNodes.*;
import cappuccino.scanner.Token;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import static cappuccino.scanner.TokenType.*;

public class CappuccinoChecker {

	public static boolean checkIntegerRange(
			BigInteger value,
			BigInteger negative,
			BigInteger positive
	) {
		if (value.equals(BigInteger.ZERO)) return false;
		return value.compareTo(negative) >= 0 && value.compareTo(positive) <= 0;
	}

	public static boolean checkDecimalRange(
			BigDecimal value,
			BigDecimal negative,
			BigDecimal positive
	) {
		if (value.compareTo(BigDecimal.ZERO) == 0) return false;
		return value.compareTo(negative) >= 0 && value.compareTo(positive) <= 0;
	}

	public static void checkerType(CappuccinoTypeNode type, Number number, CappuccinoLiteralExpressionNode expression) {
		switch (type) {
			case CappuccinoReferenceTypeNode(Token value) -> {
				if (value.type() == IdentifierToken) {
					if (!value.value().equals("number") && NumberChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Number Incompatible");
					} else if (!value.value().equals("byte") && ByteChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Byte Incompatible");
					} else if (!value.value().equals("short") && ShortChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Short Incompatible");
					} else if (!value.value().equals("integer") && IntegerChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Integer Incompatible");
					} else if (!value.value().equals("long") && LongChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Long Incompatible");
					} else if (!value.value().equals("float") && FloatChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Float Incompatible");
					} else if (!value.value().equals("double") && DoubleChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Double Incompatible");
					}
				} else {
					String valueType = value.value();
					String valueLiteral = expression.value().value();
					if (!valueType.equals(valueLiteral) && NumberChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Number Literal Incompatible");
					} else if (!valueType.equals(valueLiteral) && ByteChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Byte Literal Incompatible");
					} else if (!valueType.equals(valueLiteral) && ShortChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Short Literal Incompatible");
					} else if (!valueType.equals(valueLiteral) && IntegerChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Integer Literal Incompatible");
					} else if (!valueType.equals(valueLiteral) && LongChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Long Literal Incompatible");
					} else if (!valueType.equals(valueLiteral) && FloatChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Float Literal Incompatible");
					} else if (!valueType.equals(valueLiteral) && DoubleChecker > 0) {
						Cappuccino.printError(2, 0, 0, value.line(), value.column(), "Double Literal Incompatible");
					}
				}
			}
			case CappuccinoUnionTypeNode(ArrayList<CappuccinoTypeNode> unions) -> {
				int indexError = 0;
				for (CappuccinoTypeNode subCappuccinoTypeNode : unions) {
					try {
						checkerType$2(subCappuccinoTypeNode, number, expression);
					} catch (Exception e) {
						indexError++;
					}
				}

				Token token = getFirstToken(unions.getFirst());
				if (indexError == unions.size()) {
					Cappuccino.printError(2, 0, 0, token.line(), token.column(), "Incomplete");
				}
			}
			case null, default -> {
			}
		}
	}

	public static Token getFirstToken(CappuccinoTypeNode first) {
		if (first instanceof CappuccinoReferenceTypeNode(Token value)) {
			return value;
		} else if (first instanceof CappuccinoTupleTypeNode || first instanceof CappuccinoUnionTypeNode) {
			return getFirstToken(first);
		}
		return null;
	}

	public static void checkerType$2(CappuccinoTypeNode type, Number number, CappuccinoLiteralExpressionNode expression) {
		switch (type) {
			case CappuccinoReferenceTypeNode(Token value) -> {
				if (value.type() == IdentifierToken) {
					if (!value.value().equals("number") && NumberChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!value.value().equals("byte") && ByteChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!value.value().equals("short") && ShortChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!value.value().equals("integer") && IntegerChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!value.value().equals("long") && LongChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!value.value().equals("float") && FloatChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!value.value().equals("double") && DoubleChecker > 0) {
						throw new RuntimeException("Error!");
					}
				} else {
					String valueType = value.value().substring(0, value.value().length() - 1);
					String valueLiteral = ((number instanceof BigDecimal bd) ? bd.toPlainString() : ((number instanceof BigInteger bi) ? bi.toString(10) : number.toString()));
					if (!valueType.equals(valueLiteral) && NumberChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!valueType.equals(valueLiteral) && ByteChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!valueType.equals(valueLiteral) && ShortChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!valueType.equals(valueLiteral) && IntegerChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!valueType.equals(valueLiteral) && LongChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!valueType.equals(valueLiteral) && FloatChecker > 0) {
						throw new RuntimeException("Error!");
					} else if (!valueType.equals(valueLiteral) && DoubleChecker > 0) {
						throw new RuntimeException("Error!");
					}
				}
			}
			case CappuccinoUnionTypeNode(ArrayList<CappuccinoTypeNode> unions) -> {
				for (CappuccinoTypeNode subCappuccinoTypeNode : unions) {
					checkerType$2(subCappuccinoTypeNode, number, expression);
				}
			}
			case null, default -> {
			}
		}
	}

	public static void checkerRange(Number number, CappuccinoLiteralExpressionNode expressionNode) {
		if (number instanceof BigInteger decimal) {
			if (ByteChecker > 0 && !checkIntegerRange(decimal, CappuccinoRanges.BYTE_NEGATIVE, CappuccinoRanges.BYTE_POSITIVE)) {
				Cappuccino.printError(2, 0, 1, expressionNode.value().line(), expressionNode.value().column(), "Byte range");
			} else if (ShortChecker > 0 && !checkIntegerRange(decimal, CappuccinoRanges.SHORT_NEGATIVE, CappuccinoRanges.SHORT_POSITIVE)) {
				Cappuccino.printError(2, 0, 1, expressionNode.value().line(), expressionNode.value().column(), "Short range");
			} else if (IntegerChecker > 0 && !checkIntegerRange(decimal, CappuccinoRanges.INTEGER_NEGATIVE, CappuccinoRanges.INTEGER_POSITIVE)) {
				Cappuccino.printError(2, 0, 1, expressionNode.value().line(), expressionNode.value().column(), "Integer range");
			}
		} else if (number instanceof BigDecimal decimal) {
			if (FloatChecker > 0 && !checkDecimalRange(decimal, CappuccinoRanges.FLOAT_NEGATIVE, CappuccinoRanges.FLOAT_POSITIVE)) {
				Cappuccino.printError(2, 0, 1, expressionNode.value().line(), expressionNode.value().column(), "Float range");
			} else if (DoubleChecker > 0 && !checkDecimalRange(decimal, CappuccinoRanges.DOUBLE_NEGATIVE, CappuccinoRanges.DOUBLE_POSITIVE)) {
				Cappuccino.printError(2, 0, 1, expressionNode.value().line(), expressionNode.value().column(), "Double range");
			}
		}
	}

	public static int NumberChecker;
	public static int ByteChecker;
	public static int ShortChecker;
	public static int IntegerChecker;
	public static int FloatChecker;
	public static int LongChecker;
	public static int DoubleChecker;

	public static void reset() {
		NumberChecker = 0;
		ByteChecker = 0;
		ShortChecker = 0;
		IntegerChecker = 0;
		LongChecker = 0;
		FloatChecker = 0;
		DoubleChecker = 0;
	}

	public static Number checker(CappuccinoNumberLiteralExpressionNode t) {
		switch (t.value().type()) {
			case NumberLiteralToken -> NumberChecker++;
			case ByteLiteralToken -> ByteChecker++;
			case ShortLiteralToken -> ShortChecker++;
			case IntegerLiteralToken -> IntegerChecker++;
			case LongLiteralToken -> LongChecker++;
			case FloatLiteralToken -> FloatChecker++;
			case DoubleLiteralToken -> DoubleChecker++;
		}
		return t.number();
	}
}
