package com.MarcRubio_ReservaRestaurant.Vista_Controlador;

import com.MarcRubio_ReservaRestaurant.Model.ConnexioBD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Start extends JFrame{

    private JTabbedPane paned;
    private JPanel panel1, panel2, panel3, panel4 ;

    //TextFields Clients
    private JTextField dniTextField;
    private JTextField nombreTextField;
    private JTextField apellidoTextField;
    private JTextField telefonoTextField;

    private JButton selectButton;
    private JLabel selectedImageLabel;



    //TextFields Taula
    private JTextField idTaulaTextField;
    private JTextField dniClientsTextField;
    private JTextField horaTextField;
    private JTextField personesTextField;

    private JButton reserves;


    //TextFields Taula
    private JTextField dniClientsConsultaTextField;


    public Start() {

        paned = new JTabbedPane();

        panel1= new JPanel();
        panel1.setLayout(new GridLayout(7, 2));


        panel2 = new JPanel();
        panel2.setLayout(new GridLayout(5, 2));

        panel3 = new JPanel();

        panel4 = new JPanel();
        panel4.setLayout(new GridLayout(4,3));



        JFrame frame = new JFrame();

        frame.setTitle("Restaurant Can Rubio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(500,300);

//        Panel1

        JLabel dniLabel = new JLabel("DNI:");
        dniTextField = new JTextField();

        JLabel nombreLabel = new JLabel("Nom:");
        nombreTextField = new JTextField();

        JLabel apellidoLabel = new JLabel("Cognom:");
        apellidoTextField = new JTextField();

        JLabel telefonoLabel = new JLabel("Telèfon:");
        telefonoTextField = new JTextField();

        JLabel imagenLabel = new JLabel("Imatge:");
        selectButton = new JButton("Selecciona");

        selectedImageLabel = new JLabel();


        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String imagePath = selectedFile.getAbsolutePath();
                    ImageIcon imageIcon = new ImageIcon(imagePath);
                    selectedImageLabel.setIcon(imageIcon);

                }
            }
        });


        JButton guardarClient = new JButton("Envia");

        guardarClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarDatosCliente();
            }
        });


        panel1.add(dniLabel);
        panel1.add(dniTextField);
        panel1.add(nombreLabel);
        panel1.add(nombreTextField);
        panel1.add(apellidoLabel);
        panel1.add(apellidoTextField);
        panel1.add(telefonoLabel);
        panel1.add(telefonoTextField);
        panel1.add(imagenLabel);
        panel1.add(selectButton);
        panel1.add(selectedImageLabel);
        panel1.add(new JLabel());
        panel1.add(guardarClient);


