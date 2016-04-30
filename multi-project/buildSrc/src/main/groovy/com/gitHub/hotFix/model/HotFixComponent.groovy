package com.gitHub.hotFix.model

/**
 * hotFix组件，一个hotFix组件至少包含来源、输出目录，一次hotFix可以有多个hotFix组件
 * @author yangsai
 *
 */
class HotFixComponent {
	/**
	 * hotFix来源目录
	 */
	String source
	
	/**
	 * 排除文件
	 */
	Set<String> excludes = []
	
	/**
	 * 包含文件
	 */
	Set<String> includes = []
	
	/**
	 * hotfix输出目录
	 */
	String output
	
	@Override
	String toString() {
		return "source：${source};exclude：${excludes};include：${includes};output：${output}"
	}
	
	void exclude(String exclude) {
		if(exclude.indexOf(',')) {
			exclude.split(',').each {
				if(it) {
					this.excludes << it.trim()
				}
			}
		}else{
			this.excludes << exclude.trim()
		}
	}
}
