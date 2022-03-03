package org.jfree.data;

import static org.junit.Assert.*;

import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues2D;
import org.jfree.data.KeyedValues;
import org.jfree.data.Range;
import org.jfree.data.Values2D;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jfree.data.DefaultKeyedValue;
import org.jmock.*;

public class DataUtilitiesTest extends DataUtilities {

	@Test(expected = IllegalArgumentException.class)
	public void createNumberArray2d_NullValue() {
		double[][] testArray = null;
		
		DataUtilities.createNumberArray2D(testArray);
	}
	
    @Test
    public void createNumberArray2D_EmptyArray() {
        double[][] testArray = new double[][] {};
        
        assertArrayEquals("The created Number Array is empty.",testArray, DataUtilities.createNumberArray2D(testArray));
    }

    @Test
    public void createNumberArray2D_ValidArray() {
        double[][] testArray = new double[][] {{1.1, 12, 6.77}, {25, 65.4, 22.22}};
        
        assertArrayEquals("The created Number Array contains {{1.1, 12, 6.77}, {25, 65.4, 22.22}}.",testArray, DataUtilities.createNumberArray2D(testArray));
    }

    @Test
    public void createNumberArray2D_ExtremeValue() {
        double[][] testArray = new double [][] {{Double.MAX_VALUE, Double.MAX_VALUE}, {Double.MAX_VALUE, Double.MAX_VALUE}};
        
        assertArrayEquals("The created Number Array contains extreme values."
        		,testArray, DataUtilities.createNumberArray2D(testArray));
    }
    
    @Test
    public void createNumberArray2D_SmallNegativeValue() {
        double[][] testArray = new double [][] {{Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE}, 
        	{Double.MIN_VALUE, Double.MIN_VALUE}};
        
        assertArrayEquals("The created Number Array contains very small negative values."
        		,testArray, DataUtilities.createNumberArray2D(testArray));
    }
    
    @Test
    public void createNumberArray2D_NaNValue() {
        double[][] testArray = new double [][] {{Double.NaN, Double.NaN, Double.NaN}, 
        	{Double.NaN, Double.NaN}};
        
        assertArrayEquals("The created Number Array contains NaN values."
        		,testArray, DataUtilities.createNumberArray2D(testArray));
    }
    
