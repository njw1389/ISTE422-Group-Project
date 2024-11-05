import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreateDDLMySQLTest{
    private EdgeField edgefld1;
    private EdgeField edgefld2;
    private EdgeTable edgetbl1;
    private EdgeTable edgetbl2;
    private EdgeField[] edgeFields;
    private EdgeTable[] edgeTables;
    private CreateDDLMySQL tester;

    @Before
    public void setUp(){
        edgefld1 = new EdgeField("30|testField1");
        edgefld1.setDataType(0); 
        edgefld1.setVarcharValue(255);
        edgefld1.setIsPrimaryKey(true); 

        edgefld2 = new EdgeField("28|testField2");
        edgefld2.setDataType(2);
        edgeFields = new EdgeField[] { edgefld1, edgefld2 };

        edgetbl1 = new EdgeTable("22|testTable1");
        edgetbl2 = new EdgeTable("25|testTable2");

        edgetbl1.addNativeField(30);
        edgetbl1.makeArrays();
        edgetbl2.addNativeField(28); 
        edgetbl2.makeArrays();

        edgetbl1.setRelatedField(0, 28); 
        edgetbl2.setRelatedField(0, 30);
        edgeTables = new EdgeTable[] { edgetbl1, edgetbl2 };
        tester = new CreateDDLMySQL(edgeTables, edgeFields);

        tester.databaseName = "MySQLDB";  
        
    }
    @After
    public void tearDown() {
        edgefld1 = null;
        edgefld2 = null;
        edgetbl1 = null;
        edgetbl2 = null;
        edgeFields = null;
        edgeTables = null;
        tester = null;
    }

    @Test
    public void testConstructor(){
        assertNotNull("Tester should be initialized", tester);
        assertNotNull("Tables should be initialized", tester.tables);
        assertNotNull("Fields should be initialized", tester.fields);
    }

    @Test
    public void defaultConstructorTest() {
        tester = new CreateDDLMySQL();
        assertNull("Tables should be null for default constructor", tester.tables);
        assertNull("Fields should be null for default constructor", tester.fields);
    }

    @Test
    public void testCreateDDL() {
        String sqlString = tester.getSQLString();
        assertNotNull("SQL string should not be null", sqlString);
        assertFalse("SQL string should not be empty", sqlString.isEmpty());
        assertTrue("SQL string should contain USE statement", sqlString.contains("USE"));
        assertTrue("SQL string should contain CREATE TABLE statement", sqlString.contains("CREATE TABLE"));
        
    }

    @Test
    public void convertBooleanFalse(){
        String test = "false";
        assertEquals("Should be zero", 0, tester.convertStrBooleanToInt(test));

    }

    @Test
    public void convertBooleanTrue(){
        String test = "true";
        assertEquals("Should be one", 1, tester.convertStrBooleanToInt(test));
    }

    @Test
    public void convertBooleanInvalid(){
        String test = "fghkf";
        assertEquals("Should be false", 0, tester.convertStrBooleanToInt(test));
    }

    @Test
    public void generateDBNameValid(){
        assertEquals("Should be MySQLDB", tester.getDatabaseName(), tester.generateDatabaseName());
    }

    @Test
    public void getDBNameValid(){
        assertEquals("Should be MySQLDB", "MySQLDB", tester.getDatabaseName());
    }

    @Test
    public void getDBNameInvalid(){
        assertNotEquals("Should be different names", "randomstr", tester.getDatabaseName());
    }

    @Test
    public void getProductNameTest(){
        assertEquals("Should be MySQL", "MySQL", tester.getProductName());
    }

    @Test
    public void testGetSQLString() {
        String sqlString = tester.getSQLString();
        assertNotNull("SQL string should not be null", sqlString);
        assertFalse("SQL string should not be empty", sqlString.isEmpty());
        assertTrue("SQL string should contain USE statement", sqlString.contains("USE"));
        assertTrue("SQL string should contain CREATE TABLE statement", sqlString.contains("CREATE TABLE"));
    }
}