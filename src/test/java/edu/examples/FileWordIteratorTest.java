package edu.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FileWordIteratorTest {

	private static final String SIMPLE_TEXT_FILE = "HitchHiker.txt";
	
	private static final String LARGE_TEXT_FILE = "HitchHikersGuide.txt";

	private static final int SIMPLE_TEXT_FILE_WORDS_COUNT = 160;
	
	private static final int LARGE_TEXT_FILE_WORDS_COUNT = 47086;

	@Before
	public void setUp() {

	}

	@Test
	public void testIterateSimpleData() {
		int count = 0;
		InputStream inputStream = FileWordIteratorTest.class.getResourceAsStream(SIMPLE_TEXT_FILE);
		try (FileWordIterator fileWordIterator = new FileWordIterator(inputStream)) {

			while (fileWordIterator.hasNext()) {
				fileWordIterator.next();
				count++;
			}
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}

		Assert.assertEquals(SIMPLE_TEXT_FILE_WORDS_COUNT, count);
	}
	
	@Test
	public void testIterateSimpleDataExactWordsMatch() {
		List<String> words = new ArrayList<>();
		InputStream inputStream = FileWordIteratorTest.class.getResourceAsStream(SIMPLE_TEXT_FILE);
		try (FileWordIterator fileWordIterator = new FileWordIterator(inputStream)) {

			while (fileWordIterator.hasNext()) {
				String word = fileWordIterator.next();
				words.add(word);
			}
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}

		Assert.assertEquals(SIMPLE_TEXT_FILE_WORDS_COUNT, words.size());
	}

	@Test
	public void testIterateLargeData() {
		int count = 0;
		InputStream inputStream = FileWordIteratorTest.class.getResourceAsStream(LARGE_TEXT_FILE);
		try (FileWordIterator fileWordIterator = new FileWordIterator(inputStream)) {

			while (fileWordIterator.hasNext()) {
				System.out.println(fileWordIterator.next());
				count++;
			}
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}

		Assert.assertEquals(LARGE_TEXT_FILE_WORDS_COUNT, count);
	}
	
	@Test(expected = NullPointerException.class)
	public void testIterateNonExistingFile() {
		int count = 0;
		InputStream inputStream = FileWordIteratorTest.class.getResourceAsStream("someFile8789.txt");
		try (FileWordIterator fileWordIterator = new FileWordIterator(inputStream)) {

			while (fileWordIterator.hasNext()) {
				fileWordIterator.next();
				count++;
			}
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}

		Assert.assertEquals(SIMPLE_TEXT_FILE_WORDS_COUNT, count);
	}



}
