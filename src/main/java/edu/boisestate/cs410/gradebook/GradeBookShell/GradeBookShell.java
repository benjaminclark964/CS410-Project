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
            System.out.format("Course Number | number of Students%n");
            while(rs.next()) {
                System.out.format("%s         | %s%n",
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
    public void addCategory(String categoryName, String categoryWeight) throws SQLException {
        String query = "INSERT INTO Categories(category_name, weight)\n" +
                "VALUES(?, ?);";

        int categoryId;
        db.setAutoCommit(false);

        try {
            try (PreparedStatement stmt = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, categoryName);
                stmt.setString(2, categoryWeight);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new RuntimeException("No generated keys?");
                    }
                    categoryId = rs.getInt(1);
                    System.out.format("Creating category %d%n", categoryId);
                }
            }
        } catch (SQLException | RuntimeException e) {
            db.rollback();
            throw e;
        } finally {
            db.setAutoCommit(true);
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

    @Command
    public void addItem(String itemName, String categoryName, String description, int pointValue) throws SQLException {
        String query = "INSERT INTO Items(itemname, category_name, description, point_value)\n" +
                "VALUES(?, ?, ?, ?);";

        int itemId;
        db.setAutoCommit(false);

        try{
            try(PreparedStatement stmt = db.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, itemName);
                stmt.setString(2, categoryName);
                stmt.setString(3, description);
                stmt.setInt(4, pointValue);
                stmt.executeUpdate();

                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if(!rs.next()) {
                        throw new RuntimeException("No generated keys?");
                    }
                    itemId = rs.getInt(1);
                    System.out.format("Creating new item %d%n", itemId);
                }
            }

        } catch(SQLException | RuntimeException e) {
            db.rollback();
            throw e;
        } finally {
            db.setAutoCommit(true);
        }
    }

    //Student Management--------------------------------------------------------------

    @Command
    public void addStudent(String username, String studentID, String name) throws SQLException {
        String newStudentQuery = "INSERT INTO Student(username, student_id, name, class_id)\n" +
                "VALUES(?, ?, ?, ?);";
        String studentExistsQuery = "UPDATE Student\n" +
                "SET class_id=?\n" +
                "WHERE username = ?;";
        String checkIfNameExistsQuery = "SELECT * FROM student\n" +
                "WHERE name = ?;";
        String nameDoesNotMatchStoredNameQuery = "UPDATE Student\n" +
                "SET name=?\n" +
                "WHERE username = ?;";


        int currentClassID = 2;
        db.setAutoCommit(false);

        try {
            try(PreparedStatement stmt = db.prepareStatement(newStudentQuery)) {
                stmt.setString(1, username);
                stmt.setString(2, studentID);
                stmt.setString(3, name);
                stmt.setInt(4, currentClassID);
                stmt.executeUpdate();
                System.out.format("Added new Student " + username + " to class " + currentClassID + "%n");
            }

        } catch(SQLException | RuntimeException e) {
            db.rollback();

            String usernameExistError = "ERROR: duplicate key value violates unique constraint \"student_username_key\"\n" ;
            String nameDoesExistsError = "ERROR: duplicate key value violates unique constraint \"student_pkey\"\n";

            if(e.toString().contains(usernameExistError)) {
                try(PreparedStatement stmt = db.prepareStatement(studentExistsQuery)) {
                    stmt.setInt(1, currentClassID);
                    stmt.setString(2, username);
                    stmt.executeUpdate();
                    System.out.println("Added student " + username + " to class " + currentClassID);

                    try(PreparedStatement stmt2 = db.prepareStatement(checkIfNameExistsQuery)) {
                        stmt2.setString(1, name);

                        try(ResultSet rs = stmt2.executeQuery()) {
                            if(!rs.next()) {
                                try(PreparedStatement stmt3 = db.prepareStatement(nameDoesNotMatchStoredNameQuery)) {
                                    stmt3.setString(1, name);
                                    stmt3.setString(2, username);
                                    stmt3.executeUpdate();
                                    System.err.println("Warning: Original name was changed");
                                }
                            }
                        }
                    }
                }
            } else if(e.toString().contains(nameDoesExistsError)){
               try(PreparedStatement stmt = db.prepareStatement(nameDoesNotMatchStoredNameQuery)) {
                    stmt.setString(1, name);
                    stmt.setString(2, username);
                    stmt.executeUpdate();
                    System.err.println("Warning: Original name was changed");
               }
            } else {
                System.out.println(e.toString());
            }

        } finally {
            db.setAutoCommit(true);
        }
    }

    @Command
    public void addStudent(String username) throws SQLException {
        String query = "UPDATE Student\n" +
                "SET class_id = ?\n" +
                "WHERE username = ?;";

        int currentClassID = 2;
        db.setAutoCommit(false);

        try {
            try (PreparedStatement stmt = db.prepareStatement(query)) {
                stmt.setInt(1, currentClassID);
                stmt.setString(2, username);
                int row = stmt.executeUpdate();
                System.out.format("Updated " + username + " to class " + currentClassID + "%n");
            }
        } catch(SQLException | RuntimeException e) {
            db.rollback();
            throw e;
        } finally {
            db.setAutoCommit(true);
        }
    }

    @Command
    public void showStudents() throws SQLException {
        String query = "SELECT * FROM Student\n" +
                "WHERE class_id = ?;";
        int currentClass = 2;

        try(PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1,currentClass);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    System.out.format("%s | %d | %s | %d%n",
                            rs.getString(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getInt(4));
                }
            }
        }
    }

    @Command
    public void showStudents(String subName) throws SQLException {
        String query = "SELECT * FROM student\n" +
                "WHERE LOWER(username) LIKE '%' || ? || '%'\n" +
                "OR LOWER(name) LIKE '%' || ? || '%';";

        try(PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, subName);
            stmt.setString(2, subName);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    System.out.format("%s | %d | %s | %d%n",
                            rs.getString(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getInt(4));
                }
            }
        }
    }

    @Command
    public void grade(String itemName, String username, int pointValue) throws SQLException {
        String newGradeQuery = "INSERT INTO Student_Graded_Items(itemname, username, points)\n" +
                "VALUES(?, ?, ?);";
        String updateGradeQuery = "UPDATE Student_Graded_Items\n" +
                "SET points = ?\n" +
                "WHERE itemname = ? AND username= ?;";

        db.setAutoCommit(false);
        try {
            try(PreparedStatement stmt = db.prepareStatement(newGradeQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, itemName);
                stmt.setString(2, username);
                stmt. setInt(3, pointValue);
                stmt.executeUpdate();
                System.out.format("Added grade for " + username + " on " + itemName + " Score: " + pointValue + "%n");
                if(pointValue > getPointValueOfItem(itemName)) {
                    System.err.println("Warning: point value " + pointValue + " exceeds maximum");
                }
            }

        } catch(SQLException | RuntimeException e) {
            db.rollback();
            String updateGrade = "ERROR: duplicate key value violates unique constraint \"student_graded_items_pkey\"";

            if(e.toString().contains(updateGrade)) {
                try(PreparedStatement stmt = db.prepareStatement(updateGradeQuery)) {
                    stmt.setInt(1, pointValue);
                    stmt.setString(2, itemName);
                    stmt.setString(3, username);
                    stmt.executeUpdate();
                    System.out.format("Updated " + username + " score on " + itemName + " to " + pointValue + "%n");
                    if(pointValue > getPointValueOfItem(itemName)) {
                        System.err.println("Warning: point value " + pointValue + " exceeds maximum");
                    }
                }
            }
        } finally {
            db.setAutoCommit(true);
        }
    }

    /**
     * Gets the point value of a item
     *
     * @param itemName name of item to check point value
     * @return point value of item
     * @throws SQLException
     */
    public int getPointValueOfItem(String itemName) throws SQLException{
        String query = "SELECT point_value FROM items\n" +
                "WHERE itemname = ?;";
        int pointValue = 0;

        try(PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, itemName);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    pointValue = rs.getInt(1);
                }
            }
        } catch(Exception e) {
            throw e;
        }
        return pointValue;
    }

    //Grade Reporting-----------------------------------------------------------------

    @Command
    public void studentGrades(String username) throws SQLException{
        String query = "SELECT Student_Graded_Items.itemname, Categories.category_name, username, points as grade, point_value,\n" +
                "        (SELECT (student_points::float/total_points::float)*100 as current_grade FROM\n" +
                "                            (SELECT SUM(points) as student_points, SUM(point_value) as total_points FROM Student_Graded_Items\n" +
                "                            JOIN items i on Student_Graded_Items.itemname = i.itemname\n" +
                "                            WHERE username = ?) as grade) as current_grade\n" +
                "FROM student_graded_items\n" +
                "JOIN Items USING (itemname)\n" +
                "JOIN Categories USING (category_name)\n" +
                "WHERE username = ?\n" +
                "GROUP BY (Student_Graded_Items.itemname, username), point_value, Categories.category_name;";

        try(PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    System.out.format("%s | %s | %s | %d | %d | %f%n",
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getInt(5),
                            rs.getDouble(6));
                }
            }
        }
    }

    @Command
    public void gradebook() throws SQLException {
        String query = "SELECT username, student_id, name, (SELECT (student_points::float/total_points::float)*100 as student_grade FROM\n" +
                "                            (SELECT SUM(points) as student_points, SUM(point_value) as total_points FROM Student_Graded_Items\n" +
                "                            JOIN items i on Student_Graded_Items.itemname = i.itemname\n" +
                "                            WHERE Student_Graded_Items.username = Student.username\n" +
                "                            GROUP BY username) as grade) current_grade  --This is its own column\n" +
                "FROM STUDENT\n" +
                "WHERE class_id = ?;";
        int currentClassID = 2;
        try(PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, currentClassID);

            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    System.out.format("%s | %s | %s | %f%n",
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getDouble(4));
                }
            }
        }
    }


    public static void main(String[] args) throws SQLException, IOException {
        String dbURL = args[0];

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
