package parser;

import model.Group;
import model.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DomGroupParser implements GroupParser {
    private static final Logger logger = Logger.getLogger(DomGroupParser.class.getName());

    @Override
    public Group parse(File xml) throws Exception {
        logger.log(Level.INFO, "Начинаем DOM-парсинг файла: {0}", xml.getName());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xml);

        Element root = doc.getDocumentElement();
        Group group = new Group(
                root.getAttribute("name"),
                Integer.parseInt(root.getAttribute("course"))
        );
        logger.log(Level.CONFIG, "Инициализирована группа: {0}, курс {1}", new Object[]{group.getName(), group.getCourse()});

        NodeList studentNodes = root.getElementsByTagName("student");
        for (int i = 0; i < studentNodes.getLength(); i++) {
            Element s = (Element) studentNodes.item(i);

            Student.Builder sb = Student.builder()
                    .id(Integer.parseInt(s.getAttribute("id")))
                    .lastName(getText(s, "lastName"))
                    .firstName(getText(s, "firstName"))
                    .birthDate(LocalDate.parse(getText(s, "birthDate")));

            NodeList gradeNodes = s.getElementsByTagName("grade");
            for (int j = 0; j < gradeNodes.getLength(); j++) {
                Element g = (Element) gradeNodes.item(j);
                sb.grade(g.getAttribute("subject"),
                        Integer.parseInt(g.getTextContent().trim()));
            }

            group.addStudent(sb.build());
        }

        logger.log(Level.INFO, "Парсинг завершён. Загружено студентов: {0}", group.getStudents().size());
        return group;
    }

    private String getText(Element parent, String tagName) {
        return parent.getElementsByTagName(tagName).item(0).getTextContent().trim();
    }
}