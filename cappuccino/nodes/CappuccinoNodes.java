package cappuccino.nodes;

import static cappuccino.scanner.TokenType.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import cappuccino.scanner.Token;

public class CappuccinoNodes {
	public interface CappuccinoNode {}

	public record CappuccinoProgramNode(ArrayList<CappuccinoNode> children)
			implements CappuccinoNode {}

	public interface CappuccinoStatementNode
			extends CappuccinoNode {}

	public record CappuccinoVariableStatement(String name, CappuccinoTypeNode type, CappuccinoLiteralNode expression)
			implements CappuccinoStatementNode {}

	public record CappuccinoReferenceNode(Token value)
			implements CappuccinoLiteralExpressionNode {}

	public interface CappuccinoTypeNode
			extends CappuccinoNode {}

	public record CappuccinoUnionTypeNode(ArrayList<CappuccinoTypeNode> unions)
			implements CappuccinoTypeNode {}

	public record CappuccinoTupleTypeNode(ArrayList<CappuccinoTypeNode> tuple)
			implements CappuccinoTypeNode {}

	public record CappuccinoReferenceTypeNode(Token value)
			implements CappuccinoTypeNode {}

	public interface CappuccinoLiteralNode
			extends CappuccinoNode {}

	public interface CappuccinoLiteralExpressionNode
			extends CappuccinoLiteralNode {
		public Token value();
	}

	public record CappuccinoTupleLiteralNode(ArrayList<CappuccinoLiteralNode> tuple)
			implements CappuccinoLiteralNode {}

