package pl.sebitg.tutorials.gradleplugin.tasks

import java.util.Properties;

import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskState

/**
 * Send email task. Sending email with current build status
 * @author Sebastian Mekal <sebitg@gmail.com>
 *
 */
class SendEmailTask extends DefaultTask {
	
	@TaskAction
	void taskAction() {
		def gradle = getProject().getGradle()
		def taskGraph = gradle.getTaskGraph()
		taskGraph.allTasks.each { item ->
			if(("build" == item.name && item.state.executed) || 
				("test" == item.name && item.state.failure!=null) ||
				("compileJava" == item.name && item.state.failure!=null)) {
				sendMail(item.state)
			}
		}
	}
	
	void sendMail(TaskState state) {
		Session session = Session.getInstance( getMailProps() , new javax.mail.Authenticator() {
					
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(project.tutorialArgs.username, 
							project.tutorialArgs.password)
			}
		})
		Message msg = new MimeMessage(session)
		msg.setFrom(new InternetAddress(project.tutorialArgs.username, project.tutorialArgs.from))
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(project.tutorialArgs.mails))
		msg.setSubject("Gradle build status")
		msg.setText( buildMessage(state) )
		Transport.send(msg)
	}
	
	Properties getMailProps() {
		Properties props = new Properties()
		props.put("mail.smtp.host", project.tutorialArgs.host)
		props.put("mail.smtp.port", project.tutorialArgs.port)
		props.put("mail.smtp.auth", project.tutorialArgs.auth)
		props.put("mail.smtp.ssl.enable", project.tutorialArgs.sslEnable)
		props.put("mail.debug", true)
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
		props.put("mail.smtp.socketFactory.fallback", false)
		return props
	}
	
	String buildMessage(TaskState state) {
		StringBuilder builder = new StringBuilder()
		Throwable exception = state.getFailure()
		String status = (exception!=null) ? "FAILURE" : "SUCCESS"
		builder.append("PROJECT: " + getProject().getName() + ", ")
		builder.append("BUILD STATUS: " + status)
		if(exception!=null) {
			builder.append(", EXCEPTION: " + exception.getMessage())
		}
		return builder.toString()
	}

}