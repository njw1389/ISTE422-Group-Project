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
    void setUp(){
        edgefld1 = new EdgeField("30|testField1");
        edgefld2 = new EdgeField("28|testField2");
        edgeflds[0] = edgefld1;
        edgeflds[1] = edgefld2;

        edgetbl1 = new EdgeTable("22|testTable1");
        edgetbl2 = new EdgeTable("25|testTable2");
        edgetbls[0] = edgetbl1;
        edgetbls[1] = edgetbl2;
        
    }
    @Test
    public void testConstructor(){
        tester = new CreateDDLMySQL(edgetbls,edgeflds);
        assertNotNull("Tester should be initialized", tester);

    }
}