package matf.msnr.tests;

import android.test.AndroidTestCase;

public class TestCasees extends AndroidTestCase {

    public void testMultiplication() {
        assertEquals(9, multiply(3, 3));
        assertEquals(0, multiply(3, 0));
        assertEquals(6, multiply(3, 2));
    }

    public void testAddition() {
        assertEquals(6, add(3, 3));
        assertEquals(3, add(3, 0));
        assertEquals(5, add(3, 2));
    }

    public void testSubtraction() {
        assertEquals(0, subtract(3, 3));
        assertEquals(3, subtract(3, 0));
        assertEquals(1, subtract(3, 2));
    }

    private static int multiply(int a, int b) {
        return a * b;
    }

    private static int add(int a, int b) {
        return a + b;
    }

    private static int subtract(int a, int b) {
        return a - b;
    }
}
