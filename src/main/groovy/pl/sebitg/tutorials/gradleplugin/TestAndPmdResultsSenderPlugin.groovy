package pl.sebitg.tutorials.gradleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

import pl.sebitg.tutorials.gradleplugin.tasks.SendEmailTask

/**
 * Plugin extension class
 * @author s.mekal
 *
 */
class PluginExtensionClass {
	String from = "Sebastian Mekal <sebitg@gmail.com>"
	String username = "sebitg@gmail.com"
	String password = "password"
	String host = "smtp.gmail.com"
	Boolean auth = true
	Boolean sslEnable = true
	Integer port = 465
	String mails = "btcgrouppl@gmail.com"
}

/**
 * Gradle plugin main class
 * @author Sebastian Mekal <sebitg@gmail.com>
 */
class TestAndPmdResultsSenderPlugin implements Plugin<Project> {
	
	@Override
	public void apply(Project arg0) {
		
		arg0.extensions.create("tutorialArgs", PluginExtensionClass)
		
		//Old task remain the same
		arg0.task('someTask') << {
			def someSysProperty = System.properties['someArg']
			println "Hello someTask world!"
			println "System arg: " + someSysProperty
			println "-----------------------------"
			println "Printing tutorialArgs configuration :"
			println "${project.tutorialArgs.from}"
			println "${project.tutorialArgs.username}"
		}
		
		//Adding new sendemail task, and execution rules
		arg0.task("sendemail", type: SendEmailTask)
		arg0.tasks.getByName("test")
				.finalizedBy("sendemail")
		arg0.tasks.getByName("compileJava")
				.finalizedBy("sendemail")
		arg0.tasks.getByName("build")
				.finalizedBy("sendemail")
	}
}