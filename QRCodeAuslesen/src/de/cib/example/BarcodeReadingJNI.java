package de.cib.example;

import org.json.*;
import swig.jni.de.cib.pdf.CibPdfModuleJobInterface;

public class BarcodeReadingJNI {
	
	/**
	 * Main
	 * @param args
	 */
	public static void main (String[] args) {
		
			// for testing dpi modes (min for testdocuments 96dpi)
			String dpi = "150dpi";

			/*
			 * Test args
			 *
			 */
			String originalStringPath = ".\\input\\"+ args[0];	
			readBarcodePDFM (originalStringPath, "xml",dpi);


			/*
			 * Test UTF-8 Input
			 *
			char a = (char)0x6f;
			char b = (char)0x0308;
			String rawString = "m" + a + b + "g";
			String originalStringPath = ".\\input\\"+ rawString+".pdf";	
			readBarcodePDFM(originalStringPath, "xml",dpi);
			 */ 
			
			/*
			 * Test last known document
			 * 
			 String originalStringPath = ".\\input\\Beitritterkl+Anschr+KSV_GLS_Matrix.pdf";
			 readBarcodePDFM (originalStringPath, "xml",dpi);
			 */
			
			System.out.println ("Ende der Verarbeitung");
						
	}

	/**
	 * Read Barcode with give File and Path
	 * 
	 * @param originalStringPath
	 * @param scaling
	 */
	public static void readBarcodePDFM(String originalStringPath, String outputMode, String scaling) {
		
		// Timestart
		final long timeStart = System.currentTimeMillis();
		
		// Load CIB pdfModule from -Djava.library.path=".\lib\winXX" - 32=32 Bit, 64 = 64 Bit
		loadLibrary(64);
        
		// Create Object
		CibPdfModuleJobInterface CibPdfModule = new CibPdfModuleJobInterface();
		// Set License Informations
		CibPdfModule.setProperty("LicenseCompany", "Fiducia & GAD IT AG - Evaluierungslizenz");
		CibPdfModule.setProperty("LicenseKey", "db8b-5af0-f41b713a");
		
		// Set Parameter
		CibPdfModule.setProperty("BarcodeDetection.ImageScaling", scaling);
		
		// setting output format
		if(outputMode.equals("json"))
			CibPdfModule.setProperty("BarcodeDetection.OutputFormat", "json");
		else
			CibPdfModule.setProperty("BarcodeDetection.OutputFormat", "xml");
		
		// Use only if not in memory
		CibPdfModule.setProperty("BarcodeDetection.OutputFilename", ".\\output\\ausgabe.xml");
		
		// Activate BarcodeDetection (all Barcodes all Pages) Remove not needed Bacdoes, Pages -> improves performance
		CibPdfModule.setProperty("BarcodeDetection.Formats", "Code39;Code93;Code128;DataMatrix;CodeITF;PDF417;QR");
		CibPdfModule.setProperty("BarcodeDetection.PageSelection", "All");
		CibPdfModule.setProperty("BarcodeDetection", "1");
		
		// Alternative BarcodeDetection with Range and Types / other Properties will be ignored / json format
		//CibPdfModule.setProperty("BarcodeDetection", "[{\"PageNumber\": 1, \"Top\": \"0mm\", \"Bottom\": \"297mm\", \"Left\": \"0mm\", \"Right\": \"210mm\", \"Formats\": \"Code39;Code93;Code128;DataMatrix;CodeITF;PDF417;QR\"},{\"PageNumber\": 2, \"Top\": \"0mm\", \"Bottom\": \"297mm\", \"Left\": \"0mm\", \"Right\": \"210mm\", \"Formats\": \"Code39;Code93;Code128;DataMatrix;CodeITF;PDF417;QR\"}]");
		
		// Only for analyse, do not use in production
		//CibPdfModule.setProperty("TraceFilename", ".\\log\\pdf-qr.log");
		
		// Inputfile
		String pdfFile = originalStringPath;
		CibPdfModule.setProperty("InputFilename", pdfFile);
		
		// Execute and Errorhandling
		long error = CibPdfModule.execute();
		if (error != 0) {
		    if (error != 0) {
		        String errorMessage = (String) CibPdfModule.getErrorText();
		        System.out.println(errorMessage);
		    }
		}  

		// Read Property
		String jsonString = (String) CibPdfModule.getProperty("BarcodeDetection.Output");

		// if there is a problem in read barcode, the string is empty or not a json
		try {
			// read json only if outputmode is json
			if(outputMode.equals("json")) {	
				readJson(jsonString);
			}
					
		}
		catch (JSONException ex) {
			if (jsonString.equals(null));
				System.out.println("jsonString is empty - mybe not enoth mem: ");
			
		}

		// Dispose Module and free all handles
        CibPdfModule.delete();
        
        // Time end
        final long timeEnd = System.currentTimeMillis();
        System.out.println("Timeconsumption: " + (timeEnd - timeStart) + " Millisek."); 
	}
	
	/**
	 * loadLibrary by Version of dll
	 * @param bit
	 */
	public static void loadLibrary(int bit) {
		if(bit==64)
			// 64 Bit
			System.loadLibrary("CibPdfModule64");
		else
			// 32 Bit
			System.loadLibrary("CibPdfModule32");
	}
	
	/**
	 * readJson
	 * TODO: Implememnt own usage - here only console output
	 * @param jsonString
	 */
	public static void readJson(String jsonString) {
		// Read json
		JSONObject obj = new JSONObject(jsonString);
		
		// All Obejcts in Array of Bacodes
		JSONArray arr = obj.getJSONArray("Barcodes"); 
		for (int i = 0; i < arr.length(); i++)
		{
			// Read needed Information
		    String barcodeFormat = arr.getJSONObject(i).getString("Format");
		    String barcodeContent = arr.getJSONObject(i).getString("Content");
		    int barcodePageNumber = arr.getJSONObject(i).getInt("PageNumber");

		    // Print Informations to console
		    //System.out.println(barcodeFormat);
		    //System.out.println(barcodeContent);
		    //System.out.println(barcodePageNumber);
		    
		}
		
		// Read Barcode count
		int n = obj.getInt("Count");
		System.out.println("Found "+n+" Barcode(s) in selected Pages from Document");
		
	}
}