	public record CappuccinoNumberLiteralExpressionNode(Token value)
			implements CappuccinoLiteralExpressionNode {
		public Number number() {
			String rawValue = value.value();
			String cleanValue = rawValue.replace(
					"_",
					""
			);

			boolean isFloatingPoint = value.type() == FloatLiteralToken || value.type() == DoubleLiteralToken || value.type() == NumberLiteralToken;

			try {
				if (isFloatingPoint) {
					if (isPrefixNotationScientific(
							cleanValue,
							16
					)) {
						return parseRadixFloat(
								cleanValue.substring(2 + (cleanValue.charAt(0) == 'x' || cleanValue.charAt(0) == 'X' ? 0 : 1)),
								16
						);
					} else if (isPrefixNotationScientific(
							cleanValue,
							2
					)) {
						return parseRadixFloat(
								cleanValue.substring(2 + (cleanValue.charAt(1) == 'b' || cleanValue.charAt(1) == 'B' ? 1 : 0)),
								2
						);
					} else if (isPrefixNotationScientific(
							cleanValue,
							8
					)) {
						return parseRadixFloat(
								cleanValue.substring(2 + (cleanValue.charAt(0) == 'o' || cleanValue.charAt(0) == 'O' ? 0 : 1)),
								8
						);
					}

					if (cleanValue.toLowerCase().endsWith("f") || cleanValue.toLowerCase().endsWith("d") || cleanValue.toLowerCase().endsWith("n")) {
						cleanValue = cleanValue.substring(
								0,
								cleanValue.length() - 1
						);
					}

					return new BigDecimal(cleanValue);
				} else {
					int radix = 10;
					String numberPart = cleanValue;

					if (isPrefixNotationScientific(
							cleanValue,
							16
					)) {
						radix = 16;
						numberPart = cleanValue.substring(2 + (cleanValue.charAt(0) == 'x' || cleanValue.charAt(0) == 'X' ? 0 : 1));
					} else if (isPrefixNotationScientific(
							cleanValue,
							2
					)) {
						radix = 2;
						numberPart = cleanValue.substring(2 + (cleanValue.charAt(1) == 'b' || cleanValue.charAt(1) == 'B' ? 1 : 0));
					} else if (isPrefixNotationScientific(
							cleanValue,
							8
					)) {
						radix = 8;
						numberPart = cleanValue.substring(2 + (cleanValue.charAt(0) == 'o' || cleanValue.charAt(0) == 'O' ? 0 : 1));
					}

					if (numberPart.toLowerCase().endsWith("l") || numberPart.toLowerCase().endsWith("i") || numberPart.toLowerCase().endsWith("s") || numberPart.toLowerCase().endsWith("b") || numberPart.toLowerCase().endsWith("f") || numberPart.toLowerCase().endsWith("d") || numberPart.toLowerCase().endsWith("n")) {
						numberPart = numberPart.substring(
								0,
								numberPart.length() - 1
						);
					}

					BigInteger result = new BigInteger(
							numberPart,
							radix
					);
					result = result.add(radix == 2 || radix == 10 ? BigInteger.ZERO : BigInteger.ONE);
					return result;
				}
			} catch (NumberFormatException e) {
				throw new RuntimeException(
						"Invalid number format: " + rawValue,
						e
				);
			}
		}

		private boolean isPrefixNotationScientific(
				String value,
				int radix
		) {
			int position = 1;
			if (radix == 16) {
				return value.charAt(position) == 'x' || value.charAt(position) == 'X' || ((value.charAt(position) == 'n' || value.charAt(position) == 'N') && (value.length() > 2 && (value.charAt(position + 1) == 'x' || value.charAt(position + 1) == 'X'))) || ((value.charAt(position) == 'b' || value.charAt(position) == 'B') && (value.length() > 2 && (value.charAt(position + 1) == 'x' || value.charAt(position + 1) == 'X'))) || ((value.charAt(position) == 's' || value.charAt(position) == 'S') && (value.length() > 2 && (value.charAt(position + 1) == 'x' || value.charAt(position + 1) == 'X'))) || ((value.charAt(position) == 'i' || value.charAt(position) == 'I') && (value.length() > 2 && (value.charAt(position + 1) == 'x' || value.charAt(position + 1) == 'X'))) || ((value.charAt(position) == 'l' || value.charAt(position) == 'L') && (value.length() > 2 && (value.charAt(position + 1) == 'x' || value.charAt(position + 1) == 'X'))) || ((value.charAt(position) == 'f' || value.charAt(position) == 'F') && (value.length() > 2 && (value.charAt(position + 1) == 'x' || value.charAt(position + 1) == 'X'))) || ((value.charAt(position) == 'd' || value.charAt(position) == 'D') && (value.length() > 2 && (value.charAt(position + 1) == 'x' || value.charAt(position + 1) == 'X')));
			}
			if (radix == 2) {
				return value.charAt(position) == 'b' || value.charAt(position) == 'B' || ((value.charAt(position) == 'n' || value.charAt(position) == 'N') && (value.length() > 2 && (value.charAt(position + 1) == 'b' || value.charAt(position + 1) == 'B'))) || ((value.charAt(position) == 'b' || value.charAt(position) == 'B') && (value.length() > 2 && (value.charAt(position + 1) == 'b' || value.charAt(position + 1) == 'B'))) || ((value.charAt(position) == 's' || value.charAt(position) == 'S') && (value.length() > 2 && (value.charAt(position + 1) == 'b' || value.charAt(position + 1) == 'B'))) || ((value.charAt(position) == 'i' || value.charAt(position) == 'I') && (value.length() > 2 && (value.charAt(position + 1) == 'b' || value.charAt(position + 1) == 'B'))) || ((value.charAt(position) == 'l' || value.charAt(position) == 'L') && (value.length() > 2 && (value.charAt(position + 1) == 'b' || value.charAt(position + 1) == 'B'))) || ((value.charAt(position) == 'f' || value.charAt(position) == 'F') && (value.length() > 2 && (value.charAt(position + 1) == 'b' || value.charAt(position + 1) == 'B'))) || ((value.charAt(position) == 'd' || value.charAt(position) == 'D') && (value.length() > 2 && (value.charAt(position + 1) == 'b' || value.charAt(position + 1) == 'B')));
			}
			if (radix == 8) {
				return value.charAt(position) == 'o' || value.charAt(position) == 'O' || ((value.charAt(position) == 'n' || value.charAt(position) == 'N') && (value.length() > 2 && (value.charAt(position + 1) == 'o' || value.charAt(position + 1) == 'O'))) || ((value.charAt(position) == 'b' || value.charAt(position) == 'B') && (value.length() > 2 && (value.charAt(position + 1) == 'o' || value.charAt(position + 1) == 'O'))) || ((value.charAt(position) == 's' || value.charAt(position) == 'S') && (value.length() > 2 && (value.charAt(position + 1) == 'o' || value.charAt(position + 1) == 'O'))) || ((value.charAt(position) == 'i' || value.charAt(position) == 'I') && (value.length() > 2 && (value.charAt(position + 1) == 'o' || value.charAt(position + 1) == 'O'))) || ((value.charAt(position) == 'l' || value.charAt(position) == 'L') && (value.length() > 2 && (value.charAt(position + 1) == 'o' || value.charAt(position + 1) == 'O'))) || ((value.charAt(position) == 'f' || value.charAt(position) == 'F') && (value.length() > 2 && (value.charAt(position + 1) == 'o' || value.charAt(position + 1) == 'O'))) || ((value.charAt(position) == 'd' || value.charAt(position) == 'D') && (value.length() > 2 && (value.charAt(position + 1) == 'o' || value.charAt(position + 1) == 'O')));
			}
			return false;
		}

		private BigDecimal parseRadixFloat(
				String value,
				int radix
		) {
			String[] parts = value.toLowerCase().split("p");
			String numberPart = parts[0];
			int exponent = 0;
			if (parts.length > 1) {
				exponent = Integer.parseInt(parts[1]);
			}

			String[] pointParts = numberPart.split("\\.");
			BigInteger integerPart = new BigInteger(
					pointParts[0],
					radix
			);
			BigDecimal result = new BigDecimal(integerPart);

			if (pointParts.length > 1 && !pointParts[1].isEmpty()) {
				BigInteger fractionalPart = new BigInteger(
						pointParts[1],
						radix
				);
				BigDecimal frac = new BigDecimal(fractionalPart);
				BigDecimal divisor = new BigDecimal(BigInteger.valueOf(radix).pow(pointParts[1].length()));
				result = result.add(frac.divide(
						divisor,
						java.math.MathContext.DECIMAL128
				));
			}

			if (exponent != 0) {
				BigDecimal multiplier = new BigDecimal(BigInteger.valueOf(2).pow(Math.abs(exponent)));
				if (exponent > 0) {
					result = result.multiply(multiplier);
				} else {
					result = result.divide(
							multiplier,
							java.math.MathContext.DECIMAL128
					);
				}
			}

			result = result.add(radix == 2 || radix == 10 ? BigDecimal.ZERO : BigDecimal.ONE);
			return result;
		}
	}

	public record CappuccinoUnaryLiteralExpressionNode(Token value, CappuccinoLiteralExpressionNode right)
			implements CappuccinoLiteralExpressionNode {}

	public record CappuccinoBinaryLiteralExpressionNode(CappuccinoNode left, Token value, CappuccinoNode right)
			implements CappuccinoLiteralExpressionNode {}
}