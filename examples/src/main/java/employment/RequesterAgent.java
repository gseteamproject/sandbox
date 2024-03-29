/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
*****************************************************************/

package employment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import employment.ontology.Address;
import employment.ontology.Company;
import employment.ontology.EmploymentOntology;
import employment.ontology.Engage;
import employment.ontology.Person;
import employment.ontology.WorksFor;
import jade.content.abs.AbsContentElementList;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

/**
 * This agent is able to handle the engagement of people by requesting an
 * engager-agent to do that. It first gets from the user
 * <ul>
 * <li>The name of the engager agent to send engagement requests to.</li>
 * <li>The details of the company where to engage people</li>
 * </ul>
 * Then it cyclically gets from the user the details of a person to engage and
 * handles the engagement of that person in collaboration with the initially
 * indicated engager agent.
 * 
 * <b>Note:</b> While entering input data, all fields composed of more than one
 * word must be enclosed in "". E.g. the name Giovanni Caire must be entered as
 * "Giovanni Caire".
 * 
 * @author Giovanni Caire - CSELT S.p.A
 * @version $Date: 2002-07-31 17:27:34 +0200 (mer, 31 lug 2002) $ $Revision:
 *          3315 $
 * @see employment.EngagerAgent
 */
public class RequesterAgent extends Agent {
	private static final long serialVersionUID = 4027172853168232865L;

	// AGENT BEHAVIOURS
	/**
	 * Main behavior for the Requester Agent. First the details of a person to
	 * engage are requested to the user. Then a check is performed to verify
	 * that the indicated person is not already working for the indicated
	 * company Finally, according to the above check, the engagement is
	 * requested. This behavior is executed cyclically.
	 */
	class HandleEngagementBehaviour extends SequentialBehaviour {
		private static final long serialVersionUID = 1713046792590983441L;

		// Local variables
		Behaviour queryBehaviour = null;
		Behaviour requestBehaviour = null;

		// Constructor
		public HandleEngagementBehaviour(Agent myAgent) {
			super(myAgent);
		}

		// This is executed at the beginning of the behavior
		public void onStart() {
			System.out.println("Engagement started");
			// Get detail of person to be engaged
			Person person = new Person();
			person.getFromStandardInputStream();

			// Create an object representing the fact that person p works for
			// company c
			WorksFor wf = new WorksFor();
			wf.setPerson(person);
			wf.setCompany(((RequesterAgent) myAgent).targetCompany);

			// Create an ACL message to query the engager agent if the above
			// fact is true or false
			ACLMessage queryMsg = new ACLMessage(ACLMessage.QUERY_IF);
			queryMsg.addReceiver(((RequesterAgent) myAgent).engager);
			queryMsg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
			queryMsg.setOntology(EmploymentOntology.NAME);

			// Write the works for predicate in the :content slot of the message
			try {
				myAgent.getContentManager().fillContent(queryMsg, wf);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Create and add a behavior to query the engager agent whether
			// person p already works for company c following a FIPAQeury
			// protocol
			queryBehaviour = new CheckAlreadyWorkingBehaviour(myAgent, queryMsg);
			addSubBehaviour(queryBehaviour);
		}

		// This is executed at the end of the behavior
		public int onEnd() {
			// Check whether the user wants to continue
			try {
				BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Would you like to continue?[y/n] ");
				String stop = buff.readLine();
				if (stop.equalsIgnoreCase("y")) {
					reset(); // This makes this behavior be cyclically executed
					myAgent.addBehaviour(this);
				} else
					myAgent.doDelete(); // Exit
			} catch (IOException ioe) {
				System.err.println("I/O error: " + ioe.getMessage());
			}
			return 0;
		}

		// Extends the reset method in order to remove the sub-behaviors that
		// are dynamically added
		public void reset() {
			if (queryBehaviour != null) {
				removeSubBehaviour(queryBehaviour);
				queryBehaviour = null;
			}
			if (requestBehaviour != null) {
				removeSubBehaviour(requestBehaviour);
				requestBehaviour = null;
			}
			super.reset();
		}
	}

	/**
	 * This behavior embeds the check that the indicated person is not already
	 * working for the indicated company. This is done following a FIPA-Query
	 * interaction protocol
	 */
	class CheckAlreadyWorkingBehaviour extends SimpleAchieveREInitiator {
		private static final long serialVersionUID = 1242323210121240249L;

		// Constructor
		public CheckAlreadyWorkingBehaviour(Agent myAgent, ACLMessage queryMsg) {
			super(myAgent, queryMsg);
			queryMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
		}

