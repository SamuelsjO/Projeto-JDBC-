/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.samuelprojetos.telas;

import java.sql.*;
import br.com.samuelprojetos.dao.ModuloConexao;
import javax.swing.JOptionPane;
//a linha abaixo importa resurcos da biblioteca  res2xml.jar
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Samuel
 */
public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form TelaCliente
     */
    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    //metodo para adicionar cliente usuarios

    private void adicionarCli() {
        String sql = "insert into tbclientes"
                        + ""
                        + "(nome, "
                        + "endereço, "
                        + "cidade, "
                        + "bairro, "
                        + "Pais, "
                        + "fone, "
                        + "email) "
                        + "values(?,"
                        + "?,"
                        + "?,"
                        + "?,"
                        + "?,"
                        + "?,"
                        + "?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(3, txtCliEnd.getText());
            pst.setString(2, txtCliCidade.getText());
            pst.setString(4, txtCliBairro.getText());
            pst.setString(5, txtCliPais.getText());
            pst.setString(6, txtFoneCli.getText());
            pst.setString(7, txtCliEmail.getText());
            //validaçao dos campos obrigatorios
            if ((txtCliNome.getText().isEmpty()) || (txtCliCidade.getText().isEmpty())
                    || (txtFoneCli.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "*Preencha todos os campos obrigatorios!!");
            } else {
                // a linha abaixo atualiza a tabela usuario como os dados do formulario
                // a estrutura abaixo e usada para confirmar a insercao dos dados na tabela
                int adcionadocli = pst.executeUpdate();
                //System.out.println(adcionado);
                if (adcionadocli > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!!");
                    // as linhas abaixo limpam os campos
                    txtCliNome.setText(null);
                    txtCliEnd.setText(null);
                    txtCliCidade.setText(null);
                    txtCliBairro.setText(null);
                    txtCliPais.setText(null);
                    txtFoneCli.setText(null);
                    txtCliEmail.setText(null);
                 

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    //metodo para pesquisar cliente pelo nome com filtro
    private void pesquisar_cliente() {
        String sql = "select * from tbclientes where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteudo da caixa de pesquisa para o interroga
            //atençao ao % que e continuaçao do comando sql
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca re2xml.jar para preencher a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //a linha do metodo para setar os campos do formulario como conteudo da tabela
    public void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtCliEnd.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        txtCliCidade.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        txtCliBairro.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        txtCliPais.setText(tblClientes.getModel().getValueAt(setar, 5).toString());;
        txtFoneCli.setText(tblClientes.getModel().getValueAt(setar, 6).toString());
        txtCliEmail.setText(tblClientes.getModel().getValueAt(setar, 7).toString());

        // a linha abaixo desabilita o botao adicionar
        btnAdiconar.setEnabled(false);
        
    }

    // A linha abaixo altera dados dos clientes na tabela de pesquisa e cadastro de clientes
    private void alterar_cli() {
        String sql = "update tbclientes set nome=?, endereço=?, cidade=? , bairro=? , Pais=? , fone=? , email=? where idcliente=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliEnd.getText());
            pst.setString(3, txtCliCidade.getText());
            pst.setString(4, txtCliBairro.getText());
            pst.setString(5, txtCliPais.getText());
            pst.setString(6, txtFoneCli.getText());
            pst.setString(7, txtCliEmail.getText());
            pst.setString(8, txtCliId.getText());

            if ((txtCliNome.getText().isEmpty()) || (txtCliCidade.getText().isEmpty()) || (txtFoneCli.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatorios!!");
            } else {

                // a estrutura abaixo e usada para alterar a insercao dos dados na tabela
                int altera_cli = pst.executeUpdate();
                //System.out.println(adcionado);
                if (altera_cli > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente alterados com sucesso!!");
                    // as linhas abaixo limpam os campos
                    txtCliNome.setText(null);
                    txtCliEnd.setText(null);
                    txtCliCidade.setText(null);
                    txtCliBairro.setText(null);
                    txtCliPais.setText(null);
                    txtFoneCli.setText(null);
                    txtCliEmail.setText(null);

                    //aqui reabilita o botao adicionar
                    btnAdiconar.setEnabled(true);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    // a linha abaixo remover clientes
    private void remover_cli() {
        //a estrutura abaixo confirma a remoçao do usuario
        int confirma = JOptionPane.showConfirmDialog(null,"Tem certeza que deseja remover esse Cliente?", "Atençao", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbclientes where idcliente = ?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtCliId.getText());
                int apagado_cli = pst.executeUpdate();
                if (apagado_cli > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!!");
                    txtCliId.setText(null);
                    txtCliNome.setText(null);
                    txtCliEnd.setText(null);
                    txtCliCidade.setText(null);
                    txtCliBairro.setText(null);
                    txtCliPais.setText(null);
                    txtFoneCli.setText(null);
                    txtCliEmail.setText(null);
                   
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }
    }

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNome = new javax.swing.JLabel();
        lblEnd = new javax.swing.JLabel();
        lblCidade = new javax.swing.JLabel();
        lblBairro = new javax.swing.JLabel();
        lblPais = new javax.swing.JLabel();
        lblFonecli = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtCliEnd = new javax.swing.JTextField();
        txtCliCidade = new javax.swing.JTextField();
        txtCliNome = new javax.swing.JTextField();
        txtCliBairro = new javax.swing.JTextField();
        txtCliPais = new javax.swing.JTextField();
        txtFoneCli = new javax.swing.JTextField();
        txtCliEmail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnAdiconar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        txtCliPesquisar = new javax.swing.JTextField();
        lblLupa = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        lblCliId = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cliente");
        setPreferredSize(new java.awt.Dimension(640, 480));
        setRequestFocusEnabled(false);

        lblNome.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblNome.setText("*Nome");

        lblEnd.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblEnd.setText("Endereço");

        lblCidade.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblCidade.setText("*Cidade");

        lblBairro.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblBairro.setText("Bairro");

        lblPais.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblPais.setText("Pais");

        lblFonecli.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblFonecli.setText("*Fone");

        lblEmail.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblEmail.setText("Email");

        txtCliCidade.setMinimumSize(new java.awt.Dimension(6, 22));

        txtCliNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliNomeActionPerformed(evt);
            }
        });

        txtCliBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliBairroActionPerformed(evt);
            }
        });

        txtFoneCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFoneCliActionPerformed(evt);
            }
        });

        jLabel8.setText("*Preencher campos obrigatorios");

        btnAdiconar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/create.png"))); // NOI18N
        btnAdiconar.setToolTipText("Adicionar");
        btnAdiconar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdiconarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/read.png"))); // NOI18N
        btnAlterar.setToolTipText("Atualizar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/Delete.png"))); // NOI18N
        btnRemover.setToolTipText("Apagar");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        lblLupa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/lupa.png"))); // NOI18N

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        lblCliId.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lblCliId.setText("Id Cliente");

        txtCliId.setEnabled(false);
        txtCliId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCliIdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblLupa)
                .addGap(20, 20, 20)
                .addComponent(jLabel8)
                .addGap(0, 430, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblEnd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblBairro)
                                        .addGap(20, 20, 20))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lblEmail)))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtCliEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblPais))
                            .addComponent(txtCliEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(txtCliBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFonecli)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addComponent(txtFoneCli, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(btnAdiconar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCliPais, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCidade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblCliId)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtCliCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)
                        .addGap(19, 19, 19))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLupa)
                            .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCliId)
                    .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCidade)
                    .addComponent(txtCliCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEnd)
                    .addComponent(txtCliEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPais, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCliPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblBairro))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblFonecli)
                        .addComponent(txtCliBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtFoneCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblEmail))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCliEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdiconar)
                    .addComponent(btnAlterar)
                    .addComponent(btnRemover))
                .addGap(33, 33, 33))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtCliBairro, txtCliEnd});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFoneCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFoneCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFoneCliActionPerformed

    private void btnAdiconarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdiconarActionPerformed
        // adicionar cliente na tabela tbclientes
        adicionarCli();
    }//GEN-LAST:event_btnAdiconarActionPerformed
// o evento abaixo e do tipo "enuqanto for digitado em tempo real"
    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // chamar o metodo cliente pesquiasr clientes
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased
//evento que sera usado para setar os campos da tabela clicando com o mouse
    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // chamando o metodo para setar os campos
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // A linha abaixo altera dados de clientes
        alterar_cli();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void txtCliNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliNomeActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        // a linha abaixo remove dados de clientes
        remover_cli();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void txtCliIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliIdActionPerformed

    private void txtCliBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCliBairroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCliBairroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdiconar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBairro;
    private javax.swing.JLabel lblCidade;
    private javax.swing.JLabel lblCliId;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEnd;
    private javax.swing.JLabel lblFonecli;
    private javax.swing.JLabel lblLupa;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPais;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliBairro;
    private javax.swing.JTextField txtCliCidade;
    private javax.swing.JTextField txtCliEmail;
    private javax.swing.JTextField txtCliEnd;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPais;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtFoneCli;
    // End of variables declaration//GEN-END:variables
}
