package com.gitHub.hotFix.task

import org.gradle.api.DefaultTask
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.TaskAction

import com.gitHub.hotFix.model.HotFixComponent

/**
 * hotFix 处理器，parser生成hotFix文件列表，processor进行处理。<br>
 * <p>典型处理场景：java类需要编译，文件也需要从编译路径去获取而不是源码路径</p>
 * @author yangsai
 *
 */
class HotFixProcessor extends DefaultTask {
	static Logger buildLogger = Logging.getLogger(HotFixProcessor.class);
	
	def hotFixModel
	
	@TaskAction
	void process() {
		hotFixModel.components.each {key, component->
			if(component.compileType == 'java') {
				processJavaCode(component)
			}else if(component.compileType == 'resource'){
				processResource(component)
			}else if(component.name == 'webapp'){
				processWebapp(component)
			}
			buildLogger.quiet("{}",component.toString())
		}
	}
	
	def processJavaCode(HotFixComponent component) {
		if(!(component.compileSource?.trim())) {
			if(project.plugins.hasPlugin(org.gradle.api.plugins.JavaPlugin.class)) {
				component.compileSource = project.sourceSets.main.output.classesDir
				component.output = "${hotFixModel.targetDir}/WEB-INF/classes"
				buildLogger.debug('set java component compile source and output')
			}
		}
		Set fileSet = []
		component.hotFixFileSet.each {
			def classFileName = it.replaceAll('java$', 'class')
			fileSet << classFileName
		}
		component.hotFixFileSet = fileSet
	}

	def processResource(HotFixComponent component) {
		if(!(component.compileSource?.trim())) {
			if(project.plugins.hasPlugin(org.gradle.api.plugins.JavaPlugin.class)) {
				component.compileSource = project.sourceSets.main.output.resourcesDir
				component.output = "${hotFixModel.targetDir}/WEB-INF/classes"
				buildLogger.debug('set resource component compile source and output')
			}
		}
	}
	
	def processWebapp(HotFixComponent component) {
//		component.compileSource = project.sourceSets.main.output.resourcesDir
		component.exclude('**/classes')
		component.output = "${hotFixModel.targetDir}"
		buildLogger.debug('set webapp component exclude and output')
	}
}
