import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ParseEdgeFile extends EdgeConvertFileParser {

    public ParseEdgeFile(File constructorFile) {
      logger.info("Initializing EdgeConvertFileParser with file: {}", constructorFile.getName());
      numFigure = 0;
      numConnector = 0;
      alTables = new ArrayList();
      alFields = new ArrayList();
      alConnectors = new ArrayList();
      isEntity = false;
      isAttribute = false;
      parseFile = constructorFile;
      numLine = 0;
      this.openFile(parseFile);
   }

    public void parseFile() throws IOException {
        logger.info("Parsing Edge file");

        while ((currentLine = br.readLine()) != null) {
            currentLine = currentLine.trim();
            if (currentLine.startsWith("Figure ")) { // this is the start of a Figure entry

                logger.debug("Processing Figure: {}", currentLine);

                numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); // get the Figure
                                                                                                   // number
                currentLine = br.readLine().trim(); // this should be "{"
                currentLine = br.readLine().trim();
                if (!currentLine.startsWith("Style")) { // this is to weed out other Figures, like Labels
                    continue;
                } else {
                    style = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); // get
                                                                                                                 // the
                                                                                                                 // Style
                                                                                                                 // parameter
                    if (style.startsWith("Relation")) { // presence of Relations implies lack of normalization
                        JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile
                                + "\ncontains relations.  Please resolve them and try again.");
                        EdgeConvertGUI.setReadSuccess(false);
                        break;
                    }
                    if (style.startsWith("Entity")) {
                        isEntity = true;
                    }
                    if (style.startsWith("Attribute")) {
                        isAttribute = true;
                    }
                    if (!(isEntity || isAttribute)) { // these are the only Figures we're interested in
                        continue;
                    }
                    currentLine = br.readLine().trim(); // this should be Text
                    text = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\""))
                            .replaceAll(" ", ""); // get the Text parameter
                    if (text.equals("")) {
                        JOptionPane.showMessageDialog(null,
                                "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
                        EdgeConvertGUI.setReadSuccess(false);
                        break;
                    }
                    int escape = text.indexOf("\\");
                    if (escape > 0) { // Edge denotes a line break as "\line", disregard anything after a backslash
                        text = text.substring(0, escape);
                    }

                    do { // advance to end of record, look for whether the text is underlined
                        currentLine = br.readLine().trim();
                        if (currentLine.startsWith("TypeUnderl")) {
                            isUnderlined = true;
                        }
                    } while (!currentLine.equals("}")); // this is the end of a Figure entry

                    if (isEntity) { // create a new EdgeTable object and add it to the alTables ArrayList
                        if (isTableDup(text)) {
                            JOptionPane.showMessageDialog(null, "There are multiple tables called " + text
                                    + " in this diagram.\nPlease rename all but one of them and try again.");
                            EdgeConvertGUI.setReadSuccess(false);
                            break;
                        }
                        alTables.add(new EdgeTable(numFigure + DELIM + text));
                    }
                    if (isAttribute) { // create a new EdgeField object and add it to the alFields ArrayList
                        tempField = new EdgeField(numFigure + DELIM + text);
                        tempField.setIsPrimaryKey(isUnderlined);
                        alFields.add(tempField);
                    }
                    // reset flags
                    isEntity = false;
                    isAttribute = false;
                    isUnderlined = false;
                }
            } // if("Figure")
            if (currentLine.startsWith("Connector ")) { // this is the start of a Connector entry

                logger.debug("Processing Connector: {}", currentLine);

                numConnector = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); // get the
                                                                                                      // Connector
                                                                                                      // number
                currentLine = br.readLine().trim(); // this should be "{"
                currentLine = br.readLine().trim(); // not interested in Style
                currentLine = br.readLine().trim(); // Figure1
                endPoint1 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
                currentLine = br.readLine().trim(); // Figure2
                endPoint2 = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
                currentLine = br.readLine().trim(); // not interested in EndPoint1
                currentLine = br.readLine().trim(); // not interested in EndPoint2
                currentLine = br.readLine().trim(); // not interested in SuppressEnd1
                currentLine = br.readLine().trim(); // not interested in SuppressEnd2
                currentLine = br.readLine().trim(); // End1
                endStyle1 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); // get
                                                                                                                 // the
                                                                                                                 // End1
                                                                                                                 // parameter
                currentLine = br.readLine().trim(); // End2
                endStyle2 = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); // get
                                                                                                                 // the
                                                                                                                 // End2
                                                                                                                 // parameter

                do { // advance to end of record
                    currentLine = br.readLine().trim();
                } while (!currentLine.equals("}")); // this is the end of a Connector entry

                alConnectors.add(new EdgeConnector(
                        numConnector + DELIM + endPoint1 + DELIM + endPoint2 + DELIM + endStyle1 + DELIM + endStyle2));
            } // if("Connector")
        } // while()

        logger.info("Finished parsing Edge file");
    }

    public void openFile(File inputFile) {

        logger.info("Opening file: {}", inputFile.getName());

        try {
            fr = new FileReader(inputFile);
            br = new BufferedReader(fr);
            // test for what kind of file we have
            currentLine = br.readLine().trim();
            numLine++;
            if (currentLine.startsWith(EDGE_ID)) { // the file chosen is an Edge Diagrammer file

                logger.info("Detected Edge Diagrammer file");

                this.parseFile(); // parse the file
                br.close();
                this.makeArrays(); // convert ArrayList objects into arrays of the appropriate Class type
                this.resolveConnectors(); // Identify nature of Connector endpoints
            } else { // unrecognized file not edge file

                logger.error("Unrecognized file format");

                JOptionPane.showMessageDialog(null, "Unrecognized file format");
            }
        } // try
        catch (FileNotFoundException fnfe) {

            logger.error("File not found: {}", inputFile.getName(), fnfe);
            System.exit(0);
        } // catch FileNotFoundException
        catch (IOException ioe) {

            logger.error("IO Exception while reading file", ioe);
            System.exit(0);
        } // catch IOException

        logger.info("Finished processing file: {}", inputFile.getName());

    } // openFile()
}
