/**
 * 
 */
package junittest;

import static java.util.Objects.nonNull;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author xgadblt
 *
 */
class JUnitTestBase {
	
	protected void debug (Object msg, Object... args) {
		
		Logger logger = Logger.getLogger (getClass().getName());
		logger.log (Level.FINE, nonNull (msg)? msg.toString() : "null", args);
	}
}
