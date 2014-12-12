package pl.sebitg.tutorials.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin main class
 * @author Sebastian Mekal <sebitg@gmail.com>
 */
class TestAndPmdResultsSenderPlugin implements Plugin<Project> {

	@Override
	public void apply(Project arg0) {
		
		arg0.task('someTask') << {
			println "Hello someTask world!"
		}
		
	}

}