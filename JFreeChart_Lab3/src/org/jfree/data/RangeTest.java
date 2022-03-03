package org.jfree.data;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.*;

public class RangeTest {
    private Range exampleRange;
    private Range higherRange;
    private Range extraneousMaxRange;
    private Range extraneousMinRange;
    private Range refRange;
    private Range testRange;
    private Range smallestRange;
	private Range nanRange;

    @BeforeClass public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception { 
    	exampleRange = new Range(-1, 1);
        higherRange = new Range(2,4);
        extraneousMaxRange = new Range(1, Double.MAX_VALUE);
        extraneousMinRange = new Range(-Double.MIN_VALUE, 1);
    	refRange = new Range (-8.0, 16.0);
    	smallestRange = new Range(0.0, Double.MIN_VALUE);
    }

	@Test(expected = IllegalArgumentException.class)
	public void rangeConstructorLowerBoundGreaterThanUpper() {
		testRange = new Range(1, -1);
	}

	@Test
	public void constrainValueAboveUpper() {
		assertEquals("The constrained value should be 1.0", 1.0, exampleRange.constrain(2.0), .000000001d);
	}

	@Test
	public void constrainValueBelowLower() {
		assertEquals("The constrained value should be -1.0", -1.0, exampleRange.constrain(-3.0), .000000001d);
	}

	@Test
	public void constrainContainsValue() {
		assertEquals("The constrained value should be 0.5", 0.5, exampleRange.constrain(0.5), .000000001d);
	}

