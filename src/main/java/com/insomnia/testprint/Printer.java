package com.insomnia.testprint;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.rev6.scf.ScpDownload;
import org.rev6.scf.ScpFile;
import org.rev6.scf.SshConnection;
import org.rev6.scf.SshException;

import com.pacytology.pcs.Utils;

public class Printer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting dot matrix print");
		javax.print.DocFlavor flavor = javax.print.DocFlavor.INPUT_STREAM.AUTOSENSE;
		
		List<Byte> fileContents = new ArrayList<Byte>();

//		fileContents.add(new Byte((byte)0x1D));
//		fileContents.add(new Byte((byte)0x21));
//		fileContents.add(new Byte((byte)1));
		File file = getFile( System.getProperty("java.io.tmpdir"), "/u01/reports", "DEC2012.uns");
		byte[] bArr = new byte[fileContents.size()];
		int i = 0;
		for (Byte b : fileContents) {
			bArr[i] = fileContents.get(i);
			i++;
		}
		InputStream finalFile = new ByteArrayInputStream(bArr);
		javax.print.attribute.PrintRequestAttributeSet pras = new javax.print.attribute.HashPrintRequestAttributeSet();

		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		PrintService printService = null;
		for (PrintService ps : printServices) {
			if (ps.getName().equals("Epson DFX-5000+")) {
				printService = ps;
			}
		}
		if (printService == null) {
			throw new PrintException("Configured Printer doesn't exist.. check runpcs.bat");
		}
		javax.print.DocPrintJob job = printService.createPrintJob();

		javax.print.attribute.DocAttributeSet das = new javax.print.attribute.HashDocAttributeSet();
		
		
		javax.print.Doc doc = new javax.print.SimpleDoc(finalFile, flavor, das);
		job.print(doc, pras);
		System.out.println("Finished with dot matrix print");

	}
	public static File getFile(String localPath, String remotePath, String fileName) {

		String host = "192.168.1.110";
		String username = "oracle";
		String password = "Sa1vation";

		SshConnection ssh = null;
		OutputStream out = null ; 

		try {
			ssh = new SshConnection(host, username, password);
			ssh.setPort(Integer.parseInt("47294"));
			ssh.connect();
			ScpDownload download = new ScpDownload(
        			new ScpFile(new File(localPath + fileName),  remotePath + fileName)
        				);
			ssh.executeTask(download);
			File file = new File(localPath + fileName);
			if (file != null && file.isFile()) return file;
			else throw new FileNotFoundException();	
		} catch (FileNotFoundException fnf){

			fnf.printStackTrace();
		} catch (SshException e) {
			
			e.printStackTrace();
		} finally {
			if (ssh != null) {
				ssh.disconnect();
			}
		}
		return null;
		
	}

}
