package edu.examples;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * File based iterator which parses a file and breaks it into words using
 * whitespace or a sequence of whitespace characters as delimiter. It expects a
 * file name as an input. Passing a <code>null</code> parameter into the
 * constructor of the <code>FileWordIterator</code> will cause a
 * <code>NullPointerException</code> to be thrown.
 * 
 * <p>
 * <b>Example</b>
 * <p>
 * The text:<br/>
 * <b>HitchHiker's[Space][Space]Guide[Tab][Space]to[Space]the[Space]Galaxy[Space]Intro[NewLine]</b>
 * will be broken into words using the iterator the following way:<br/>
 * <ul>
 * <li>HitchHiker's</li>
 * <li>Guide</li>
 * <li>to</li>
 * <li>the</li>
 * <li>Galaxy</li>
 * <li>Intro</li>
 * </ul>
 * 
 * Note that if a word is spread over two lines using dash symbol '-' it will be
 * interpreted by the iterator as two separate words due to the whitespace
 * symbol (new line). This is a limitation of the class that has to be
 * considered when using it.
 * 
 * @author dkolarova
 *
 */
public final class FileWordIterator implements Iterator<String>, Closeable {

	/**
	 * Regular expression <code>\\s+</code> - matches sequence of one or more
	 * whitespace characters.
	 */
	private static final String WHITE_SPACE_PATTERN = "\\s+";

	/**
	 * Wrapper class used for input source construction and buffering.
	 */
	private BufferedReader sourceReader;

	/**
	 * Buffer containing words/tokens that has been read form the underlying source.
	 * Words are cached after each read operations.
	 */
	private String[] tokensBuffer;

	/**
	 * Pointer for the current position in the tokens buffer
	 */
	private int currentPosition = -1;

	/**
	 * Boolean flag that holds information about the status of the iterator
	 * implementing <code>Closable</code>.
	 */
	private boolean closed = false;

	/**
	 * Constructs a <code>FileWordIterator</code> that returns values scanned from
	 * the specified source delimited by whitespace or a sequence of whitespace
	 * characters.
	 *
	 * @param sourceFileName
	 *            Name of the source file being iterated.
	 * 
	 *            <br/>
	 *            Attempting to pass a <code>null</code> value for the input
	 *            parameter <code>sourceFileName</code> will result in a
	 *            {@link NullPointerException}.
	 * 
	 *            <br/>
	 *            Attempting to pass a name of an non existing file will result in a
	 *            {@link IllegalArgumentException}.
	 */
	public FileWordIterator(String sourceFileName) {
		Objects.requireNonNull(sourceFileName, "Source file name");

		Reader fileReader = null;
		try {
			fileReader = new FileReader(sourceFileName);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Source file is invalid: " + e.getMessage(), e);
		}

		this.sourceReader = new BufferedReader(fileReader);
	}

	/**
	 * Returns {@code true} if the file contains more elements/words. (In other
	 * words, returns {@code true} if {@link #next} would return an element rather
	 * than throwing an exception.)
	 * 
	 * <br/>
	 * Attempting to call method <code>hasNext</code> on a closed iterator will
	 * result in a {@link IllegalStateException}.
	 * 
	 * @return {@code true} if the iteration has more elements/words
	 */
	@Override
	public boolean hasNext() {
		ensureReaderIsOpen();
		if (!hasWordInBuffer()) {
			buffer();
		}

		return hasWordInBuffer();
	}

	/**
	 * Returns the next word in the iteration.
	 * 
	 * <br/>
	 * Inability to read from the underlying source due to internal errors or an
	 * attempt to call method <code>hasNext</code> on a closed iterator will result
	 * in an {@link IllegalStateException}.
	 * 
	 * @return the next word in the iteration
	 * 
	 * @throws NoSuchElementException
	 *             if the iteration has no more elements
	 */
	@Override
	public String next() {
		if (!hasNext()) {
			throw new NoSuchElementException("No words to read");
		}

		String word = tokensBuffer[currentPosition];
		currentPosition++;

		return word;
	}

	/**
	 * Closes the iterator.
	 *
	 * <br/>
	 * If this scanner is already closed then invoking this method will have no
	 * effect.
	 *
	 * <br/>
	 * Attempting to perform search operations after the iterator has been closed
	 * will result in an {@link IllegalStateException}.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
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

	/**
	 * Buffers the next list of words if any.
	 * 
	 * <br/>
	 * Inability to read from the underlying source due to internal errors will
	 * result in an {@link IllegalStateException}.
	 */
	private void buffer() {
		String line = null;
		try {
			line = readLine();
		} catch (IOException e) {
			throw new IllegalStateException("Iteration failed due to an error: " + e.getMessage(), e);
		}

		if (line != null) {
			tokensBuffer = line.split(WHITE_SPACE_PATTERN);
			currentPosition = 0;
		} else {
			tokensBuffer = null;
			currentPosition = -1;
		}
	}

	/**
	 * Reads the next line from the underlying data source.
	 * 
	 * @return Next line from the underlying data source or <code>null</code> if the
	 *         end of the underlying data source has been reached.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	private String readLine() throws IOException {
		String line;

		do {
			line = sourceReader.readLine();
		} while (line != null && line.length() == 0);

		return line;
	}

	/**
	 * Returns {@code true} if the internal words buffer contains more
	 * elements/words.
	 * 
	 * @return {@code true} if the internal words buffer contains more
	 *         elements/words
	 * 
	 */
	private boolean hasWordInBuffer() {
		return tokensBuffer != null && currentPosition > -1 && currentPosition < tokensBuffer.length;
	}

}