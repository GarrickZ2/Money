package finalproject.db;

import finalproject.entities.Person;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBase extends DBInterface{

    public DataBase(String path) throws SQLException{
        this.url = "jdbc:sqlite:" + path;
        setConnection();
        System.out.println(url);
    }

    @Override
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

    public Person selectByID(String id) {
        String sql = "SELECT * FROM People where id ="+id;
        Person person=null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set

            person = new Person(
                    rs.getString("first"),
                    rs.getString("last"),
                    rs.getInt("age"),
                    rs.getString("city"), rs.getInt("id")+"",
                    rs.getBoolean("sent")?1:0);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return person;
    }


    public void updateSend(String id,String afterStatus){

        String sql = "UPDATE People SET sent=" +afterStatus+ " WHERE id="+id;

        try {

            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public void insertPerson(Person person){
        String sql = "INSERT INTO People (first,last,age,city,sent,id) VALUES (\""+person.getFirstName()+"\",\""+person.getLastName()+"\","+person.getAge()+",\""+person.getCity()+"\","+person.getSent()+","+person.getId()+")";

        try {

            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void resetClient(){
        ArrayList<Person> persons = selectAll();
        for(Person each: persons){
            updateSend(each.getId(), "0");
        }
    }

    public void resetServer(){
        String sql = "DELETE FROM People";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
