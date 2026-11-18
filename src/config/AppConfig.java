package config;

public class AppConfig {

    private static volatile AppConfig instance;

    private final String xmlPath = "group.xml";
    private final String xsdPath = "group.xsd";
    private final int debtMark = 2;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        AppConfig result = instance;
        if (result == null) {
            synchronized (AppConfig.class) {
                result = instance;
                if (result == null) {
                    result = new AppConfig();
                    instance = result;
                }
            }
        }
        return result;
    }

    public String getXmlPath() { return xmlPath; }
    public String getXsdPath() { return xsdPath; }
    public int getDebtMark()   { return debtMark; }
}
