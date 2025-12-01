package cappuccino.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CappuccinoRanges {

	// BYTE
	public static final BigInteger BYTE_NEGATIVE = BigInteger.valueOf(-128);
	public static final BigInteger BYTE_POSITIVE = BigInteger.valueOf(128);

	// SHORT
	public static final BigInteger SHORT_NEGATIVE = BigInteger.valueOf(-32768);
	public static final BigInteger SHORT_POSITIVE = BigInteger.valueOf(32768);

	// INTEGER
	public static final BigInteger INTEGER_NEGATIVE = BigInteger.valueOf(-2_147_483_648L);
	public static final BigInteger INTEGER_POSITIVE = BigInteger.valueOf(2_147_483_648L);

	// FLOAT
	public static final BigDecimal FLOAT_NEGATIVE = new BigDecimal(-Float.MAX_VALUE).subtract(BigDecimal.ONE);
	public static final BigDecimal FLOAT_POSITIVE = new BigDecimal(Float.MAX_VALUE).add(BigDecimal.ONE);

	// DOUBLE
	public static final BigDecimal DOUBLE_NEGATIVE = new BigDecimal(-Double.MAX_VALUE).subtract(BigDecimal.ONE);
	public static final BigDecimal DOUBLE_POSITIVE = new BigDecimal(Double.MAX_VALUE).add(BigDecimal.ONE);

}