	@Test
	public void combineNullAndValidRange() {
		testRange = Range.combine(null, exampleRange);
		assertEquals("The upper bound should be 1", 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
	}

	@Test
	public void combineNullAndInvalidRange() {
		testRange = Range.combine(higherRange, null);
		assertEquals("The upper bound should be 4", 4, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 4", 2, testRange.getLowerBound(), .000000001d);
	}

	@Test
	public void combineValidRanges() {
		testRange = Range.combine(exampleRange, higherRange);
		assertEquals("The upper bound should be 4", 4, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
	}

	@Test
	public void combineIgnoreNaNNullAndValidRange() {
		testRange = Range.combineIgnoringNaN(null, exampleRange);
		assertEquals("The upper bound should be 1", 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
	}

	@Test
	public void combineIgnoreNaNNullAndNanRange() {
		nanRange = new Range(Double.NaN, Double.NaN);
		testRange = Range.combineIgnoringNaN(null, nanRange);
		assertNull("The test range should be null", testRange);
	}

	@Test
	public void combineIgnoreNaNValidRangeAndNull() {
		testRange = Range.combineIgnoringNaN(higherRange, null);
		assertEquals("The upper bound should be 4", 4, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 4", 2, testRange.getLowerBound(), .000000001d);
	}

	@Test
	public void combineIgnoreNaNNaNAndNullRange() {
		nanRange = new Range(Double.NaN, Double.NaN);
		testRange = Range.combineIgnoringNaN(nanRange, null);
		assertNull("The test range should be null", testRange);
	}

	@Test
	public void combineIgnoreNaNValidRanges() {
		nanRange = new Range(Double.NaN, Double.NaN);
		testRange = Range.combineIgnoringNaN(exampleRange, higherRange);
		assertEquals("The upper bound should be 4", 4, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
	}

	@Test
	public void combineIgnoreNaNNaNRanges() {
		nanRange = new Range(Double.NaN, Double.NaN);
		testRange = Range.combineIgnoringNaN(nanRange, nanRange);
		assertNull("The test range should be null", testRange);
	}

	@Test
	public void expandUpperAboveLower() {
		testRange = Range.expand(higherRange, 2, 4);
		assertEquals("The upper bound should be 12.0", 12, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -2", -2, testRange.getLowerBound(), .000000001d);

	}

	@Test
	public void expandUpperBelowLower() {
		testRange = Range.expand(exampleRange, 4, -6);
		assertEquals("The upper bound should be -10", -10, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -10", -10, testRange.getLowerBound(), .000000001d);

	}

    @Test
    public void expandToIncludeValidRangeAboveRange() {
        Range testRange = Range.expandToInclude(exampleRange, 2);
        assertEquals("The upper bound should be 2", 2, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void expandToIncludeValidRangeBelowRange() {
        Range testRange = Range.expandToInclude(exampleRange, -2);
        assertEquals("The upper bound should be 1", 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -2", -2, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void expandToIncludeValidRangeInRange() {
        Range testRange = Range.expandToInclude(exampleRange, 0);
        assertEquals("The upper bound should be 1", 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void expandToIncludeValidRangeUpperBound() {
        Range testRange = Range.expandToInclude(exampleRange, 1);
        assertEquals("The upper bound should be 1", 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void expandToIncludeValidRangeLowerBound() {
        Range testRange = Range.expandToInclude(exampleRange, -1);
        assertEquals("The upper bound should be 1", 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
    }
    
    @Test
    public void expandToIncludeNullRangeAboveRange() {
        Range testRange = Range.expandToInclude(null, 2);
        assertEquals("The upper bound should be 2", 2, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 2", 2, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void expandToIncludeValidRangeLargeExtraneousValue() {
        Range testRange = Range.expandToInclude(exampleRange, Double.MAX_VALUE);
        assertEquals("The upper bound of the range should be " + Double.toString(Double.MAX_VALUE), Double.MAX_VALUE, testRange.getUpperBound(), .000000001d);
    }

    @Test
    public void shiftValidRangeGreaterThanZero() {
        Range testRange = Range.shift(higherRange, 2, false);
        assertEquals("The upper bound should be 6", 6, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 4", 4, testRange.getLowerBound(), .000000001d);
    }
    
    @Test
    public void shiftValidRangeGreaterThanZeroTwoParams() {
        Range testRange = Range.shift(higherRange, 2, false);
        assertEquals("The upper bound should be 6", 6, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 4", 4, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void shiftValidRangeEqualsZero() {
        Range testRange = Range.shift(exampleRange, 0, false);
        assertEquals("The upper bound should be 1", 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void shiftValidRangeLessThanZero() {
        Range testRange = Range.shift(higherRange, -1, false);
        assertEquals("The upper bound should be 3", 3, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 1", 1, testRange.getLowerBound(), .000000001d);
    }

	@Test
	public void shiftValidRangeGreaterThanZeroIncludesZeroBounds() {
		Range testRange = Range.shift(smallestRange, 1, false);
		assertEquals("The upper bound should be " + Double.toString(Double.MIN_VALUE + 1), Double.MIN_VALUE + 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 1", 1, testRange.getLowerBound(), .000000001d);
	}

	@Test
	public void shiftValidRangeGreaterThanZeroWithZeroCrossing() {
		Range testRange = Range.shift(exampleRange, 2, true);
		assertEquals("The upper bound should be 3.0", 3.0, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 1.0", 1.0, testRange.getLowerBound(), .000000001d);
	}

    @Test(expected = IllegalArgumentException.class)
    public void shiftNullRangeLessThanZero() {
        Range testRange = Range.shift(null, -2, false);
    }
    
    @Test
    public void shiftValidRangeMinNegativeDoubleValue() {
        Range testRange = Range.shift(exampleRange, -1 * Double.MIN_VALUE, false);
        assertEquals("The upper bound should be -1", -1, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void shiftValidRangeMaxDoubleValue() {
        Range testRange = Range.shift(exampleRange, Double.MAX_VALUE, false);
        assertEquals("The upper bound should be " + Double.toString(Double.MAX_VALUE + 1), Double.MAX_VALUE + 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 0", 0, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void shiftValidRangeCrossesZero() {
        Range testRange = Range.shift(exampleRange, 2, false);
        assertEquals("The upper bound should be 3", 3, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be 0", 0, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void scaleValidRangeGreaterThanOne() {
        Range testRange = Range.scale(exampleRange, 2);
        assertEquals("The upper bound should be 2", 2, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -2", -2, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void scaleValidRangeEqualsOne() {
        Range testRange = Range.scale(exampleRange, 1);
        assertEquals("The upper bound should be 1", 1, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -1", -1, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void scaleValidRangeLessThanOne() {
        Range testRange = Range.scale(exampleRange, 0.5);
        assertEquals("The upper bound should be 0.5", 0.5, testRange.getUpperBound(), .000000001d);
        assertEquals("The lower bound should be -0.5", -0.5, testRange.getLowerBound(), .000000001d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scaleValidRangeLessThanZero() {
        Range testRange = Range.scale(exampleRange, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void scaleNullRangeLessThanOne() {
        Range testRange = Range.scale(null, 2);
    }

    @Test
    public void scaleExtraneousMaxRangeGreaterThanOne() {
        Range testRange = Range.scale(extraneousMaxRange, 2);
        assertTrue("The upper bound of the range should be infinity", Double.isInfinite(testRange.getUpperBound()));
    }

    @Test
    public void scaleExtraneousMinRangeLessThanOne() {
        Range testRange = Range.scale(extraneousMinRange, 0.5);
        assertEquals("The lower bound of the range should be " + Double.toString(Double.MIN_VALUE / 2), Double.MIN_VALUE / 2, testRange.getLowerBound(), .000000001d);
    }

    @Test
    public void scaleValidRangeByMaxDouble() {
        Range testRange = Range.scale(exampleRange, Double.MAX_VALUE);
        assertEquals("The upper bound of the range should be " + Double.toString(Double.MAX_VALUE), Double.MAX_VALUE, testRange.getUpperBound(), .000000001d);
    }

    @Test
    public void scaleValidRangeByMinDouble() {
        Range testRange = Range.scale(exampleRange, Double.MIN_VALUE);
        assertEquals("The upper bound of the range should be " + Double.toString(Double.MIN_VALUE), Double.MIN_VALUE, testRange.getUpperBound(), .000000001d);
    }

	@Test
	public void intersectsWithinBoundaryNotTouchingBoundaryDoubles() {
		assertTrue("The range from -4.0 to 8.0 intersects the range from -8 to 16.", refRange.intersects(-4.0, 8.0));
	}

	@Test
	public void intersectsWithinBoundaryTouchingBoundaryDoubles() {
		assertTrue("The range from -8.0 to 16.0 intersects the range from -8.0 to 16.0",
				refRange.intersects(-8.0, 16.0));
	}

	@Test
	public void intersectsAboveUpperBoundaryAndBelowLowerBoundaryNotTouchingBoundaryDoubles() {
		assertTrue("The range from -28.0 to 36.0 intersects the range from -8.0 to 16.0",
				refRange.intersects(-8.0, 16.0));
	}

	@Test
	public void intersectsBelowLowerBoundaryAndWithinBoundaryDoubles() {
		assertTrue("The range from -14.0 to 2.0 intersects the range from -8.0 to 16.0",
				refRange.intersects(-14.0, 2.0));
	}

	// BUG: Should pass, because the test range overlaps with refRange at -8.0, but
	// the test fails instead
	@Test
	public void intersectsOnlyTouchingLowerBoundaryDoubles() {
		assertTrue("The range from -14 to -8 intersects the range from -8.0 to 16.0", refRange.intersects(-14.0, -8.0));
	}

	@Test
	public void intersectsFullyBelowLowerBoundaryNotTouchingBoundaryDoubles() {
		assertFalse("The range from -43.0 to -16.0 does not intersect the range from -8.0 to 16.0",
				refRange.intersects(-43.0, -16.0));
	}

	@Test
	public void intersectsAboveUpperBoundaryAndWithinBoundaryDoubles() {
		assertTrue("The range from 3.0 to 28.0 intersects the range from -8.0 to 16.0", refRange.intersects(3.0, 28.0));
	}

	// BUG: Should pass, because the test range overlaps with refRange at 16.0, but
	// the test fails instead
	@Test
	public void intersectsOnlyTouchingUpperBoundaryDoubles() {
		assertTrue("The range from 16.0 to 32.0 intersects the range from -8.0 to 16.0",
				refRange.intersects(16.0, 32.0));
	}

	@Test
	public void intersectsFullyAboveUpperBoundaryNotTouchingBoundaryDoubles() {
		assertFalse("The range from 17.0 to 19.0 does not intersect the range from -8.0 to 16.0",
				refRange.intersects(17.0, 19.0));
	}

	@Test
	public void interesectsMinDoubleValToMaxDoubleValDoubles() {
		assertTrue(
				"The range from MIN_VALUE for double to MAX_VALUE for double intersects with the range from -8.0 to 16.0",
				refRange.intersects(Double.MIN_VALUE, Double.MAX_VALUE));
	}

	@Test
	public void intersectSmallestPossibleRangeDoubles() {
		assertTrue("The range from 0.0 to MIN_VALUE for double intersects with the range from -8.0 to 16.0",
				refRange.intersects(0.0, Double.MIN_VALUE));
	}

	@Test
	public void intersectsWithinBoundaryNotTouchingBoundaryRangeArg() {
		testRange = new Range(2.0, 5.0);
		assertTrue("The range from 2.0 to 5.0 intersects the range from -8 to 16.", refRange.intersects(testRange));
	}

	@Test
	public void intersectsWithinBoundaryTouchingBoundaryRangeArg() {
		testRange = new Range(-8.0, 16.0);
		assertTrue("The range from -8.0 to 16.0 intersects the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	@Test
	public void intersectsAboveUpperBoundaryAndBelowLowerBoundaryNotTouchingBoundaryRangeArg() {
		testRange = new Range(-9.5, 17.5);
		assertTrue("The range from -9.5 to 17.5 intersects the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	@Test
	public void intersectsBelowLowerBoundaryAndWithinBoundaryRangeArg() {
		testRange = new Range(-10.0, 14.2);
		assertTrue("The range from -10.0 to 14.2 intersects the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	// BUG: Should pass, because the test range overlaps with refRange at -8.0, but
	// the test fails instead
	@Test
	public void intersectsOnlyTouchingLowerBoundaryRangeArg() {
		testRange = new Range(-45.0, -8.0);
		assertTrue("The range from -45.0 to -8.0 intersects the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	@Test
	public void intersectsFullyBelowLowerBoundaryNotTouchingBoundaryRangeArg() {
		testRange = new Range(-1004.0, -16.2);
		assertFalse("The range from -1004.0 to -16.2 does not intersect the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	@Test
	public void intersectsAboveUpperBoundaryAndWithinBoundaryRangeArg() {
		testRange = new Range(0.03, 17.7);
		assertTrue("The range from 0.03 to 17.7 intersects the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	// BUG: Should pass, because the test range overlaps with refRange at 16.0, but
	// the test fails instead
	@Test
	public void intersectsOnlyTouchingUpperBoundaryRangeArg() {
		testRange = new Range(16.0, 23.0);
		assertTrue("The range from 16.0 to 23.0 intersects the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	@Test
	public void intersectsFullyAboveUpperBoundaryNotTouchingBoundaryRangeArg() {
		testRange = new Range(200.75, 302.0);
		assertFalse("The range from 200.75 to 302.0 does not intersect the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	@Test
	public void intersectsMinDoubleValToMaxDoubleValRangeArg() {
		testRange = new Range(Double.MIN_VALUE, Double.MAX_VALUE);
		assertTrue(
				"The range from MIN_VALUE for double to MAX_VALUE for double intersects with the range from -8.0 to 16.0",
				refRange.intersects(testRange));
	}

	@Test
	public void intersectsInBoundsReverseValues() {
		assertFalse("Range intersects but provided range is invalid", higherRange.intersects(3, 2));
	}

	@Test
	public void intersectSmallestPossibleRangeArgs() {
		assertTrue("The range from 0.0 to MIN_VALUE for double intersects with the range from -8.0 to 16.0",
				refRange.intersects(smallestRange));
	}

	@Test
	public void containsBelowLowerBoundary() {
		assertFalse("The value -22. is not contained within the range from -8.0 to 16.0", refRange.contains(-22.0));
	}

	@Test
	public void containsOnLowerBoundary() {
		assertTrue("The value -8.0 is contained within the range from -8.0 to 16.0", refRange.contains(-8.0));
	}

	@Test
	public void containsBetweenUpperAndLowerBoundary() {
		assertTrue("The value 0.0 is contained within the range from -8.0 to 16.0", refRange.contains(0.0));
	}

	@Test
	public void containsOnUpperBoundary() {
		assertTrue("The value 16.0 is contained within the range from -8.0 to 16.0", refRange.contains(16.0));
	}

	@Test
	public void containsAboveUpperBoundary() {
		assertFalse("The value 99.0 is not contained within the range from -8.0 to 16.0", refRange.contains(99.0));
	}

	@Test
	public void containsMaxDoubleValue() {
		assertFalse("The maximum value of a double is not contained within the range from -8.0 to 16.0",
				refRange.contains(Double.MAX_VALUE));
	}

	@Test
	public void containsMinDoubleValue() {
		assertTrue("The minimum value of a double is contained within the range from -8.0 to 16.0",
				refRange.contains(Double.MIN_VALUE));
	}

	@Test
	public void getLengthTwo() {
		assertEquals("The length of the range should be " + 2.0, 2.0, exampleRange.getLength(), .000000001d);
	}

	@Test
	public void getCentralValueZero() {
		assertEquals("The central value of the range should be " + 0, 0, exampleRange.getCentralValue(), .000000001d);
	}

	@Test
	public void equalsInvalidRange() {
		String test = "testing";
		assertFalse("The comparator range is not an object of type range", exampleRange.equals(test));
	}

	@Test
	public void equalsSameUpperDifferentLower() {
		testRange = new Range(-2, 1);
		assertFalse("The ranges have different lower bounds", exampleRange.equals(testRange));
	}

	@Test
	public void equalsDifferentUpperSameLower() {
		testRange = new Range(-1, 2);
		assertFalse("The ranges have different upper bounds", exampleRange.equals(testRange));
	}

	@Test
	public void equalsSameUpperSameLower() {
		testRange = new Range(-1, 1);
		assertTrue("The ranges have the same bounds", exampleRange.equals(testRange));
	}

	@Test
	public void isNanRangeValidLowerInvalidUpper() {
		nanRange = new Range(-1, Double.NaN);
		assertFalse("The range only has an upper value that is NaN", nanRange.isNaNRange());
	}

	@Test
	public void isNanRangeInvalidLowerValidUpper() {
		nanRange = new Range(Double.NaN, 1);
		assertFalse("The range only has an lower value that is NaN", nanRange.isNaNRange());
	}

	@Test
	public void isNanRangeInvalidLowerInvalidUpper() {
		nanRange = new Range(Double.NaN, Double.NaN);
		assertTrue("The range has an upper and lower value that is NaN", nanRange.isNaNRange());
	}

	@Test
	public void isNanRangeValidLowerValidUpper() {
		assertFalse("The range has an upper value that is NaN", exampleRange.isNaNRange());
	}

	@Test
	public void toStringKnownRange() {
		String test = exampleRange.toString();
		assertEquals("Should return 'Range[-1.0,1.0]'", test.toString(), "Range[-1.0,1.0]");
	}

	@After
	public void tearDown() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
}
