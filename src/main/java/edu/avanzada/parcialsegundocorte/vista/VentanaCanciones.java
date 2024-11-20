/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package edu.avanzada.parcialsegundocorte.vista;

import edu.avanzada.parcialsegundocorte.control.ControladorVentanas;

/**Ventana de las canciones
 *
 * @author Nicolas
 */
public class VentanaCanciones extends javax.swing.JFrame {

    
    /**
     * Creates new form VentanaCanciones
     */
    public VentanaCanciones() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JTextField();
        btnSaldo = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListCanciones = new javax.swing.JList<>();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("Escuchar Canción");
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, 140, 30));

        jButton2.setText("Salir");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 400, -1, -1));

        jButton3.setText("Descargar");
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 300, -1, -1));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Total: ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 200, -1));

        jLabel2.setText("Ingrese su saldo: ");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 90, 100, -1));

        txtSaldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoActionPerformed(evt);
            }
        });
        getContentPane().add(txtSaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 90, 150, -1));

        btnSaldo.setText("Ingresar Saldo");
        btnSaldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaldoActionPerformed(evt);
            }
        });
        getContentPane().add(btnSaldo, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 130, -1, -1));

        jListCanciones.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jListCanciones);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 250, 280));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSaldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoActionPerformed

    private void btnSaldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaldoActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnSaldo;
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jButton2;
    public javax.swing.JButton jButton3;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JList<String> jListCanciones;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTextField txtSaldo;
    // End of variables declaration//GEN-END:variables
}
