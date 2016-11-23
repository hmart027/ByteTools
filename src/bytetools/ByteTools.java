package bytetools;

import java.nio.ByteBuffer;

/* Has implementations to convert to Integer
 * Float
 * from byte arrays
 */

public class ByteTools {
	
	/**
	 * Gets an internal array from a byte array. Start point is inclusive and end point is exclusive.
	 * @param start inclusive start point
	 * @param end exclusive end point
	 * @return sub array
	 */
	public static byte[] subArray(byte[] array, int start, int end){
		int length = end-start;
		if ( length < 0) return null;
		byte[] newArray = new byte[length];
		for(int i = 0; i<length; i++){
			newArray[i] = array[i+start];
		}
		return newArray;
	}
	
	public static byte[] reverseArray(byte[] array){
		if ( (array==null) || (array.length < 0)) return null;
		int length = array.length;
		byte[] newArray = new byte[length];
		for(int i = 0 ; i<length; i++ ){
			newArray[i] = array[length - 1-i];
		}
		return newArray;
	}
	
	/**
	 * This method creates a 32bit integer from and array. It gets an inclusive start byte and the number of bytes in the integer. The Array should 
	 * be arranged with the less significant byte first.
	 * @param info	array containing the Integer
	 * @param start	position of the first byte
	 * @param length number of bytes for the integer
	 * @param signed whether the integer is signed or unsigned
	 * @return 32bit Integer
	 */
	public static int byteArray2Integer(byte[] info,int start, int length, boolean signed) {
		
		byte[] newArray = new byte[4];
		for (int i=0; i< length; i++)
			newArray[i]=info[start+length-1-i];

		int finalv;
		if(signed)
			finalv = newArray[0];
		else
			finalv = newArray[0] & 0x00FF;
		for (int i = 1; i < length; i++) {
			finalv = (finalv << 8) | (newArray[i] & 0x00FF);
		}
		return finalv;
	}
	
	/** The Array should be arranged with the most 
	 *  significant byte first.
	 */
	public static float byteArray2Float(int start, byte[] info) {
		float finalv = 0;
		int sign = (info[start + 0] & 0x080) >> 7;
		byte[] mantisa = { (byte) (info[start + 1] & 0X07F), info[start + 2],
				info[start + 3] };
		short exponent =(short)(((short)((info[start + 0] << 1) | ((info[start + 1] >> 7) & 0x01)) & 0x00FF) - 127);
		finalv = (float) (1*Math.pow(2,exponent));
		for (int x = 0; x < 3; x++) {
			finalv += (float) ((mantisa[x] & 0x0FF) * Math.pow(2,
					exponent - x*8 - 7));
		}
		if (sign != 0)
			finalv = finalv * (-1);
		return finalv;
	}
	
	/** The Array should be arranged with the most 
	 *  significant byte first.
	 */
	public static float charArray2Float(int start, char[] info) {
		float finalv = 0;
		int sign = (info[start + 0] & 0x080) >> 7;
		char[] mantisa = { (char) (info[start + 1] & 0X07F), info[start + 2],
				info[start + 3] };
		short exponent =(short)(((short)((info[start + 0] << 1) | ((info[start + 1] >> 7) & 0x01)) & 0x00FF) - 127);
		finalv = (float) (1*Math.pow(2,exponent));
		for (int x = 0; x < 3; x++) {
			finalv += (float) ((mantisa[x] & 0x0FF) * Math.pow(2,
					exponent - x*8 - 7));
		}
		if (sign != 0)
			finalv = finalv * (-1);
		return finalv;
	}
	
	/**
	 * Requires 64 bits; 8 bytes
	 * @param start -start point in the byte array
	 * @param info -byte array to extract the double
	 * @return a double floating point value
	 */
	public static double byteArray2Double(int start, byte[] info) {
		double finalv = 0;
		int sign = (info[start] & 0x00080) >> 7;
		byte[] mantisa = { (byte) ((info[start + 1] & 0x00F)), info[start + 2],
				info[start + 3], info[start + 4], info[start + 5], info[start + 6], info[start + 7]};
		int exponent = (((int)(info[start]&0x07F))<<4 | (int)((info[start+1]>>4) & 0x0F))-1023;
		finalv = 1*Math.pow(2,exponent);
		for (int x = 0; x < 7; x++) {
			finalv += (double) ((mantisa[x] & 0x0FF) * Math.pow(2,
					exponent - x*8 - 4));
		}
		if (sign != 0)
			finalv = finalv * (-1);		
		return finalv;
	}
	
	public static byte [] float2ByteArray (float value, boolean leastSigFirst){  
		byte[] o = ByteBuffer.allocate(4).putFloat(value).array();
		if(leastSigFirst) o = reverseArray(o); 
	    return o;
	}
	
	public static byte [] double2ByteArray (double value, boolean leastSigFirst){  
		byte[] o = ByteBuffer.allocate(8).putDouble(value).array();
		if(leastSigFirst) o = reverseArray(o); 
	    return o;
	}

	public static byte [] integer2ByteArray (int value, boolean leastSigFirst){  
		byte[] o =  ByteBuffer.allocate(4).putInt(value).array();
	    if(leastSigFirst) o = reverseArray(o); 
		return o;
	}
	
	public static byte [] integer2ByteArray (int value, int size, boolean leastSigFirst){  
	     byte[] t = ByteBuffer.allocate(4).putInt(value).array();
	     byte[] o = new byte[size];
	     for(int i = 0; i < size ; i++){
	    	 o[size-1-i] = t[3-i];
	     }
	     if(leastSigFirst) o = reverseArray(o); 
	     return o;
	}
}