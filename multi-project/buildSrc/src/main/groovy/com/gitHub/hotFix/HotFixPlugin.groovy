package com.gitHub.hotFix

import com.gitHub.hotFix.model.HotFixModel

import org.gradle.api.Plugin	
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.plugins.ide.internal.IdePlugin;

/**
 * 增量插件
 * @author ys
 *
 */
class HotFixPlugin implements Plugin<Project> {
	static final String HOTFIX_FILE_GROUP = "hotFix"
	static final String READ_HOTFIX_FILE = "readHotFixFile"
//	static final String ECLIPSE_PROJECT_TASK_NAME = "eclipseProject"
//	static final String ECLIPSE_CP_TASK_NAME = "eclipseClasspath"
//	static final String ECLIPSE_JDT_TASK_NAME = "eclipseJdt"
	
	@Override
	public void apply(Project project) {
		HotFixModel model = project.extensions.create("hotFix", HotFixModel)
		Task readHotFix = configureReadHotFixFile(project, model)
		configureCopyClassHotFix(project, model).dependsOn(readHotFix, 'jar')
		configureCopyWebappHotFix(project, model).dependsOn(readHotFix)
		
		Task task = project.task('hotFixDump') << {
			println model.dumps()
		}
		task.dependsOn('copyClassHotFix','copyWebappHotFix')
		task.setGroup(HOTFIX_FILE_GROUP)
		readHotFix.setGroup(HOTFIX_FILE_GROUP)
	}
	
	private Task configureCopyClassHotFix(Project project, HotFixModel model) {
		if (project.tasks.findByName('copyClassHotFix')) {
			return null
		}
		return project.task('copyClassHotFix') << {
			println "classFile:$ext.hotFixFiles"
			//hasProperty
			if(!ext.hotFixFiles.empty) {
				project.copy {
					//class 目录
					from model.java.source
					//resource 目录
					from model.resource.source
					into "${model.output}/WEB-INF/classes"
					include ext.hotFixFiles
					exclude model.java.exclude
					exclude model.resource.exclude
				}
			}
		}
	}
	
	private Task configureCopyWebappHotFix(Project project, HotFixModel model) {
		if (project.tasks.findByName('copyWebappHotFix')) {
			return null
		}
		return project.task('copyWebappHotFix') << {
			println "webFile:$ext.hotFixFiles"
			if(!ext.hotFixFiles.empty) {
				project.copy {
					//webApp目录
					from model.webapp.source, {
						exclude '**/classes'
						//exclude '**/lib'
					}
					into model.output
					include ext.hotFixFiles
					exclude model.webapp.exclude
				}
			}
		}
	}
	
	private Task configureReadHotFixFile(Project project, HotFixModel model) {
		if (project.tasks.findByName(READ_HOTFIX_FILE)) {
			return null
		}
		return project.task(READ_HOTFIX_FILE) << {
			description = "读取hotFix文件列表"
			File readme = new File("${project.projectDir}/hotFix.txt")
			def webHotFixFiles = []
			def classHotFixFiles = []
			readme.eachLine("UTF-8"){
				it = it.replaceAll("\\\\", '/');
				def javaFlag = '/src/main/java/'
				def resourceFlag = '/src/main/resources/'
				def webAppFlag = '/src/main/webapp/'
				def index = it.indexOf(javaFlag)
				if(index != -1) {
					it = it.replaceAll('java$', 'class')
					classHotFixFiles << it.substring(index+javaFlag.length(), it.length())
					//注意不能使用continue
					return false
				}
				index = it.indexOf(resourceFlag)
				if(index != -1) {
					classHotFixFiles << it.substring(index+resourceFlag.length(), it.length())
					return false
				}
				index = it.indexOf(webAppFlag)
				if(index != -1) {
					webHotFixFiles << it.substring(index + webAppFlag.length(), it.length())
					return false
				}
				println "$index:$it"
			}
			project.tasks.findByName('copyWebappHotFix').configure { obj->
				obj.ext.hotFixFiles = webHotFixFiles
			}
			
			project.tasks.findByName('copyClassHotFix').configure { obj->
				obj.ext.hotFixFiles = classHotFixFiles
			}
			//先清理
			project.delete(model.output)
		}
	}
	
	private void maybeAddTask(Project project, String taskName, Class taskType, Closure action) {
		if (project.tasks.findByName(taskName)) { 
			return
		}
		def task = project.tasks.create(taskName, taskType)
		project.configure(task, action)
	}
}
