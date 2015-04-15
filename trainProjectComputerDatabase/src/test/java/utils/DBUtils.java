package utils;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.dbunit.IDatabaseTester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

// TODO finir de mettre en place la bdd de test.
class DBUtils {
    private static final String CONFIG_TEST = "db.properties";
    private static final String SQL_SCHEMA_FILE = "1-SCHEMA.sql";
    private static final String SQL_DATA_FILE = "3-ENTRIES.sql";

    static {
        final Properties properties = new Properties();
        try (final InputStream is = ConnectionFactory.class
                .getClassLoader().getResourceAsStream(CONFIG_TEST)) {
            properties.load(is);
            jdbcDriver = properties.getProperty("jdbc.test.driver");
            jdbcUrl = properties.getProperty("jdbc.test.url");
            user = properties.getProperty("jdbc.test.username");
            password = properties.getProperty("jdbc.test.password");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static IDatabaseTester databaseTester;
    private static String jdbcDriver;
    private static String jdbcUrl;
    private static String user;
    private static String password;

    /**
     * Execute the sql file.
     *
     * @param file       File to execute
     * @param connection Connection
     * @throws IOException
     * @throws SQLException
     */
    private static void executeSqlFile(String file, Connection connection) throws IOException, SQLException {
        final InputStream is = ConnectionFactory.class
                .getClassLoader().getResourceAsStream(SQL_SCHEMA_FILE);
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            final StringBuilder sb = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str + "\n ");
            }
            try (final Statement stmt = connection.createStatement()) {
                stmt.execute(sb.toString());
            }
        }
    }
    
    public static void executeSqlFiles() throws IOException, SQLException {
    	executeSqlFile(SQL_SCHEMA_FILE, getConnection());
    	executeSqlFile(SQL_DATA_FILE, getConnection());
    }

    private static Connection getConnection() throws IOException, SQLException {
        final Properties properties = new Properties();
        try (final InputStream is = ConnectionFactory.class
                .getClassLoader().getResourceAsStream(CONFIG_TEST)) {
            properties.load(is);
            final String url = properties.getProperty("jdbc.test.url");
            return DriverManager.getConnection(url, properties);
        }
    }
}
