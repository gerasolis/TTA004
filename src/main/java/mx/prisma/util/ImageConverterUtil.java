package mx.prisma.util;

import org.apache.commons.codec.binary.Base64;

public class ImageConverterUtil {
	public static byte[] decodeByteArrayB64( byte[] bImage) {
		Base64 decoder = new Base64();
		byte[] bImageDecod = null;
		if(bImage != null) {
			bImageDecod = decoder.decode(bImage);
		} 
		return bImageDecod;
	}

	public static byte[] encodeByteArrayB64(byte[] bImagen) {
		Base64 encoder = new Base64();
		byte[] bImageEncod = null;
		if(bImagen != null) {
			bImageEncod = encoder.encode(bImagen);
		} 
		
		return bImageEncod;
	}
	public static byte[] parsePNGB64StringToBytes(String string) {
		if(string != null && !string.equals("")) {
			int index = string.indexOf("base64") + 7;
			return string.substring(index).getBytes();
		} 
		return null;
	}

	public static String parseBytesToPNGB64String(byte[] bytes) {
		String string = null;
		if(bytes != null) {
			string = new String(bytes);
			string = "data:image/png;base64,".concat(string);
		} 
		
		return string;
	}
	public static String parseBytesToB64String(Object obj) {
		String string = null;
		try { 
			byte[] bytes = (byte[])obj;
			if(bytes != null) {
				string = new String(bytes);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
}
