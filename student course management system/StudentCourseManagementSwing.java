import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentCourseManagementSwing extends JFrame {

   
    Connection connectDB() throws Exception {
        String url = "jdbc:mysql://localhost:3306/student_database";
        String user = "root";
        String pass = "root"; // change this
        return DriverManager.getConnection(url, user, pass);
    }

    
    StudentCourseManagementSwing() {
        setTitle("Student Course Management System");
        setSize(500, 400);
        setLayout(new GridLayout(6, 1, 10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnAddStudent = new JButton("Add Student");
        JButton btnAddCourse = new JButton("Add Course");
        JButton btnEnroll = new JButton("Enroll Student");
        JButton btnAssignGrade = new JButton("Assign Grade");
        JButton btnViewGrades = new JButton("View Grades");
        JButton btnExit = new JButton("Exit");

        add(btnAddStudent);
        add(btnAddCourse);
        add(btnEnroll);
        add(btnAssignGrade);
        add(btnViewGrades);
        add(btnExit);

        btnAddStudent.addActionListener(e -> addStudent());
        btnAddCourse.addActionListener(e -> addCourse());
        btnEnroll.addActionListener(e -> enrollStudent());
        btnAssignGrade.addActionListener(e -> assignGrade());
        btnViewGrades.addActionListener(e -> viewGrades());
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

   
    void addStudent() {
        try (Connection con = connectDB()) {
            JTextField nameField = new JTextField();
            JTextField dobField = new JTextField();

            Object[] input = {
                "Name:", nameField,
                "DOB (YYYY-MM-DD):", dobField
            };
            int result = JOptionPane.showConfirmDialog(null, input, 
                    "Add Student", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String sql = "INSERT INTO students11(name,dob) VALUES(?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setString(2, dobField.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Student Added Successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }

 
    void addCourse() {
        try (Connection con = connectDB()) {
            JTextField nameField = new JTextField();
            JTextField creditsField = new JTextField();

            Object[] input = {
                "Course Name:", nameField,
                "Credits:", creditsField
            };
            int result = JOptionPane.showConfirmDialog(null, input, 
                    "Add Course", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String sql = "INSERT INTO courses1(course_name,credits) VALUES(?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, nameField.getText());
                ps.setInt(2, Integer.parseInt(creditsField.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Course Added Successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }

    
    void enrollStudent() {
        try (Connection con = connectDB()) {
            JTextField sidField = new JTextField();
            JTextField cidField = new JTextField();

            Object[] input = {
                "Student ID:", sidField,
                "Course ID:", cidField
            };
            int result = JOptionPane.showConfirmDialog(null, input, 
                    "Enroll Student", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String sql = "INSERT INTO enrollments11(student_id,course_id) VALUES(?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(sidField.getText()));
                ps.setInt(2, Integer.parseInt(cidField.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Student Enrolled Successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }

    
    void assignGrade() {
        try (Connection con = connectDB()) {
            JTextField sidField = new JTextField();
            JTextField cidField = new JTextField();
            JTextField gradeField = new JTextField();

            Object[] input = {
                "Student ID:", sidField,
                "Course ID:", cidField,
                "Grade (A/B/C):", gradeField
            };
            int result = JOptionPane.showConfirmDialog(null, input, 
                    "Assign Grade", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                CallableStatement cs = con.prepareCall("{CALL assign_grade(?,?,?)}");
                cs.setInt(1, Integer.parseInt(sidField.getText()));
                cs.setInt(2, Integer.parseInt(cidField.getText()));
                cs.setString(3, gradeField.getText());
                cs.execute();
                JOptionPane.showMessageDialog(null, "Grade Assigned Successfully!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }

   
    void viewGrades() {
        try (Connection con = connectDB()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM student_grades");

            StringBuilder sb = new StringBuilder();
            sb.append("StudentID | Name | Course | Grade\n");
            sb.append("---------------------------------\n");
            while (rs.next()) {
                sb.append(rs.getInt("student_id")+" | ");
                sb.append(rs.getString("name")+" | ");
                sb.append(rs.getString("course_name")+" | ");
                sb.append(rs.getString("grade")+"\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(textArea), 
                    "Student Grades", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: "+e.getMessage());
        }
    }

    
    public static void main(String[] args) {
        new StudentCourseManagementSwing();
}
}