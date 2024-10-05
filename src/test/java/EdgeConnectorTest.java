import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeConnectorTest {
    private static final Logger logger = LogManager.getLogger(EdgeConnectorTest.class);

    EdgeConnector testObj;

    @Before
    public void setUp() throws Exception {
        logger.info("Setting up EdgeConnectorTest");
        testObj = new EdgeConnector("1|2|3|testStyle1|testStyle2");
        logger.debug("Created EdgeConnector: {}", testObj);
    }

    @Test
    public void testGetNumConnector() {
        logger.info("Running testGetNumConnector");
        String opt1Str = System.getProperty("optionone");
        final long opt1;
        if (opt1Str == null) {
            opt1 = 1;
            logger.debug("optionone not set, using default value: {}", opt1);
        } else {
            opt1 = Long.parseLong(opt1Str);
            logger.debug("optionone set to: {}", opt1);
        }
        assertEquals("numConnector was intialized to 1 so it should be 1", (long)opt1, testObj.getNumConnector());
        logger.info("testGetNumConnector passed");
    }

    @Test
    public void testGetEndPoint1() {
        logger.info("Running testGetEndPoint1");
        assertEquals("EndPoint1 was intialized to 2", 2, testObj.getEndPoint1());
        logger.info("testGetEndPoint1 passed");
    }

    @Test
    public void testGetEndPoint2() {
        logger.info("Running testGetEndPoint2");
        assertEquals("EndPoint2 was intialized as 3", 3, testObj.getEndPoint2());
        logger.info("testGetEndPoint2 passed");
    }

    @Test
    public void testGetEndStyle1() {
        logger.info("Running testGetEndStyle1");
        assertEquals("endStyle1 was intialized to \"testStyle1\"", "testStyle1", testObj.getEndStyle1());
        logger.info("testGetEndStyle1 passed");
    }

    @Test
    public void testGetEndStyle2() {
        logger.info("Running testGetEndStyle2");
        assertEquals("endStyle1 was intialized to \"testStyle1\"", "testStyle2", testObj.getEndStyle2());
        logger.info("testGetEndStyle2 passed");
    }

    @Test
    public void testGetIsEP1Field() {
        logger.info("Running testGetIsEP1Field");
        assertEquals("isEP1Field should be false", false, testObj.getIsEP1Field());
        logger.info("testGetIsEP1Field passed");
    }

    @Test
    public void testGetIsEP2Field() {
        logger.info("Running testGetIsEP2Field");
        assertEquals("IsEP2Field should be false", false, testObj.getIsEP2Field());
        logger.info("testGetIsEP2Field passed");
    }

    @Test
    public void testGetIsEP1Table() {
        logger.info("Running testGetIsEP1Table");
        assertEquals("isEP1Table should be false", false, testObj.getIsEP1Table());
        logger.info("testGetIsEP1Table passed");
    }

    @Test
    public void testGetIsEP2Table() {
        logger.info("Running testGetIsEP2Table");
        assertEquals("isEP2Table should be false", false, testObj.getIsEP2Table());
        logger.info("testGetIsEP2Table passed");
    }

    @Test
    public void testSetIsEP1Field() {
        logger.info("Running testSetIsEP1Field");
        testObj.setIsEP1Field(false);
        assertEquals("isEP1Field should be what you set it to", false, testObj.getIsEP1Field());
        logger.info("testSetIsEP1Field passed");
    }

    @Test
    public void testSetIsEP2Field() {
        logger.info("Running testSetIsEP2Field");
        testObj.setIsEP2Field(false);
        assertEquals("isEP2Field should be what you set it to", false, testObj.getIsEP2Field());
        logger.info("testSetIsEP2Field passed");
    }

    @Test
    public void testSetIsEP1Table() {
        logger.info("Running testSetIsEP1Table");
        testObj.setIsEP1Table(false);
        assertEquals("isEp1Table should be what you set it to", false, testObj.getIsEP1Table());
        logger.info("testSetIsEP1Table passed");
    }

    @Test
    public void testSetIsEP2Table() {
        logger.info("Running testSetIsEP2Table");
        testObj.setIsEP2Table(false);
        assertEquals("isEp2Table should be what you set it to", false, testObj.getIsEP2Table());
        logger.info("testSetIsEP2Table passed");
    }
}