package com.insomnia.testprint;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;




public class DotMatrixPrint {

	/**
	 * @param args
	 * @throws IOException
	 * @throws PrintException 
	 */
	final private static Integer INIT = new Integer(64);
	final private static Integer MASTER_SELECT = new Integer(33);
	final public static Integer PICA = new Integer(80);
	final public static Integer ELITE = new Integer(77);
	final public static Integer COMPRESSED = new Integer(48);
	final public static Integer CONDENSED = new Integer(15);
	final public static Integer EMPHASIZED = new Integer(69);

	public static void main(String[] args) throws IOException, PrintException {

		String[] files = getFileList(".", "txt") ; 
		
		List<File> filelist = new ArrayList<File>() ;
		for (String filename : files) {
			filelist.add(new File(filename));
		}
		byte[] bArr = concatenate(filelist);


		javax.print.DocFlavor flavor = javax.print.DocFlavor.INPUT_STREAM.AUTOSENSE;		
		InputStream finalFile = addPrintCharTo(bArr);
		AttributeSet pras = new HashPrintRequestAttributeSet();

		PrintService printService = getPrintService(flavor, pras,
				"Local");
		if (printService == null) {
			throw new PrintException("Configured Printer doesn't exist.. check runpcs.bat");
		}
		javax.print.DocPrintJob job = printService.createPrintJob();
		JobCompleteMonitor monitor = new JobCompleteMonitor();

		javax.print.attribute.DocAttributeSet das = new javax.print.attribute.HashDocAttributeSet();
 
		javax.print.Doc doc = new javax.print.SimpleDoc(finalFile, flavor, das);
		job.print(doc, (PrintRequestAttributeSet) pras);
		System.out.println("Printing in progress");
		monitor.waitForJobCompletion();
		System.out.println("Finished with dot matrix print");
	}

	public static PrintService getPrintService(javax.print.DocFlavor flavor,
			javax.print.attribute.AttributeSet pras, String printerName) {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService printService = null;
		for (PrintService ps : printServices) {
			System.out.println("Printer = " +  ps.getName());
			if (ps.getName().equals(printerName)) {
				printService = ps;
			}
		}
		return printService;
	}
	private static byte[] concatenate(List<File> files) throws FileNotFoundException, IOException {


	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        for (File file : files) {

	                System.out.println("Processing " + file.getPath() + "... ");
	                BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
	            	String line = br.readLine();
	                while (line != null) {
	                	out.write(line.getBytes());
	                	String newline = System.getProperty("line.separator");

	                	out.write(newline.getBytes());
	                	//out.write(CARRIAGE_RETURN) ;
	                	line = br.readLine();
	                }
	                
	                br.close();
	        }
	        byte[] bArr = out.toByteArray();
	        return bArr ;
		}
	
		private static String[] getFileList(String directory, final String ext) {
			class OnlyCertainFiles implements FilenameFilter {

				public boolean accept(File dir, String name) {
					if (name.endsWith(ext)) return true ; 
					return false ;
				}
				
			}
			return new File(directory).list( new OnlyCertainFiles()) ; 
		}
		private static InputStream addPrintCharTo(byte[]  bArr)
				throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			out.write(INIT);
			//	out.write(CONDENSED);
			//	out.write(PICA);
			out.write(MASTER_SELECT);
			out.write(new Integer(0));
			for (int i = 0; i<bArr.length; i++) {
				out.write(new byte[]{bArr[i]}, 0, 1) ;
			}
			byte[] outArr = out.toByteArray();

			InputStream finalFile = new ByteArrayInputStream(outArr);
			return finalFile;
		}
}


