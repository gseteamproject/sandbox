package knowledge.ontology;

import jade.content.Concept;

public class Fact implements Concept {

	private static final long serialVersionUID = -7707134783555070375L;

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
