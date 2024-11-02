import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import javax.swing.*;

public class EdgeConvertFileParserTest {
    private EdgeConvertFileParser parser;
    private File validEdgeFile;
    private File validSaveFile;
    private File invalidFile;
    private static final String TEST_FILE_PATH = "test_files/";
    
    @Before
    public void setUp() throws IOException {
        // Create test files directory if it doesn't exist
        new File(TEST_FILE_PATH).mkdirs();
        
        // Create temporary test files
        validEdgeFile = createTempEdgeFile();
        validSaveFile = createTempSaveFile();
        invalidFile = createTempInvalidFile();
        
        new JOptionPane();
    }
    
    @After
    public void tearDown() {
        // Clean up temporary files
        validEdgeFile.delete();
        validSaveFile.delete();
        invalidFile.delete();
        new File(TEST_FILE_PATH).delete();
    }

    private File createTempEdgeFile() throws IOException {
        File temp = File.createTempFile("test", ".edg", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(temp)) {
            writer.println("EDGE Diagram File");
            writer.println("Figure 1");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"Customer\"");
            writer.println("}");
            writer.println("Figure 2");
            writer.println("{");
            writer.println("Style \"Attribute\"");
            writer.println("Text \"customerID\"");
            writer.println("TypeUnderl TRUE");
            writer.println("}");
            writer.println("Figure 3");
            writer.println("{");
            writer.println("Style \"Attribute\"");
            writer.println("Text \"customerName\"");
            writer.println("}");
            writer.println("Connector 1");
            writer.println("{");
            writer.println("Style \"Line\"");
            writer.println("Figure1 1");
            writer.println("Figure2 2");
            writer.println("End1 \"none\"");
            writer.println("End2 \"none\"");
            writer.println("}");
            writer.println("Connector 2");
            writer.println("{");
            writer.println("Style \"Line\"");
            writer.println("Figure1 1");
            writer.println("Figure2 3");
            writer.println("End1 \"none\"");
            writer.println("End2 \"none\"");
            writer.println("}");
        }
        return temp;
    }

    private File createTempSaveFile() throws IOException {
        File temp = File.createTempFile("test", ".sav", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(temp)) {
            writer.println("EdgeConvert Save File");
            writer.println("Table: 1");
            writer.println("{");
            writer.println("TableName Customer");
            writer.println("NativeFields 2|3");
            writer.println("RelatedTables ");
            writer.println("RelatedFields ");
            writer.println("}");
            writer.println("#Fields#");
            writer.println("2|customerID|1|1|1|0|0|true|false");
            writer.println("3|customerName|1|1|1|0|0|false|false");
        }
        return temp;
    }

    private File createTempInvalidFile() throws IOException {
        File temp = File.createTempFile("test", ".txt", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(temp)) {
            writer.println("Invalid content");
        }
        return temp;
    }

    @Test
    public void testConstructorInitialization() {
        parser = new EdgeConvertFileParser(validEdgeFile);
        assertNotNull("Parser should be initialized", parser);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        assertNotNull("Tables array should be initialized", tables);
        assertNotNull("Fields array should be initialized", fields);
    }

    @Test(expected = RuntimeException.class)
    public void testFileNotFound() {
        File nonExistentFile = new File("nonexistent.edg");
        new EdgeConvertFileParser(nonExistentFile);
    }

    @Test
    public void testParseValidEdgeFile() {
        parser = new EdgeConvertFileParser(validEdgeFile);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        // Verify tables
        assertEquals("Should have one table", 1, tables.length);
        assertEquals("Table name should be Customer", "Customer", tables[0].getName());
        
        // Verify fields
        assertEquals("Should have two fields", 2, fields.length);
        assertTrue("customerID should be primary key", fields[0].getIsPrimaryKey());
        assertFalse("customerName should not be primary key", fields[1].getIsPrimaryKey());
    }

    @Test
    public void testParseValidSaveFile() {
        parser = new EdgeConvertFileParser(validSaveFile);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        // Verify tables
        assertEquals("Should have one table", 1, tables.length);
        assertEquals("Table name should be Customer", "Customer", tables[0].getName());
        
        // Verify fields
        assertEquals("Should have two fields", 2, fields.length);
        assertEquals("First field should be customerID", "customerID", fields[0].getName());
        assertEquals("Second field should be customerName", "customerName", fields[1].getName());
    }

    @Test
    public void testInvalidFileFormat() {
        parser = new EdgeConvertFileParser(invalidFile);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        // Verify no tables or fields were created
        assertEquals("Should have no tables", 0, tables.length);
        assertEquals("Should have no fields", 0, fields.length);
    }

    @Test
    public void testDuplicateTableNames() throws IOException {
        File dupFile = File.createTempFile("test-dup", ".edg", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(dupFile)) {
            writer.println("EDGE Diagram File");
            writer.println("Figure 1");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"Customer\"");
            writer.println("}");
            writer.println("Figure 2");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"Customer\"");
            writer.println("}");
        }
        
        parser = new EdgeConvertFileParser(dupFile);
        EdgeTable[] tables = parser.getEdgeTables();
        
        // Verify no tables were created due to duplicate names
        assertEquals("Should have no tables due to duplicates", 0, tables.length);
        
        dupFile.delete();
    }

    @Test
    public void testFieldConnectedToMultipleTables() throws IOException {
        File multiFile = File.createTempFile("test-multi", ".edg", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(multiFile)) {
            writer.println("EDGE Diagram File");
            writer.println("Figure 1");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"Customer\"");
            writer.println("}");
            writer.println("Figure 2");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"Order\"");
            writer.println("}");
            writer.println("Figure 3");
            writer.println("{");
            writer.println("Style \"Attribute\"");
            writer.println("Text \"ID\"");
            writer.println("}");
            writer.println("Connector 1");
            writer.println("{");
            writer.println("Style \"Line\"");
            writer.println("Figure1 1");
            writer.println("Figure2 3");
            writer.println("End1 \"none\"");
            writer.println("End2 \"none\"");
            writer.println("}");
            writer.println("Connector 2");
            writer.println("{");
            writer.println("Style \"Line\"");
            writer.println("Figure1 2");
            writer.println("Figure2 3");
            writer.println("End1 \"none\"");
            writer.println("End2 \"none\"");
            writer.println("}");
        }
        
        parser = new EdgeConvertFileParser(multiFile);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        // Verify the error condition
        assertEquals("Should have no tables due to field-multiple-tables error", 0, tables.length);
        assertEquals("Should have no fields due to field-multiple-tables error", 0, fields.length);
        
        multiFile.delete();
    }

    @Test
    public void testBlankEntityName() throws IOException {
        File blankFile = File.createTempFile("test-blank", ".edg", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(blankFile)) {
            writer.println("EDGE Diagram File");
            writer.println("Figure 1");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"\"");
            writer.println("}");
        }
        
        parser = new EdgeConvertFileParser(blankFile);
        EdgeTable[] tables = parser.getEdgeTables();
        
        // Verify no tables were created due to blank name
        assertEquals("Should have no tables due to blank name", 0, tables.length);
        
        blankFile.delete();
    }

    @Test
    public void testLineBreakInText() throws IOException {
        File lineBreakFile = File.createTempFile("test-break", ".edg", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(lineBreakFile)) {
            writer.println("EDGE Diagram File");
            writer.println("Figure 1");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"Customer\\line with break\"");
            writer.println("}");
        }

        parser = new EdgeConvertFileParser(lineBreakFile);
        EdgeTable[] tables = parser.getEdgeTables();
        assertEquals("Should truncate text at line break", "Customer", tables[0].getName());
        lineBreakFile.delete();
    }

    @Test
    public void testManyToManyRelationship() throws IOException {
        File manyFile = File.createTempFile("test-many", ".edg", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(manyFile)) {
            writer.println("EDGE Diagram File");
            writer.println("Figure 1");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"Customer\"");
            writer.println("}");
            writer.println("Figure 2");
            writer.println("{");
            writer.println("Style \"Entity\"");
            writer.println("Text \"Product\"");
            writer.println("}");
            writer.println("Connector 1");
            writer.println("{");
            writer.println("Style \"Line\"");
            writer.println("Figure1 1");
            writer.println("Figure2 2");
            writer.println("End1 \"many\"");
            writer.println("End2 \"many\"");
            writer.println("}");
        }
        
        parser = new EdgeConvertFileParser(manyFile);
        EdgeTable[] tables = parser.getEdgeTables();
        
        // Verify no tables were created due to many-to-many error
        assertEquals("Should have no tables due to many-to-many relationship", 0, tables.length);
        
        manyFile.delete();
    }

    @Test
    public void testCompositeAttributeError() throws IOException {
        File compositeFile = File.createTempFile("test-composite", ".edg", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(compositeFile)) {  // Fixed: Changed writer to compositeFile
            writer.println("EDGE Diagram File");
            writer.println("Figure 1");
            writer.println("{");
            writer.println("Style \"Attribute\"");
            writer.println("Text \"Name\"");
            writer.println("}");
            writer.println("Figure 2");
            writer.println("{");
            writer.println("Style \"Attribute\"");
            writer.println("Text \"FirstName\"");
            writer.println("}");
            writer.println("Connector 1");
            writer.println("{");
            writer.println("Style \"Line\"");
            writer.println("Figure1 1");
            writer.println("Figure2 2");
            writer.println("End1 \"none\"");
            writer.println("End2 \"none\"");
            writer.println("}");
        }
        
        parser = new EdgeConvertFileParser(compositeFile);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        // Verify no tables/fields were created due to composite attribute error
        assertEquals("Should have no tables due to composite attributes", 0, tables.length);
        assertEquals("Should have no fields due to composite attributes", 0, fields.length);
        
        compositeFile.delete();
    }

    @Test
    public void testRelationsError() throws IOException {
        File relFile = File.createTempFile("test-rel", ".edg", new File(TEST_FILE_PATH));
        try (PrintWriter writer = new PrintWriter(relFile)) {
            writer.println("EDGE Diagram File");
            writer.println("Figure 1");
            writer.println("{");
            writer.println("Style \"Relation\"");
            writer.println("Text \"HasA\"");
            writer.println("}");
        }
        
        parser = new EdgeConvertFileParser(relFile);
        EdgeTable[] tables = parser.getEdgeTables();
        
        // Verify no tables were created due to relations error
        assertEquals("Should have no tables due to relations", 0, tables.length);
        
        relFile.delete();
    }
}