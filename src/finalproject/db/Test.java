package finalproject.db;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        DataBase dataBase = new DataBase("server.db");
        dataBase.resetServer();

    }
}
