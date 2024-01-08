import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public final class DBSingletonInstance implements AutoCloseable {
    private volatile static DBSingletonInstance dbSingletonInstance;

    private Connection connectionToDB;

    private DBSingletonInstance() {
        try {
            Class.forName("org.sqlite.JDBC");
            connectionToDB = DriverManager.getConnection("jdbc:sqlite:/var/lib/sqlite3/list.db");
            connectionToDB.setAutoCommit(false);
            Statement statement = connectionToDB.createStatement();
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                    account   TEXT PRIMARY KEY NOT NULL,
                    user_name TEXT,
                    password  TEXT             NOT NULL,
                    token     TEXT,
                    todo_list TEXT
                    );""");
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS tokens (
                    token       TEXT   PRIMARY KEY NOT NULL,
                    account     TEXT               NOT NULL,
                    expire_time BIGINT             NOT NULL
                    );""");
            connectionToDB.commit();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static DBSingletonInstance getDbSingletonInstance() {
        if (dbSingletonInstance == null) {
            synchronized (DBSingletonInstance.class) {
                if (dbSingletonInstance == null) {
                    dbSingletonInstance = new DBSingletonInstance();
                }
            }
        }
        return dbSingletonInstance;
    }

    public Connection getConnectionToDB() {
        return connectionToDB;
    }

    @Override
    public void close() throws Exception {
        connectionToDB.close();
    }
}
