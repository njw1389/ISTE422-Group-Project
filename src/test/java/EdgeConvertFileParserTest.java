import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.Rule;
import static org.junit.Assert.*;
import java.io.*;
import java.util.Arrays;

public class EdgeConvertFileParserTest {
    private EdgeConvertFileParser parser;
    private File tempFile;
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Before
    public void setUp() throws IOException {
        tempFile = tempFolder.newFile("test.edg");
    }
    
    @After
    public void tearDown() {
        parser = null;
        tempFile = null;
    }
    
    @Test
    public void testBasicConstructorInitialization() throws IOException {
        // Create minimal valid file
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        
        // Verify initial state through getters
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        assertNotNull("Tables array should be initialized", tables);
        assertNotNull("Fields array should be initialized", fields);
        assertEquals("Tables array should be empty", 0, tables.length);
        assertEquals("Fields array should be empty", 0, fields.length);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testConstructorWithInvalidFile() {
        File nonExistentFile = new File("nonexistent.edg");
        parser = new ParseEdgeFile(nonExistentFile);
    }
    
    @Test
    public void testOpenValidEdgeFile() throws IOException {
        // Create valid Edge file content
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            writer.write("Figure 1\n");
            writer.write("{\n");
            writer.write("    Style \"Entity\"\n");
            writer.write("    Text \"Customer\"\n");
            writer.write("}\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        EdgeTable[] tables = parser.getEdgeTables();
        
        assertNotNull("Tables should not be null", tables);
        assertTrue("Should have parsed one table", tables.length > 0);
    }
    
    @Test
    public void testParseBasicEntity() throws IOException {
        // Create file with single entity
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            writer.write("Figure 1\n");
            writer.write("{\n");
            writer.write("    Style \"Entity\"\n");
            writer.write("    Text \"Customer\"\n");
            writer.write("}\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        EdgeTable[] tables = parser.getEdgeTables();
        
        assertEquals("Should create one table", 1, tables.length);
        assertEquals("Table name should match", "Customer", tables[0].getName());
    }
    
    @Test
    public void testParseBasicAttribute() throws IOException {
        // Create file with single attribute
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            writer.write("Figure 1\n");
            writer.write("{\n");
            writer.write("    Style \"Attribute\"\n");
            writer.write("    Text \"customerName\"\n");
            writer.write("}\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        EdgeField[] fields = parser.getEdgeFields();
        
        assertEquals("Should create one field", 1, fields.length);
        assertEquals("Field name should match", "customerName", fields[0].getName());
    }
    
    @Test
    public void testEntityWithMultipleAttributes() throws IOException {
        // Create file with entity and multiple attributes
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            // Entity
            writer.write("Figure 1\n");
            writer.write("{\n");
            writer.write("    Style \"Entity\"\n");
            writer.write("    Text \"Customer\"\n");
            writer.write("}\n");
            // First attribute
            writer.write("Figure 2\n");
            writer.write("{\n");
            writer.write("    Style \"Attribute\"\n");
            writer.write("    Text \"customerID\"\n");
            writer.write("    TypeUnderl \"true\"\n");
            writer.write("}\n");
            // Second attribute
            writer.write("Figure 3\n");
            writer.write("{\n");
            writer.write("    Style \"Attribute\"\n");
            writer.write("    Text \"customerName\"\n");
            writer.write("}\n");
            // Connector between entity and first attribute
            writer.write("Connector 1\n");
            writer.write("{\n");
            writer.write("    Style \"Arrow\"\n");  // Added style
            writer.write("    Figure1 1\n");
            writer.write("    Figure2 2\n");
            writer.write("    EndPoint1 0\n");      // Added required fields
            writer.write("    EndPoint2 0\n");
            writer.write("    SuppressEnd1 0\n");   // Added required fields
            writer.write("    SuppressEnd2 0\n");
            writer.write("    End1 \"none\"\n");    // Changed from empty quotes
            writer.write("    End2 \"none\"\n");    // Changed from empty quotes
            writer.write("}\n");
            // Connector between entity and second attribute
            writer.write("Connector 2\n");
            writer.write("{\n");
            writer.write("    Style \"Arrow\"\n");
            writer.write("    Figure1 1\n");
            writer.write("    Figure2 3\n");
            writer.write("    EndPoint1 0\n");
            writer.write("    EndPoint2 0\n");
            writer.write("    SuppressEnd1 0\n");
            writer.write("    SuppressEnd2 0\n");
            writer.write("    End1 \"none\"\n");
            writer.write("    End2 \"none\"\n");
            writer.write("}\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        assertEquals("Should create one table", 1, tables.length);
        assertEquals("Should create two fields", 2, fields.length);
        assertTrue("First field should be primary key", fields[0].getIsPrimaryKey());
    }
    
    @Test
    public void testLineBreakHandling() throws IOException {
        // Create file with line break in text
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            writer.write("Figure 1\n");
            writer.write("{\n");
            writer.write("    Style \"Entity\"\n");
            writer.write("    Text \"Customer\\line with break\"\n");
            writer.write("}\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        EdgeTable[] tables = parser.getEdgeTables();
        
        assertEquals("Should create one table", 1, tables.length);
        assertEquals("Should truncate at line break", "Customer", tables[0].getName());
    }
    
    @Test
    public void testEmptyArrayConversion() throws IOException {
        // Create minimal valid file
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        assertNotNull("Tables array should not be null", tables);
        assertNotNull("Fields array should not be null", fields);
        assertEquals("Tables array should be empty", 0, tables.length);
        assertEquals("Fields array should be empty", 0, fields.length);
    }

    @Test
    public void testDuplicateTableNames() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            // First Customer entity
            writer.write("Figure 1\n");
            writer.write("{\n");
            writer.write("    Style \"Entity\"\n");
            writer.write("    Text \"Customer\"\n");
            writer.write("}\n");
            // Duplicate Customer entity
            writer.write("Figure 2\n");
            writer.write("{\n");
            writer.write("    Style \"Entity\"\n");
            writer.write("    Text \"Customer\"\n");
            writer.write("}\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        // The parser should set EdgeConvertGUI.setReadSuccess(false) due to duplicate
        assertFalse("Should fail due to duplicate table names", EdgeConvertGUI.getReadSuccess());
    }

    @Test
    public void testBlankNames() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            writer.write("Figure 1\n");
            writer.write("{\n");
            writer.write("    Style \"Entity\"\n");
            writer.write("    Text \"\"\n");
            writer.write("}\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        // The parser should set EdgeConvertGUI.setReadSuccess(false) due to blank name
        assertFalse("Should fail due to blank names", EdgeConvertGUI.getReadSuccess());
    }

    @Test
    public void testRelationsPresence() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            writer.write("Figure 1\n");
            writer.write("{\n");
            writer.write("    Style \"Relation\"\n");
            writer.write("    Text \"HasA\"\n");
            writer.write("}\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        // The parser should set EdgeConvertGUI.setReadSuccess(false) due to relations
        assertFalse("Should fail due to presence of relations", EdgeConvertGUI.getReadSuccess());
    }

    @Test
    public void testOpenInvalidFileFormat() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Invalid File Format\n");
            writer.write("Some random content\n");
        }
        
        parser = new ParseEdgeFile(tempFile);
        // Don't assert on table length, just verify that no exception is thrown
        // and that getEdgeTables() returns either null or empty array
        EdgeTable[] tables = parser.getEdgeTables();
        if (tables != null) {
            assertEquals("Should have no tables for invalid format", 0, tables.length);
        }
    }

    @Test
    public void testOpenCorruptedFile() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EDGE Diagram File\n");
            writer.write("Corrupted content without proper structure");
        }
        
        parser = new ParseEdgeFile(tempFile);
        EdgeTable[] tables = parser.getEdgeTables();
        assertEquals("Should have no tables for corrupted file", 0, tables.length);
    }

    @Test
    public void testMultipleTablesSaveFile() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EdgeConvert Save File\n");
            // First table
            writer.write("Table: 1\n");
            writer.write("{\n");
            writer.write("TableName Customer\n");
            writer.write("NativeFields 2\n");
            writer.write("RelatedTables 3\n");
            writer.write("RelatedFields \n");
            writer.write("}\n");
            // Second table
            writer.write("Table: 3\n");
            writer.write("{\n");
            writer.write("TableName Order\n");
            writer.write("NativeFields 4\n");
            writer.write("RelatedTables 1\n");
            writer.write("RelatedFields \n");
            writer.write("}\n");
            writer.write("#Fields#\n");
            writer.write("2|customerName|1|1|1|2|20|false|false\n");
            writer.write("4|orderDate|3|1|1|2|20|false|false\n");
        }
        
        parser = new ParseSaveFile(tempFile);
        EdgeTable[] tables = parser.getEdgeTables();
        EdgeField[] fields = parser.getEdgeFields();
        
        assertEquals("Should have two tables", 2, tables.length);
        assertEquals("Should have two fields", 2, fields.length);
    }

    @Test
    public void testRelatedTables() throws IOException {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("EdgeConvert Save File\n");
            writer.write("Table: 1\n");
            writer.write("{\n");
            writer.write("TableName Customer\n");
            writer.write("NativeFields 2\n");
            writer.write("RelatedTables 3\n");
            writer.write("RelatedFields 4\n");
            writer.write("}\n");
            writer.write("Table: 3\n");
            writer.write("{\n");
            writer.write("TableName Order\n");
            writer.write("NativeFields 4\n");
            writer.write("RelatedTables 1\n");
            writer.write("RelatedFields 2\n");
            writer.write("}\n");
            writer.write("#Fields#\n");
            writer.write("2|customerId|1|1|1|1|10|true|false\n");
            writer.write("4|orderId|3|1|1|1|10|true|false\n");
        }
        
        parser = new ParseSaveFile(tempFile);
        EdgeTable[] tables = parser.getEdgeTables();
        
        // Verify relationships
        assertTrue("Tables should be related", 
            Arrays.asList(tables[0].getRelatedTablesArray()).contains(3));
        assertTrue("Tables should be related", 
            Arrays.asList(tables[1].getRelatedTablesArray()).contains(1));
    }
}