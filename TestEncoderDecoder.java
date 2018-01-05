import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Used for testing the encoder and decoder in EncoderDecoder
 */
public class TestEncoderDecoder {
   
    /**
     * Encodes a file and then decodes the result. 
     * Checks if the resulting file is equal to the original file
     */
    public static void main(String[] args) {
	String orgFile = "testinput.txt";
	String encodedFile = "encoded.txt";
	String decodedFile = "decoded.txt";
	EncoderDecoder encodeDecode = new EncoderDecoder();
	
	System.out.println("\nEncoding " + orgFile + ". The result is stored in " + encodedFile);	
	try {
	    encodeDecode.encode(orgFile, encodedFile);
	}
	catch (FileNotFoundException e) {
	    System.out.println("Something went wrong with finding the original file (" + orgFile + 
			       ") or creating the encoded file (" + encodedFile +")!");
	    e.printStackTrace();
	}
	catch (IOException e) {
	    System.out.println("Something went wrong with either reading the original file (" + orgFile + 
			       ") or writing to the encoded file (" + encodedFile +")!");
	    e.printStackTrace();
	}
	catch (NullPointerException e) {
	    System.out.println("Caught an NullPointerException!");
	    e.printStackTrace();
	}

	System.out.println("\nDecoding " + encodedFile + ". The result is stored in " + decodedFile);
	try{
	    encodeDecode.decode(encodedFile, decodedFile);
	}
	catch (FileNotFoundException e) {
	    System.out.println("Something went wrong with finding the encoded file (" + encodedFile + 
			       ") or creating the decoded file (" + decodedFile + ")!");
	    e.printStackTrace();
	}
	catch (IOException e) {
	    System.out.println("Something went wrong with either reading the encoded file (" + encodedFile +
			       ") or writing to the decoded file (" + decodedFile +")!");
	    e.printStackTrace();
	}
	catch (NullPointerException e) {
	    System.out.println("Caught an NullPointerException!");
	    e.printStackTrace();
	}

	//Checking if the original file and the encoded+decoded file is equal
	try (Scanner orgReader = new Scanner(new File(orgFile), "ISO-8859-1");
	     Scanner decodedReader = new Scanner(new File(decodedFile), "ISO-8859-1");
	     ) {
		
	    //Reads through both files line for line and checks that they are equal. If not the loop breaks
	    String orgLine, decodedLine;
	    while( orgReader.hasNext() && decodedReader.hasNext() ) {
		orgLine = orgReader.nextLine();
		decodedLine = decodedReader.nextLine();
		if(!orgLine.equals(decodedLine) ) {
		    break;
		}
	    }

	    //If one (or both) of the readers have remaining tokens, then the files are not equal
	    if(orgReader.hasNext() || decodedReader.hasNext()) {
		System.out.println("\nFAILURE! The original file and the encoded+decoded file is NOT equal");	    
	    }
	    else {
		System.out.println("\nSUCCESS! The original file and the encoded+decoded file is equal");	    
	    }
	}
	catch(FileNotFoundException e) {
	    System.out.println("Something went wrong with finding the original file (" + orgFile +
			       ") or the decoded file (" + decodedFile +")!");
	    e.printStackTrace();
	}
	catch(IllegalArgumentException e) {
	    System.out.println("The encoding is not found!");
	    e.printStackTrace();
	}
    }
}
