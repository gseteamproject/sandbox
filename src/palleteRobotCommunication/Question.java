package palleteRobotCommunication;

import jade.content.Concept;

public class Question implements Concept {

	private static final long serialVersionUID = 418155681200341499L;

	private String text;

	public String getText() {
		return text;
	}

	public void setText(String question) {
		this.text = question;
	}

}
