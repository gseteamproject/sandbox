package kernelService;

import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.ServiceException;
import jade.core.SliceProxy;

public class LoggingProxy extends SliceProxy implements LoggingSlice {

	private static final long serialVersionUID = 7746924653227428881L;

	@Override
	public void logMessage(String s) throws ServiceException, IMTPException {
		GenericCommand cmd = new GenericCommand(H_LOGMESSAGE, LoggingService.NAME, null);
		cmd.addParam(s);
		getNode().accept(cmd);
	}
}
