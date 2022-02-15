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
        
        assertArrayEquals("The created Number Array is empty",testArray, DataUtilities.createNumberArray2D(testArray));
    }

    @Test
    public void createNumberArray2D_ValidArray() {
        double[][] testArray = new double[][] {{1.1, 12, 6.77}, {25, 65.4, 22.22}};
        
        assertArrayEquals("The created Number Array contains {{1.1, 12, 6.77}, {25, 65.4, 22.22}}",testArray, DataUtilities.createNumberArray2D(testArray));
    }

    @Test
    public void createNumberArray2D_ExtremeValue() {
        double[][] testArray = new double [][] {{Double.MAX_VALUE, Double.MAX_VALUE}, {Double.MAX_VALUE, Double.MAX_VALUE}};
        
        assertArrayEquals("The created Number Array contains extreme values"
        		,testArray, DataUtilities.createNumberArray2D(testArray));
    }
    
    @Test
    public void createNumberArray2D_SmallNegativeValue() {
        double[][] testArray = new double [][] {{Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE}, 
        	{Double.MIN_VALUE, Double.MIN_VALUE}};
        
        assertArrayEquals("The created Number Array contains very small negative values"
        		,testArray, DataUtilities.createNumberArray2D(testArray));
    }
    
    //-----------------------------------------------------------------------------------------------------------------------------//
    
    @Test
    public void calculateRowTotal_NullData() {
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
    
    @After
    public void tearDown() throws Exception {
    }

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

    //Too Large value or Too Small Value. Checked for Conversion. 
    @Test
    public void createNumberArray_ExtremeDataInput() {
        //Sample Input with Extreme (large/small) doubles
        double [] data = { Double.MAX_VALUE, Double.MIN_VALUE};
        Number [] result = DataUtilities.createNumberArray(data);
        Number [] expected = { Double.MAX_VALUE, Double.MIN_VALUE};
        
        assertArrayEquals("Extreme values should be allowed (large doubles and small doubles)", expected, result);
    }

    //Invalid Array is used as an Argument. 
    @Test
     public void createNumberArray_NullDataArray() {
         //Sample Input with Null. Expect a null return
         double [] data = {};
         Number [] result = DataUtilities.createNumberArray(data);
         assertNotNull("Checking that createNumberArray can throw exception when a null array is sent",  result);
     }
 
    
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
     
     @Test
     public void calculateColumnTotal_TooSmallColumn(){
         DefaultKeyedValues2D test = new DefaultKeyedValues2D();
         test.addValue(1, 0, 0);
         org.jfree.data.Values2D values = (org.jfree.data.Values2D) test;
 
         int [] validRows = {0};
 
 
         try{
            double value = DataUtilities.calculateColumnTotal((org.jfree.data.Values2D) values, -1, validRows);
            assertNull("Calculation should throw exception to out of bounds value (<0)",value);
         } catch (Exception e){

        }         
     }

     @After
     public void tearDown_TooSmallColumn() throws Exception {
         context = null;
     }
 
    //Testing column that is too large. Exception should be thrown. 
    @Test
    public void calculateColumnTotal_TooLargeColumn(){
        DefaultKeyedValues2D test = new DefaultKeyedValues2D();
        test.addValue(1, 0, 0);
        org.jfree.data.Values2D values = (org.jfree.data.Values2D) test;

        int [] validRows = {0};


        try{
           double value = DataUtilities.calculateColumnTotal((org.jfree.data.Values2D) values, 5, validRows);
           assertNull("Calculation should throw exception to out of bounds value (>Values Length)",value);
        } catch (Exception e){

       }         
    }

    //Upper Boundary Valid Rows check. Viewing
    @Before
    public void setUp_ValidRowsExceeded() throws Exception { 
        context = new Mockery();
    }
    
    @Test
    public void calculateColumnTotal_ValidRowsExceeded(){
        final org.jfree.data.Values2D values = context.mock(org.jfree.data.Values2D.class);

        int dataRowCount = 3;
        int column = 2;

        context.checking(new Expectations() {{
            one(values).getRowCount();
            will(returnValue(dataRowCount));
        }});
        
        int [] validRows = {4};

        try{

            double result = DataUtilities.calculateColumnTotal(values, 3, validRows);
            assertNull("Valid Rows Should not Exceed Rows in Values Table", result);

        } catch (Exception e){

        }
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
 


    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}
