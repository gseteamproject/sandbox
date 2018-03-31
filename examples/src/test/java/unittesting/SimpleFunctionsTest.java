package unittesting;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SimpleFunctionsTest {

	SimpleFunctions simpleFunctions;

	@Before
	public void setUp() {
		simpleFunctions = new SimpleFunctions();
	}

	@Test
	public void compare_less() {
		assertEquals(-1, simpleFunctions.compare(5, 6));
	}

	@Test
	public void compare_greater() {
		assertEquals(1, simpleFunctions.compare(6, 5));
	}

	@Test
	public void compare_equal() {
		assertEquals(0, simpleFunctions.compare(5, 5));
	}
}
