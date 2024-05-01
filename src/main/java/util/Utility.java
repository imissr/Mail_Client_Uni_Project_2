package util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.*;

public final class Utility {

    public static boolean isPositiveInteger(String input){
        return Pattern.compile("^\\d+$").matcher(input).matches();
    }

   /* public static boolean checkIntegerInBounds(int low, int high, String str){
        return Pattern.compile("^[" + low + "-" + high + "]$").matcher(str).matches();
    }*/

    /*public static String concatinateStringFromArray(int low, int high, String[] arr){
        StringBuilder sb = new StringBuilder();
        for (int i = low; i < high; i++) {
            sb.append(arr[i]).append(" ");
        }

        return sb.toString();
    }*/
    
    
    public static Address[] spltitedEmail(String input) throws AddressException{
    	String[] part = input.split(";");
    	 InternetAddress[] list =new InternetAddress[part.length];
    	for (int i = 0; i < part.length; i++) {
    		list[i] = new InternetAddress(part[i]);
		}
    	return list;
    	
    }
    
    
    
    
    public static String charArrayToString(char[] a) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < a.length; i++) {
			sb.append(a[i]);
		}
    	return sb.toString();
    }

    public static Boolean emailCheck(String input){
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matcher(input).matches();
    }
    
//    public static Boolean nameCheck(String input) {
//    	return Pattern.compile("^(.+?)●([^\\s,]+)(,?●(?:[JS]r\\.?|III?|IV))?$").matcher(input).matches();
//    }

   /* public static void clearLine(){
        System.out.print("\r" + "\s".repeat(60) + "\r");
    }*/
    
    
    /*public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
        List<T> mergedList = new ArrayList<>(list1);
        mergedList.addAll(list2);
        return mergedList;
    }

	public static List<Message> mergeLists1(List<Message> list1, List<Message> list2) {
		
		 List<Message> mergedList = new ArrayList<>(list1);
	        mergedList.addAll(list2);
	     
	        return mergedList;
	}*/


    /*public static String cleanEmailAddresses(String emailList) {
        if(emailList.startsWith("mailto"))
        return   emailList.substring(7);
        else
            return emailList;

    }*/

    public static String removeSpecificWord(String input, String wordToRemove) {
        return input.replaceAll(wordToRemove, "");
    }

    public static void removeWordFromFile(String filePath) {
        try {
            byte b;
            RandomAccessFile raf = new RandomAccessFile(new File(filePath), "rw");
            long length = raf.length() - 1;
            do {
                length -= 1;
                raf.seek(length);
                b= raf.readByte();
            } while(b != 10);
            raf.setLength(length+1);
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
    
    

}
