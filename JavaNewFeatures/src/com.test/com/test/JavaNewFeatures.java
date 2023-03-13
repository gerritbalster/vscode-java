/**
 * 
 */
package com.test;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EdECPoint;
import java.security.spec.EdECPublicKeySpec;
import java.security.spec.NamedParameterSpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import jdk.jfr.Configuration;
import jdk.jfr.consumer.RecordingStream;

/**
 * @author gerrit b
 */
public class JavaNewFeatures {

	private static final String ALGORITHM = "Ed25519";

	/******************************************************************************
	 * Java 9			
	 */
	@Test
	void testAssert() throws Exception {

		// TODO org.harmcrest.core als Module?
//		assertThat (Integer.valueOf( 100 ), Is.is (Integer.valueOf( 100 )));
		assertSame( Integer.valueOf( 100 ), Integer.valueOf( 100 ) );
	}

	@Test
	void testIfPresent() throws Exception {

		Optional<String> opt = Optional.of( "Hallo" );
		opt.ifPresentOrElse( System.out::println, () -> System.out.println( "Nicht da" ) );
		opt = Optional.empty();
		opt.ifPresentOrElse( System.out::println, () -> System.out.println( "Nicht da" ) );
	}

	@Test
	void testImmutableList() throws Exception {

		List<String> list = List.of( "Peter", "Franz", "Heinz", "Sophie" );
		list.forEach( System.out::println );
		assertThrows( UnsupportedOperationException.class, list::clear );

		Files.write( Paths.get( "names.txt" ), list, StandardOpenOption.CREATE );
	}

	@Test
	void testThread() {
		new Thread( () -> System.out.println( "ok" ) ).run();
	}

	@Test
	void testExecutor() {

		// Task mit 3s Verz�gerung
		Executor ex = CompletableFuture.delayedExecutor( 3L, TimeUnit.SECONDS );
		ex.execute( () -> System.out.println( "r1" ) );
		try {
			// 4s warten, bis der verz�gerte Task gestartet wurde
			Thread.sleep( 4000L );
		} catch ( InterruptedException e1 ) {
			e1.printStackTrace();
		}

		// Der hier startet sofort
		CompletableFuture<?> cf = CompletableFuture.runAsync( () -> System.out.println( "r2" ) );

		try {
			cf.get();
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		} catch ( ExecutionException e ) {
			e.printStackTrace();
		}
	}

	/******************************************************************************
	 * Java 10
	 */
	@Test
	void testVar() {

		var list = Arrays.asList( "Dies", "ist", "ein", "Array", "von", "Strings" );
		debug( list );
	}

	/******************************************************************************
	 * Java 11
	 */
	@Test
	void testRepeat() {

		var list = Arrays.asList( "Dies", "ist", "ein", "Array", "von", "Strings" );
		debug( list.stream().collect( Collectors.joining() ).repeat( 3 ) );
	}

	@Test
	void testIsBlank() {

		var textNotBlank = "Ist nicht blank";
		debug( textNotBlank.isBlank() );

		var textIsBlank = "      ";
		debug( textIsBlank.isBlank() );
	}

	@Test
	void testLines() {

		var multiLines = "Hat mehrere\nZeilen";
		multiLines.lines().forEach( this::debug );
	}

	/******************************************************************************
	 * Java 12 (yield ab Java 13)
	 */
	@Test
	void testSwitch() {

		var list = Arrays.asList( "Dies", "ist", "ein", "Array", "von", "Strings" );
		var str = list.get( new Random().nextInt( list.size() ) );

		String switchRetVal = switch ( str ) {
			case "Dies", "ist", "ein" -> {
				String ret = "Erster Fall: " + str;
				yield ret;
			}
			case "Array", "von", "Strings" -> {
				String ret = "Zweiter Fall: " + str;
				yield ret;
			}
			default -> throw new IllegalArgumentException( "Unexpected value: " + str );
		};
		debug( switchRetVal );
	}

	/******************************************************************************
	 * Java 13
	 */
	@Test
	void testMultilineString() {

		String multi = """
							1. Zeile
				0			2. Zeile
				1			3. Zeile
						   "4. Zeile"
						""";

		debug( multi );
	}

