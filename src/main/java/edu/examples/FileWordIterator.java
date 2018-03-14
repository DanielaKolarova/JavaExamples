package edu.examples;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class FileWordIterator implements Iterator<String>, Closeable {

	private static final String WHITE_SPACE_PATTERN = "\\s+";

	private BufferedReader sourceReader;

	private String[] tokensBuffer;

	private int currentPosition;

	private boolean closed = false;

	public FileWordIterator(InputStream source) {
		Objects.requireNonNull(source, "source");
		this.sourceReader = new BufferedReader(new InputStreamReader(source));
	}

	@Override
	public boolean hasNext() {
		ensureReaderIsOpen();
		if (!hasWordInBuffer()) {
			buffer();
		}

		return hasWordInBuffer();
	}

	@Override
	public String next() {
		ensureReaderIsOpen();

		if (!hasNext()) {
			throw new NoSuchElementException("No words to read");
		}

		String word = tokensBuffer[currentPosition];
		currentPosition++;

		return word;
	}

	/**
	 * Closes this scanner.
	 *
	 * <p>
	 * If this scanner has not yet been closed then if its underlying
	 * {@linkplain java.lang.Readable readable} also implements the
	 * {@link java.io.Closeable} interface then the readable's <tt>close</tt> method
	 * will be invoked. If this scanner is already closed then invoking this method
	 * will have no effect.
	 *
	 * <p>
	 * Attempting to perform search operations after a scanner has been closed will
	 * result in an {@link IllegalStateException}.
	 * 
	 * @throws IOException
	 *
	 */
	public void close() throws IOException {
		if (!closed) {
			sourceReader.close();
		}

		sourceReader = null;
		closed = true;
		tokensBuffer = null;
		currentPosition = -1;
	}

	/**
	 * Throws {@link IllegalStateException} if the source reader is closed.
	 */
	private void ensureReaderIsOpen() {
		if (closed)
			throw new IllegalStateException("File based word iterator closed");
	}

	private void buffer() {
		String line = null;
		try {
			line = readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (line != null) {
			tokensBuffer = line.split(WHITE_SPACE_PATTERN);
			currentPosition = 0;
		} else {
			tokensBuffer = null;
			currentPosition = -1;
		}
	}

	private String readLine() throws IOException {
		String line;

		do {
			line = sourceReader.readLine();
		} while (line != null && line.length() == 0);

		return line;
	}

	private boolean hasWordInBuffer() {
		return tokensBuffer != null && (currentPosition < tokensBuffer.length);
	}

}