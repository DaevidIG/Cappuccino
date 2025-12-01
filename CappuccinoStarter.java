import cappuccino.interpreter.CappuccinoInterpreter;
import cappuccino.scanner.CappuccinoScanner;
import cappuccino.parser.CappuccinoParser;
import cappuccino.nodes.CappuccinoNodes.*;

import java.nio.file.Paths;

public class CappuccinoStarter {
	public static void main(String... args) {
		CappuccinoScanner cappuccinoScanner = new CappuccinoScanner(Paths.get(args[0]).toFile());
		CappuccinoParser cappuccinoParser = new CappuccinoParser(cappuccinoScanner);
		CappuccinoProgramNode cappuccinoNode = cappuccinoParser.parse();
		CappuccinoInterpreter cappuccinoInterpreter = new CappuccinoInterpreter(cappuccinoNode);
		cappuccinoInterpreter.interpreter();
	}
}