	/******************************************************************************
	 * Java 14
	 */
	@Test
	void testInstancOfWithoutCast() {

		Object test = "    ";
		if ( test instanceof String s && s.isBlank() )
			debug( "String hat nur blanks" );
	}

	private record TestRecord( String name, int age ) {
	}

	@Test
	void testRecords() {
		TestRecord record = new TestRecord( "Anne", 33 );
		debug( "name = {0}, age = {1}", record.name(), record.age() );
	}

	@Test
	void testFlightRecorder() throws Exception {

		Configuration c = Configuration.getConfiguration( "default" );
		try ( var rs = new RecordingStream(c) ) {
			
			rs.enable( "jdk.CPULoad" ).withPeriod( Duration.ofSeconds( 1 ) );
			rs.onEvent( "jdk.CPULoad", event -> debug( format( "%.1f%%", event.getFloat( "machineTotal" ) * 100 ) ) );
			rs.setEndTime( Instant.now().plusSeconds( 5L ) );
			rs.start();
		}
		try ( var rs = new RecordingStream(c) ) {

			rs.onEvent( "jdk.JVMInformation", this::debug );
			rs.setEndTime( Instant.now().plusSeconds( 1L ) );
			rs.start();
		}
	}
	
	/******************************************************************************
	 * Java 15
	 * sealed/non-sealed war hier noch "preview"
	 */
	private sealed class SealedClass permits SubClass {
		
		int a = 1;

		public int getA() {
			return a;
		}
	}

	private non-sealed class SubClass extends SealedClass {
	}

	/*
	 * non compilable weil nicht in der SealedClass permitted
	 * 
	private non-sealed class SubClass2 extends SealedClass {
	}
	 */

	@Test
	void testSealedClass() throws Exception {
		debug( new SubClass().getA() );
	}
	
	@Test
	void testSignWithKey() throws Exception {
		
		// example: generate a key pair and sign
		KeyPairGenerator kpg = KeyPairGenerator.getInstance (ALGORITHM);
		KeyPair kp = kpg.generateKeyPair();
		// algorithm is pure Ed25519
		Signature sig = Signature.getInstance (ALGORITHM);
		sig.initSign (kp.getPrivate());
		byte[] msg = new byte[] {23,55,23,11};
		sig.update (msg );
		byte[] s = sig.sign();
		HexFormat hexFormat = HexFormat.ofDelimiter (" ");
		debug ("sig: {0}", hexFormat.formatHex(s));
		 
		// example: use KeyFactory to contruct a public key
		KeyFactory kf = KeyFactory.getInstance ("EdDSA");
		boolean xOdd = false;
		BigInteger y = BigInteger.valueOf (938475934875937453L);
		NamedParameterSpec paramSpec = new NamedParameterSpec (ALGORITHM);
		EdECPublicKeySpec pubSpec = new EdECPublicKeySpec (paramSpec, new EdECPoint (xOdd, y));
		PublicKey pubKey = kf.generatePublic (pubSpec);
		
		debug ("pubKey: {0}", pubKey);
	}
	
	/******************************************************************************
	 * Java 18 (Java 16 hat nur Finalisierungen von Previews, f�r Java 17 ben�tigt man die --preview-Option.
	 * Die aktuelle Eclipse-Version 4.24 kann aber nur --preview f�r Java 18 setzen.)
	 */
	@SuppressWarnings("preview")
	@Test
	void testInstanceOfWithPatternMatch() throws Exception {
		
		List.of ("abc", 1, 'c', Long.MAX_VALUE).forEach (o -> { 
			switch (o) {
				case String s -> debug ("String {0}", s);
				case  Integer i -> debug ("Integer {0}", i);
				case Character c -> debug ("Character {0}", c);
				default -> debug ("Something else");
			}
		});
	}
	
	private void debug( Object msg, Object... args ) {

		Logger logger = Logger.getLogger( getClass().getName() );
		logger.log( Level.FINE, nonNull( msg ) ? msg.toString() : "null", args );
		// System.out.println (String.format (nonNull( msg ) ? msg.toString() : "null", args));
	}

}
