/**
 * 
 */
package mockitotest;

/**
 * @author xgadblt
 *
 */
public class OtherClass {

	String callMock() {
		
		String expectedValue = String.valueOf ("a");
		TestUtil.debug ("Call of String.valueOf in OtherClass (\"a\") results to " + expectedValue);
		return expectedValue;
	}
}
