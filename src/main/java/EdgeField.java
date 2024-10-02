import java.util.StringTokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeField {
    private static final Logger logger = LogManager.getLogger(EdgeField.class);

    private int numFigure, tableID, tableBound, fieldBound, dataType, varcharValue;
    private String name, defaultValue;
    private boolean disallowNull, isPrimaryKey;
    private static String[] strDataType = {"Varchar", "Boolean", "Integer", "Double"};
    public static final int VARCHAR_DEFAULT_LENGTH = 1;

    public EdgeField(String inputString) {
        logger.info("Creating new EdgeField from input string");
        logger.debug("Input string: {}", inputString);
        StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
        numFigure = Integer.parseInt(st.nextToken());
        name = st.nextToken();
        tableID = 0;
        tableBound = 0;
        fieldBound = 0;
        disallowNull = false;
        isPrimaryKey = false;
        defaultValue = "";
        varcharValue = VARCHAR_DEFAULT_LENGTH;
        dataType = 0;
        logger.debug("EdgeField created: numFigure={}, name={}", numFigure, name);
    }

    public int getNumFigure() {
        logger.trace("Getting numFigure: {}", numFigure);
        return numFigure;
    }

    public String getName() {
        logger.trace("Getting name: {}", name);
        return name;
    }

    public int getTableID() {
        logger.trace("Getting tableID: {}", tableID);
        return tableID;
    }

    public void setTableID(int value) {
        logger.debug("Setting tableID to: {}", value);
        tableID = value;
    }

    public int getTableBound() {
        logger.trace("Getting tableBound: {}", tableBound);
        return tableBound;
    }

    public void setTableBound(int value) {
        logger.debug("Setting tableBound to: {}", value);
        tableBound = value;
    }

    public int getFieldBound() {
        logger.trace("Getting fieldBound: {}", fieldBound);
        return fieldBound;
    }

    public void setFieldBound(int value) {
        logger.debug("Setting fieldBound to: {}", value);
        fieldBound = value;
    }

    public boolean getDisallowNull() {
        logger.trace("Getting disallowNull: {}", disallowNull);
        return disallowNull;
    }

    public void setDisallowNull(boolean value) {
        logger.debug("Setting disallowNull to: {}", value);
        disallowNull = value;
    }

    public boolean getIsPrimaryKey() {
        logger.trace("Getting isPrimaryKey: {}", isPrimaryKey);
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(boolean value) {
        logger.debug("Setting isPrimaryKey to: {}", value);
        isPrimaryKey = value;
    }

    public String getDefaultValue() {
        logger.trace("Getting defaultValue: {}", defaultValue);
        return defaultValue;
    }

    public void setDefaultValue(String value) {
        logger.debug("Setting defaultValue to: {}", value);
        defaultValue = value;
    }

    public int getVarcharValue() {
        logger.trace("Getting varcharValue: {}", varcharValue);
        return varcharValue;
    }

    public void setVarcharValue(int value) {
        logger.debug("Attempting to set varcharValue to: {}", value);
        if (value > 0) {
            varcharValue = value;
            logger.info("varcharValue set to: {}", varcharValue);
        } else {
            logger.warn("Attempted to set invalid varcharValue: {}. Value must be > 0.", value);
        }
    }

    public int getDataType() {
        logger.trace("Getting dataType: {}", dataType);
        return dataType;
    }

    public void setDataType(int value) {
        logger.debug("Attempting to set dataType to: {}", value);
        if (value >= 0 && value < strDataType.length) {
            dataType = value;
            logger.info("dataType set to: {} ({})", dataType, strDataType[dataType]);
        } else {
            logger.warn("Attempted to set invalid dataType: {}. Value must be between 0 and {}.", value, strDataType.length - 1);
        }
    }

    public static String[] getStrDataType() {
        logger.trace("Getting strDataType array");
        return strDataType;
    }

    public String toString() {
        String result = numFigure + EdgeConvertFileParser.DELIM +
                        name + EdgeConvertFileParser.DELIM +
                        tableID + EdgeConvertFileParser.DELIM +
                        tableBound + EdgeConvertFileParser.DELIM +
                        fieldBound + EdgeConvertFileParser.DELIM +
                        dataType + EdgeConvertFileParser.DELIM +
                        varcharValue + EdgeConvertFileParser.DELIM +
                        isPrimaryKey + EdgeConvertFileParser.DELIM +
                        disallowNull + EdgeConvertFileParser.DELIM +
                        defaultValue;
        logger.debug("toString() called, returning: {}", result);
        return result;
    }
}