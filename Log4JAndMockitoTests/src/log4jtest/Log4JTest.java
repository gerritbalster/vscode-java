/**
 * 
 */
package log4jtest;

import static java.lang.String.format;

import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.Test;

/**
 * @author xgadblt
 */
public class Log4JTest {

	private static final String
		LINE = "%d Lange, lange, lange %szeile, um das Log sehr, sehr, sehr, sehr, sehr, sehr, sehr, sehr, voll zu schreiben.";
	
	private static final Logger
		LOGGER = LogManager.getLogger ("logger"),
		PROTOCOL = LogManager.getLogger ("protocol");

	@Test
	public void writeLogfile() {
		// modifyAppender();
		IntStream.range (0, 10).mapToObj (Integer::valueOf).forEach (i -> {
			LOGGER.info (format (LINE, i, "Log"));
			PROTOCOL.info (format (LINE, i, "Protokoll"));
		});
	}
	
	private void modifyAppender() {
		
		LoggerContext ctx = (LoggerContext) LogManager.getContext();
		Configuration conf = ctx.getConfiguration();
		Appender oldAppender = conf.getAppender ("PROTOCOL");
		
		PatternLayout patternLayout = PatternLayout.newBuilder()
			.withConfiguration (conf)
			.withPattern ("%d{---dd.MM.yyyy HH:mm:ss,SSS} %.50m%n")
			.build();
		
		FileAppender fileAppender = FileAppender.newBuilder()
			.withConfiguration(conf)
			.withName (oldAppender.getName())
			.withFileName("./logfile.log")
			.withLayout(patternLayout)
			.build();
		
		conf.addAppender (fileAppender);
//		conf.
		
		/*
		ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
		AppenderComponentBuilder appender = builder.newAppender ("PROTOCOL", "File");
		LayoutComponentBuilder layoutBuilder = builder.newLayout ("Pattern");
		appender.add (layoutBuilder);
		debug ("Appender is now " + appender.getName());
		
		Appender appender = PROTOCOL.getAppender ("PROTOCOL");
		appender.setLayout (new PatternLayout ("%d{---dd.MM.yyyy HH:mm:ss,SSS} %.50m%n"));
		 */
	}

	private void debug (Object msg, Object... args) {
		System.out.printf (msg != null ? msg.toString() : "null", args);
		System.out.println();
	}
}
