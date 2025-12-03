package den_n.financeaccount.util;

import java.util.prefs.Preferences;

public class ConnectionPreferences {

    private static final String CONNECTION_PREFS_NODE = "/den_n/financeaccount/connection";
    private static final String HOST_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String DATABASE_KEY = "database";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String SCHEMA_KEY = "schema";

    private static final Preferences prefs = Preferences.userRoot().node(CONNECTION_PREFS_NODE);

    public static void saveConnectionSettings(String host, String port, String database, String username, String password, String schema) {
        prefs.put(HOST_KEY, host);
        prefs.put(PORT_KEY, port);
        prefs.put(DATABASE_KEY, database);
        prefs.put(USERNAME_KEY, username);
        prefs.put(PASSWORD_KEY, password);
        prefs.put(SCHEMA_KEY, schema);
    }

    public static String getHost() {
        return prefs.get(HOST_KEY, "localhost");
    }

    public static String getPort() {
        return prefs.get(PORT_KEY, "5454");
    }

    public static String getDatabase() {
        return prefs.get(DATABASE_KEY, "account");
    }

    public static String getUsername() {
        return prefs.get(USERNAME_KEY, "postgres");
    }

    public static String getPassword() {
        return prefs.get(PASSWORD_KEY, "root");
    }

    public static String getSchema() {
        return prefs.get(SCHEMA_KEY, "public");
    }

    public static boolean hasConnectionSettings() {
        return prefs.get(HOST_KEY, null) != null;
    }
}