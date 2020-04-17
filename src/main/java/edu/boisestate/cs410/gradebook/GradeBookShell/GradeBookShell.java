package edu.boisestate.cs410.gradebook.GradeBookShell;

import com.budhash.cliche.Command;
import com.budhash.cliche.ShellFactory;

import java.io.IOException;
import java.sql.*;

public class GradeBookShell {
    private final Connection db;

    public GradeBookShell(Connection connect) {
        db = connect;
    }

    @Command
    public void newClass(String courseNumber, String semester, String sectionNumber,
                         String description) {

    }

    @Command
    public void showClass() throws SQLException {
        String query = "SELECT * FROM Class\n" +
                "WHERE term='SP20';";
        try(Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while(rs.next()) {
                System.out.format("%s %s %s %s %s%n",
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                        );
            }
        }
    }

    @Command
    public void showCategories() throws SQLException {
        String query = "SELECT * FROM Categories;";
        try(Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while(rs.next()) {
                System.out.format("%s %s%n",
                        rs.getString(2),
                        rs.getString(3));
            }
        }
    }

    @Command
    public void showItems() throws SQLException {
        String query = "SELECT itemname, point_value FROM Items\n" +
                "GROUP BY id, category_name;";
        try(Statement stmt = db.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

            while(rs.next()) {
                System.out.format("%s | %s%n",
                        rs.getString(1),
                        rs.getString(2));
            }
        }
    }

    @Command
    public void listClasses() throws SQLException {
        String query = "SELECT course_number as class, COUNT(Student.class_id) as num_students FROM Class\n" +
                "JOIN Student USING (class_id)\n" +
                "GROUP BY course_number, class_id;";
        try(Statement stmt = db.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {
            while(rs.next()) {
                System.out.format("%s | %s%n",
                        rs.getString(1),
                        rs.getString(2));
            }
        }
    }

    public static void main(String[] args) throws SQLException, IOException {
        String dbURL = args[0];
        System.out.println(dbURL);
        try(Connection connect = DriverManager.getConnection("jdbc:" + dbURL)) {
            GradeBookShell shell = new GradeBookShell(connect);
            ShellFactory.createConsoleShell("grade-manager", "", shell).commandLoop();
            System.out.println("Connection Successful");
        } catch(Exception e) {
            System.err.println("Error: Failed to connect");
            System.err.println(e.toString());
        }
    }
}
