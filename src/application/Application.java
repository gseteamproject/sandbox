package application;

import bookTrading.BookTitle;
import jade.Boot;

public class Application {

	public static void main(String[] p_args) {
		Boot.main(bookTradingScenario());
	}

	/*
	 * http://jade.tilab.com/doc/tutorials/JADEProgramming-Tutorial-for-
	 * beginners.pdf
	 */
	public static String[] bookTradingScenario() {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "reader:bookTrading.BookBuyerAgent(" + BookTitle.LORD_OF_THE_RINGS + ");";
		parameters[1] += "noviceProgrammer:bookTrading.BookBuyerAgent(" + BookTitle.JAVA_TUTORIAL + ");";
		parameters[1] += "advancedProgrammer:bookTrading.BookBuyerAgent(" + BookTitle.JADE_PROGRAMMING_TUTORIAL + ");";
		parameters[1] += "seller1:bookTrading.BookSellerAgent;";
		parameters[1] += "seller2:bookTrading.BookSellerAgent;";
		return parameters;
	}
}
