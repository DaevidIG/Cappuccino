package cappuccino.parser;

import static cappuccino.scanner.TokenType.*;

import cappuccino.Cappuccino;
import cappuccino.nodes.CappuccinoNodes.*;
import cappuccino.scanner.CappuccinoScanner;
import cappuccino.scanner.Token;
import cappuccino.scanner.TokenType;

import java.util.ArrayList;

public class CappuccinoParser {
    private static final int CURRENT = 1 << 0;

	private static final int NEXT = 1 << 1;
	private static final int CONSUME = 1 << 2;
	private static final int SEEK = 1 << 3;

    private final CappuccinoScanner scanner;

    public CappuccinoParser(CappuccinoScanner scanner) {
        this.scanner = scanner;
    }

    public CappuccinoProgramNode parse() {
        return this.parseProgramNode();
    }

    private CappuccinoProgramNode parseProgramNode() {
	    ArrayList<CappuccinoNode> nodes = new ArrayList<>();

	    Token start;
		while (!(Boolean)this.validator(CURRENT | SEEK, EndOfInputFileToken)) {
			start = (Token) this.validator(CURRENT);
			switch (start.type()) {
				case LetKeywordToken -> nodes.add(this.parseVariableStatementNode());
				default -> nodes.add(this.parseBinaryExpressionNode());
			}
		}

        return new CappuccinoProgramNode(nodes);
    }

	private CappuccinoVariableStatement parseVariableStatementNode() {
		this.validator(CURRENT | NEXT);
		
		String name  = ((Token) this.validator(CURRENT | CONSUME | NEXT, IdentifierToken)).value();

		this.validator(CURRENT | CONSUME | NEXT, ColonSymbolDelimiterOperatorToken);

		CappuccinoTypeNode type = parseVariableStatementTypeNode();

		CappuccinoLiteralNode expressionNode;

		if ((Boolean)this.validator(CURRENT | SEEK | NEXT, EqualSymbolOperatorToken)) {
			expressionNode = this.parseVariableStatementLiteralNode();
		} else {
			expressionNode = new CappuccinoLiteralExpressionNode() {
				@Override
				public Token value() {
					return new Token("undefined", UndefinedLiteralToken, 1, new int[] {
							0,
							9,
					});
				}
			};
		}

		this.validator(CURRENT | CONSUME | NEXT, SemicolonSymbolDelimiterOperatorToken);
		
		return new CappuccinoVariableStatement(name, type, expressionNode);
	}

	private CappuccinoTypeNode parseVariableStatementTypeNode() {
		Token token = (Token) this.validator(CURRENT | CONSUME | NEXT,
		                                     IdentifierToken,
		                                     NumberLiteralToken, ByteLiteralToken, ShortLiteralToken, IntegerLiteralToken, LongLiteralToken, DoubleLiteralToken, FloatLiteralToken,
		                                     SquareLeftSymbolDelimiterSeparatorOperatorToken
		);

		ArrayList<CappuccinoTypeNode> subNodes = new ArrayList<>();
		CappuccinoTypeNode node;
		if (token.type() == SquareLeftSymbolDelimiterSeparatorOperatorToken) {
			do {
				node = this.parseVariableStatementTypeNode();
				subNodes.add(node);
			} while ((Boolean) this.validator(CURRENT | NEXT | SEEK, CommaSymbolDelimiterOperatorToken));

			this.validator(CURRENT | CONSUME | NEXT, SquareRightSymbolDelimiterSeparatorOperatorToken);
			node = new CappuccinoTupleTypeNode(subNodes);
		} else {
			node = new CappuccinoReferenceTypeNode(token);
			subNodes.add(node);

			while ((Boolean)this.validator(CURRENT | NEXT | SEEK, PipeSymbolArithmeticalOperatorToken)) {
				node = this.parseVariableStatementTypeNode();
				subNodes.add(node);
			}

			if (subNodes.size() != 1) {
				node = new CappuccinoUnionTypeNode(subNodes);
			}
		}
		return node;
	}

	private CappuccinoLiteralNode parseVariableStatementLiteralNode() {
		Token token = (Token) this.validator(CURRENT);

		if (token.type() == SquareLeftSymbolDelimiterSeparatorOperatorToken) {
			this.validator(CURRENT | NEXT);
			ArrayList<CappuccinoLiteralNode> subNodes = new ArrayList<>();

			do {
				CappuccinoLiteralNode node = this.parseVariableStatementLiteralNode();
				subNodes.add(node);
			} while ((Boolean)this.validator(CURRENT | NEXT | SEEK, CommaSymbolDelimiterOperatorToken));

			this.validator(CURRENT | CONSUME | NEXT, SquareRightSymbolDelimiterSeparatorOperatorToken);

			return new CappuccinoTupleLiteralNode(subNodes);
		}

		return this.parseBinaryExpressionNode();
	}

