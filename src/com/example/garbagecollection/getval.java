package com.example.garbagecollection;
import java.io.IOException;

import java.io.UnsupportedEncodingException;

public class getval {

	//static String text_value = "QEE_";
	static String text_value = "Supervisor_Quantum";

	public static void main(String args[]) {

		for (int i = 1; i <= 10; i++) {

			data(text_value + i);

			try {

				Thread.sleep(100);

			} catch (InterruptedException e) {

				// TODO Auto-generated catch block

				e.printStackTrace();

			}

		}
		
		//data("Supervisor_Quantum");//H4sIAAAAAAAAAAsuLUgtKssszi+KDyxNzCspzQUAoUe3BRIAAAA=

		/*
		 * H4sIAAAAAAAAAAt0dY03BADjtzd1BQAAAA==
		 * 
		 * H4sIAAAAAAAAAAt0dY03AgBZ5j7sBQAAAA==
		 * 
		 * H4sIAAAAAAAAAAt0dY03BgDP1jmbBQAAAA==
		 * 
		 * H4sIAAAAAAAAAAt0dY03AQBsQ10FBQAAAA==
		 * 
		 * try {
		 * 
		 * String string= "H4sIAAAAAAAAAAt0dY03NDAwAAAhySiaCAAAAA==";
		 * 
		 * byte[] datadecode = Base64.decode(string);
		 * 
		 * String textString = new String(datadecode, "UTF-8");
		 * 
		 * System.out.println("String"+textString);
		 * 
		 * } catch (UnsupportedEncodingException e) {
		 * 
		 * // TODO Auto-generated catch block
		 * 
		 * e.printStackTrace();
		 * 
		 * } catch (IOException e) {
		 * 
		 * // TODO Auto-generated catch block
		 * 
		 * e.printStackTrace();
		 * 
		 * }
		 */

	}

	static void data(String text) {

		byte[] data;

		try {

			data = text.getBytes("UTF-8");

			String base64 = Base64.encodeBytes(data, 455);

			// System.out.println("Bytes:"+base64);

			byte[] datadecode = Base64.decode(base64);

			String textString = new String(datadecode, "UTF-8");

			System.out.println(text+"\t\t"+base64);

		} catch (UnsupportedEncodingException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

	}

}