package knowledge.ontology;

import jade.content.Predicate;

public class Question implements Predicate {

	private static final long serialVersionUID = -378001128757897600L;

	private Fact fact;

	public Fact getFact() {
		return fact;
	}

	public void setFact(Fact fact) {
		this.fact = fact;
	}
}
