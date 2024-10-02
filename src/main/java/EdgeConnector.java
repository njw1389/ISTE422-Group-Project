import java.util.StringTokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeConnector {
    private static final Logger logger = LogManager.getLogger(EdgeConnector.class);

    private int numConnector, endPoint1, endPoint2;
    private String endStyle1, endStyle2;
    private boolean isEP1Field, isEP2Field, isEP1Table, isEP2Table;
    
    public EdgeConnector(String inputString) {
        logger.info("Creating new EdgeConnector from input string");
        logger.debug("Input string: {}", inputString);
        
        StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
        
        try {
            numConnector = Integer.parseInt(st.nextToken());
            endPoint1 = Integer.parseInt(st.nextToken());
            endPoint2 = Integer.parseInt(st.nextToken());
            endStyle1 = st.nextToken();
            endStyle2 = st.nextToken();
            
            logger.debug("Parsed values: numConnector={}, endPoint1={}, endPoint2={}, endStyle1={}, endStyle2={}",
                         numConnector, endPoint1, endPoint2, endStyle1, endStyle2);
        } catch (NumberFormatException e) {
            logger.error("Error parsing numeric values from input string", e);
            throw e; // Re-throw the exception to maintain original behavior
        }
        
        isEP1Field = false;
        isEP2Field = false;
        isEP1Table = false;
        isEP2Table = false;
        
        logger.trace("Initialized boolean flags to false");
    }
    
    public int getNumConnector() {
        logger.trace("Getting numConnector: {}", numConnector);
        return numConnector;
    }
    
    public int getEndPoint1() {
        logger.trace("Getting endPoint1: {}", endPoint1);
        return endPoint1;
    }
    
    public int getEndPoint2() {
        logger.trace("Getting endPoint2: {}", endPoint2);
        return endPoint2;
    }
    
    public String getEndStyle1() {
        logger.trace("Getting endStyle1: {}", endStyle1);
        return endStyle1;
    }
    
    public String getEndStyle2() {
        logger.trace("Getting endStyle2: {}", endStyle2);
        return endStyle2;
    }
    
    public boolean getIsEP1Field() {
        logger.trace("Getting isEP1Field: {}", isEP1Field);
        return isEP1Field;
    }
    
    public boolean getIsEP2Field() {
        logger.trace("Getting isEP2Field: {}", isEP2Field);
        return isEP2Field;
    }
    
    public boolean getIsEP1Table() {
        logger.trace("Getting isEP1Table: {}", isEP1Table);
        return isEP1Table;
    }
    
    public boolean getIsEP2Table() {
        logger.trace("Getting isEP2Table: {}", isEP2Table);
        return isEP2Table;
    }
    
    public void setIsEP1Field(boolean value) {
        logger.debug("Setting isEP1Field to: {}", value);
        isEP1Field = value;
    }
    
    public void setIsEP2Field(boolean value) {
        logger.debug("Setting isEP2Field to: {}", value);
        isEP2Field = value;
    }
    
    public void setIsEP1Table(boolean value) {
        logger.debug("Setting isEP1Table to: {}", value);
        isEP1Table = value;
    }
    
    public void setIsEP2Table(boolean value) {
        logger.debug("Setting isEP2Table to: {}", value);
        isEP2Table = value;
    }
}