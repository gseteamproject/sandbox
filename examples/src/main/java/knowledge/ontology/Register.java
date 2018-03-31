package knowledge.ontology;

import jade.content.AgentAction;

public class Register implements AgentAction {

	private static final long serialVersionUID = 1569207849831954852L;

	private Fact fact;

	public Fact getFact() {
		return fact;
	}

	public void setFact(Fact fact) {
		this.fact = fact;
	}
}
