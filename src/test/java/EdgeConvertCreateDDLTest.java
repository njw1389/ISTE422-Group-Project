import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

public class EdgeConvertCreateDDLTest {
    private EdgeField edgeField1;
    private EdgeField edgeField2;
    private EdgeTable edgeTable1;
    private EdgeTable edgeTable2;
    private EdgeField[] edgeFields;
    private EdgeTable[] edgeTables;
    private CreateDDLMySQL tester;


    //Before
    @Before
    public void setUp() {
        edgeField1 = new EdgeField("30|testField1");
        edgeField1.setDataType(0); 
        edgeField1.setVarcharValue(255);
        edgeField1.setIsPrimaryKey(true); 

        edgeField2 = new EdgeField("28|testField2");
        edgeField2.setDataType(2);

        edgeFields = new EdgeField[] { edgeField1, edgeField2 };

        edgeTable1 = new EdgeTable("22|testTable1");
        edgeTable2 = new EdgeTable("25|testTable2");

        edgeTable1.addNativeField(30);
        edgeTable1.makeArrays();
        edgeTable2.addNativeField(28); 
        edgeTable2.makeArrays();
        edgeTable1.setRelatedField(0, 28); 
        edgeTable2.setRelatedField(0, 30);

        edgeTables = new EdgeTable[] { edgeTable1, edgeTable2 };

        tester = new CreateDDLMySQL(edgeTables, edgeFields);

        tester.databaseName = "TestDatabase";  
    }


    //After
    @After
    public void tearDown() {
        edgeField1 = null;
        edgeField2 = null;
        edgeTable1 = null;
        edgeTable2 = null;
        edgeFields = null;
        edgeTables = null;
        tester = null;
    }


    //Tests
    @Test
    public void testConstructorWithTablesAndFields() {
        tester = new CreateDDLMySQL(edgeTables, edgeFields);

        assertNotNull("Tables should be initialized", tester.tables);
        assertNotNull("Fields should be initialized", tester.fields);

        assertEquals("Tables array should have length 2", 2, tester.tables.length);
        assertEquals("Fields array should have length 2", 2, tester.fields.length);

        assertEquals("First table should be testTable1", "testTable1", tester.tables[0].getName());
        assertEquals("Second table should be testTable2", "testTable2", tester.tables[1].getName());
        assertEquals("First field should be testField1", "testField1", tester.fields[0].getName());
        assertEquals("Second field should be testField2", "testField2", tester.fields[1].getName());
    }

    @Test
    public void testDefaultConstructor() {
        tester = new CreateDDLMySQL();

        assertNull("Tables should be null for default constructor", tester.tables);
        assertNull("Fields should be null for default constructor", tester.fields);
    }

    @Test
    public void testInitializeWithBoundFields() {
        tester.initialize();

        assertEquals("First table should have 1 bound field", 1, tester.numBoundTables[0]);
        assertEquals("Second table should have 1 bound field", 1, tester.numBoundTables[1]);
        assertEquals("maxBound should be 1", 1, tester.maxBound);
    }

    @Test
    public void testInitializeWithNoBoundFields() {
        edgeTable1 = new EdgeTable("22|testTable1");
        edgeTable1.addNativeField(1);
        edgeTable1.makeArrays();

        edgeTable2 = new EdgeTable("25|testTable2");
        edgeTable2.addNativeField(2);
        edgeTable2.makeArrays();

        edgeTables = new EdgeTable[] { edgeTable1, edgeTable2 };

        tester = new CreateDDLMySQL(edgeTables, edgeFields);
        tester.initialize();

        assertEquals("First table should have 0 bound fields", 0, tester.numBoundTables[0]);
        assertEquals("Second table should have 0 bound fields", 0, tester.numBoundTables[1]);
        assertEquals("maxBound should be 0", 0, tester.maxBound);
    }

    @Test
    public void testGetTableByFigureNumberValid() {
        EdgeTable result = tester.getTable(22);
        assertNotNull("Should return a valid EdgeTable for figure number 22", result);
        assertEquals("Returned table should have the name testTable1", "testTable1", result.getName());
    }

    @Test
    public void testGetTableByFigureNumberInvalid() {
        EdgeTable result = tester.getTable(99);
        assertNull("Should return null for a non existent figure number", result);
    }

    @Test
    public void testGetFieldByFigureNumberValid() {
        EdgeField result = tester.getField(30);
        assertNotNull("Should return a valid EdgeField for figure number 30", result);
        assertEquals("Returned field should have the name testField1", "testField1", result.getName());
    }

    @Test
    public void testGetFieldByFigureNumberInvalid() {
        EdgeField result = tester.getField(99);
        assertNull("Should return null for a non existent figure number", result);
    }

    @Test
    public void testGetDatabaseName() {
        String dbName = tester.getDatabaseName();
        assertNotNull("Database name should not be null", dbName);
        assertEquals("Database name should match", "TestDatabase", dbName);
    }

    @Test
    public void testGetProductName() {
        String productName = tester.getProductName();
        assertEquals("Product name should be MySQL", "MySQL", productName);
    }

    @Test
    public void testGetSQLString() {
        String sqlString = tester.getSQLString();
        assertNotNull("SQL string should not be null", sqlString);
        assertFalse("SQL string should not be empty", sqlString.isEmpty());
        assertTrue("SQL string should contain CREATE DATABASE", sqlString.contains("CREATE DATABASE"));
    }

    @Test
    public void testCreateDDL() {
        try {
            tester.createDDL();
            String sqlString = tester.getSQLString();
            assertNotNull("SQL string should not be null after calling createDDL", sqlString);
            assertFalse("SQL string should not be empty after calling createDDL", sqlString.isEmpty());
            assertTrue("SQL string should contain USE statement", sqlString.contains("USE"));
            assertTrue("SQL string should contain CREATE TABLE statement", sqlString.contains("CREATE TABLE"));
        } catch (NullPointerException e) {
            fail("NullPointerException should not occur in createDDL: " + e.getMessage());
        }
    }
}
