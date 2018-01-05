# EncoderDecoder
An encoder and decoder that compress text files

EncoderDecoder.java: The encoder and decoder. Called by two methods, encode() and decode(). Throws exception to the caller. Uses an enum for the two states COPY and and SKIP. It encodes the file by taking repeating characters and stores them as two bytes. The number of repeated characters and the character. Non-repeating characters are stored by first a byte with number of non-repeated characters and then the characters. For both repeated and non-repeating characters there is a maximum of 127. 

TestEncoderDecoder.java: Contains the main-method and tests the encoder and decoder by encoding a file and then decoding it. It then checks that the result is the same as the original file.
