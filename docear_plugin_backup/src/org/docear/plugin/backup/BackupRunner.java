package org.docear.plugin.backup;

import org.docear.plugin.communications.CommunicationsController;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.util.LogUtils;
import org.freeplane.features.mode.Controller;
import org.jdesktop.swingworker.SwingWorker;


public class BackupRunner {
	private SwingWorker<Void, Void> runner;
//	private boolean running = false;

	public void run() {
//		if (running) {
//			return;
//		}
		
		LogUtils.info("running Docear BackupRunner");
		
//		running = true;
		
		final ResourceController resourceCtrl = Controller.getCurrentController().getResourceController();
		final BackupController backupCtrl = BackupController.getController();
		final CommunicationsController commCtrl = CommunicationsController.getController();
		
		final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		
		runner = new SwingWorker<Void, Void>() {
			public Void doInBackground() {				
				while (true) {
					synchronized (this) {						
						int backupMinutes = resourceCtrl.getIntProperty("save_backup_automcatically", 0);
						if (backupMinutes <= 0) {
							backupMinutes = 30;
						}
						try {			
							if (backupCtrl.isBackupEnabled() && commCtrl.allowTransmission() 
									&& commCtrl.getAccessToken()!=null && commCtrl.getAccessToken().trim().length()>0) {
								LogUtils.info("Docear BackupRunner: synchronizing backups with server");
								boolean success = CommunicationsController.getController().postFileToDocearService("mindmaps", true, backupCtrl.getBackupQueue());								
								if (success) {
									LogUtils.info("Docear BackupRunner: synchronizing successfull");
								}
								else {
									LogUtils.info("Docear BackupRunner: synchronizing failed");
								}
							}
							this.wait(60000 * backupMinutes);
						}
						catch (Exception e) {
							LogUtils.warn(e);
						}
					}
				}
			}
		};
		Thread.currentThread().setContextClassLoader(contextClassLoader);
		
		runner.execute();
	}
	
//	public void stop() {
//		LogUtils.info("stoping Docear BackupRunner");
//		
//		runner.cancel(true);
//		running = false;
//	}

}