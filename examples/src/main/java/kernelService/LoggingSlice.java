package kernelService;

import jade.core.IMTPException;
import jade.core.Service;
import jade.core.ServiceException;

public interface LoggingSlice extends Service.Slice {
	public static final String H_LOGMESSAGE = "log-message";

	public void logMessage(String s) throws IMTPException, ServiceException;
}
