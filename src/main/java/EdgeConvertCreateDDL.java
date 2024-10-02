import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EdgeConvertCreateDDL {
    private static final Logger logger = LogManager.getLogger(EdgeConvertCreateDDL.class);

    static String[] products = {"MySQL"};
    protected EdgeTable[] tables; //master copy of EdgeTable objects
    protected EdgeField[] fields; //master copy of EdgeField objects
    protected int[] numBoundTables;
    protected int maxBound;
    protected StringBuffer sb;
    protected int selected;

    public EdgeConvertCreateDDL(EdgeTable[] tables, EdgeField[] fields) {
        logger.info("Creating EdgeConvertCreateDDL with tables and fields");
        this.tables = tables;
        this.fields = fields;
        initialize();
    }

    public EdgeConvertCreateDDL() {
        logger.info("Creating EdgeConvertCreateDDL with default constructor");
    }

    public void initialize() {
        logger.info("Initializing EdgeConvertCreateDDL");
        numBoundTables = new int[tables.length];
        maxBound = 0;
        sb = new StringBuffer();

        for (int i = 0; i < tables.length; i++) {
            int numBound = 0;
            int[] relatedFields = tables[i].getRelatedFieldsArray();
            
            logger.debug("Processing table: {}", tables[i].getName());
            
            for (int j = 0; j < relatedFields.length; j++) {
                if (relatedFields[j] != 0) {
                    numBound++;
                }
            }
            
            numBoundTables[i] = numBound;
            if (numBound > maxBound) {
                maxBound = numBound;
            }
            
            logger.debug("Table {} has {} bound fields", tables[i].getName(), numBound);
        }
        
        logger.debug("Maximum number of bound fields for any table: {}", maxBound);
    }

    protected EdgeTable getTable(int numFigure) {
        logger.trace("Getting table for figure number: {}", numFigure);
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (numFigure == tables[tIndex].getNumFigure()) {
                logger.debug("Found table: {}", tables[tIndex].getName());
                return tables[tIndex];
            }
        }
        logger.warn("No table found for figure number: {}", numFigure);
        return null;
    }

    protected EdgeField getField(int numFigure) {
        logger.trace("Getting field for figure number: {}", numFigure);
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (numFigure == fields[fIndex].getNumFigure()) {
                logger.debug("Found field: {}", fields[fIndex].getName());
                return fields[fIndex];
            }
        }
        logger.warn("No field found for figure number: {}", numFigure);
        return null;
    }

    public abstract String getDatabaseName();

    public abstract String getProductName();

    public abstract String getSQLString();

    public abstract void createDDL();
}