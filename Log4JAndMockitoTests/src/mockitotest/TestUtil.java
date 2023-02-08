/**
 * 
 */
package mockitotest;

/**
 * @author xgadblt
 *
 */
public class TestUtil {

	public static void debug (Object msg, Object... args) {
		System.out.printf (msg != null ? msg.toString() : "null", args);
		System.out.println();
	}
}
