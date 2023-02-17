/**
 * 
 */
package junittest;

import static java.util.Objects.nonNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author xgadblt
 *
 */
class JUnitTestBase {
	
	protected void debug (Object msg, Object... args) {
		
		Logger logger = LogManager.getLogger (getClass());
		logger.debug (nonNull (msg)? msg.toString() : "null", args);
	}
}
