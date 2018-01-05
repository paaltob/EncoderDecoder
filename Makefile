all: Test EncoderDecoder

Test:
	javac TestEncoderDecoder.java

EncoderDecoder:
	javac EncoderDecoder.java

clean:
	rm -f *class *~ encoded.txt decoded.txt
