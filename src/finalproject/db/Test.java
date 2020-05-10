package finalproject.db;

import finalproject.entities.Person;

import java.sql.SQLException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws SQLException {
        ClientDB clientDB = new ClientDB("client.db");
        clientDB.setConnection();
        clientDB.updateSend("1","1");

    }
}
