package pl.sebitg.tutorials.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin extension class
 * @author s.mekal
 *
 */
class PluginExtensionClass {
	String someVar1 = "default val";
	String someVar2 = "default-val2";
}

/**
 * Gradle plugin main class
 * @author Sebastian Mekal <sebitg@gmail.com>
 */
class TestAndPmdResultsSenderPlugin implements Plugin<Project> {
	
	@Override
	public void apply(Project arg0) {
		
		arg0.extensions.create("tutorialArgs", PluginExtensionClass)
		
		arg0.task('someTask') << {
			def someSysProperty = System.properties['someArg']
			println "Hello someTask world!"
			println "System arg: " + someSysProperty
			println "-----------------------------"
			println "Printing tutorialArgs configuration :"
			println "${project.tutorialArgs.someVar1}"
			println "${project.tutorialArgs.someVar2}"
		}
		
	}
}