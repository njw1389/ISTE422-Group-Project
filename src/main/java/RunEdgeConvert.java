import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RunEdgeConvert {
    private static final Logger logger = LogManager.getLogger(RunEdgeConvert.class);

    public static void main(String[] args) {
        logger.info("Starting EdgeConvert application");
        try {
            EdgeConvertGUI edge = new EdgeConvertGUI();
            logger.info("EdgeConvert GUI initialized");
        } catch (Exception e) {
            logger.error("Error initializing EdgeConvert GUI", e);
        }
        logger.info("EdgeConvert application finished");
    }
}