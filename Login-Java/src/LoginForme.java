import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForme extends JDialog {
    private JTextField tfEmail;
    private JTextField psFeild;
    private JButton OKButton;
    private JButton cancelButton;
    private JPanel LoginPanel;

    public static User user;

    public LoginForme(JFrame parent){
        super(parent);
        setTitle("Login Forme");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(450,475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password= String.valueOf(psFeild.getText());

                user = getAuthenticationUser(email,password);
                if (user!=null){
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(LoginForme.this,
                            "Email or Password Invalid",
                            "Try again ",
                            JOptionPane.ERROR_MESSAGE );
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Authentication Canceled");
                dispose();
            }
        });
        setVisible(true);
    }

    public static void main(String[] args) {
        LoginForme loginF = new LoginForme(null);
        User user=LoginForme.user;

        if (user != null){

            System.out.println("Successfull Authentication of Name : "+user.name);
            System.out.println("Successfull Authentication of Email: "+user.email);
            System.out.println("Successfull Authentication of Address: "+user.address);
            System.out.println("Successfull Authentication of Phone: "+user.phone);

        }
    }

    private User getAuthenticationUser(String email,String password){
        User user = null;

        final String DB_URL="jdbc:mysql://localhost/Lginforme?serverTimeZone=UTC";
        final String USERNAME="root";
        final String PASSWORD="";




        try {
            Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Statement stmt=connection.createStatement();
            String sql="SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet resultSet =preparedStatement.executeQuery();


            if (resultSet.next()){



                user=new User();
                user.name=resultSet.getString("name");
                user.email=resultSet.getString("email");
                user.phone=resultSet.getString("phone");
                user.address=resultSet.getString("address");
                user.password =resultSet.getString("password");


            }

            stmt.close();
            connection.close();


        }catch (Exception e){
            e.printStackTrace();
        }

        return user;


    }


}
