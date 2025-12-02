package cappuccino.interpreter;

import cappuccino.Cappuccino;
import cappuccino.nodes.CappuccinoNodes;
import cappuccino.scanner.Token;
import cappuccino.nodes.CappuccinoNodes.*;
import cappuccino.utils.VariableTable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import static cappuccino.scanner.TokenType.MinusSymbolArithmeticalOperatorToken;
import static cappuccino.scanner.TokenType.UndefinedLiteralToken;
import static cappuccino.utils.CappuccinoChecker.*;

public class CappuccinoInterpreter {
	private final CappuccinoProgramNode node;

	public CappuccinoInterpreter(CappuccinoProgramNode node) {
		this.node = node;
	}

	public void interpreter() {
		for (CappuccinoNode subNode : this.node.children()) {
			this.evaluate(subNode);
		}
	}

	private void evaluate(CappuccinoNode node) {
		switch (node) {
			case CappuccinoLiteralExpressionNode expressionNode -> {
				Number number = this.evaluateNumeric(expressionNode);
				checkerRange(number, expressionNode);
				reset();
			}

			case CappuccinoVariableStatement(String name, CappuccinoTypeNode type, CappuccinoLiteralNode expression) -> {
				checkerAll(type, expression);
				VariableTable.statement.put(name, (CappuccinoVariableStatement) node);
			}

			default -> { }
		}
	}

	private void checkerAll(
			CappuccinoTypeNode type,
			CappuccinoLiteralNode expression
	) {
		if (((type instanceof CappuccinoTupleTypeNode(java.util.ArrayList<CappuccinoTypeNode> types)) && (expression instanceof CappuccinoTupleLiteralNode(java.util.ArrayList<CappuccinoLiteralNode> assignation)))) {
			if (types.size() != assignation.size()) {
				System.out.println("No es igual");
				System.exit(1);
			}

			int length = (types.size() + assignation.size())/ 2;
			for (int index = 0; index < length; index++) {
				checkerAll(types.get(index), assignation.get(index));
			}
			return;
		} else if ((type instanceof CappuccinoUnionTypeNode(ArrayList<CappuccinoTypeNode> unions))) {
			int indexError = 0;
			for (CappuccinoTypeNode union : unions) {
				try {
					checkerAll(
							union,
							expression
					);
				} catch (Exception _) {
					indexError++;
				}
			}

			Token token = getFirstToken(unions.getFirst());
			if (indexError == unions.size()) {
				Cappuccino.printError(2, 0, 0, token.line(), "Incomplete");
			}
			return;
		} else if (type instanceof CappuccinoTupleTypeNode || expression instanceof CappuccinoTupleLiteralNode) {
			throw new RuntimeException("No se puede combinar un tuple de tipos con asignacion solo.");
		}

		if (((CappuccinoLiteralExpressionNode) expression).value().type() != UndefinedLiteralToken) {
			Number number = evaluateNumeric((CappuccinoLiteralExpressionNode) expression);
			checkerRange(number, (CappuccinoLiteralExpressionNode) expression);
			checkerType(type, number, (CappuccinoLiteralExpressionNode) expression);
			reset();
		}
	}

	public static Number evaluateNumeric(CappuccinoLiteralExpressionNode expr) {
		return switch (expr) {
			case CappuccinoReferenceNode r -> evaluateNumeric((CappuccinoLiteralExpressionNode) VariableTable.statement.get(r.value().value()).expression());

			case CappuccinoNumberLiteralExpressionNode c -> checker(c);

			case CappuccinoUnaryLiteralExpressionNode(Token operator, CappuccinoLiteralExpressionNode right) -> {
				if (operator.type() == MinusSymbolArithmeticalOperatorToken) {
					Number l = evaluateNumeric(right);

					if (l instanceof BigInteger bi) {
						bi = bi.negate();
						yield bi;
					} else if (l instanceof BigDecimal bd) {
						bd = bd.negate();
						yield bd;
					}
				}

				yield evaluateNumeric(right);
			}

			case CappuccinoBinaryLiteralExpressionNode(CappuccinoLiteralExpressionNode leftExpr, Token operator, CappuccinoLiteralExpressionNode rightExpr) -> {
				Number l = evaluateNumeric(leftExpr);
				Number r = evaluateNumeric(rightExpr);

				yield operate(operator, l, r);
			}

			default -> -1;
		};
	}

	public static Number operate(Token op, Number l, Number r) {

		int isDecimal = (l instanceof BigDecimal) && (r instanceof BigDecimal) ? 1 : ((l instanceof BigInteger) && (r instanceof BigInteger) ? 2 : 0);

		if (NumberChecker > 0 && (ByteChecker != 0 || ShortChecker != 0 || IntegerChecker != 0 || FloatChecker != 0 || DoubleChecker != 0)) {
			Cappuccino.printError(2, 0, 0, op.line(), "Number mix");
		} else if (ByteChecker > 0 && (ShortChecker != 0 || IntegerChecker != 0 || FloatChecker != 0 || DoubleChecker != 0)) {
			Cappuccino.printError(2, 0, 0, op.line(), "Byte mix");
		} else if (ShortChecker > 0 && (ByteChecker != 0 || IntegerChecker != 0 || FloatChecker != 0 || DoubleChecker != 0)) {
			Cappuccino.printError(2, 0, 0, op.line(), "Short mix");
		} else if (IntegerChecker > 0 && (ByteChecker != 0 || ShortChecker != 0 || FloatChecker != 0 || DoubleChecker != 0)) {
			Cappuccino.printError(2, 0, 0, op.line(), "Integer mix");
		} else if (FloatChecker > 0 && (ByteChecker != 0 || ShortChecker != 0 || IntegerChecker != 0 || DoubleChecker != 0)) {
			Cappuccino.printError(2, 0, 0, op.line(), "Float mix");
		} else if (DoubleChecker > 0 && (ByteChecker != 0 || ShortChecker != 0 || IntegerChecker != 0 || FloatChecker != 0)) {
			Cappuccino.printError(2, 0, 0, op.line(), "Double mix");
		}

		switch (isDecimal) {
			case 1 -> {

				BigDecimal L = (BigDecimal) l;
				BigDecimal R = (BigDecimal) r;

				return switch (op.type()) {
					case PlusSymbolArithmeticalOperatorToken -> L.add(R);
					case MinusSymbolArithmeticalOperatorToken -> L.subtract(R);
					case AsteriskSymbolArithmeticalOperatorToken -> L.multiply(R);
					case SlashSymbolArithmeticalOperatorToken -> L.divide(R); // precisión
					case PercentSymbolArithmeticalOperatorToken -> L.remainder(R);
					default -> throw new IllegalArgumentException("Operación no válida: " + op);
				};
			}
			case 2 -> {

				BigInteger L = (BigInteger) l;
				BigInteger R = (BigInteger) r;

				return switch (op.type()) {
					case PlusSymbolArithmeticalOperatorToken -> L.add(R);
					case MinusSymbolArithmeticalOperatorToken -> L.subtract(R);
					case AsteriskSymbolArithmeticalOperatorToken -> L.multiply(R);
					case SlashSymbolArithmeticalOperatorToken -> L.divide(R); // entero
					case PercentSymbolArithmeticalOperatorToken -> L.remainder(R);
					default -> throw new IllegalArgumentException("Operación no válida: " + op);
				};
			}
			default -> {

				Cappuccino.printError(2, 0, 0, op.line(), "Cannot evaluate an decimal operation with an integer operation");
				return 0;
			}
		}
	}
}
