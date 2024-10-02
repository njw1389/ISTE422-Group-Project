import java.awt.*;
import java.awt.event.*;
import javax.swing.*;   
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateDDLMySQL extends EdgeConvertCreateDDL {

   private static final Logger logger = LogManager.getLogger(CreateDDLMySQL.class);

   protected String databaseName;
   protected String[] strDataType = {"VARCHAR", "BOOL", "INT", "DOUBLE"};
   protected StringBuffer sb;

   public CreateDDLMySQL(EdgeTable[] inputTables, EdgeField[] inputFields) {
      super(inputTables, inputFields);
      logger.info("CreateDDLMySQL constructor called with {} tables and {} fields", inputTables.length, inputFields.length);
      sb = new StringBuffer();
      logger.debug("CreateDDLMySQL constructor called with {} tables and {} fields", inputTables.length, inputFields.length);
   }
   
   public CreateDDLMySQL() {
      logger.debug("CreateDDLMySQL default constructor called");
   }
   
   public void createDDL() {
      logger.info("Creating DDL for MySQL");
      EdgeConvertGUI.setReadSuccess(true);
      databaseName = generateDatabaseName();
      sb.append("CREATE DATABASE " + databaseName + ";\r\n");
      sb.append("USE " + databaseName + ";\r\n");
      
      for (int boundCount = 0; boundCount <= maxBound; boundCount++) {
         logger.trace("Processing bound level: {}", boundCount);
         for (int tableCount = 0; tableCount < numBoundTables.length; tableCount++) {
            if (numBoundTables[tableCount] == boundCount) {
               logger.debug("Processing table: {}", tables[tableCount].getName());
               sb.append("CREATE TABLE " + tables[tableCount].getName() + " (\r\n");
               int[] nativeFields = tables[tableCount].getNativeFieldsArray();
               int[] relatedFields = tables[tableCount].getRelatedFieldsArray();
               boolean[] primaryKey = new boolean[nativeFields.length];
               int numPrimaryKey = 0;
               int numForeignKey = 0;
               
               for (int nativeFieldCount = 0; nativeFieldCount < nativeFields.length; nativeFieldCount++) {
                  EdgeField currentField = getField(nativeFields[nativeFieldCount]);
                  logger.trace("Processing field: {}", currentField.getName());
                  sb.append("\t" + currentField.getName() + " " + strDataType[currentField.getDataType()]);
                  
                  if (currentField.getDataType() == 0) {
                     sb.append("(" + currentField.getVarcharValue() + ")");
                  }
                  if (currentField.getDisallowNull()) {
                     sb.append(" NOT NULL");
                  }
                  if (!currentField.getDefaultValue().equals("")) {
                     if (currentField.getDataType() == 1) {
                        sb.append(" DEFAULT " + convertStrBooleanToInt(currentField.getDefaultValue()));
                     } else {
                        sb.append(" DEFAULT " + currentField.getDefaultValue());
                     }
                  }
                  if (currentField.getIsPrimaryKey()) {
                     primaryKey[nativeFieldCount] = true;
                     numPrimaryKey++;
                     logger.debug("Field {} is part of the primary key", currentField.getName());
                  } else {
                     primaryKey[nativeFieldCount] = false;
                  }
                  if (currentField.getFieldBound() != 0) {
                     numForeignKey++;
                  }
                  sb.append(",\r\n");
               }
               
               if (numPrimaryKey > 0) {
                  logger.debug("Adding primary key constraint for table: {}", tables[tableCount].getName());
                  sb.append("CONSTRAINT " + tables[tableCount].getName() + "_PK PRIMARY KEY (");
                  for (int i = 0; i < primaryKey.length; i++) {
                     if (primaryKey[i]) {
                        sb.append(getField(nativeFields[i]).getName());
                        numPrimaryKey--;
                        if (numPrimaryKey > 0) {
                           sb.append(", ");
                        }
                     }
                  }
                  sb.append(")");
                  if (numForeignKey > 0) {
                     sb.append(",");
                  }
                  sb.append("\r\n");
               }
               
               if (numForeignKey > 0) {
                  logger.debug("Adding foreign key constraints for table: {}", tables[tableCount].getName());
                  int currentFK = 1;
                  for (int i = 0; i < relatedFields.length; i++) {
                     if (relatedFields[i] != 0) {
                        sb.append("CONSTRAINT " + tables[tableCount].getName() + "_FK" + currentFK +
                                  " FOREIGN KEY(" + getField(nativeFields[i]).getName() + ") REFERENCES " +
                                  getTable(getField(nativeFields[i]).getTableBound()).getName() + "(" + getField(relatedFields[i]).getName() + ")");
                        if (currentFK < numForeignKey) {
                           sb.append(",\r\n");
                        }
                        currentFK++;
                        logger.trace("Added foreign key: {} referencing {}.{}", 
                                     getField(nativeFields[i]).getName(), 
                                     getTable(getField(nativeFields[i]).getTableBound()).getName(), 
                                     getField(relatedFields[i]).getName());
                     }
                  }
                  sb.append("\r\n");
               }
               sb.append(");\r\n\r\n");
               logger.debug("Finished processing table: {}", tables[tableCount].getName());
            }
         }
      }
      logger.info("DDL creation completed");
   }

   protected int convertStrBooleanToInt(String input) {
      logger.trace("Converting boolean string '{}' to int", input);
      if (input.equals("true")) {
         return 1;
      } else {
         return 0;
      }
   }
   
   public String generateDatabaseName() {
      logger.info("Generating database name");
      String dbNameDefault = "MySQLDB";
      String databaseName = "";

      do {
         databaseName = (String)JOptionPane.showInputDialog(
                       null,
                       "Enter the database name:",
                       "Database Name",
                       JOptionPane.PLAIN_MESSAGE,
                       null,
                       null,
                       dbNameDefault);
         if (databaseName == null) {
            logger.warn("Database name selection cancelled");
            EdgeConvertGUI.setReadSuccess(false);
            return "";
         }
         if (databaseName.equals("")) {
            logger.warn("Empty database name entered");
            JOptionPane.showMessageDialog(null, "You must select a name for your database.");
         }
      } while (databaseName.equals(""));
      
      logger.info("Database name set to: {}", databaseName);
      return databaseName;
   }
   
   public String getDatabaseName() {
      logger.trace("Getting database name: {}", databaseName);
      return databaseName;
   }
   
   public String getProductName() {
      logger.trace("Getting product name: MySQL");
      return "MySQL";
   }

   public String getSQLString() {
      logger.debug("Getting SQL string");
      createDDL();
      return sb.toString();
   }
}