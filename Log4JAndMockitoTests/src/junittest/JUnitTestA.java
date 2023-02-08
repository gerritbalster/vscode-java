/**
 * 
 */
package junittest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author gerrit
 */
class JUnitTestA extends JUnitTestBase {

	@Tag ("JUnitTestA_test1")
	@Test
	void test1() {
		debug ("JUnitTestA_test1");
		// Verwendung einer Methodenreferenz f�hrt hier zu Nullpointer-Exception
		assertThrows (NullPointerException.class, () -> ((String) null).chars());
	}
	
	@Tag ("JUnitTestA_test2")
	@Test
	void test2() {
		debug ("JUnitTestA_test2");
		// Verwendung einer Methodenreferenz f�hrt hier zu Nullpointer-Exception
		assertThrows (IllegalArgumentException.class, () -> ((String) null).chars());
	}

	@Tag ("JUnitTestA_test3")
	@Test
	void test3() {
		debug ("JUnitTestA_test3");
		// Verwendung einer Methodenreferenz f�hrt hier zu Nullpointer-Exception
		assertThrows (IllegalArgumentException.class, () -> ((String) null).chars());
	}

}