//        Panel2
        JLabel idTaula = new JLabel("Numero Taula:");
        idTaulaTextField = new JTextField();

        JLabel dniClient = new JLabel("Dni Client:");
        dniClientsTextField = new JTextField();

        JLabel hora = new JLabel("Hora:");
        horaTextField = new JTextField();

        JLabel persones = new JLabel("Num persones:");
        personesTextField = new JTextField();

        JButton guardarTaula = new JButton("Envia");

        guardarTaula.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarDatosTaula();
            }
        });

        panel2.add(idTaula);
        panel2.add(idTaulaTextField);
        panel2.add(dniClient);
        panel2.add(dniClientsTextField);
        panel2.add(hora);
        panel2.add(horaTextField);
        panel2.add(persones);
        panel2.add(personesTextField);
        panel2.add(new JLabel());
        panel2.add(guardarTaula);


        //Panel3

        JButton reserves = new JButton("Mostrar Reserves");
        reserves.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarDatosReservaTaula();
            }
        });

        panel3.add(reserves);
        panel3.add(reserves);


        //Panel 4

        JLabel consulta_client = new JLabel("Dni Client:");
        dniClientsConsultaTextField = new JTextField();

        JButton consulta = new JButton("Mostrar Dades");

        consulta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dniCliente = dniClientsConsultaTextField.getText();
                mostrarDatosCliente(dniCliente);
            }
        });


        panel4.add(consulta_client);
        panel4.add(dniClientsConsultaTextField);
        panel4.add(consulta);

        paned.addTab("Registre Client",panel1);
        paned.addTab("Reserva Taula",panel2);
        paned.addTab("Reserves",panel3);
        paned.addTab("Consulta Client", panel4);

        frame.add(paned);
        frame.setVisible(true);
    }

    private void guardarDatosCliente() {
        String dni = dniTextField.getText();
        String nombre = nombreTextField.getText();
        String apellido = apellidoTextField.getText();
        String telefono = telefonoTextField.getText();
        String imatge = selectedImageLabel.getIcon() != null ? selectedImageLabel.getIcon().toString():"";

        try (Connection connection = ConnexioBD.getCon()) {

            String sql = "INSERT INTO client (dni_client, nom_client, cognom_client, tel_client, imatge) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dni);
            statement.setString(2, nombre);
            statement.setString(3, apellido);
            statement.setString(4, telefono);
            statement.setString(5, imatge);


            int filasAfectadas = statement.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Client registrat");
                limpiarFormularioClient();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el client");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error conexió base de dades.");
        }
    }

    private void mostrarDatosCliente(String dniCliente) {
        try (Connection connection = ConnexioBD.getCon()) {

            String sqlConsulta = "SELECT * FROM client WHERE dni_client = ?";
            PreparedStatement statementConsulta = connection.prepareStatement(sqlConsulta);
            statementConsulta.setString(1, dniCliente);
            ResultSet resultSet = statementConsulta.executeQuery();

            Component[] components = panel4.getComponents();
            for (Component component : components) {
                if (component instanceof JScrollPane) {
                    panel4.remove(component);
                }
            }
            panel4.revalidate();
            panel4.repaint();

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            if (resultSet.next()) {

                String dni = resultSet.getString("dni_client");
                String nom = resultSet.getString("nom_client");
                String cognom = resultSet.getString("cognom_client");
                String tel = resultSet.getString("tel_client");

//                byte[] foto = resultSet.getBytes("imatge");
//                ImageIcon imatgeMostrar = new ImageIcon(foto);
//                Image imatge = imatgeMostrar.getImage().getScaledInstance(150,150,Image.SCALE_SMOOTH);
//                ImageIcon imatgeDefinitiva = new ImageIcon(imatge);
//                JLabel imatgeLabel = new JLabel();
//                imatgeLabel.setIcon(imatgeDefinitiva);
//                imatgeLabel.setPreferredSize(new Dimension(150,150));

                String resultado = "DNI: " + dni + "\n";
                resultado += "Nom: " + nom + "\n";
                resultado += "Cognom: " + cognom + "\n";
                resultado += "Tel: " + tel + "\n";
                textArea.append(resultado);


            } else {
                textArea.append("No s'ha torbat cap client amb aquest dni: " + dniCliente);
            }

            panel4.add(scrollPane);
            panel4.revalidate();
            panel4.repaint();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error conexió base de dades");
        }
    }


    private void mostrarDatosReservaTaula() {
        try (Connection connection = ConnexioBD.getCon()) {

            String sqlConsulta = "SELECT * FROM reserva_taula";
            PreparedStatement statementConsulta = connection.prepareStatement(sqlConsulta);
            ResultSet resultSet = statementConsulta.executeQuery();


            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID Taula");
            tableModel.addColumn("Client Reserva");
            tableModel.addColumn("Hora Reserva");
            tableModel.addColumn("Num Persones");

            while (resultSet.next()) {
                int id_taula = resultSet.getInt("id_taula");
                String client_reserva = resultSet.getString("client_reserva");
                String hora_reserva = resultSet.getString("hora_reserva");
                int num_persones = resultSet.getInt("num_persones");

                Object[] row = { id_taula, client_reserva, hora_reserva, num_persones };
                tableModel.addRow(row);
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            JButton btnActualizar = new JButton("Actualizar");
            btnActualizar.addActionListener(e -> mostrarDatosReservaTaula());

            panel3.removeAll();

            panel3.add(btnActualizar);
            panel3.add(scrollPane);
            panel3.revalidate();
            panel3.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error conexió base de dades.");
        }
    }


    private void guardarDatosTaula() {
        int taula = Integer.parseInt(idTaulaTextField.getText());
        String dni_taula = dniClientsTextField.getText();
        String hora = horaTextField.getText();
        int persones = Integer.parseInt(personesTextField.getText());

        try (Connection connection = ConnexioBD.getCon()) {
            String sqlConsulta = "SELECT dni_client FROM client WHERE dni_client = ?";
            PreparedStatement statementConsulta = connection.prepareStatement(sqlConsulta);
            statementConsulta.setString(1, dni_taula);
            ResultSet resultSet = statementConsulta.executeQuery();

            if (resultSet.next()) {
                String sqlInsercion = "INSERT INTO reserva_taula (id_taula, client_reserva, hora_reserva, num_persones) VALUES (?, ?, ?, ?)";
                PreparedStatement statementInsercion = connection.prepareStatement(sqlInsercion);
                statementInsercion.setString(1, String.valueOf(taula));
                statementInsercion.setString(2, dni_taula);
                statementInsercion.setString(3, hora);
                statementInsercion.setString(4, String.valueOf(persones));

                int filasAfectadas = statementInsercion.executeUpdate();
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Taula reservada");
                    limpiarFormularioTaula();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al reservar una taula");
                }
            } else {
                JOptionPane.showMessageDialog(this, "El DNI del cliente no existe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error conexió base de dades.");
        }
    }

    private void limpiarFormularioClient() {
        dniTextField.setText("");
        nombreTextField.setText("");
        apellidoTextField.setText("");
        telefonoTextField.setText("");
        selectedImageLabel.setIcon(null);
    }

    private void limpiarFormularioTaula() {
        idTaulaTextField.setText("");
        dniClientsTextField.setText("");
        horaTextField.setText("");
        personesTextField.setText("");
    }


    public static void main(String[] args) {
        System.out.println("Comença el programa");
        ConnexioBD.ConnexioMYSQL();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Start();
            }
        });

    }
}


