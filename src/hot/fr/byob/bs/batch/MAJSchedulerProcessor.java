package fr.byob.bs.batch;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;

import fr.byob.bs.AdminManager;
import fr.byob.bs.BSException;


//@Name("majSchedulerProc")
//@AutoCreate
//@Scope(ScopeType.APPLICATION)
public class MAJSchedulerProcessor {


	// @Logger
	private Log log;
    
    @In(create = true, required = true)
	private AdminManager adminManager;

	// @Asynchronous
	// @Transactional
	public QuartzTriggerHandle createQuartzTestTimer(@Expiration Date when, @IntervalCron String interval) {

		updateData();
		SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy hh:mm:ss");
		log.info("Quartz Test updateData : " + format.format(new Date()));
		return null;
	}
	private void updateData() {
		try {
			Conversation.instance().begin();
			adminManager.updateAllBateauxNotes();
			adminManager.updateAllUtilisateursNotes();
			adminManager.updateAllOffresNotes();
			adminManager.updateAllDemandesNotes();
			Conversation.instance().end();
		} catch (Exception ex) {
			new BSException(ex);
		}
	}
}
