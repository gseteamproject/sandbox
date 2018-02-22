package kernelService;

import jade.core.AID;
import jade.core.Agent;
import jade.core.BaseService;
import jade.core.Filter;
import jade.core.HorizontalCommand;
import jade.core.IMTPException;
import jade.core.Node;
import jade.core.Profile;
import jade.core.Service;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.VerticalCommand;
import jade.core.messaging.GenericMessage;
import jade.core.messaging.MessagingSlice;
import jade.lang.acl.ACLMessage;

public class LoggingService extends BaseService {

	// Service name
	public static final String NAME = "kernelService.Logging";

	// Service parameter names
	public static final String VERBOSE = "loggingService_verbose";

	private boolean verbose = false;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void boot(Profile p) throws ServiceException {
		super.boot(p);
		verbose = p.getBooleanProperty(VERBOSE, false);
		System.out.println("VERBOSE = " + verbose);
	}

	private Filter outFilter = new OutgoingLoggingFilter();

	@Override
	public Filter getCommandFilter(boolean direction) {
		if (direction == Filter.OUTGOING) {
			return outFilter;
		}
		return null;
	}

	// THIS IS ANOTHER EXAMPLE
	//
	// private class OutgoingLoggingFilter extends Filter {
	// @Override
	// protected boolean accept(VerticalCommand cmd) {
	// if (cmd.getName().equals(MessagingSlice.SEND_MESSAGE)) {
	// Object[] params = cmd.getParams();
	// AID sender = (AID) params[0];
	// GenericMessage gMsg = (GenericMessage) params[1];
	// ACLMessage msg = gMsg.getACLMessage();
	// AID receiver = (AID) params[2];
	// System.out.println("Message from " + sender + " to " + receiver + ":");
	// if (verbose) {
	// System.out.println(msg);
	// } else {
	// System.out.println(ACLMessage.getPerformative(msg.getPerformative()));
	// }
	// }
	//
	// return super.accept(cmd);
	// }
	// }

	private class LoggingSliceImpl implements Service.Slice {

		private static final long serialVersionUID = 5883174830601259134L;

		@Override
		public Service getService() {
			return LoggingService.this;
		}

		@Override
		public Node getNode() throws ServiceException {
			try {
				return LoggingService.this.getLocalNode();
			} catch (IMTPException e) {
				throw new ServiceException("Unexpected error retrieving local node");
			}
		}

		@Override
		public VerticalCommand serve(HorizontalCommand cmd) {
			if (cmd.getName().equals(LoggingSlice.H_LOGMESSAGE)) {
				Object[] params = cmd.getParams();
				System.out.println(params[0]);
			}
			return null;
		}
	}

	private Service.Slice localSlice = new LoggingSliceImpl();

	@SuppressWarnings("rawtypes")
	@Override
	public Class getHorizontalInterface() {
		return LoggingSlice.class;
	}

	@Override
	public Service.Slice getLocalSlice() {
		return localSlice;
	}

	private class OutgoingLoggingFilter extends Filter {

		public boolean accept(VerticalCommand cmd) {
			if (cmd.getName().equals(MessagingSlice.SEND_MESSAGE)) {
				Object[] params = cmd.getParams();
				AID sender = (AID) params[0];
				GenericMessage gMsg = (GenericMessage) params[1];
				ACLMessage msg = gMsg.getACLMessage();
				AID receiver = (AID) params[2];
				// Prepare the log record
				String logRecord = "Message from " + sender + " to" + receiver + ": \n";
				if (verbose) {
					logRecord = logRecord + msg;
				} else {
					logRecord = logRecord + ACLMessage.getPerformative(msg.getPerformative());
				}

				// Send the log record to the logging slice on the Main Container
				try {
					LoggingSlice mainSlice = (LoggingSlice) getSlice(MAIN_SLICE);
					mainSlice.logMessage(logRecord);
				} catch (ServiceException se) {
					System.out.println("Error retrieving Main Logging Slice");
					se.printStackTrace();
				} catch (IMTPException se) {
					System.out.println("Error contacting Main Logging Slice");
					se.printStackTrace();
				}
			}
			// Never block a command
			return true;
		}
	}

	private ServiceHelper helper = new LoggingHelperImpl();

	@Override
	public ServiceHelper getHelper(Agent a) {
		return helper;
	}

	public class LoggingHelperImpl implements LoggingHelper {

		@Override
		public void init(Agent a) {
		}

		@Override
		public void setVerbose(boolean v) {
			verbose = v;
		}
	}
}
