/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.samuelprojetos.telas;

import java.sql.*;
import br.com.samuelprojetos.dao.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Samuel
 */
public class TelaOs extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    //a linha abaixo cria uma variavel para armazenar um texto de acordo como radion button selecionado
    private String tipo;

    /**
     * Creates new form TelaOs
     */
    public TelaOs() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void pesquisar_cli() {
        String sql = "select idcliente as Id, nome as Nome, fone as Fone from tbclientes where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesqCli.getText() + "%");
            rs = pst.executeQuery();
            tblResultPesquOs.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //a linha abaixo seta campos 
    private void setar_campos_os() {
        int setar = tblResultPesquOs.getSelectedRow();
        txtCliId.setText(tblResultPesquOs.getModel().getValueAt(setar, 0).toString());

    }

    // a linha abaixo metodo para cadastrar uma ordem de serviço
    private void emitir_os() {
        String sql = "insert into tbos (tipo,situaçao, equipamento, defeito,serviço, tecnico, valor, idcliente) values(?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboSituaçao.getSelectedItem().toString());
            pst.setString(3, txtEquipamOs.getText());
            pst.setString(4, txtDefeito.getText());
            pst.setString(5, txtServiço.getText());
            pst.setString(6, txtTecnico.getText());
            // .replace subtituir a virgula pelo pelo ponto
            pst.setString(7, txtValorTotal.getText().replace(",", "."));
            pst.setString(8, txtCliId.getText());
            //validaçao dos campos obrigatorio
            if ((txtCliId.getText().isEmpty()) || (txtEquipamOs.getText().isEmpty()) || (txtDefeito.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorio");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Ordem de serviço emitida com sucesso!!");
                    txtCliId.setText(null);
                    txtEquipamOs.setText(null);
                    txtDefeito.setText(null);
                    txtServiço.setText(null);
                    txtTecnico.setText(null);
                    txtValorTotal.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //metodo para pesquisar uma os
    private void pesquisar_os() {
        //a linha abaixo cria uma caixa de entrada do tipo JOption
        String num_os = JOptionPane.showInputDialog("Numero da Os");
        String sql = "select * from tbos where os= " + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                txtNos.setText(rs.getString(1));
                txtData.setText(rs.getString(2));

                //setando os radion buttons
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("OS")) {
                    rdiOrdems.setSelected(true);
                    tipo = "OS";
                } else {
                    rdiOrcam.setSelected(true);
                    tipo = "Orçamento";
                }
                cboSituaçao.setSelectedItem(rs.getString(4));
                txtEquipamOs.setText(rs.getString(5));
                txtDefeito.setText(rs.getString(6));
                txtServiço.setText(rs.getString(7));
                txtTecnico.setText(rs.getString(8));
                txtValorTotal.setText(rs.getString(9));
                txtCliId.setText(rs.getString(10));
                // desativar o botao adicionar
                btnAdcOs.setEnabled(false);
                txtPesqCli.setEnabled(false);
                tblResultPesquOs.setVisible(false);

            } else {
                JOptionPane.showMessageDialog(null, "Os nao cadastrada!!");
            }
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "OS invalida!!");
            //System.out.println(e);
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null,e2 );
        }
    }
    // o metodo abaixo altera uma OS
    private void alterar_os(){
        String sql ="update tbos set tipo=?, situaçao=?, equipamento=?, defeito=?, serviço=?, tecnico=?, valor=? where os = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboSituaçao.getSelectedItem().toString());
            pst.setString(3, txtEquipamOs.getText());
            pst.setString(4, txtDefeito.getText());
            pst.setString(5, txtServiço.getText());
            pst.setString(6, txtTecnico.getText());
            // .replace subtituir a virgula pelo pelo ponto
            pst.setString(7, txtValorTotal.getText().replace(",", "."));
            pst.setString(8, txtNos.getText());
            //validaçao dos campos obrigatorio
            if ((txtCliId.getText().isEmpty()) || (txtEquipamOs.getText().isEmpty()) || (txtDefeito.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorio");
            } else {
                int altera_os = pst.executeUpdate();
                if (altera_os > 0) {
                    JOptionPane.showMessageDialog(null, "Ordem de serviço alterada com sucesso!!");
                    txtNos.setText(null);
                    txtData.setText(null);
                    txtCliId.setText(null);
                    txtEquipamOs.setText(null);
                    txtDefeito.setText(null);
                    txtServiço.setText(null);
                    txtTecnico.setText(null);
                    txtValorTotal.setText(null);
                    //habilitar os campos desabitado no metodo pesquisa
                    btnAdcOs.setEnabled(true);
                    txtPesqCli.setEnabled(true);
                    tblResultPesquOs.setVisible(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblNos = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        txtNos = new javax.swing.JTextField();
        txtData = new javax.swing.JTextField();
        rdiOrcam = new javax.swing.JRadioButton();
        rdiOrdems = new javax.swing.JRadioButton();
        lblSituaçao = new javax.swing.JLabel();
        cboSituaçao = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtPesqCli = new javax.swing.JTextField();
        lblLupa = new javax.swing.JLabel();
        lblIdOs = new javax.swing.JLabel();
        txtCliId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResultPesquOs = new javax.swing.JTable();
        lblEquipamOs = new javax.swing.JLabel();
        lblDefeito = new javax.swing.JLabel();
        lblServiço = new javax.swing.JLabel();
        lblTecnico = new javax.swing.JLabel();
        lblValorTotal = new javax.swing.JLabel();
        txtEquipamOs = new javax.swing.JTextField();
        txtDefeito = new javax.swing.JTextField();
        txtServiço = new javax.swing.JTextField();
        txtTecnico = new javax.swing.JTextField();
        txtValorTotal = new javax.swing.JTextField();
        btnImprimirOs = new javax.swing.JButton();
        btnAdcOs = new javax.swing.JButton();
        btnPesqOs = new javax.swing.JButton();
        btnAlterarOs = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Os");
        setPreferredSize(new java.awt.Dimension(640, 480));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblNos.setText("N° Os");

        lblData.setText("Data");

        txtNos.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        txtNos.setEnabled(false);

        txtData.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        txtData.setEnabled(false);

        buttonGroup1.add(rdiOrcam);
        rdiOrcam.setText("Orçamento");
        rdiOrcam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdiOrcamActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdiOrdems);
        rdiOrdems.setText("Os");
        rdiOrdems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdiOrdemsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNos)
                            .addComponent(txtNos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblData)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtData)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdiOrcam)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rdiOrdems)
                        .addGap(0, 66, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNos)
                    .addComponent(lblData))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdiOrcam)
                    .addComponent(rdiOrdems))
                .addContainerGap())
        );

        lblSituaçao.setText("Situaçao");

        cboSituaçao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Na Bancada", "Entrega ok", "Orçamento REPROVADO", "Aguardando Aprovaçao", "Aguardando Peça", "Abandonado Pelo Cliente", "Retornou " }));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        txtPesqCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesqCliKeyReleased(evt);
            }
        });

        lblLupa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/lupa.png"))); // NOI18N

        lblIdOs.setText("*Id");

        txtCliId.setEnabled(false);

        tblResultPesquOs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Nome", "Fone"
            }
        ));
        tblResultPesquOs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblResultPesquOsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblResultPesquOs);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtPesqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblLupa)
                        .addGap(18, 18, 18)
                        .addComponent(lblIdOs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblLupa)
                        .addComponent(lblIdOs)
                        .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtPesqCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblEquipamOs.setText("*Equipamento");

        lblDefeito.setText("*Defeito");

        lblServiço.setText("Serviço");

        lblTecnico.setText("Tecnico");

        lblValorTotal.setText("Valor Total");

        txtValorTotal.setText("0");

        btnImprimirOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/Print.png"))); // NOI18N
        btnImprimirOs.setToolTipText("Imprimir OS");
        btnImprimirOs.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimirOs.setPreferredSize(new java.awt.Dimension(64, 64));

        btnAdcOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/create.png"))); // NOI18N
        btnAdcOs.setToolTipText("Adicionar Os");
        btnAdcOs.setPreferredSize(new java.awt.Dimension(64, 64));
        btnAdcOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdcOsActionPerformed(evt);
            }
        });

        btnPesqOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/Update.png"))); // NOI18N
        btnPesqOs.setToolTipText("Pesquisar Os");
        btnPesqOs.setPreferredSize(new java.awt.Dimension(64, 64));
        btnPesqOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesqOsActionPerformed(evt);
            }
        });

        btnAlterarOs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/read.png"))); // NOI18N
        btnAlterarOs.setToolTipText("Alterar Os");
        btnAlterarOs.setPreferredSize(new java.awt.Dimension(64, 64));
        btnAlterarOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarOsActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/samuelprojetos/icones/Delete.png"))); // NOI18N
        jButton4.setToolTipText("Delete Os");
        jButton4.setPreferredSize(new java.awt.Dimension(64, 64));

        jLabel1.setText("**Os: Ordem de Serviço");

        jLabel2.setText("*Preencher campos obrigatorios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSituaçao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboSituaçao, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblEquipamOs)
                            .addComponent(lblDefeito)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblServiço)
                                .addComponent(lblTecnico)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtServiço)
                            .addComponent(txtDefeito)
                            .addComponent(txtEquipamOs)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblValorTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtValorTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)))))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdcOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPesqOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlterarOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimirOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboSituaçao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSituaçao)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEquipamOs)
                    .addComponent(txtEquipamOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDefeito)
                    .addComponent(txtDefeito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblServiço)
                    .addComponent(txtServiço, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTecnico)
                    .addComponent(lblValorTotal)
                    .addComponent(txtTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnImprimirOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdcOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPesqOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAlterarOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );

        setBounds(0, 0, 640, 495);
    }// </editor-fold>//GEN-END:initComponents

    private void rdiOrcamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdiOrcamActionPerformed
        // atribuindo um texto a variavel  tipo selecionado
        tipo = "Orçamento";
    }//GEN-LAST:event_rdiOrcamActionPerformed

    private void txtPesqCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesqCliKeyReleased
        // Chamando o metodo pesquisar cliente
        pesquisar_cli();

    }//GEN-LAST:event_txtPesqCliKeyReleased

    private void tblResultPesquOsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblResultPesquOsMouseClicked
        // chamando o metodo setar campos
        setar_campos_os();
    }//GEN-LAST:event_tblResultPesquOsMouseClicked

    private void rdiOrdemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdiOrdemsActionPerformed
        // a linha a baixo atribui um texto a variavel tipo se o radio button estiver selecionado
        tipo = "OS";
    }//GEN-LAST:event_rdiOrdemsActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // ao abrir o forme marcar o radion button orcamento
        rdiOrcam.setSelected(true);
        tipo = "Orçamento";
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnAdcOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdcOsActionPerformed
        // chamar o metodo emitir os
        emitir_os();
    }//GEN-LAST:event_btnAdcOsActionPerformed

    private void btnPesqOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesqOsActionPerformed
        // chamando o metodo pesquisar
        pesquisar_os();
    }//GEN-LAST:event_btnPesqOsActionPerformed

    private void btnAlterarOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarOsActionPerformed
        // chamando o metodo alterar_os
        alterar_os();
    }//GEN-LAST:event_btnAlterarOsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdcOs;
    private javax.swing.JButton btnAlterarOs;
    private javax.swing.JButton btnImprimirOs;
    private javax.swing.JButton btnPesqOs;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboSituaçao;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblDefeito;
    private javax.swing.JLabel lblEquipamOs;
    private javax.swing.JLabel lblIdOs;
    private javax.swing.JLabel lblLupa;
    private javax.swing.JLabel lblNos;
    private javax.swing.JLabel lblServiço;
    private javax.swing.JLabel lblSituaçao;
    private javax.swing.JLabel lblTecnico;
    private javax.swing.JLabel lblValorTotal;
    private javax.swing.JRadioButton rdiOrcam;
    private javax.swing.JRadioButton rdiOrdems;
    private javax.swing.JTable tblResultPesquOs;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtDefeito;
    private javax.swing.JTextField txtEquipamOs;
    private javax.swing.JTextField txtNos;
    private javax.swing.JTextField txtPesqCli;
    private javax.swing.JTextField txtServiço;
    private javax.swing.JTextField txtTecnico;
    private javax.swing.JTextField txtValorTotal;
    // End of variables declaration//GEN-END:variables
}
