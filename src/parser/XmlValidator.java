package parser;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;  // [ДОБАВЛЕНО] Импорт уровней логирования
import java.util.logging.Logger; // [ДОБАВЛЕНО] Импорт логгера

public class XmlValidator {
    private static final Logger logger = Logger.getLogger(XmlValidator.class.getName());

    public static void validate(File xml, File xsd) throws SAXException, IOException {

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(xsd);
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xml));

        logger.info("XML-документ успешно прошёл валидацию по XSD-схеме.");
    }
}