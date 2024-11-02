import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class EdgeFieldTest {
    private String TEST_DATA_PATH = "../resources/EdgeFieldTestData.psv";
    private EdgeField getterSetterTester;

    @Before
    void setUp() {
        getterSetterTester = new EdgeField("30|testField");
    }

    @Test 
    void testConstructor() throws IOException{
        String[] inputStrings = loadTestData();
        EdgeField ef;
        for (String str : inputStrings) {
            ef = new EdgeField(str);
            StringTokenizer sTokenizer = new StringTokenizer(str, "|");
            assertEquals("EdgeField numFigure is equal to string token value.", ef.getNumFigure(), sTokenizer.nextToken());
            assertEquals("EdgeField name is equal to string token value.", ef.getName(), sTokenizer.nextToken());
        }
    }

    @Test
    void testGetNumFigure() {
        assertEquals("Testing getNumFigure.", getterSetterTester.getNumFigure(), 30);
    }

    @Test
    void testGetName() {
        assertEquals("Testing getName.", getterSetterTester.getName(), "testField");
    }

    @Test
    void testGetTableID() {
        assertEquals("Table ID should be equal to zero on initialization.", getterSetterTester.getTableID(), 0);
    }

    @Test
    void testSetTableID() {
        getterSetterTester.setTableID(300);
        assertEquals("Table ID should be equal to the set value.", getterSetterTester.getTableID(), 300);
    }

    @Test
    void testGetTableBound() {
        assertEquals("Table Bound should be equal to zero on initialization.", getterSetterTester.getTableBound(), 0);
    }

    @Test
    void testSetTableBound() {
        getterSetterTester.setTableBound(300);
        assertEquals("Table Bound should be equal to the set value.", getterSetterTester.getTableBound(), 300);
    }

    @Test
    void testGetFieldBound() {
        assertEquals("Field Bound should be equal to zero on initialization.", getterSetterTester.getFieldBound(), 0);
    }

    @Test
    void testSetFieldBound() {
        getterSetterTester.setFieldBound(300);
        assertEquals("Field Bound should be equal to the set value.", getterSetterTester.getFieldBound(), 300);
    }

    @Test
    void testGetDisallowNull() {
        assertEquals("Disallow Null should be equal to false on initialization.", getterSetterTester.getDisallowNull(), false);
    }

    @Test
    void testSetDisallowNull() {
        getterSetterTester.setDisallowNull(true);
        assertEquals("Disallow Null should be equal to the set value.", getterSetterTester.getDisallowNull(), true);
    }

    @Test
    void testGetIsPrimaryKey() {
        assertEquals("Primary Key should be equal to false on initialization.", getterSetterTester.getIsPrimaryKey(), false);
    }

    @Test
    void testSetIsPrimaryKey() {
        getterSetterTester.setIsPrimaryKey(true);
        assertEquals("Primary Key should be equal to the set value.", getterSetterTester.getIsPrimaryKey(), true);
    }

    @Test
    void testGetDefaultValue() {
        assertEquals("Default value should be equal to empty string on initialization.", getterSetterTester.getDefaultValue(), "");
    }

    @Test
    void testSetDefaultValue() {
        getterSetterTester.setDefaultValue("test");
        assertEquals("Default value should be equal to the set value.", getterSetterTester.getDefaultValue(), "test");
    }

    @Test
    void testGetVarcharValue() {
        assertEquals("Varchar value should be equal to 1 on initialization.", getterSetterTester.getVarcharValue(), 1);
    }

    @Test
    void testSetVarcharValue() {
        getterSetterTester.setVarcharValue(5);
        assertEquals("Varchar value should be equal to the set value.", getterSetterTester.getVarcharValue(), 5);
        getterSetterTester.setVarcharValue(-5);
        assertEquals("Varchar value should not allow assignment of negative integers.", getterSetterTester.getVarcharValue(), 5);
    }

    @Test
    void testGetDataType() {
        assertEquals("Data type should be equal to 0 on initialization.", getterSetterTester.getDataType(), 0);
    }

    @Test
    void testSetDataType() {
        getterSetterTester.setDataType(3);
        assertEquals("Data type should be equal to the set value.", getterSetterTester.getDataType(), 3);
        getterSetterTester.setDataType(-3);
        assertEquals("Data type should not allow assignment of negative integers.", getterSetterTester.getDataType(), 3);
        getterSetterTester.setDataType(6);
        assertEquals("Data type should not allow assignment of integers outside of data type array length bounds.", getterSetterTester.getDataType(), 3);
    }

    @Test
    void testGetStrDataType() {
        String[] dataTypeArrayReplica = {"Varchar", "Boolean", "Integer", "Double"};
        assertEquals("Data type array equals the replica.", EdgeField.getStrDataType(), dataTypeArrayReplica);
    }

    @Test
    void testToString() {
        String toStringReplica = getterSetterTester.getNumFigure() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getName() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getTableID() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getTableBound() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getFieldBound() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getDataType() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getVarcharValue() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getIsPrimaryKey() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getDisallowNull() + EdgeConvertFileParser.DELIM +
        getterSetterTester.getDefaultValue();

        assertEquals("toString method constructs as expected.", getterSetterTester.toString(), toStringReplica);
    }

    String[] loadTestData() throws IOException{
        FileReader fr = new FileReader(TEST_DATA_PATH);
        BufferedReader br = new BufferedReader(fr);
        List<String> ls = new LinkedList<>();

        while(br.ready()) {
            ls.add(br.readLine());
        }

        br.close();
        return (String[])ls.toArray();
    }
}
