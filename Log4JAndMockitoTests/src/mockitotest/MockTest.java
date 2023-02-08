/**
 * 
 */
package mockitotest;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author xgadblt
 *
 */
@RunWith (PowerMockRunner.class)
@PrepareForTest ({String.class, PowerMockTest.class})
@PowerMockIgnore ("jdk.internal.reflect.*") // Damit es auch mit Java 11 l�uft
public class MockTest {

	//	@Ignore
	@Test
	public void powerMockStaticMethod() {
		
		final String expectedValue = "xyz";
		
		PowerMockito.mockStatic (String.class);
		Mockito.when (String.valueOf ("a")).thenReturn (expectedValue);
		
		String mockValue = String.valueOf ("a");
		TestUtil.debug ("Call of String.valueOf (\"a\") results to " + mockValue);
		
		// Pr�ft, ob die Methode 1x aufgerufen wurde
		PowerMockito.verifyStatic (String.class);
		
		assertEquals (expectedValue, mockValue);
	}
	
	@Ignore
	@Test
	public void powerMockPrivateMethod() throws Exception {
		
		final String expectedValue = "xyz";
		
		PowerMockTest mockTest = PowerMockito.spy (new PowerMockTest());
		PowerMockito.doReturn (expectedValue).when (mockTest, "getValue");
		
		String mockValue = mockTest.getValue();
		TestUtil.debug ("Call of mockTest.getValue() results to mocked value " + mockValue);
		
		// Pr�ft, ob die Methode 1x aufgerufen wurde
		PowerMockito.verifyPrivate(mockTest).invoke("getValue");
		
		assertEquals (expectedValue, mockValue);
	}

	@Ignore
	@Test
	public void powerMockStaticField() {
		
//		LogLevel levelBeforeMock = LogLevel.DEBUG;
//		mockStaticField (LogLevel.class, "DEBUG", "FINEST");
//		debug (levelBeforeMock + " is now " + LogLevel.DEBUG);
		
		double piBeforeMock = Math.PI;
		mockStaticField (Math.class, "PI", "E");
		double pi = Math.PI;
		TestUtil.debug (piBeforeMock + " is now " + pi);
		
	}

	@Ignore
	@Test
	public void powerMockStaticMethodInOtherClass() {
		
		final String expectedValue = "xyz";
		
		PowerMockito.mockStatic (String.class);
		Mockito.when (String.valueOf ("a")).thenReturn (expectedValue);
		
		String mockValue = new OtherClass().callMock();
		
		// Pr�ft, ob die Methode 1x aufgerufen wurde
		PowerMockito.verifyStatic (String.class);
		
		// Mock zieht ausserhalb dieser Klasse nicht
		assertNotEquals (expectedValue, mockValue);
	}

	private void mockStaticField (Class<?> classToMock, String nameFieldToMock, String mockValue) {
		
		try {
			// Feld, welches gemockt werden soll
			Field fieldToMock = classToMock.getDeclaredField (nameFieldToMock);
			
			// FINAL-Modifier wegnehmen, sonst kann man das Feld nicht �ndern
			Field modifiersField = Field.class.getDeclaredField ("modifiers");
			modifiersField.setAccessible (true);
			modifiersField.setInt (fieldToMock, fieldToMock.getModifiers() & ~Modifier.FINAL);
			
			// Feld, auf welches gemockt werden soll aus der Mockclass holen und dann
			// in das zu mockende Feld schreiben 
			Field mockedField = classToMock.getDeclaredField (mockValue);
			fieldToMock.set (null, mockedField.get(null));
			
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
}
