/**
 * 
 */
package junittest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author gerrit
 */
class JUnitTestB {

	@Tag ("JUnitTestB_test1")
	@Test
	void test1() {
		// Verwendung einer Methodenreferenz führt hier zu Nullpointer-Exception
		assertThrows (NullPointerException.class, () -> ((String) null).chars());
		fail ("JUnitTestB_test1 failed");
	}
	
	@Tag ("JUnitTestB_test2")
	@Test
	void test2() {
		// Verwendung einer Methodenreferenz führt hier zu Nullpointer-Exception
		assertThrows (IllegalArgumentException.class, () -> ((String) null).chars());
		fail ("JUnitTestB_test2 failed");
	}

	@Tag ("JUnitTestB_test3")
	@Test
	void test3() {
		// Verwendung einer Methodenreferenz führt hier zu Nullpointer-Exception
		assertThrows (IllegalArgumentException.class, () -> ((String) null).chars());
		fail ("JUnitTestB_test3 failed");
	}

}
