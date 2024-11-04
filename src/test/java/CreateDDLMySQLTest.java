import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreateDDLMySQLTest{
    private EdgeField edgefld1;
    private EdgeField edgefld2;
    private EdgeTable edgetbl1;
    private EdgeTable edgetbl2;
    private EdgeField edgeflds[]= new EdgeField[2];
    private EdgeTable edgetbls[] = new EdgeTable[2];
    CreateDDLMySQL tester;

    @Before
    public void setUp(){
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
        edgetbls[1] = edgetbl2;
        
    }
    @Test
    public void testConstructor(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);
        assertNotNull("Tester should be initialized", tester);
    }
    @Test
    public void convertBooleanFalse(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);

        String test = "false";
        assertEquals("Should be zero", 0, tester.convertStrBooleanToInt(test));

    }
    @Test
    public void convertBooleanTrue(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);
        String test = "true";
        assertEquals("Should be one", 1, tester.convertStrBooleanToInt(test));
    }
    @Test
    public void convertBooleanInvalid(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);
        String test = "fghkf";
        assertEquals("Should be false", 0, tester.convertStrBooleanToInt(test));
    }
    @Test
    public void generateDBNameValid(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);
        //how to set db name??
        assertEquals("Should be testDB", tester.getDatabaseName(), tester.generateDatabaseName());
    }
    @Test
    public void getDBNameValid(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);
        //how to set db name??
        assertEquals("Should be testDB", "testDB", tester.getDatabaseName());
    }
    @Test
    public void getDBNameInvalid(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);
        //how to set db name??
        assertNotEquals("Should be different names", "randomstr", tester.getDatabaseName());
    }
    @Test
    public void getProductNameTest(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);
        //how to set db name??
        assertEquals("Should be MySQL", "MySQL", tester.getProductName());
    }
   
}