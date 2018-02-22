package kernelService;

import jade.core.ServiceHelper;

public interface LoggingHelper extends ServiceHelper {

	public void setVerbose(boolean verbose);
}
