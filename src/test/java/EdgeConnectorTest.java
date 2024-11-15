import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeConnectorTest {
    private static final Logger logger = LogManager.getLogger(EdgeConnectorTest.class);
    private static final String VALID_INPUT = "1|2|3|testStyle1|testStyle2";
    private EdgeConnector testObj;

    @Before
    public void setUp() {
        logger.info("Setting up EdgeConnectorTest");
        testObj = new EdgeConnector(VALID_INPUT);
        logger.debug("Created EdgeConnector: {}", testObj);
    }

    // Constructor Tests
    @Test
    public void testValidConstruction() {
        EdgeConnector connector = new EdgeConnector(VALID_INPUT);
        assertNotNull("Constructor should create non-null object", connector);
        assertEquals("numConnector should be initialized correctly", 1, connector.getNumConnector());
        assertEquals("endPoint1 should be initialized correctly", 2, connector.getEndPoint1());
        assertEquals("endPoint2 should be initialized correctly", 3, connector.getEndPoint2());
        assertEquals("endStyle1 should be initialized correctly", "testStyle1", connector.getEndStyle1());
        assertEquals("endStyle2 should be initialized correctly", "testStyle2", connector.getEndStyle2());
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidNumericInput() {
        logger.info("Testing constructor with invalid numeric input");
        new EdgeConnector("invalid|2|3|testStyle1|testStyle2");
    }

    @Test(expected = RuntimeException.class)
    public void testInsufficientTokens() {
        logger.info("Testing constructor with insufficient tokens");
        new EdgeConnector("1|2|3|testStyle1");
    }

    // Numeric Property Tests
    @Test
    public void testNumericProperties() {
        logger.info("Testing numeric properties");
        assertEquals("numConnector should match constructor input", 1, testObj.getNumConnector());
        assertEquals("endPoint1 should match constructor input", 2, testObj.getEndPoint1());
        assertEquals("endPoint2 should match constructor input", 3, testObj.getEndPoint2());
    }

    // Style Tests
    @Test
    public void testStyleProperties() {
        logger.info("Testing style properties");
        assertEquals("endStyle1 should match constructor input", "testStyle1", testObj.getEndStyle1());
        assertEquals("endStyle2 should match constructor input", "testStyle2", testObj.getEndStyle2());
    }

    // EP1 Flag Tests
    @Test
    public void testEP1Flags() {
        logger.info("Testing EP1 flags");
        
        // Test initial states
        assertFalse("isEP1Field should initially be false", testObj.getIsEP1Field());
        assertFalse("isEP1Table should initially be false", testObj.getIsEP1Table());
        
        // Test setting flags
        testObj.setIsEP1Field(true);
        assertTrue("isEP1Field should be true after setting", testObj.getIsEP1Field());
        
        testObj.setIsEP1Table(true);
        assertTrue("isEP1Table should be true after setting", testObj.getIsEP1Table());
        
        // Test toggling flags
        testObj.setIsEP1Field(false);
        assertFalse("isEP1Field should be false after toggling", testObj.getIsEP1Field());
        
        testObj.setIsEP1Table(false);
        assertFalse("isEP1Table should be false after toggling", testObj.getIsEP1Table());
    }

    // EP2 Flag Tests
    @Test
    public void testEP2Flags() {
        logger.info("Testing EP2 flags");
        
        // Test initial states
        assertFalse("isEP2Field should initially be false", testObj.getIsEP2Field());
        assertFalse("isEP2Table should initially be false", testObj.getIsEP2Table());
        
        // Test setting flags
        testObj.setIsEP2Field(true);
        assertTrue("isEP2Field should be true after setting", testObj.getIsEP2Field());
        
        testObj.setIsEP2Table(true);
        assertTrue("isEP2Table should be true after setting", testObj.getIsEP2Table());
        
        // Test toggling flags
        testObj.setIsEP2Field(false);
        assertFalse("isEP2Field should be false after toggling", testObj.getIsEP2Field());
        
        testObj.setIsEP2Table(false);
        assertFalse("isEP2Table should be false after toggling", testObj.getIsEP2Table());
    }

    // Combined Flag State Tests
    @Test
    public void testFlagIndependence() {
        logger.info("Testing flag independence");
        
        // Set all flags to true
        testObj.setIsEP1Field(true);
        testObj.setIsEP1Table(true);
        testObj.setIsEP2Field(true);
        testObj.setIsEP2Table(true);
        
        // Verify all flags can be true simultaneously
        assertTrue("EP1Field should remain true", testObj.getIsEP1Field());
        assertTrue("EP1Table should remain true", testObj.getIsEP1Table());
        assertTrue("EP2Field should remain true", testObj.getIsEP2Field());
        assertTrue("EP2Table should remain true", testObj.getIsEP2Table());
        
        // Set individual flags to false and verify others remain unchanged
        testObj.setIsEP1Field(false);
        assertFalse("EP1Field should be false", testObj.getIsEP1Field());
        assertTrue("EP1Table should still be true", testObj.getIsEP1Table());
        assertTrue("EP2Field should still be true", testObj.getIsEP2Field());
        assertTrue("EP2Table should still be true", testObj.getIsEP2Table());
    }

    @Test
    public void testEdgeCases() {
        logger.info("Testing edge cases");
        
        // Test repeated flag toggling
        for (int i = 0; i < 5; i++) {
            testObj.setIsEP1Field(true);
            assertTrue("EP1Field should be true after setting", testObj.getIsEP1Field());
            testObj.setIsEP1Field(false);
            assertFalse("EP1Field should be false after setting", testObj.getIsEP1Field());
        }
        
        // Test setting same value multiple times
        testObj.setIsEP1Table(true);
        testObj.setIsEP1Table(true);
        assertTrue("EP1Table should remain true after multiple sets", testObj.getIsEP1Table());
    }
}