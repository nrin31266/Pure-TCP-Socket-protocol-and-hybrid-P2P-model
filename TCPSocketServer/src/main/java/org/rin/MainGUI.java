package org.rin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainGUI  extends javax.swing.JFrame{
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainGUI.class.getName());
    private static final UserManagement  userManagement = UserManagement.getInstance();
    private DefaultTableModel tableModel;
    private JTable userTable;

    public MainGUI(){
        initComponents();
        initLogic();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setSize(new java.awt.Dimension(800, 600));
//        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Key","Username", "Peer IP Address", "Peer Port"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(userTable);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void initLogic(){
        Server server = Server.getInstance(8080);
        server.setListener(port -> EventQueue.invokeLater(() -> {
            setTitle("Server is running on port: " + port);



            userManagement.setListener(new UserChangeListener() {
                @Override
                public void onUserAdded(String key, UserDto user) {
                    //Mọi thay đổi GUI từ luồng ngoài EDT → bọc trong EventQueue.invokeLater(...)
                    //Hàng đợi EDT đảm bảo cập nhật giao diện mượt mà, không lỗi race condition
                    EventQueue.invokeLater(() -> {
                        tableModel.insertRow(0, new Object[]{key, user.getUsername(), user.getIPAddress(), user.getPort()});
                    });
                }

                @Override
                public void onUserRemoved(String key) {
                    EventQueue.invokeLater(() -> {
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableModel.getValueAt(i, 0).equals(key)) {
                                tableModel.removeRow(i);
                                break;
                            }
                        }
                    });
                }

            });

        }));
        new Thread(server::startServer).start();


    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainGUI().setVisible(true));

    }
}