	private CappuccinoLiteralExpressionNode parseBinaryExpressionNode() {
        CappuccinoLiteralExpressionNode left = this.parseBinaryExpression2Node();

		Token operator;

		if ((Boolean)this.validator(CURRENT | SEEK, PlusSymbolArithmeticalOperatorToken, MinusSymbolArithmeticalOperatorToken)) {
			operator = (Token) this.validator(CURRENT | NEXT);
			CappuccinoLiteralExpressionNode right = this.parseBinaryExpression2Node();
			return new CappuccinoBinaryLiteralExpressionNode(left, operator, right);
		}

		return left;
    }

    private CappuccinoLiteralExpressionNode parseBinaryExpression2Node() {
        CappuccinoLiteralExpressionNode left = this.parseBinaryExpression3Node();

		Token operator;

		if ((Boolean)this.validator(CURRENT | SEEK, AsteriskSymbolArithmeticalOperatorToken, SlashSymbolArithmeticalOperatorToken, PercentSymbolArithmeticalOperatorToken)) {
			operator = (Token) this.validator(CURRENT | NEXT);
			CappuccinoLiteralExpressionNode right = this.parseBinaryExpression3Node();
			return new CappuccinoBinaryLiteralExpressionNode(left, operator, right);
		}
		return left;
    }

	private CappuccinoLiteralExpressionNode parseBinaryExpression3Node() {
		Token operator;
		if ((Boolean)this.validator(CURRENT | SEEK, MinusSymbolArithmeticalOperatorToken, PlusSymbolArithmeticalOperatorToken)) {
			operator = (Token) this.validator(CURRENT | NEXT);
			CappuccinoLiteralExpressionNode right = this.parseNumberLiteralNode();
			return new CappuccinoUnaryLiteralExpressionNode(operator, right);
		}

		return this.parseNumberLiteralNode();
	}

	private CappuccinoLiteralExpressionNode parseNumberLiteralNode() {
		Token token = (Token) this.validator(CURRENT | CONSUME | NEXT, NumberLiteralToken, ByteLiteralToken, ShortLiteralToken, IntegerLiteralToken, FloatLiteralToken, DoubleLiteralToken, ParenthesisLeftSymbolDelimiterSeparatorOperatorToken);

		if (token.type() == ParenthesisLeftSymbolDelimiterSeparatorOperatorToken) {
			CappuccinoLiteralExpressionNode node = this.parseBinaryExpressionNode();
			this.validator(CURRENT | CONSUME | NEXT, ParenthesisRightSymbolDelimiterSeparatorOperatorToken);
			return node;
		}

		return new CappuccinoNumberLiteralExpressionNode(token);
	}














    public Object validator(int number, TokenType... types) {
		boolean isCurrent = (number & CURRENT) != 0;
		boolean isNext = (number & NEXT) != 0;

		Token token;

		if (!isCurrent) {
			Cappuccino.printError(/* section */1, /* error */0, /* subError */1, this.scanner.getCurrentToken().line(), this.scanner.getCurrentToken().column(), "Flag Mask Missing (Error): Missing the Flag Mask 'Current' for get the Token, Otherwise nothing can be done.");
		}

		token = this.scanner.getCurrentToken();

		if (token == CappuccinoScanner.SOIF) {
			this.scanner.getNextToken();
			token = this.scanner.getCurrentToken();
		}

		if (types.length == 0) {
			if (isNext) {
				this.scanner.getNextToken();
			}
		} else {
			boolean isConsume = (number & CONSUME) != 0;
			boolean isPeek = (number & SEEK) != 0;

			if (isConsume) {
				for (TokenType type : types) {
					if (token.type() == type) {
						if (isNext) {
							this.scanner.getNextToken();
						}
						return token;
					}
				}
				Cappuccino.printError(/* section */1, /* error */0, /* subError */1, token.line(), token.column(), "The type '" + token.type() + "' does bit exactly match the '" + this.buildError(types) + "'");
			}

			if (isPeek) {
				for (TokenType type : types) {
					if (token.type() == type) {
						if (isNext) {
							this.scanner.getNextToken();
						}
						return true;
					}
				}
				return false;
			}

		}
	    return token;
    }

	private String buildError(TokenType... types) {
		StringBuilder sb = new StringBuilder();

		for (TokenType type : types) sb.append(type)
		                               .append(" | ");
		sb.setLength(sb.length() - 3);

		return sb.toString();
	}
}

// Flag Mask Missing (Error): 