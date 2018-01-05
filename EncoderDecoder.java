import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * A class for encoding and decoding files
 *
 * The encoding algorithm copies repeating characters and counts them
 * The copied characters are stored by first writing a control byte that is the count the
 * character is repeated (maximum 127 times) in the first 7 bits, but with the leftmost bit set to 1.
 *
 * Non-repeating characters is stored as they are (maximum 127 at a time), but with an control byte
 * with the count of non-repeating characters in the first 7 bits and the leftmost bit set to 0.
 */ 
public class EncoderDecoder {

    //Used for setting which state the encoder is in
    private enum State {
	COPY, SKIP;
    }


    /**
     * Used to encode an file by the described algorithm
     *
     * @param inFilename The name of the file to be encoded
     * @param outFilename The filename for the encoded file
     * @throws NullPointerException if one (or both) of the filenames are null 
     * @throws FileNotFoundException rethrows from FileOutputStream and FileInputStream
     * @throws IOException rethrows from FileOutputStream and FileInputStream
     */
    public void encode(String inFilename, String outFilename) throws NullPointerException, FileNotFoundException, IOException {
	try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(outFilename));
	     BufferedInputStream reader = new BufferedInputStream(new FileInputStream(inFilename));
	     ) {

	    if( (inFilename == null) || (outFilename == null) ){
		throw new NullPointerException("One (or both) of the filenames is null!");
	    }
		
	    int count = 0;  //Counts the number of copied/skipped characters
	    byte copy; //for storing the character that is copied
	    int cur, next, nextNext; //for reading through the file
	    byte[] skipped = new byte[127]; //Stores the skipped characters
	    State state; //The state of the encoder

	    //Intialize the input variables and copy
	    cur =  reader.read();
	    next = reader.read();
	    nextNext = reader.read();
	    copy = (byte) cur;
	    
	    //Sets the state for either copying or skipping
	    if(cur == next) {
		state = State.COPY;
	    }
	    else {
		state = State.SKIP;
	    }

	
	    //Goes through the original file byte for byte and encodes it
	    while(cur != -1) {	
		if(state == State.COPY) {
		    count++;
		    
		    //If the next letter isn't equal to the current, then it needs to start over with either copying or skipping
		    if(cur != next) {
			writeCopied(count, copy, writer); //Writes the copied letters to file
	
			/* If the next two letters are equal to each other then it will stay in COPY (but with an other copy byte), else
			 * it will start skipping
			 */
			if(next == nextNext) {
			    copy = (byte) next;
			}
			else {
			    state = State.SKIP; //Sets the state to SKIP, since it's now going to skip characters
			}

			count = 0;
		    }
		    
		    /* If the count goes to 127, all the copyied characters are written to file and 
		     * it starts over copying characters
		     */
		    if(count == 127) {
			writeCopied(count, copy, writer); //Writes the copied letters to file
			count = 0;
		    }
		}
		else {
		    skipped[count++] = (byte) cur;
		    
		    //If the next two letters are equal, then it needs to start copying instead
		    if(next == nextNext) {
			writeSkipped(count, skipped, writer); //Writes all the skipped letters to file			
	
			state = State.COPY; //Sets the state to COPY since it's now going to copying characters
			copy = (byte) next; //Sets copy to the correct character
			
			count = 0;
		    }

		    /* If the count goes to 127, all the skipped characters are written to file and 
		     * it starts over skipping characters
		     */
		    if(count == 127) {
			writeSkipped(count, skipped, writer); //Writes all the skipped letters to file
			count = 0;
		    }
		}

		//Shifts the values forward in the variables and reads in the next byte in nextNext
		cur = next; next = nextNext; nextNext = reader.read(); 
	    }
	}
	catch (FileNotFoundException e) {
	    throw e;
	}
	catch (IOException e) {
	    throw e;
	}
	
    }
    

    /**
     * Decodes a file encoded by the described algorithm
     * 
     * @param inFilename The filename of the encoded file
     * @param outFilename The filename of the decoded file
     * @throws FileNotFoundException rethrows from FileOutputStream and FileInputStream
     * @throws IOException rethrows from FileOutputStream and FileInputStream
     */
    public void decode(String inFilename, String outFilename) throws NullPointerException, FileNotFoundException, IOException {
	try (BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(outFilename));
	     BufferedInputStream reader = new BufferedInputStream(new FileInputStream(inFilename));
	     ) {
		
	    if( (inFilename == null) || (outFilename == null) ){
		throw new NullPointerException("One (or both) of the filenames is null!");
	    }

	    
	    /* Reads through the encoded file. Finds the control byte and writes out the 
	     * correct characters depending on the control bit
	     */
	    int count;
	    while( (count = reader.read()) != -1) {
		/* Checks if the leftmost bit is 1.
		 * If it is 1, then sets the leftmost bit to 0 and gets how many times the copy is going to be written
		 * Else the next (count) characters in the encoded file is written
		 */ 
		if( (count & 128) == 128) {
		    count = count ^ 128;
		    byte copy = (byte) reader.read();
		    for(int i = 0; i < count; i++) {
			writer.write(copy);
		    }
		}
		else {
		    for(int i = 0; i < count; i++) {
			writer.write(reader.read());
		    }
		}
	    }
	}
	catch (FileNotFoundException e) {
	    throw e;
	}
	catch (IOException e) {
	    throw e;
	}
	
    }
    
    /* Used by encode() for writing the copied characters to file with an control byte to file.
     * Takes in count, the copied character and a writer
     */
    private void writeCopied(int count, byte copy, BufferedOutputStream writer) throws IOException{
	//sets the leftmost bit to 1
	byte control = (byte) (count ^ 128);
	
	try {
	    writer.write(control);
	    writer.write(copy);
	}	
	catch (IOException e) {
	    throw e;
	}
    }

    /* Used by encode() for writing the skipped characters with an control byte to file.
     * Takes in count, the skipped characters as an array and a writer
     */
    private void writeSkipped(int count, byte[] skipped, BufferedOutputStream writer) throws IOException{
	byte control = (byte) count;
	try {
	    writer.write(control);	
	    writer.write(skipped, 0, count); //writes count bytes from skipped starting from index 0
	}
	catch (IOException e) {
	    throw e;
	}
    }

}
