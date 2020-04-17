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

    //Class Management----------------------------------------------------------------

    @Command
    public void newClass(String courseNumber, String semester, String sectionNumber,
                         String description) {

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

    @Command
    public void selectClass(String courseNumber) throws SQLException {
        String query = "SELECT * FROM Class\n" +
                "WHERE course_number=? AND term='SP20';";
        try(PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, courseNumber);
            try(ResultSet rs = stmt.executeQuery()) {
                int numSections = 0;
                while(rs.next()) {
                    numSections++;
                    if(numSections < 2) {
                        System.out.format("%d %s %s %s %d %s%n",
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getInt(5),
                                rs.getString(6));
                    } else {
                        System.err.println("ERROR: Only one section should exist");
                        return;
                    }
                }
            }
        }
    }

    @Command
    public void selectClass(String courseNumber, String term) throws SQLException {
        String query = "SELECT * FROM Class\n" +
                "WHERE course_number=? AND term=?;";
        try(PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, courseNumber);
            stmt.setString(2, term);
            try(ResultSet rs = stmt.executeQuery()) {
                int numSections = 0;
                while(rs.next()) {
                    numSections++;
                    if(numSections < 2) {
                        System.out.format("%d %s %s %s %d %s%n",
                                rs.getInt(1),
                                rs.getString(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getInt(5),
                                rs.getString(6));
                    } else {
                        System.err.println("ERROR: Only one section should exist");
                        return;
                    }
                }
            }
        }
    }

    @Command
    public void selectClass(String courseNumber, String term, String section) throws SQLException {
        String query = "SELECT * FROM Class\n" +
                "WHERE course_number=? AND term=? AND section_number=?;";
        try(PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, courseNumber);
            stmt.setString(2, term);
            stmt.setString(3, section);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    System.out.format("%d %s %s %s %d %s%n",
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getInt(5),
                            rs.getString(6));
                }
            }

        }
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

    //Category and Item Management-----------------------------------------------------

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
        try (Statement stmt = db.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.format("%s | %s%n",
                        rs.getString(1),
                        rs.getString(2));
            }
        }
    }

    //Student Management--------------------------------------------------------------


    //Grade Reporting-----------------------------------------------------------------


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
