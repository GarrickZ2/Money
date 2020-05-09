package finalproject.db;

import finalproject.entities.Person;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ClientDB extends DBInterface{

    public ClientDB(){
        this.url = "jdbc:sqlite:client.db";
    }

    public void setConnection() throws SQLException {
        //this.conn =
        try{
            this.conn = DriverManager.getConnection(url);
            System.out.println("secceed");
        }catch (SQLException e) {
            System.out.println("failed");
            System.out.println(e.getMessage());
        }

    }
    public ArrayList<Person> selectAll() {
        String sql = "SELECT * FROM People";
        ArrayList<Person> results = new ArrayList<Person>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                Person tmp = new Person(
                        rs.getString("first"),
                        rs.getString("last"),
                        rs.getInt("age"),
                        rs.getString("city"),
                        rs.getInt("id")+"",
                        rs.getBoolean("sent")?1:0);
                results.add(tmp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

}
