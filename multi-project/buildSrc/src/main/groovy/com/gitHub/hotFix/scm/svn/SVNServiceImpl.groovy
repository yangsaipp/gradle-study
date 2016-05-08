package com.gitHub.hotFix.scm.svn

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.SVNLogEntry
import org.tmatesoft.svn.core.SVNLogEntryPath
import org.tmatesoft.svn.core.SVNURL
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl
import org.tmatesoft.svn.core.io.SVNRepository
import org.tmatesoft.svn.core.io.SVNRepositoryFactory
import org.tmatesoft.svn.core.wc.SVNWCUtil

import com.gitHub.hotFix.model.ProjectSCM
import com.gitHub.hotFix.scm.SCMService
import com.gitHub.hotFix.scm.model.SCMLog
import com.sun.java.util.jar.pack.Instruction.Switch;

/**
 * 提供svn服务，如查询log
 * <p>
 * 参考:<a href='http://wiki.svnkit.com/Printing_Out_Repository_History'>SVN Printing Out Repository History</a>
 * <p>
 * @author yangsai
 *
 */
class SVNServiceImpl implements SCMService {
	static Logger buildLogger = Logging.getLogger(SCMService.class);
	
	@Override
	public SCMLog getLog(ProjectSCM scmInfo, String startRevision, String endRevision) {
		SVNRepository repository = null;
		String url = scmInfo.url
		String name = scmInfo.username
		String password = scmInfo.password
		long startR = startRevision as long
		long endR = endRevision as long
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
		} catch (SVNException svne) {
			buildLogger.error("error while creating an SVNRepository for the location '" + url + "': " + svne.getMessage());
		}

		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password.toCharArray());
		repository.setAuthenticationManager(authManager);

		/*
		 * Gets the latest revision number of the repository
		 */
		try {
			endRevision = repository.getLatestRevision();
		} catch (SVNException svne) {
			buildLogger.error("error while fetching the latest repository revision: " + svne.getMessage());
		}

		Collection logEntries = null;
		try {
			String[] targetPath = [''] 
			logEntries = repository.log(targetPath, null, startR, endR, true, true);

		} catch (SVNException svne) {
			buildLogger.error("error while collecting log information for '" + url + "': " + svne.getMessage());
		}
		
		SCMLog scmLog = new SCMLog()
		for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
			/*
			 * gets a next SVNLogEntry
			 */
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
//			System.out.println("---------------------------------------------");
			/*
			 * gets the revision number
			 */
//			System.out.println("revision: " + logEntry.getRevision());
			/*
			 * gets the author of the changes made in that revision
			 */
//			System.out.println("author: " + logEntry.getAuthor());
			/*
			 * gets the time moment when the changes were committed
			 */
//			System.out.println("date: " + logEntry.getDate());
			/*
			 * gets the commit log message
			 */
//			System.out.println("log message: " + logEntry.getMessage());
			/*
			 * displaying all paths that were changed in that revision; cahnged
			 * path information is represented by SVNLogEntryPath.
			 */
			if (logEntry.getChangedPaths().size() > 0) {
//				System.out.println();
//				System.out.println("changed paths:");
				/*
				 * keys are changed paths
				 */
				Set changedPathsSet = logEntry.getChangedPaths().keySet();

				for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
					
					SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
					switch(entryPath.getType()) {
						case SVNLogEntryPath.TYPE_ADDED:
							scmLog.addPath(entryPath.getPath())
							break
						case SVNLogEntryPath.TYPE_DELETED:
							scmLog.addDeletePath(entryPath.getPath())
							break
						case SVNLogEntryPath.TYPE_MODIFIED:
							scmLog.addPath(entryPath.getPath())
							break
						case SVNLogEntryPath.TYPE_REPLACED:
							//FIXME replace类型时获oldpath
							scmLog.addRenamePath(entryPath.getPath(), entryPath.getCopyPath())
							break
						default:
							break
					}
				}
			}
		}
		return scmLog;
	}
	
	/*
	 * Initializes the library to work with a repository via
	 * different protocols.
	 */
	private static void setupLibrary() {
		/*
		 * For using over http:// and https://
		 */
		DAVRepositoryFactory.setup();
		/*
		 * For using over svn:// and svn+xxx://
		 */
		SVNRepositoryFactoryImpl.setup();
		
		/*
		 * For using over file:///
		 */
		FSRepositoryFactory.setup();
	}
}
