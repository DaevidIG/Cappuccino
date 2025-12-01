import cappuccino.interpreter.CappuccinoInterpreter;
import cappuccino.scanner.CappuccinoScanner;
import cappuccino.parser.CappuccinoParser;
import cappuccino.nodes.CappuccinoNodes.*;

public class CappuccinoStarter {
	public static void main(String... args) {
		long start = System.nanoTime();

		CappuccinoScanner cappuccinoScanner = new CappuccinoScanner("let valor: integer | [number, number] = 1n;");
		CappuccinoParser cappuccinoParser = new CappuccinoParser(cappuccinoScanner);
		CappuccinoProgramNode cappuccinoNode = cappuccinoParser.parse();
		CappuccinoInterpreter cappuccinoInterpreter = new CappuccinoInterpreter(cappuccinoNode);
		cappuccinoInterpreter.interpreter();

		long end = System.nanoTime();
		System.out.println("Time in: " + ((end - start) / 1_000_000) + "ns");
	}
}