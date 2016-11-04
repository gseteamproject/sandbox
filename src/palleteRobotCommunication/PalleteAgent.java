package palleteRobotCommunication;

import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public abstract class PalleteAgent extends Agent {

	private static final long serialVersionUID = 756024496424346973L;

	protected Pallete pallete = new Pallete();

	private Codec codec = new SLCodec();
	private Ontology ontology = PalleteOntology.getInstance();

	/**
	 * 
	 * @param msg
	 *            the message which gets filled
	 * @return filled message with the current state of the pallete
	 */
	protected abstract State getPalleteState();

	protected abstract void trace(String message);

	protected void setup() {
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		initializeData();
		initializeBehaviour();
		trace("I am a Pallete and I have " + pallete.getCapacity() + " Blocks");
	}

	/**
	 * Sets capacity of the pallete to the given argument. If no argument is
	 * passed, capacity = 5
	 */
	private void initializeData() {
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			this.pallete.setCapacity(Integer.parseInt(args[0].toString()));
		} else {
			this.pallete.setCapacity(5);
		}
	}

	private void initializeBehaviour() {
		addBehaviour(new CyclicBehaviour(this) {
			private static final long serialVersionUID = 7774831398907094833L;

			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					trace("got Message from Robot: " + msg.getContent());
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					reply.setContent("");
					try {
						ContentManager cm = myAgent.getContentManager();
						Action a = (Action) myAgent.getContentManager().extractContent(msg);
						Question question = (Question) a.getAction();
						if (question != null) {
							a.setAction(getPalleteState());							
							Done d = new Done(a);
							cm.fillContent(reply, d);
							reply.setPerformative(ACLMessage.INFORM);
						}

					} catch (OntologyException oe) {
						oe.printStackTrace();
					} catch (jade.content.lang.Codec.CodecException e) {
						e.printStackTrace();
					}
					trace("answering: " + reply.getContent());
					myAgent.send(reply);
					/*
					 * TODO: remove after testing
					 * if (msg.getContent().equals(RobotRequest.WHAT_YOUR_STATE
					 * )) { trace("got Message from Robot: " +
					 * msg.getContent()); trace("answering..."); ACLMessage
					 * reply = fillReply(msg); myAgent.send(reply); }
					 */
				}
				block();
			}
		});
	}

	protected void takeDown() {
		trace("terminated");
	}
}
