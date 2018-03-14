package edu.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

public class FileWordIteratorTest {

	private static final String EMPTY_TEXT_FILE = "Empty.txt";
	
	private static final String MISSING_TEXT_FILE = "SomeFile8789.txt";

	private static final String SIMPLE_TEXT_FILE = "HitchHiker.txt";

	private static final String SIMPLE_TEXT_FILE_EXACT_MATCH = "HitchHikerExactMatch.txt";

	private static final String LARGE_TEXT_FILE = "HitchHikersGuide.txt";

	private static final int SIMPLE_TEXT_FILE_WORDS_COUNT = 160;

	private static final int LARGE_TEXT_FILE_WORDS_COUNT = 47086;

	@Test(expected = NullPointerException.class)
	public void testIteratorConstructionWithNullAsInputFileName() {
		try (FileWordIterator fileWordIterator = new FileWordIterator(null)) {

		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIteratorConstructionWithNonExistingFile() {
		try (FileWordIterator fileWordIterator = new FileWordIterator(MISSING_TEXT_FILE)) {

		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}
	}

	@Test
	public void testIterateSimpleData() {
		int count = 0;
		try (FileWordIterator fileWordIterator = new FileWordIterator(
				FileWordIteratorTest.class.getResource(SIMPLE_TEXT_FILE).getFile())) {

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
		try (FileWordIterator fileWordIterator = new FileWordIterator(
				FileWordIteratorTest.class.getResource(SIMPLE_TEXT_FILE_EXACT_MATCH).getFile())) {

			while (fileWordIterator.hasNext()) {
				String word = fileWordIterator.next();
				words.add(word);
			}
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}

		List<String> expectedWordsList = loadRowsIntoList();

		Assert.assertTrue(expectedWordsList.equals(words));
	}

	@Test(expected = NoSuchElementException.class)
	public void testIterateSimpleDataAfterEndReached() {
		try (FileWordIterator fileWordIterator = new FileWordIterator(
				FileWordIteratorTest.class.getResource(SIMPLE_TEXT_FILE).getFile())) {

			while (fileWordIterator.hasNext()) {
				fileWordIterator.next();
			}

			fileWordIterator.next();
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testHasNextSimpleDataAfterIteratorClosed() {
		try (FileWordIterator fileWordIterator = new FileWordIterator(
				FileWordIteratorTest.class.getResource(SIMPLE_TEXT_FILE).getFile())) {

			fileWordIterator.close();
			fileWordIterator.hasNext();
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testIterateSimpleDataAfterIteratorClosed() {
		try (FileWordIterator fileWordIterator = new FileWordIterator(
				FileWordIteratorTest.class.getResource(SIMPLE_TEXT_FILE_EXACT_MATCH).getFile())) {

			while (fileWordIterator.hasNext()) {
				fileWordIterator.close();
				fileWordIterator.next();
			}
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}
	}

	@Test
	public void testIterateLargeData() {
		int count = 0;
		try (FileWordIterator fileWordIterator = new FileWordIterator(
				FileWordIteratorTest.class.getResource(LARGE_TEXT_FILE).getFile())) {

			while (fileWordIterator.hasNext()) {
				fileWordIterator.next();
				count++;
			}
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}

		Assert.assertEquals(LARGE_TEXT_FILE_WORDS_COUNT, count);
	}

	@Test
	public void testHasNextOnEmptyFile() {
		try (FileWordIterator fileWordIterator = new FileWordIterator(
				FileWordIteratorTest.class.getResource(EMPTY_TEXT_FILE).getFile())) {

			Assert.assertFalse(fileWordIterator.hasNext());
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}
	}

	@Test(expected = NoSuchElementException.class)
	public void testIterateOnEmptyFile() {
		try (FileWordIterator fileWordIterator = new FileWordIterator(
				FileWordIteratorTest.class.getResource(EMPTY_TEXT_FILE).getFile())) {

			fileWordIterator.next();
		} catch (IOException ioEx) {
			Assert.assertTrue(ioEx.getMessage(), false);
		}
	}

	private List<String> loadRowsIntoList() {
		List<String> words = new ArrayList<>();
		words.addAll(Arrays.asList("HitchHiker's", "Guide", "to", "the", "Galaxy", "Intro", "Far", "out", "in", "the",
				"uncharted", "backwaters", "of", "the", "unfashionable", "end", "of", "the", "western", "spiral", "arm",
				"of", "the", "Galaxy", "lies", "a", "small", "unregarded", "yellow", "sun."));

		return words;
	}

}
