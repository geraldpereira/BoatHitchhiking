package fr.byob.bs.batch;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.log.Log;

//@Name("majController")
//@Scope(APPLICATION)
//@AutoCreate
//@Startup
public class MAJSchedulerController implements Serializable {

	private static final long serialVersionUID = 7609983147081676186L;

	// @In(create = true, required = true)
	MAJSchedulerProcessor majSchedulerProc;

	// @Logger
	Log log;

	private static String CRON_INTERVAL = "0 0 3 ? * MON-FRI";

	// @Create
	public void scheduleTimer() {
		majSchedulerProc.createQuartzTestTimer(new Date(), CRON_INTERVAL);
	}
}
