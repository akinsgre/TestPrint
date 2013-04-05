package com.insomnia.testprint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
	public static void main(String[] args) throws IOException, PrintException {
		javax.print.DocFlavor flavor = javax.print.DocFlavor.INPUT_STREAM.AUTOSENSE;
		File file = new File("ppr_clm_test");
		InputStream fileio = new FileInputStream(file);
		
		System.out.println("Printing from " + file.getAbsolutePath());

		List<Byte> fileContents = new ArrayList<Byte>();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024*16];
		int size = 0;
		out.write(new Byte((byte) 0x1D));
		//out.write(new Byte((byte) 0x21));
		//out.write(new Byte((byte) 1));
		while ((size = fileio.read(buffer)) != -1) {
			out.write(buffer, 0, size) ;
		}
		byte[] bArr = out.toByteArray();
//		fileContents.add(new Byte((byte) 0x1D));
//		fileContents.add(new Byte((byte) 0x21));
//		fileContents.add(new Byte((byte) 1));
		// for each byte in file
//		byte x = 0;
//		while (x > -1) {
//			x = (byte) fileio.read();
//			fileContents.add(x);
//		}
//		fileio.close();

		//byte[] bArr = new byte[fileContents.size()];
//		byte[] bArr = fileio.toString().getBytes();
//		int i = 0;
//		for (Byte b : fileContents) {
//			bArr[i] = fileContents.get(i);
//			i++;
//		}
		InputStream finalFile = new ByteArrayInputStream(bArr);
		AttributeSet pras = new HashPrintRequestAttributeSet();

		PrintService printService = getPrintService(flavor, pras,
				"Epson DFX-5000+");
		if (printService == null) {
			throw new PrintException("Configured Printer doesn't exist.. check runpcs.bat");
		}
		javax.print.DocPrintJob job = printService.createPrintJob();

		javax.print.attribute.DocAttributeSet das = new javax.print.attribute.HashDocAttributeSet();

		javax.print.Doc doc = new javax.print.SimpleDoc(finalFile, flavor, das);
		job.print(doc, (PrintRequestAttributeSet) pras);
		System.out.println("Finished with dot matrix print");
	}

	public static PrintService getPrintService(javax.print.DocFlavor flavor,
			javax.print.attribute.AttributeSet pras, String printerName) {
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService printService = null;
		for (PrintService ps : printServices) {
			if (ps.getName().equals(printerName)) {
				printService = ps;
			}
		}
		return printService;
	}
}


