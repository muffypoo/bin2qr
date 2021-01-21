/**
 * 
 */
package com.kensoft.bin2qr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * @author ken_kum
 *
 */
public class BinaryToQR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args == null || args.length < 1) {
			System.out.println("Usage: BinaryToQR [e|d] [input_filename]");
			System.out.println("e - encode provided file into QR code image in PNG format");
			System.out.println("d - decode provided file while removing the .b45 extension");
			return;
		}
		
		String op = args[0];
		String inFile = args[1];
		String outFormat = "png";
		
		if(op == null) op = "e";
		
		if(op.equals("e")) {
			try {
				byte[] bytes = FileUtils.readFileToByteArray(new File(inFile));
				String inputStr = "";
				inputStr = BinaryToBase45Encoder.encodeToBase45QrPayload(bytes);
				BitMatrix bm = generateQRCodeImage(inputStr);
				MatrixToImageWriter.writeToFile(bm, outFormat, new File(inFile+"."+outFormat));
			} catch (IOException e) {
				System.err.println("Unable to read file: [" + inFile + "]");
			} catch (Exception e) {
				System.err.println("Error generating image: " + e.getMessage());
				e.printStackTrace();
			}
		} else if(op.equals("d")) {
			try {
				String inputStr = "";
				inputStr = FileUtils.readFileToString(new File(inFile), Charset.defaultCharset());
				byte[] bytes = BinaryToBase45Encoder.decodeBase45QrPayload(inputStr);
				FileUtils.writeByteArrayToFile(new File(inFile.replace(".b45", "")), bytes);
			} catch (IOException e) {
				System.err.println("Unable to read file: [" + inFile + "]");
			}
		}
	}

	private static BitMatrix generateQRCodeImage(String barcodeText) throws Exception {
	    QRCodeWriter barcodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 1000, 1000);
	    return bitMatrix;
	}
}
