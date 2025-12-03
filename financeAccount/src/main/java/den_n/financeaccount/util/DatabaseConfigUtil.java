package den_n.financeaccount.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseConfigUtil {

    public static SessionFactory createSessionFactory() throws Exception {
        Configuration configuration = new Configuration();

        // Load connection properties from preferences
        String host = ConnectionPreferences.getHost();
        String port = ConnectionPreferences.getPort();
        String database = ConnectionPreferences.getDatabase();
        String username = ConnectionPreferences.getUsername();
        String schema = ConnectionPreferences.getSchema();
        String password = ConnectionPreferences.getPassword();
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?currentSchema=" + schema;;

        // Configure Hibernate with dynamic properties
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", username);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");

        // Add entity mappings
        configuration.addAnnotatedClass(den_n.financeaccount.module.Account.class);
        configuration.addAnnotatedClass(den_n.financeaccount.module.Category.class);
        configuration.addAnnotatedClass(den_n.financeaccount.module.Transaction.class);

        return configuration.buildSessionFactory();
    }

    public static void saveConnectionSettings(String host, String port, String database, String username, String password, String schema) {
        ConnectionPreferences.saveConnectionSettings(host, port, database, username, password, schema);
    }
}