    @Test
    public void createNumberArray2D_PositiveInf() {
        double[][] testArray = new double [][] {{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, 
        	{Double.POSITIVE_INFINITY}};
        
        assertArrayEquals("The created Number Array contains positive infinity values."
        		,testArray, DataUtilities.createNumberArray2D(testArray));
    }
    
    @Test
    public void createNumberArray2D_NegativeInf() {
        double[][] testArray = new double [][] {{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}, 
        	{Double.NEGATIVE_INFINITY}};
        
        assertArrayEquals("The created Number Array contains negative infinity values."
        		,testArray, DataUtilities.createNumberArray2D(testArray));
    }
    
    //-----------------------------------------------------------------------------------------------------------------------------//
    
    @Test
    public void calculateRowTotal_NullDataValidRow() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(3));
                one(values).getValue(0, 0);
                will(returnValue(null));
                one(values).getValue(0, 1);
                will(returnValue(null));
                one(values).getValue(0, 2);
                will(returnValue(null));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals("The calculated total is 0.0",result, 0.0, .000000001d);
    }
    
    @Test
    public void calculateRowTotal_ValidDataValidRow() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(3));
                one(values).getValue(0, 0);
                will(returnValue(5.67));
                one(values).getValue(0, 1);
                will(returnValue(5.2));
                one(values).getValue(0, 2);
                will(returnValue(1.2));
                one(values).getValue(1, 0);
                will(returnValue(1.7));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 0);
        assertEquals("The calculated total is 12.07",result, 12.07, .000000001d);
    }
    
    @Test
    public void calculateRowTotal_ValidDataExtremeValuesValidRow() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(2));
                one(values).getValue(2, 0);
                will(returnValue(Double.MAX_VALUE));
                one(values).getValue(2, 1);
                will(returnValue(Double.MAX_VALUE));
                one(values).getValue(2, 2);
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 2);
        assertEquals("The calculated total is Double.MAX_VALUE+Double.MAX_VALUE",result, (Double.MAX_VALUE+Double.MAX_VALUE), .000000001d);
    }

    @Test
    public void calculateRowTotal_ValidDataNegativeValueValidRow() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(3));
                one(values).getValue(1, 0);
                will(returnValue(Double.MIN_VALUE));
                one(values).getValue(1, 1);
                will(returnValue(Double.MIN_VALUE));
                one(values).getValue(1, 2);
                will(returnValue(Double.MIN_VALUE));
            }
        });
        double result = DataUtilities.calculateRowTotal(values, 1);
        assertEquals("The calculated total is Double.MIN_VALUE+Double.MIN_VALUE+Double.MIN_VALUE"
        		,result, (Double.MIN_VALUE+Double.MIN_VALUE+Double.MIN_VALUE), .000000001d);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateRowTotal_ValidDataInvalidRowAboveUpperBoundary() {
    	DefaultKeyedValues2D test = new DefaultKeyedValues2D();
    	test.addValue(1, 0, 0);
        Values2D values = test;
        
        DataUtilities.calculateRowTotal(values, 5);
    }
    

    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateRowTotal_ValidDataInvalidRowBelowLowerBoundary() {
    	DefaultKeyedValues2D test = new DefaultKeyedValues2D();
    	test.addValue(1, 0, 0);
        Values2D values = test;
        
        DataUtilities.calculateRowTotal(values, -2);
    }
    
    //-----------------------------------------------------------------------------------------------------------------------------//
    
    @Test
    public void calculateRowTotal_ValidDataValidRowValidColumns() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(3));
                one(values).getRowCount();
                will(returnValue(3));
                one(values).getValue(0, 0);
                will(returnValue(4));
                one(values).getValue(1, 1);
                will(returnValue(5));
                one(values).getValue(1, 2);
                will(returnValue(9));
            }
        });
        int[] columns = {1, 2};
        double result = DataUtilities.calculateRowTotal(values, 1, columns);
        assertEquals("The calculated total is 14.0",result, 14.0, .000000001d);
    }
    
    @Test
    public void calculateRowTotal_NullDataValidRowValidColumns() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(3));
                one(values).getRowCount();
                will(returnValue(3));
                one(values).getValue(0, 0);
                will(returnValue(null));
                one(values).getValue(0, 1);
                will(returnValue(null));
                one(values).getValue(0, 2);
                will(returnValue(null));
            }
        });
        int[] columns = {0, 1, 2};
        double result = DataUtilities.calculateRowTotal(values, 0, columns);
        assertEquals("The calculated total is 0.0",result, 0.0, .000000001d);
    }
    
    @Test
    public void calculateRowTotal_ExtremeDataValidRowValidColumns() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(3));
                one(values).getRowCount();
                will(returnValue(3));
                one(values).getValue(0, 0);
                will(returnValue(Double.MAX_VALUE));
                one(values).getValue(0, 1);
                will(returnValue(Double.MAX_VALUE));
            }
        });
        int[] columns = {0, 1};
        double result = DataUtilities.calculateRowTotal(values, 0, columns);
        assertEquals("The calculated total is Double.MAX_VALUE+Double.MAX_VALUE",result, Double.MAX_VALUE+Double.MAX_VALUE, .000000001d);
    }
    
    @Test
    public void calculateRowTotal_SmallNegativeDataValidRowValidColumns() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getColumnCount();
                will(returnValue(3));
                one(values).getRowCount();
                will(returnValue(3));
                one(values).getValue(2, 0);
                will(returnValue(Double.MIN_VALUE));
                one(values).getValue(2, 1);
                will(returnValue(Double.MIN_VALUE));
            }
        });
        int[] columns = {0, 1};
        double result = DataUtilities.calculateRowTotal(values, 2, columns);
        assertEquals("The calculated total is Double.MIN_VALUE+Double.MIN_VALUE",result, 0.0, .000000001d);
    }
    
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateRowTotal_ValidDataValidRowInvalidColumnBelowLowerBoundary() {
    	DefaultKeyedValues2D test = new DefaultKeyedValues2D();
    	int row = 0;
    	int column = 0;
    	test.addValue(1, row, column);
        Values2D values = test;
        
        int[] columns = {-5, 0};
        
        DataUtilities.calculateRowTotal(values, 0, columns);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateRowTotal_ValidDataValidRowInvalidColumnAboveUpperBoundary() {
    	DefaultKeyedValues2D test = new DefaultKeyedValues2D();
    	int row = 0;
    	int column = 0;
    	test.addValue(1, row, column);
        Values2D values = test;
        
        int[] columns = {3, 4};
        
        DataUtilities.calculateRowTotal(values, 0, columns);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateRowTotal_ValidDataInvalidRowBelowLowerBoundaryValidColumn() {
    	DefaultKeyedValues2D test = new DefaultKeyedValues2D();
    	int row = 0;
    	int column = 0;
    	test.addValue(1, row, column);
        Values2D values = test;
        
        int[] columns = {0};
        
        DataUtilities.calculateRowTotal(values, -1, columns);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateRowTotal_ValidDataInvalidRowAboveUpperBoundaryValidColumn() {
    	DefaultKeyedValues2D test = new DefaultKeyedValues2D();
    	int row = 0;
    	int column = 0;
    	test.addValue(1, row, column);
        Values2D values = test;
        
        int[] columns = {0};
        
        DataUtilities.calculateRowTotal(values, -1, columns);
    }
    
    
    
    
    //-----------------------------------------------------------------------------------------------------------------------------//
    
    @Test
    public void getCumulativePercentages_ValidValues() {
        Mockery mockingContext = new Mockery();
        final KeyedValues values = mockingContext.mock(KeyedValues.class);
        mockingContext.checking(new Expectations() {
            {
            	allowing(values).getItemCount();
                will(returnValue(2));
                allowing(values).getKey(0);
                will(returnValue(0));
                allowing(values).getValue(0);
                will(returnValue(6.0));
                allowing(values).getKey(1);
                will(returnValue(1));
                allowing(values).getValue(1);
                will(returnValue(4.0));
            }
        });
        KeyedValues result = DataUtilities.getCumulativePercentages(values);
        
        Number[] expectedResults = {(6.0/10.0), 1.0};
        Number [] actualResults = new Number[2];
        for(int i = 0; i < result.getItemCount(); i++) {
        	actualResults[i] = result.getValue(i);
        }
        
        assertArrayEquals("The KeyedValues object contains cumulative percentages 0.6 and 1.0",expectedResults, actualResults);
    }
    
    @Test
    public void getCumulativePercentages_ExtremeValues() {
    	Mockery mockingContext = new Mockery();
        final KeyedValues values = mockingContext.mock(KeyedValues.class);
        mockingContext.checking(new Expectations() {
            {
            	allowing(values).getItemCount();
                will(returnValue(2));
                allowing(values).getKey(0);
                will(returnValue(0));
                allowing(values).getValue(0);
                will(returnValue(Double.MAX_VALUE));
                allowing(values).getKey(1);
                will(returnValue(1));
                allowing(values).getValue(1);
                will(returnValue(Double.MAX_VALUE));
            }
        });
        KeyedValues result = DataUtilities.getCumulativePercentages(values);
        
        Number[] expectedResults = {(Double.MAX_VALUE/(Double.MAX_VALUE+Double.MAX_VALUE)), 
        		((Double.MAX_VALUE+Double.MAX_VALUE)/(Double.MAX_VALUE+Double.MAX_VALUE))};
        Number [] actualResults = new Number[2];
        for(int i = 0; i < result.getItemCount(); i++) {
        	actualResults[i] = result.getValue(i);
        }
        
        assertArrayEquals("The KeyedValues object contains cumulative percentages (Double.MAX_VALUE/(Double.MAX_VALUE+Double.MAX_VALUE) and ((Double.MAX_VALUE+Double.MAX_VALUE)/(Double.MAX_VALUE+Double.MAX_VALUE))"
        		,expectedResults, actualResults);
    }
    
    @Test
    public void getCumulativePercentages_ZeroValues() {
        Mockery mockingContext = new Mockery();
        final KeyedValues values = mockingContext.mock(KeyedValues.class);
        mockingContext.checking(new Expectations() {
            {
            	allowing(values).getItemCount();
                will(returnValue(2));
                allowing(values).getKey(0);
                will(returnValue(0));
                allowing(values).getValue(0);
                will(returnValue(0));
                allowing(values).getKey(1);
                will(returnValue(1));
                allowing(values).getValue(1);
                will(returnValue(0));
            }
        });
        KeyedValues result = DataUtilities.getCumulativePercentages(values);

        Number[] expectedResults = {Double.NaN, Double.NaN};
        Number [] actualResults = {0.0, 0.0};
        for(int i = 0; i < result.getItemCount(); i++) {
        	actualResults[i] = result.getValue(i);
        }
        
        assertArrayEquals("The KeyedValues object contains cumulative percentages Double.NaN and Double.NaN",expectedResults, actualResults);
    }
    
    @Test
    public void getCumulativePercentages_WithNullValues() {
    	Mockery mockingContext = new Mockery();
        final KeyedValues values = mockingContext.mock(KeyedValues.class);
        mockingContext.checking(new Expectations() {
            {
            	allowing(values).getItemCount();
                will(returnValue(4));
                allowing(values).getKey(0);
                will(returnValue(0));
                allowing(values).getValue(0);
                will(returnValue(null));
                allowing(values).getKey(1);
                will(returnValue(1));
                allowing(values).getValue(1);
                will(returnValue(4.5));
                allowing(values).getKey(2);
                will(returnValue(2));
                allowing(values).getValue(2);
                will(returnValue(null));
                allowing(values).getKey(3);
                will(returnValue(3));
                allowing(values).getValue(3);
                will(returnValue(3.5));
            }
        });
        KeyedValues result = DataUtilities.getCumulativePercentages(values);
        
        Number[] expectedResults = {0.0, (4.5/8.0), (4.5/8.0), 1.0};
        Number [] actualResults = new Number[4];
        for(int i = 0; i < result.getItemCount(); i++) {
        	actualResults[i] = result.getValue(i);
        }
        
        assertArrayEquals("The KeyedValues object contains cumulative percentages 0.5625 and 0.5625",expectedResults, actualResults);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void getCumulativePercentages_NullData() {
    	KeyedValues values = null;
    	DataUtilities.getCumulativePercentages(values);
    }
    
    
    //-----------------------------------------------------------------------------------------------------------------------------//

    private DataUtilities exampleDataUtilities;
    @BeforeClass public static void setUpBeforeClass() throws Exception {
    }
   

    //Normal Input with non-null values
    @Test
    public void createNumberArray_NonNullDataInput() {
        //Sample Input with Non Null Data Input
        double [] data = { 13.4, 12.00004, 0.001, 1235.0, 234};
        Number [] result = DataUtilities.createNumberArray(data);
        Number [] expected = { 13.4, 12.00004, 0.001, 1235.0, 234.0};
        
        assertArrayEquals("The data input should include non-null double values", expected, result);
    }

    //Too Large value. Checked for Conversion. 
    @Test
    public void createNumberArray_ExtremeDataInput() {
        //Sample Input with Extreme (large/small) doubles
        double [] data = { Double.MAX_VALUE, Double.MAX_VALUE};
        Number [] result = DataUtilities.createNumberArray(data);
        Number [] expected = { Double.MAX_VALUE, Double.MAX_VALUE};
        
        assertArrayEquals("Extreme values should be allowed (large doubles)", expected, result);
    }
    
    //Too small value. Checked for Conversion. 
    @Test
    public void createNumberArray_SmallNegativeDataInput() {
        //Sample Input with Extreme (large/small) doubles
        double [] data = { Double.MIN_VALUE, Double.MIN_VALUE};
        Number [] result = DataUtilities.createNumberArray(data);
        Number [] expected = { Double.MIN_VALUE, Double.MIN_VALUE};
        
        assertArrayEquals("Extreme values should be allowed (small doubles)", expected, result);
    }
    
    //Invalid Array is used as an Argument. 
    @Test
     public void createNumberArray_EmptyArray() {
         //Sample Input with Null. Expect a null return
         double [] data = {};
         Number [] result = DataUtilities.createNumberArray(data);
         assertNotNull("Checking that createNumberArray can throw exception when a null array is sent",  result);
     }
    
    //Array is set to null
	@Test(expected = IllegalArgumentException.class)
	public void createNumberArray_NullValue() {
		double[] data = null;
		
		DataUtilities.createNumberArray(data);
	}
	
    @Test
    public void createNumberArray_NaNValue() {
        //Sample Input with NaN Data Input
        double [] data = {Double.NaN, Double.NaN, Double.NaN};
        Number [] result = DataUtilities.createNumberArray(data);
        Number [] expected = {Double.NaN, Double.NaN, Double.NaN};
        
        assertArrayEquals("The data input should include NaN double values", expected, result);
    }
    
    @Test
    public void createNumberArray_PositiveInf() {
        //Sample Input with Positive Infinity Data Input
        double [] data = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        Number [] result = DataUtilities.createNumberArray(data);
        Number [] expected = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        
        assertArrayEquals("The data input should include Positive Infinity double values", expected, result);
    }
    
    @Test
    public void createNumberArray_NegativeInf() {
        //Sample Input with Negative Infinity Data Input
        double [] data = { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        Number [] result = DataUtilities.createNumberArray(data);
        Number [] expected = { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        
        assertArrayEquals("The data input should include Negative Infinity double values", expected, result);
    }

    
    //-----------------------------------------------------------------------------------------------------------------------------//
	
    @Test
    public void calculateColumnTotal_NullDataValidColumn() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(3));
                one(values).getValue(0, 0);
                will(returnValue(null));
                one(values).getValue(1, 0);
                will(returnValue(null));
                one(values).getValue(2, 0);
                will(returnValue(null));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals("The calculated total is 0.0",result, 0.0, .000000001d);
    }
    
    @Test
    public void calculateColumnTotal_ValidDataValidColumn() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(3));
                one(values).getValue(0, 0);
                will(returnValue(5.67));
                one(values).getValue(1, 0);
                will(returnValue(5.2));
                one(values).getValue(2, 0);
                will(returnValue(1.2));
                one(values).getValue(0, 1);
                will(returnValue(1.7));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 0);
        assertEquals("The calculated total is 12.07",result, 12.07, .000000001d);
    }
    
    @Test
    public void calculateColumnTotal_ValidDataExtremeValuesValidColumn() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(2));
                one(values).getValue(0, 2);
                will(returnValue(Double.MAX_VALUE));
                one(values).getValue(1, 2);
                will(returnValue(Double.MAX_VALUE));
                one(values).getValue(2, 2);
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 2);
        assertEquals("The calculated total is Double.MAX_VALUE+Double.MAX_VALUE",result, (Double.MAX_VALUE+Double.MAX_VALUE), .000000001d);
    }

    @Test
    public void calculateColumnTotal_ValidDataNegativeValueValidColumn() {
        Mockery mockingContext = new Mockery();
        final Values2D values = mockingContext.mock(Values2D.class);
        mockingContext.checking(new Expectations() {
            {
                one(values).getRowCount();
                will(returnValue(3));
                one(values).getValue(0, 1);
                will(returnValue(Double.MIN_VALUE));
                one(values).getValue(1, 1);
                will(returnValue(Double.MIN_VALUE));
                one(values).getValue(2, 1);
                will(returnValue(Double.MIN_VALUE));
            }
        });
        double result = DataUtilities.calculateColumnTotal(values, 1);
        assertEquals("The calculated total is Double.MIN_VALUE+Double.MIN_VALUE+Double.MIN_VALUE"
        		,result, (Double.MIN_VALUE+Double.MIN_VALUE+Double.MIN_VALUE), .000000001d);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateColumnTotal_ValidDataInvalidColumnAboveUpperBoundary() {
    	DefaultKeyedValues2D test = new DefaultKeyedValues2D();
    	test.addValue(1, 0, 0);
        Values2D values = test;
        
        DataUtilities.calculateColumnTotal(values, 5);
    }
    

    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateColumnTotal_ValidDataInvalidColumnBelowLowerBoundary() {
    	DefaultKeyedValues2D test = new DefaultKeyedValues2D();
    	test.addValue(1, 0, 0);
        Values2D values = test;
        
        DataUtilities.calculateColumnTotal(values, -2);
    }
	
	 //-----------------------------------------------------------------------------------------------------------------------------//
    
    Mockery context;

    //Checking whether calculateColumnTotal works as expected with non-null values
    @Before
    public void setUp_NonNullAndValidColumn() throws Exception { 
        context = new Mockery();
    }
    
    @Test
    public void calculateColumnTotal_NonNullAndValidColumn(){
        final org.jfree.data.Values2D values = context.mock(org.jfree.data.Values2D.class);

        int dataRowCount = 3;
        int column = 2;

        context.checking(new Expectations() {{
            one(values).getRowCount();
            will(returnValue(dataRowCount));
            one(values).getValue(0,column);
            will(returnValue(2.4));
            one(values).getValue(1,column);
            will(returnValue(7.2));
            one(values).getValue(2,column);
            will(returnValue(9.4));
        }});
        
        int [] validRows = {0,1,2};

        double result = DataUtilities.calculateColumnTotal(values, column, validRows);
        assertEquals("Method cannot support a valid Values2D object, column and valid rows", result, 19.0, .000000001d);
    }

    @After
    public void tearDown_NonNullAndValidColumn() throws Exception {
        context = null;
    }


    //Test a column value not allowed by Value 2D Array. ie < 0
    @Before
     public void setUp_TooSmallColumn() throws Exception { 
         context = new Mockery();
     }
     
     @Test(expected = IndexOutOfBoundsException.class)
     public void calculateColumnTotal_TooSmallColumn(){
         DefaultKeyedValues2D test = new DefaultKeyedValues2D();
         test.addValue(1, 0, 0);
         org.jfree.data.Values2D values = (org.jfree.data.Values2D) test;
 
         int [] validRows = {0};
         DataUtilities.calculateColumnTotal((org.jfree.data.Values2D) values, -1, validRows);
     }

     @After
     public void tearDown_TooSmallColumn() throws Exception {
         context = null;
     }
 
    //Testing column that is too large. Exception should be thrown. 
    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateColumnTotal_TooLargeColumn(){
        DefaultKeyedValues2D test = new DefaultKeyedValues2D();
        test.addValue(1, 0, 0);
        org.jfree.data.Values2D values = (org.jfree.data.Values2D) test;

        int [] validRows = {0};
        DataUtilities.calculateColumnTotal((org.jfree.data.Values2D) values, 5, validRows);
    }

    //Upper Boundary Valid Rows check. Viewing
    @Before
    public void setUp_ValidRowsExceeded() throws Exception { 
        context = new Mockery();
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void calculateColumnTotal_ValidRowsExceeded(){
        final org.jfree.data.Values2D values = context.mock(org.jfree.data.Values2D.class);

        int dataRowCount = 3;
        int column = 2;

        context.checking(new Expectations() {{
            one(values).getRowCount();
            will(returnValue(dataRowCount));
        }});
        
        int [] validRows = {4};
        DataUtilities.calculateColumnTotal(values, 3, validRows);
    }

    @After
    public void tearDown_ValidRowsExceeded() throws Exception {
        context = null;
    }

    //Testing whether Invalid Row (<0) is accepted. Exception thrown by methods means test passes
    @Test
    public void calculateColumnTotal_RowsBelowZero(){
        DefaultKeyedValues2D test = new DefaultKeyedValues2D();
        test.addValue(1, 0, 0);
        org.jfree.data.Values2D values = (org.jfree.data.Values2D) test;

        int [] validRows = {-1};


        try{
           double value = DataUtilities.calculateColumnTotal((org.jfree.data.Values2D) values, 0, validRows);
           assertNull("Calculation should throw exception to out of bounds value (<0) for Rows",value);
        } catch (Exception e){

        }         
    }
 
	 //-----------------------------------------------------------------------------------------------------------------------------//
    
    @Test
    public void Equal_ValidPositiveValuesArrays() {
    	double[][] array1 = {{15.0, 12.67}, {5.6, 7.8}};
    	double[][] array2 = {{15.0, 12.67}, {5.6, 7.8}};
    	
    	assertTrue("Equal method is expected to return true with positive values.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_ValidNegativeValuesArrays() {
    	double[][] array1 = {{-15.0, -12.67}, {-5.6, -7.8}};
    	double[][] array2 = {{-15.0, -12.67}, {-5.6, -7.8}};
    	
    	assertTrue("Equal method is expected to return true with negative values.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_NullArrays() {
    	double[][] array1 = null;
    	double[][] array2 = null;
    	
    	assertTrue("Equal method is expected to return true with null values.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_ExtemeValueArrays() {
    	double[][] array1 = {{Double.MAX_VALUE, 12.67}, {Double.MAX_VALUE, Double.MAX_VALUE}};
    	double[][] array2 = {{Double.MAX_VALUE, 12.67}, {Double.MAX_VALUE, Double.MAX_VALUE}};
    	
    	assertTrue("Equal method is expected to return true with extreme values.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_SmallNegativeValueArrays() {
    	double[][] array1 = {{Double.MIN_VALUE, Double.MIN_VALUE}, {Double.MIN_VALUE, Double.MIN_VALUE}};
    	double[][] array2 = {{Double.MIN_VALUE, Double.MIN_VALUE}, {Double.MIN_VALUE, Double.MIN_VALUE}};
    	
    	assertTrue("Equal method is expected to return true with very small negative values.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_EmptyArrays() {
    	double[][] array1 = {{}, {}};
    	double[][] array2 = {{}, {}};
    	
    	assertTrue("Equal method is expected to return true with empty arrays.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_DifferentValueArrays() {
    	double[][] array1 = {{12.55, 1.0}, {13.4, 7.8}};
    	double[][] array2 = {{12.67, 1.1}, {16.4, 9.8}};
    	
    	assertFalse("Equal method is expected to return false with arrays containing different values.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_NaNValueArrays() {
    	double[][] array1 = {{Double.NaN, Double.NaN}, {Double.NaN, Double.NaN}};
    	double[][] array2 = {{Double.NaN, Double.NaN}, {Double.NaN, Double.NaN}};
    	
    	assertTrue("Equal method is expected to return true with NaN values.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_PositiveInfValueArrays() {
    	double[][] array1 = {{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}};
    	double[][] array2 = {{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}};
    	
    	assertTrue("Equal method is expected to return true with positive infinity values.", DataUtilities.equal(array1, array2));
    }
    
    @Test
    public void Equal_NegativeInfValueArrays() {
    	double[][] array1 = {{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}};
    	double[][] array2 = {{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}};
    	
    	assertTrue("Equal method is expected to return true with negative infinity values.", DataUtilities.equal(array1, array2));
    }
    
	 //-----------------------------------------------------------------------------------------------------------------------------//
    
    @Test
    public void Clone_ValidPositiveValuesArrayData() {
    	double [][] expected = {{15.0, 12.67}, {5.6, 7.8}};
    	double [][] result = DataUtilities.clone(expected);
    	
    	assertArrayEquals("Clone method returns double 2D array containing {15.0, 12.67}, {5.6, 7.8}.", result, expected);
    }
    
    @Test
    public void Clone_ValidNegativeValuesArrayData() {
    	double [][] expected = {{-15.0, -12.67}, {-5.6, -7.8}};
    	double [][] result = DataUtilities.clone(expected);
    	
    	assertArrayEquals("Clone method returns double 2D array containing {-15.0, -12.67}, {-5.6, -7.8}.", result, expected);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void Clone_NullArrayData() {
    	double [][] expected = null;
    	DataUtilities.clone(expected);
    }
    
    @Test
    public void Clone_EmptyArrayData() {
    	double [][] expected = {{}};
    	double [][] result = DataUtilities.clone(expected);
    	
    	assertArrayEquals("Clone method returns double 2D array that is empty.", result, expected);
    }
    
    @Test
    public void Clone_ExtremeValuesArrayData() {
    	double [][] expected = {{Double.MAX_VALUE, 12.67}, {Double.MAX_VALUE, Double.MAX_VALUE}};
    	double [][] result = DataUtilities.clone(expected);
    	
    	assertArrayEquals("Clone method returns double 2D array containing {Double.MAX_VALUE, 12.67}, {Double.MAX_VALUE, Double.MAX_VALUE}.", result, expected);
    }
    
    @Test
    public void Clone_SmallNegativeValuesArrayData() {
    	double [][] expected = {{Double.MIN_VALUE, Double.MIN_VALUE}, {Double.MIN_VALUE, Double.MIN_VALUE}};
    	double [][] result = DataUtilities.clone(expected);
    	
    	assertArrayEquals("Clone method returns double 2D array containing {Double.MIN_VALUE, Double.MIN_VALUE}, {Double.MIN_VALUE, Double.MIN_VALUE}.", result, expected);
    }
    
    @Test
    public void Clone_NaNArrayData() {
    	double [][] expected = {{Double.NaN, Double.NaN}};
    	double [][] result = DataUtilities.clone(expected);
    	
    	assertArrayEquals("Clone method returns double 2D array containing {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}.", result, expected);
    }
    
    @Test
    public void Clone_PositiveInfArrayData() {
    	double [][] expected = {{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}};
    	double [][] result = DataUtilities.clone(expected);
    	
    	assertArrayEquals("Clone method returns double 2D array containing {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}.", result, expected);
    }
    
    @Test
    public void Clone_NegativeInfArrayData() {
    	double [][] expected = {{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}};
    	double [][] result = DataUtilities.clone(expected);
    	
    	assertArrayEquals("Clone method returns double 2D array containing {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY}.", result, expected);
    }
    
    //-----------------------------------------------------------------------------------------------------------------------------//

    @After
    public void tearDown() throws Exception {
    }
}
