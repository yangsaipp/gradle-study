package com.gitHub.hotFix

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 增量插件
 * @author ys
 *
 */
class HotFixPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		// Add the 'greeting' extension object
		project.extensions.create("hotFix", HotFixPluginExtension)
		// Add a task that uses the configuration
		project.task('hotFix') << {
			println project.hotFix.greeter + ':' + project.hotFix.message
		}
	}

}

/**
 * hotFix插件扩展参数
 * @author ys
 *
 */
class HotFixPluginExtension {
	String message
	String greeter
}
