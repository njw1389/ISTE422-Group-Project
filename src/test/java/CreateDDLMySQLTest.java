import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreateDDLMySQLTest {
    private EdgeField edgefld1;
    private EdgeField edgefld2;
    private EdgeField edgefld3;
    private EdgeField edgefld4;
    private EdgeTable edgetbl1;
    private EdgeTable edgetbl2;
    private EdgeTable edgetbl3;
    private EdgeField[] edgeFields;
    private EdgeTable[] edgeTables;
    private CreateDDLMySQL tester;

    @Before
    public void setUp() {
        // Original field setup
        edgefld1 = new EdgeField("30|testField1");
        edgefld1.setDataType(0);  // VARCHAR
        edgefld1.setVarcharValue(255);
        edgefld1.setIsPrimaryKey(true);

        // Add more diverse field types
        edgefld2 = new EdgeField("28|testField2");
        edgefld2.setDataType(2);  // INT
        
        edgefld3 = new EdgeField("32|testField3");
        edgefld3.setDataType(1);  // BOOL
        edgefld3.setDefaultValue("true");
        
        edgefld4 = new EdgeField("34|testField4");
        edgefld4.setDataType(3);  // DOUBLE
        edgefld4.setDisallowNull(true);
        
        edgeFields = new EdgeField[] { edgefld1, edgefld2, edgefld3, edgefld4 };

        // Enhanced table setup
        edgetbl1 = new EdgeTable("22|testTable1");
        edgetbl2 = new EdgeTable("25|testTable2");
        edgetbl3 = new EdgeTable("27|testTable3");

        // Setup more complex relationships
        edgetbl1.addNativeField(30);
        edgetbl1.addNativeField(32);
        edgetbl1.makeArrays();
        
        edgetbl2.addNativeField(28);
        edgetbl2.addNativeField(34);
        edgetbl2.makeArrays();
        
        edgetbl3.addNativeField(32);
        edgetbl3.makeArrays();

        // Set up relationships
        edgetbl1.setRelatedField(0, 28);
        edgetbl2.setRelatedField(0, 30);
        edgetbl3.setRelatedField(0, 34);
        
        edgeTables = new EdgeTable[] { edgetbl1, edgetbl2, edgetbl3 };
        tester = new CreateDDLMySQL(edgeTables, edgeFields);
        tester.databaseName = "MySQLDB";
    }

    @After
    public void tearDown() {
        edgefld1 = edgefld2 = edgefld3 = edgefld4 = null;
        edgetbl1 = edgetbl2 = edgetbl3 = null;
        edgeFields = null;
        edgeTables = null;
        tester = null;
    }

    // Original tests remain...

    @Test
    public void testFieldDataTypes() {
        String sqlString = tester.getSQLString();
        assertTrue("Should contain VARCHAR type", sqlString.contains("VARCHAR(255)"));
        assertTrue("Should contain BOOL type", sqlString.contains("BOOL"));
        assertTrue("Should contain INT type", sqlString.contains("INT"));
        assertTrue("Should contain DOUBLE type", sqlString.contains("DOUBLE"));
    }

    @Test
    public void testNotNullConstraints() {
        String sqlString = tester.getSQLString();
        assertTrue("Should contain NOT NULL constraint", sqlString.contains("NOT NULL"));
    }

    @Test
    public void testDefaultValues() {
        String sqlString = tester.getSQLString();
        assertTrue("Should contain DEFAULT 1 for boolean true", sqlString.contains("DEFAULT 1"));
    }

    @Test
    public void testPrimaryKeyConstraints() {
        String sqlString = tester.getSQLString();
        assertTrue("Should contain PRIMARY KEY constraint", sqlString.contains("PRIMARY KEY"));
        assertTrue("Should contain correct primary key field", sqlString.contains("testField1"));
    }

    @Test
    public void testMultipleTableCreation() {
        String sqlString = tester.getSQLString();
        int tableCount = countOccurrences(sqlString, "CREATE TABLE");
        assertEquals("Should create correct number of tables", 3, tableCount);
    }

    @Test
    public void testDatabaseCreation() {
        String sqlString = tester.getSQLString();
        assertTrue("Should contain CREATE DATABASE statement", sqlString.contains("CREATE DATABASE MySQLDB"));
    }

    @Test
    public void testEmptyDefaultValue() {
        EdgeField testField = new EdgeField("40|testField5");
        testField.setDataType(0);
        testField.setDefaultValue("");
        EdgeField[] singleField = new EdgeField[] { testField };
        EdgeTable testTable = new EdgeTable("40|testTable5");
        testTable.addNativeField(40);
        testTable.makeArrays();
        EdgeTable[] singleTable = new EdgeTable[] { testTable };
        
        CreateDDLMySQL testerEmpty = new CreateDDLMySQL(singleTable, singleField);
        testerEmpty.databaseName = "TestDB";
        String sqlString = testerEmpty.getSQLString();
        
        assertFalse("Should not contain DEFAULT keyword for empty default value", 
            sqlString.contains("DEFAULT"));
    }

    // Helper method to count string occurrences
    private int countOccurrences(String str, String findStr) {
        int lastIndex = 0;
        int count = 0;
        while (lastIndex != -1) {
            lastIndex = str.indexOf(findStr, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        return count;
    }
}