		protected void handleInform(ACLMessage msg) {
			try {
				AbsPredicate cs = (AbsPredicate) myAgent.getContentManager().extractAbsContent(msg);
				Ontology o = myAgent.getContentManager().lookupOntology(EmploymentOntology.NAME);
				if (cs.getTypeName().equals(EmploymentOntology.WORKS_FOR)) {
					// The indicated person is already working for company c.
					// Inform the user
					WorksFor wf = (WorksFor) o.toObject((AbsObject) cs);
					Person p = (Person) wf.getPerson();
					Company c = (Company) wf.getCompany();
					System.out.println("Person " + p.getName() + " is already working for " + c.getName());
				} else if (cs.getTypeName().equals(SLVocabulary.NOT)) {
					// The indicated person is NOT already working for company
					// c.
					// Get person and company details and create an object
					// representing the engagement action
					WorksFor wf = (WorksFor) o.toObject(cs.getAbsObject(SLVocabulary.NOT_WHAT));
					Person p = (Person) wf.getPerson();
					Company c = (Company) wf.getCompany();
					Engage e = new Engage();
					e.setPerson(p);
					e.setCompany(c);
					Action a = new Action();
					a.setActor(((RequesterAgent) myAgent).engager);
					a.setAction(e);

					// Create an ACL message to request the above action
					ACLMessage requestMsg = new ACLMessage(ACLMessage.REQUEST);
					requestMsg.addReceiver(((RequesterAgent) myAgent).engager);
					requestMsg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
					requestMsg.setOntology(EmploymentOntology.NAME);
					// Write the action in the :content slot of the message

					try {
						myAgent.getContentManager().fillContent(requestMsg, a);
					} catch (Exception pe) {
					}
					// Create and add a behavior to request the engager agent to
					// engage
					// person p in company c following a FIPARequest protocol
					((HandleEngagementBehaviour) parent).requestBehaviour = new RequestEngagementBehaviour(myAgent,
							requestMsg);
					((SequentialBehaviour) parent)
							.addSubBehaviour(((HandleEngagementBehaviour) parent).requestBehaviour);
				} else {
					// Unexpected response received from the engager agent.
					// Inform the user
					System.out.println("Unexpected response from engager agent");
				}

			} // End of try
			catch (Codec.CodecException fe) {
				System.err.println("FIPAException in fill/extract Msgcontent:" + fe.getMessage());
			} catch (OntologyException fe) {
				System.err.println("OntologyException in getRoleName:" + fe.getMessage());
			}
		}

	}

	/**
	 * This behavior embeds the request to engage the indicated person in the
	 * indicated company. This is done following a FIPA-Request interaction
	 * protocol
	 */
	class RequestEngagementBehaviour extends SimpleAchieveREInitiator {
		private static final long serialVersionUID = -8699565427879297481L;

		// Constructor
		public RequestEngagementBehaviour(Agent myAgent, ACLMessage requestMsg) {
			super(myAgent, requestMsg);
			requestMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		}

		protected void handleAgree(ACLMessage msg) {
			System.out.println("Engagement agreed. Waiting for completion notification...");
		}

		protected void handleInform(ACLMessage msg) {
			System.out.println("Engagement successfully completed");
		}

		protected void handleNotUnderstood(ACLMessage msg) {
			System.out.println("Engagement request not understood by engager agent");
		}

		protected void handleFailure(ACLMessage msg) {
			System.out.println("Engagement failed");
			// Get the failure reason and communicate it to the user
			try {
				AbsPredicate absPred = (AbsPredicate) myAgent.getContentManager().extractContent(msg);

				System.out.println("The reason is: " + absPred.getTypeName());
			} catch (Codec.CodecException fe) {
				System.err.println("FIPAException reading failure reason: " + fe.getMessage());
			} catch (OntologyException oe) {
				System.err.println("OntologyException reading failure reason: " + oe.getMessage());
			}
		}

		protected void handleRefuse(ACLMessage msg) {
			System.out.println("Engagement refused");
			// Get the refusal reason and communicate it to the user
			try {
				AbsContentElementList list = (AbsContentElementList) myAgent.getContentManager().extractAbsContent(msg);
				AbsPredicate absPred = (AbsPredicate) list.get(1);
				System.out.println("The reason is: " + absPred.getTypeName());
			} catch (Codec.CodecException fe) {
				System.err.println("FIPAException reading refusal reason: " + fe.getMessage());
			} catch (OntologyException oe) {
				System.err.println("OntologyException reading refusal reason: " + oe.getMessage());
			}
		}
	}

	// AGENT LOCAL VARIABLES
	AID engager; // AID of the agent the engagement requests will have to be
					// sent to
	Company targetCompany; // The company where people will be engaged

	// AGENT SETUP
	protected void setup() {

		// Register the codec for the SL0 language
		getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);

		// Register the ontology used by this application
		getContentManager().registerOntology(EmploymentOntology.getInstance());

		// Get from the user the name of the agent the engagement requests
		// will have to be sent to
		try {
			BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));

			System.out.print("ENTER the local name of the Engager agent --> ");
			String name = buff.readLine();
			engager = new AID(name, AID.ISLOCALNAME);

			// Get from the user the details of the company where people will
			// be engaged
			targetCompany = new Company();
			Address a = new Address();
			System.out.println("ENTER details of the company where people will be engaged");
			System.out.print("  Company name --> ");
			targetCompany.setName(buff.readLine());
			System.out.println("  Company address");
			System.out.print("    Street ------> ");
			a.setStreet(buff.readLine());
			System.out.print("    Number ------> ");
			a.setNumber(Long.valueOf(buff.readLine()));
			System.out.print("    City   ------> ");
			a.setCity(buff.readLine());
			targetCompany.setAddress(a);
		} catch (IOException ioe) {
			System.err.println("I/O error: " + ioe.getMessage());
		}

		// Create and add the main behavior of this agent
		addBehaviour(new HandleEngagementBehaviour(this));
	}
}
