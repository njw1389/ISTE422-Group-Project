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
        /*
        edgefld1 = new EdgeField("30|testField1");
        edgefld2 = new EdgeField("28|testField2");
        edgeflds[0] = edgefld1;
        edgeflds[1] = edgefld2;

        edgetbl1 = new EdgeTable("22|testTable1");
        edgetbl2 = new EdgeTable("25|testTable2");
        edgetbl1.setRelatedField(0,1);
        edgetbl2.setRelatedField(1,1);
        edgetbl1.makeArrays();
        edgetbl2.makeArrays();
        edgetbls[0] = edgetbl1;
        edgetbls[1] = edgetbl2; */
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
    @Test
    public void testConstructor(){
       
        assertNotNull("Tester should be initialized", tester);
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
       
        //how to set db name??
        assertEquals("Should be MySQLDB", tester.getDatabaseName(), tester.generateDatabaseName());
    }
    @Test
    public void getDBNameValid(){
       
        //how to set db name??
        assertEquals("Should be MySQLDB", "MySQLDB", tester.getDatabaseName());
    }
    @Test
    public void getDBNameInvalid(){
       
        //how to set db name??
        assertNotEquals("Should be different names", "randomstr", tester.getDatabaseName());
    }
    @Test
    public void getProductNameTest(){
       
        //how to set db name??
        assertEquals("Should be MySQL", "MySQL", tester.getProductName());
    }
   
}