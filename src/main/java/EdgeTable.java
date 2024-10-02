import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdgeTable {
   private static final Logger logger = LogManager.getLogger(EdgeTable.class);

   private int numFigure;
   private String name;
   private ArrayList alRelatedTables, alNativeFields;
   private int[] relatedTables, relatedFields, nativeFields;
   
   public EdgeTable(String inputString) {
      logger.info("Creating new EdgeTable from input string");
      logger.debug("Input string: {}", inputString);
      StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
      numFigure = Integer.parseInt(st.nextToken());
      name = st.nextToken();
      alRelatedTables = new ArrayList();
      alNativeFields = new ArrayList();
      logger.debug("EdgeTable created: numFigure={}, name={}", numFigure, name);
   }
   
   public int getNumFigure() {
      logger.trace("Getting numFigure: {}", numFigure);
      return numFigure;
   }
   
   public String getName() {
      logger.trace("Getting name: {}", name);
      return name;
   }
   
   public void addRelatedTable(int relatedTable) {
      logger.debug("Adding related table: {}", relatedTable);
      alRelatedTables.add(new Integer(relatedTable));
   }
   
   public int[] getRelatedTablesArray() {
      logger.trace("Getting relatedTables array");
      return relatedTables;
   }
   
   public int[] getRelatedFieldsArray() {
      logger.trace("Getting relatedFields array");
      return relatedFields;
   }
   
   public void setRelatedField(int index, int relatedValue) {
      logger.debug("Setting related field at index {} to value {}", index, relatedValue);
      relatedFields[index] = relatedValue;
   }
   
   public int[] getNativeFieldsArray() {
      logger.trace("Getting nativeFields array");
      return nativeFields;
   }

   public void addNativeField(int value) {
      logger.debug("Adding native field: {}", value);
      alNativeFields.add(new Integer(value));
   }

   public void moveFieldUp(int index) {
      logger.info("Moving field up at index: {}", index);
      if (index == 0) {
         logger.warn("Attempted to move up field at index 0, no action taken");
         return;
      }
      int tempNative = nativeFields[index - 1];
      nativeFields[index - 1] = nativeFields[index];
      nativeFields[index] = tempNative;
      int tempRelated = relatedFields[index - 1];
      relatedFields[index - 1] = relatedFields[index];
      relatedFields[index] = tempRelated;
      logger.debug("Field moved up successfully");
   }
   
   public void moveFieldDown(int index) {
      logger.info("Moving field down at index: {}", index);
      if (index == (nativeFields.length - 1)) {
         logger.warn("Attempted to move down field at last index, no action taken");
         return;
      }
      int tempNative = nativeFields[index + 1];
      nativeFields[index + 1] = nativeFields[index];
      nativeFields[index] = tempNative;
      int tempRelated = relatedFields[index + 1];
      relatedFields[index + 1] = relatedFields[index];
      relatedFields[index] = tempRelated;
      logger.debug("Field moved down successfully");
   }

   public void makeArrays() {
      logger.info("Converting ArrayLists to arrays");
      Integer[] temp;
      temp = (Integer[])alNativeFields.toArray(new Integer[alNativeFields.size()]);
      nativeFields = new int[temp.length];
      for (int i = 0; i < temp.length; i++) {
         nativeFields[i] = temp[i].intValue();
      }
      
      temp = (Integer[])alRelatedTables.toArray(new Integer[alRelatedTables.size()]);
      relatedTables = new int[temp.length];
      for (int i = 0; i < temp.length; i++) {
         relatedTables[i] = temp[i].intValue();
      }
      
      relatedFields = new int[nativeFields.length];
      for (int i = 0; i < relatedFields.length; i++) {
         relatedFields[i] = 0;
      }
      logger.debug("Arrays created: nativeFields={}, relatedTables={}, relatedFields={}", 
                   nativeFields.length, relatedTables.length, relatedFields.length);
   }

   public String toString() {
      logger.trace("toString() called");
      StringBuffer sb = new StringBuffer();
      sb.append("Table: " + numFigure + "\r\n");
      sb.append("{\r\n");
      sb.append("TableName: " + name + "\r\n");
      sb.append("NativeFields: ");
      for (int i = 0; i < nativeFields.length; i++) {
         sb.append(nativeFields[i]);
         if (i < (nativeFields.length - 1)){
            sb.append(EdgeConvertFileParser.DELIM);
         }
      }
      sb.append("\r\nRelatedTables: ");
      for (int i = 0; i < relatedTables.length; i++) {
         sb.append(relatedTables[i]);
         if (i < (relatedTables.length - 1)){
            sb.append(EdgeConvertFileParser.DELIM);
         }
      }
      sb.append("\r\nRelatedFields: ");
      for (int i = 0; i < relatedFields.length; i++) {
         sb.append(relatedFields[i]);
         if (i < (relatedFields.length - 1)){
            sb.append(EdgeConvertFileParser.DELIM);
         }
      }
      sb.append("\r\n}\r\n");
      
      logger.debug("toString() result: {}", sb.toString());
      return sb.toString();
   }
}