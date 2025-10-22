import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistrationForm extends JFrame implements ActionListener {
    private JTextField nameField, emailField, ageField, courseField;
    private JButton registerButton, clearButton, viewRegisteredButton;

    public StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Course:"));
        courseField = new JTextField();
        add(courseField);

        registerButton = new JButton("Register");
        clearButton = new JButton("Clear");
        viewRegisteredButton = new JButton("View Registered");

        registerButton.addActionListener(this);
        clearButton.addActionListener(this);
        viewRegisteredButton.addActionListener(this);

        add(registerButton);
        add(clearButton);
        add(viewRegisteredButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            registerStudent();
        } else if (e.getSource() == clearButton) {
            clearFields();
        } else if (e.getSource() == viewRegisteredButton) {
            new ViewRegisteredStudents();
        }
    }

    private void registerStudent() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String ageText = ageField.getText().trim();
        String course = courseField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || ageText.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }

            String query = "INSERT INTO students (name, email, age, course) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, age);
            stmt.setString(4, course);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student Registered Successfully!");
            clearFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data!");
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        ageField.setText("");
        courseField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationForm().setVisible(true));
    }
}
