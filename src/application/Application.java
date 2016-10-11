package application;

import examples.bookTrading.BookTitle;
import jade.Boot;

public class Application {
	public static void main(String[] p_args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "reader:examples.bookTrading.BookBuyerAgent(" + BookTitle.LORD_OF_THE_RINGS + ");";
		parameters[1] += "noviceProgrammer:examples.bookTrading.BookBuyerAgent(" + BookTitle.JAVA_TUTORIAL + ");";
		parameters[1] += "advancedProgrammer:examples.bookTrading.BookBuyerAgent(" + BookTitle.JADE_PROGRAMMING_TUTORIAL
				+ ");";
		parameters[1] += "seller1:examples.bookTrading.BookSellerAgent;";
		parameters[1] += "seller2:examples.bookTrading.BookSellerAgent;";

		Boot.main(parameters);
	}
}
