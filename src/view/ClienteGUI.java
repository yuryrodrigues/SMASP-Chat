package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import service.Cliente;
import service.Mensagem;
import service.RecebeMensagem;

/**
 *
 * @author yury-antergos
 */
public class ClienteGUI extends javax.swing.JFrame {

    // objeto do cliente
    private Cliente cliente;
    // armazena os estilos dos textos do chat
    private StyleContext styleContextChat = new StyleContext();
    // cores disponíveis para o nome do usuário
    private ArrayList<Color> coresNomeUser;
    // nome do servidor
    private String nomeServidor = "servidorChat";
    // nome da aba de mensagens globais
    private String nomeAbaGeral = "global";
    // caixas de mensagens: global e privada
    private Map<String, CaixaMensagem> caixasMensagens = new HashMap<String, CaixaMensagem>(); 
    // referencia para esta janela
    private ClienteGUI estaJanela = this;
    // codigo dos emojis que serão exibidos no chat
    String[] emojisCodigo = {
        "1f44a",
        "1f44b",
        "1f44c",
        "1f44d",
        "1f44e",
        "1f44f",
        "270c",
        "2615",
        "1f605",
        "1f621",
        "1f62b",
        "1f62d",
        "1f622",
        "1f624",
        "1f625",
        "1f60a",
        "1f60b",
        "1f60c",
        "1f60d",
        "1f61c",
        "1f61d",
        "1f602",
        "1f60e",
        "1f631",        
        "1f61e",
        "1f61f",
        "1f62a",
        "1f62c",        
        "1f62e",
        "1f62f",
        "1f61a",
        "1f61b",        
        "1f600",
        "1f601",        
        "1f603",
        "1f60f",
        "1f604",        
        "1f606",
        "1f607",
        "1f608",
        "1f609",
        "1f610",
        "1f611",
        "1f612",
        "1f613",
        "1f614",
        "1f615",
        "1f616",
        "1f617",
        "1f618",
        "1f619",
        "1f620",        
        "1f623",        
        "1f626",
        "1f627",
        "1f628",
        "1f629",
        "1f630",        
        "1f632",
        "1f633",
        "1f634",
        "1f635",
        "1f636",
        "1f637",        
        "263a",
        "1f64b",
        "1f645",
        "1f649",
        "1f648",
        "1f64a",
    };
    MensagemParaEnviar msgParaEnviar = new MensagemParaEnviar();
    // nome que o usuário utiliza no chat
    private String nomeUsuario;
    // classe que exibira as mensagens do chat
    private final ExibeMensagem exibeMensagem;
    
    /**
     * Creates new form testeGUI
     */
    public ClienteGUI() {
        initComponents();

        // oculta o painel de abas das caixas de mensagens
        jtpPainelAbasMensagem.setVisible(false);
        
        // detecta mudança de aba do painel de mensagens
        jtpPainelAbasMensagem.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // pega a cor da aba selecionada
                Color c = jtpPainelAbasMensagem.getBackgroundAt(jtpPainelAbasMensagem.getSelectedIndex());
                
                // se a aba recebeu mensagens anteriormente
                if(c.equals(Color.yellow)){
                    jtpPainelAbasMensagem.setBackgroundAt(jtpPainelAbasMensagem.getSelectedIndex(), null);
                }
                
                // rola para a última mensagem enviada
                JScrollPane jSP = (JScrollPane) jtpPainelAbasMensagem.getComponentAt(jtpPainelAbasMensagem.getSelectedIndex());
                rolaScrollBar(jSP);
                
                // se o usuário da caixa de msg privada desconectou, 
                // desabilita o botão de enviar
                habilitaBtnEnviarCaixaPrivada();
            }
        });
        
        // exibe a janela do chat
        setVisible(true);
        
        // cores disponíveis para o nome do usuário
        coresNomeUser = new ArrayList<>(asList(
            new Color(0, 153, 153), Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW, Color.CYAN, Color.DARK_GRAY, Color.BLACK
        ));
        
        // inicia o servico do cliente
        cliente = new Cliente(this);
        // inicia a classe que exibira as mensagens do chat
        exibeMensagem = new ExibeMensagem(estaJanela);
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClienteGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClienteGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClienteGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClienteGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClienteGUI();
            }
        });
    }

    // exibe mensagens para o usuário
    public void mostrarCaixaDialogo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblInfoConectando = new javax.swing.JLabel();
        lblNomeUser = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        btnEntrar = new javax.swing.JButton();
        lblUserOnline = new javax.swing.JLabel();
        jSPListaUserOnline = new javax.swing.JScrollPane();
        listUserOnline = new javax.swing.JList<>();
        jSPtxtMsgUser = new javax.swing.JScrollPane();
        txtMensagemUser = new javax.swing.JEditorPane();
        btnEnviar = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        btnPrivado = new javax.swing.JButton();
        jSPanelChat = new javax.swing.JScrollPane();
        jtpPainelAbasMensagem = new javax.swing.JTabbedPane();
        btnMostraBoxEmoji = new javax.swing.JButton();
        jSPBoxEmoji = new javax.swing.JScrollPane();
        jPanelBoxEmoji = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuServidor = new javax.swing.JMenu();
        jMenuAlterarServidor = new javax.swing.JMenuItem();
        jMenuPortaPadrao = new javax.swing.JMenuItem();
        jMenuSobre = new javax.swing.JMenu();
        jMenuSobreChat = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Secretaria do Meio Ambiente do Estado de São Paulo");
        setResizable(false);

        jPanel2.setLayout(null);

        lblInfoConectando.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        lblInfoConectando.setForeground(new java.awt.Color(0, 102, 204));
        lblInfoConectando.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoConectando.setText("conectando...");
        lblInfoConectando.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel2.add(lblInfoConectando);
        lblInfoConectando.setBounds(10, 15, 330, 20);
        lblInfoConectando.setVisible(false);

        lblNomeUser.setText("Nome:");
        lblNomeUser.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel2.add(lblNomeUser);
        lblNomeUser.setBounds(12, 16, 50, 20);

        txtNome.setMargin(new java.awt.Insets(0, 5, 0, 0));
        txtNome.setMinimumSize(new java.awt.Dimension(22, 35));
        txtNome.setPreferredSize(new java.awt.Dimension(22, 35));
        jPanel2.add(txtNome);
        txtNome.setBounds(62, 10, 190, 30);

        btnEntrar.setText("entrar");
        btnEntrar.setMargin(new java.awt.Insets(1, 0, 2, 0));
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });
        jPanel2.add(btnEntrar);
        btnEntrar.setBounds(261, 10, 79, 30);

        lblUserOnline.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        lblUserOnline.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUserOnline.setLabelFor(jSPListaUserOnline);
        lblUserOnline.setText("Usuários online");
        lblUserOnline.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(122, 138, 153)));
        lblUserOnline.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel2.add(lblUserOnline);
        lblUserOnline.setBounds(349, 50, 137, 27);

        listUserOnline.setBackground(javax.swing.UIManager.getDefaults().getColor("inactiveCaption"));
        listUserOnline.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        listUserOnline.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listUserOnline.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jSPListaUserOnline.setViewportView(listUserOnline);

        jPanel2.add(jSPListaUserOnline);
        jSPListaUserOnline.setBounds(349, 76, 138, 313);
        jSPListaUserOnline.getVerticalScrollBar().setUnitIncrement(16);

        jSPtxtMsgUser.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtMensagemUser.setBackground(javax.swing.UIManager.getDefaults().getColor("inactiveCaption"));
        txtMensagemUser.setContentType("text/html"); // NOI18N
        txtMensagemUser.setText("<html>\n  <head>\n  <style type='text/css'>\n    body{color:black; font-family: Dialog; font-size: 11px}\n  </style>\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      \n    </p>\n  </body>\n</html>\n");
        txtMensagemUser.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtMensagemUser.setEnabled(false);
        jSPtxtMsgUser.setViewportView(txtMensagemUser);

        jPanel2.add(jSPtxtMsgUser);
        jSPtxtMsgUser.setBounds(12, 319, 329, 70);
        jSPtxtMsgUser.getVerticalScrollBar().setUnitIncrement(16);

        btnEnviar.setText("enviar");
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });
        jPanel2.add(btnEnviar);
        btnEnviar.setBounds(12, 395, 280, 25);

        lblStatus.setFont(new java.awt.Font("Cantarell", 1, 15)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(153, 0, 0));
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStatus.setText("desconectado");
        jPanel2.add(lblStatus);
        lblStatus.setBounds(347, 0, 140, 50);

        btnPrivado.setText("privado");
        btnPrivado.setEnabled(false);
        btnPrivado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrivadoActionPerformed(evt);
            }
        });
        jPanel2.add(btnPrivado);
        btnPrivado.setBounds(349, 395, 138, 25);

        jSPanelChat.setAutoscrolls(true);
        jPanel2.add(jSPanelChat);
        jSPanelChat.setBounds(12, 50, 329, 263);
        jSPanelChat.getVerticalScrollBar().setUnitIncrement(16);

        jtpPainelAbasMensagem.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jPanel2.add(jtpPainelAbasMensagem);
        jtpPainelAbasMensagem.setBounds(12, 50, 328, 262);

        btnMostraBoxEmoji.setFont(new java.awt.Font("Dialog", 1, 20));
        btnMostraBoxEmoji.setEnabled(false);
        btnMostraBoxEmoji.setMargin(new java.awt.Insets(3, 2, 2, 2));
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/emojis2/1f600.png"));
        Image image = imageIcon.getImage();
        Image novaimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_DEFAULT); 
        imageIcon = new ImageIcon(novaimg); 
        btnMostraBoxEmoji.setIconTextGap(0);
        btnMostraBoxEmoji.setIcon(imageIcon);
        btnMostraBoxEmoji.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostraBoxEmojiActionPerformed(evt);
            }
        });
        jPanel2.add(btnMostraBoxEmoji);
        btnMostraBoxEmoji.setBounds(300, 395, 40, 25);

        jSPBoxEmoji.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jSPBoxEmoji.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanelBoxEmoji.setBackground(new java.awt.Color(255, 255, 255));
        jPanelBoxEmoji.setPreferredSize(new java.awt.Dimension(310, 210));

        javax.swing.GroupLayout jPanelBoxEmojiLayout = new javax.swing.GroupLayout(jPanelBoxEmoji);
        jPanelBoxEmoji.setLayout(jPanelBoxEmojiLayout);
        jPanelBoxEmojiLayout.setHorizontalGroup(
            jPanelBoxEmojiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );
        jPanelBoxEmojiLayout.setVerticalGroup(
            jPanelBoxEmojiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 210, Short.MAX_VALUE)
        );

        jSPBoxEmoji.setViewportView(jPanelBoxEmoji);

        jSPBoxEmoji.setVisible(false);
        jPanel2.add(jSPBoxEmoji);
        jSPBoxEmoji.setBounds(12, 260, 329, 130);
        jSPBoxEmoji.getVerticalScrollBar().setUnitIncrement(16);

        jMenuServidor.setText("Servidor");

        jMenuAlterarServidor.setText("Alterar servidor");
        jMenuAlterarServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAlterarServidorActionPerformed(evt);
            }
        });
        jMenuServidor.add(jMenuAlterarServidor);

        jMenuPortaPadrao.setText("Servidor padrão");
        jMenuPortaPadrao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuPortaPadraoActionPerformed(evt);
            }
        });
        jMenuServidor.add(jMenuPortaPadrao);

        jMenuBar1.add(jMenuServidor);

        jMenuSobre.setText("Sobre");

        jMenuSobreChat.setText("Chat");
        jMenuSobreChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSobreChatActionPerformed(evt);
            }
        });
        jMenuSobre.add(jMenuSobreChat);

        jMenuBar1.add(jMenuSobre);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
        if(btnEntrar.getText().equals("entrar")){
            conectar();
        }
        else{
            desconectar();
        }
    }//GEN-LAST:event_btnEntrarActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        /* a mensagem tem que ser limpa antes de retirar a tag 'span', senão
           mensagem com somente emoji não serão enviadas!
        */

        // limpa a mensagem que será enviada
        // deixando apenas o texto e os códigos dos emoji
        String msgLimpa = msgParaEnviar.limpaMsgEnviar(txtMensagemUser.getText().trim());

        // retira o parágrafo padrão, que existe mesmo sem o usuário ter digitado
        String msg_user = msgLimpa.replaceAll("<span style='texto-mensagem'>", "");
        msg_user = msg_user.replaceAll("</span><br />", "").trim();
        
        // verifica se a mensagem não está vazia
        if(msg_user.length() > 0){            
            Mensagem msg = new Mensagem();
            
            // verifica se existe alguma caixa de mensagem privada
            if(jtpPainelAbasMensagem.isVisible()){
                // pega o titulo da aba selecionada
                String tituloAba = jtpPainelAbasMensagem.getTitleAt(jtpPainelAbasMensagem.getSelectedIndex());
                
                // se a aba selecionada foi a global, ou seja, a primeira aba
                if(tituloAba.equals(jtpPainelAbasMensagem.getTitleAt(0))){
                    // deve enviar a mensagem para todos os usuários
                    msg.setAcao(Mensagem.Acao.ENVIAR_PARA_TODOS);
                }
                else{
                    // deve enviar a mensagem somente para o usuário privado
                    msg.setAcao(Mensagem.Acao.ENVIAR);
                    msg.setUsuariosEnviar(new ArrayList<>(asList(tituloAba, nomeUsuario)));
                }
            }
            // se não tiver, envia para a caixa global
            else{
                msg.setAcao(Mensagem.Acao.ENVIAR_PARA_TODOS);                
            }
        
            // define a mensagem que sera enviada
            msg.setRemetente(nomeUsuario);
            msg.setTexto(msgLimpa); // TEM QUE SER A MENSAGEM LIMPA ORIGINAL!
            
            // envia a mensagem
            cliente.envia(msg);
        }
        
        // limpa a caixa de texto
        txtMensagemUser.setText("");
        // mostra o cursor do mouse dentro da caixa de mensagens
        txtMensagemUser.requestFocus();
    }//GEN-LAST:event_btnEnviarActionPerformed
    
    private void btnPrivadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrivadoActionPerformed
        // caso tenha selecionado algum usuário da list
        if(!listUserOnline.isSelectionEmpty()){
            // nome do usuário selecionado para iniciar um chat privado
            String nomeUserChatPrivado = listUserOnline.getSelectedValue();

            criaAbaMensagemPrivada(nomeUserChatPrivado);

            // limpa a seleção
            listUserOnline.clearSelection();
        }
        else{
            mostrarCaixaDialogo("Selecione o nome de um usuário na lista");
        }
        
        // mostra o cursor do mouse dentro da caixa de mensagens
        txtMensagemUser.requestFocus();
    }//GEN-LAST:event_btnPrivadoActionPerformed

    private void jMenuPortaPadraoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuPortaPadraoActionPerformed
       // reseta o servidor para o padrão
       cliente.setHost(Cliente.HOST_PADRAO);
       cliente.setPorta(Cliente.PORTA_PADRAO);
       
       mostrarCaixaDialogo("Servidor alterado para o padrão. \nDesconecte e conecte novamente no servidor.\n");
    }//GEN-LAST:event_jMenuPortaPadraoActionPerformed

    private void jMenuSobreChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSobreChatActionPerformed
        JPanel sobre = new SobreGUI();
        
        JOptionPane.showOptionDialog(this, 
			sobre, 
		        "Sobre", 
		        JOptionPane.NO_OPTION, 
		        JOptionPane.PLAIN_MESSAGE, 
		        null, 
		        new String[]{},
		        "default");
    }//GEN-LAST:event_jMenuSobreChatActionPerformed

    private void jMenuAlterarServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAlterarServidorActionPerformed
        AlterarServidorGUI sobre = new AlterarServidorGUI(this);
        
        JOptionPane.showOptionDialog(this, 
			sobre, 
		        "Alterar servidor", 
		        JOptionPane.NO_OPTION, 
		        JOptionPane.PLAIN_MESSAGE, 
		        null, 
		        new String[]{},
		        "default");
    }//GEN-LAST:event_jMenuAlterarServidorActionPerformed

    private void btnMostraBoxEmojiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostraBoxEmojiActionPerformed
        criaCaixaEmoji();
        
        // se a caixa de emojis estiver invisível, mostra ela
        if(!jSPBoxEmoji.isVisible()){
            mostrarCaixaEmoji(true);
        }
        else{
            mostrarCaixaEmoji(false);
        }
    }//GEN-LAST:event_btnMostraBoxEmojiActionPerformed
    
    private void criaCaixaEmoji(){
        // cria a caixa de emojis, caso seja a primeira abertura
        if(jPanelBoxEmoji.getComponentCount() < 1){
            // pega a quantidade de linhas necessárias para mostrar o emoji
            // verifica se a quantidade é multipla de 8
            double modEmoji = emojisCodigo.length%8;
            int linhas;
            
            // se a quantidade não for um múltiplo de 8
            if(modEmoji>0){
                // adiciona uma linha a mais, para mostrar os emojis que não
                // completam uma fileira inteira
                linhas = emojisCodigo.length/8+1;
            }
            else{
                linhas = emojisCodigo.length/8;
            }
            
            // gera a quantidade de linhas necessárias
            for(int i=0; i<linhas; i++){
                // com 8 emojis cada
                for(int j=0; j<8; j++){ 
                    // sai do for quando atingir a quantidade de emojis totais
                    if(i==linhas-1 && j==modEmoji){
                        break;
                    }
                    
                    // redimensiona as imagens para o tamanho ideal para botões
                    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/emojis2/"+emojisCodigo[8*i+j]+".png"));
                    Image image = imageIcon.getImage();
                    Image novaimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_DEFAULT); 
                    imageIcon = new ImageIcon(novaimg); 
                    
                    // cria o botao com o emoji
                    JButton jbtn = new JButton();
                    jbtn.setBackground(new java.awt.Color(255, 255, 255));
                    jbtn.setName(emojisCodigo[8*i+j]);
                    jbtn.setIcon(imageIcon);
                    jbtn.setBorderPainted(false);
                    jbtn.setIconTextGap(0);
                    jbtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
                    jbtn.addActionListener(new java.awt.event.ActionListener() {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            msgParaEnviar.addEmojiMsg(evt, estaJanela);
                        }
                    });
                    
                    // se for o primeiro emoji da linha
                    if(j==0){
                        // adiciona uma margem entre o emoji e a borda esquerda
                        jbtn.setBounds(2, i*37, 36, 36);
                    }
                    else{
                        // adiciona o espaçamento padrão entre os emojis
                        jbtn.setBounds(39*j, i*37, 36, 36);
                    }
                    
                    // adiciona o emotica a caixa de emojis
                    jPanelBoxEmoji.add(jbtn);
                }
            }
            
            // redimensiona o painel para mostrar todos os emojis
            jPanelBoxEmoji.setPreferredSize(new Dimension(jPanelBoxEmoji.getWidth(), 37*linhas+5));
        }
    }
    
    protected void mostrarCaixaEmoji(boolean opt){
        // se a caixa de mensagens do usuário estiver visível
        if(opt){
            // oculta a caixa de msg e mostra a caixa de emojis
            jSPtxtMsgUser.setVisible(false);
            jSPBoxEmoji.setVisible(true);
            btnEnviar.setEnabled(false);
            
            // altera o tamanho dos paineis
            jSPanelChat.setSize(329, 205);
            jtpPainelAbasMensagem.setSize(328, 204);
            
            // move a barra de rolagem para o topo da caixa de emojis
            jSPBoxEmoji.getVerticalScrollBar().setValue(0);
        }
        else{
            // oculta a caixa de emojis e mostra a caixa msg
            jSPtxtMsgUser.setVisible(true);
            jSPBoxEmoji.setVisible(false);
            
            // soment habilita o botão de enviar caso o usuário da caixa de msg
            // privada esta online, ou se for uma caixa de msg geral
            habilitaBtnEnviarCaixaPrivada();
            
            // retorna os paineis ao tamanho original
            jSPanelChat.setSize(329, 263);
            jtpPainelAbasMensagem.setSize(328, 262);            
            
            // mostra o cursor do mouse dentro da caixa de mensagens
            txtMensagemUser.requestFocus();
        }
    }
    
    // conecta o usuário ao servidor
    private void conectar(){   
        String nomeUser = txtNome.getText().trim();
        
        // verifica se o usuário escolheu um nome reservado
        if(txtNome.getText().equals(nomeServidor)){
            mostrarCaixaDialogo("Não foi possível entrar no chat. \nDigite outro nome de usuário");
        }
        else if (!nomeUser.isEmpty() && nomeUser.length() >= 3) {
            ClienteGUI clGUI = this;
            
            // inicializa a conecção do usuário em uma nova thread
            // para não travar a tela
            Thread thConectando = new Thread(new Runnable() {
                @Override
                public void run() {
                    // tenta conectar ao servidor
                    if (!cliente.conecta()) {
                        mostrarCaixaDialogo("Nâo foi possível conectar ao servidor.");
                    }
                    else{
                        // cria a thread que receberá as mensagens do servidor
                        new Thread(new RecebeMensagem(cliente.getSocket(), clGUI)).start();

                        // msg para informar ao servidor que irá se conectar com este usuario
                        Mensagem msg = new Mensagem();
                        msg.setAcao(Mensagem.Acao.CONECTAR);
                        msg.setRemetente(nomeUser);

                        // envia o pedido de conexao para este usuário
                        cliente.envia(msg);
                    }
                    
                    // oculta a informação que está conectando
                    exibeInfoConectando(false);
                }
            });
            thConectando.start();
            
            // se o servidor demorar para responder deve mostrar a informação
            // que está conectando
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // tempo de tentativa de conexao até exibir a informação
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClienteGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    // verifica se ainda não conectou
                    if(thConectando.isAlive() && !btnEnviar.isEnabled()){
                        // exibe a informação que está conectando
                        exibeInfoConectando(true);
                    }
                    
                    // tempo máximo de tentativa de conexão até encerrar a thread
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClienteGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    // verifica se ainda está tentando conectar ao servidor
                    if(thConectando.isAlive()){
                        // interrompe a thread
                        thConectando.interrupt();
                        // oculta a informação de conexao
                        exibeInfoConectando(false);
                        // informa que não foi possível conectar ao servidor
                        mostrarCaixaDialogo("Nâo foi possível conectar ao servidor.");
                    }
                }
            }).start();
        } 
        else {
            mostrarCaixaDialogo("Digite um nome com pelo menos 3 letras");
        }
    }
    
    // mostra a informação de que está conectando ao servidor
    private void exibeInfoConectando(boolean opt){
        if(txtNome.isVisible() && btnEntrar.isVisible() && opt){
            // oculta o botão de entrar e o nome do usuário
            lblNomeUser.setVisible(false);
            txtNome.setVisible(false);
            btnEntrar.setVisible(false);
            
            // mostra a informação que está conectando ao servidor
            lblInfoConectando.setVisible(true);
        }
        else{
            // oculta a informação que está conectando ao servidor
            lblInfoConectando.setVisible(false);
            
            // mostra o botão de entrar e o nome do usuário
            lblNomeUser.setVisible(true);
            txtNome.setVisible(true);
            btnEntrar.setVisible(true);            
        }
    }
    
    // desconecta o usuário do servidor
    private void desconectar(){
        cliente.desconecta();
        userDesconectado();
    }
    
    // quando o usuário estiver conectar ao chat
    public void userConectado() {
        // salva o nome do usuário
        nomeUsuario = txtNome.getText();
        
        // modifica a gui
        btnEnviar.setEnabled(true);
        txtMensagemUser.setEnabled(true);
        btnMostraBoxEmoji.setEnabled(true);
        txtNome.setEnabled(false);
        btnEntrar.setText("sair");
        lblStatus.setText("conectado");
        lblStatus.setForeground(new java.awt.Color(0, 102, 0));
        
        // verifica se existe uma caixa de mensagens global
        if(caixasMensagens.isEmpty()){
            // cria uma nova caixa de mensagens
            addNovaCaixaMensagens(nomeAbaGeral);
            
            // adiciona a caixa de mensagens ao painel
            jSPanelChat.setViewportView(caixasMensagens.get(nomeAbaGeral).getCaixaMensagem());          
        }
        
        // tem que alterar a cor por ultimo, senao a caixa de mensagens é "mostrada 
        // atrasada"
        txtMensagemUser.setBackground(new java.awt.Color(255, 255, 255));
        listUserOnline.setBackground(new java.awt.Color(255, 255, 255));
        
        // msg para informar que foi conectado ao servidor com sucesso
        Mensagem msg = new Mensagem();
        msg.setRemetente(nomeServidor);
        msg.setAcao(Mensagem.Acao.CONECTAR);        
        msg.setTexto("Bem vindo ao chat :)");
        exibeMensagem.exibeMensagemGlobal(msg);
    }
    
    // quando o usuário não estiver conectar ao chat
    public void userDesconectado() {        
        // remove o estilo do nome do usuário
        styleContextChat.removeStyle(nomeUsuario);
        
        // apaga o nome do usuário
        nomeUsuario = "";
        
        // atualiza a tela
        btnEnviar.setEnabled(false);
        txtMensagemUser.setEnabled(false);
        btnMostraBoxEmoji.setEnabled(false);
        txtNome.setEnabled(true);
        btnPrivado.setEnabled(false);
        btnEntrar.setText("entrar");
        lblStatus.setText("desconectado");
        txtMensagemUser.setText("");
        listUserOnline.setListData(new String[0]);
        lblStatus.setForeground(new java.awt.Color(153, 0, 0));
        txtMensagemUser.setBackground(javax.swing.UIManager.getDefaults().getColor("inactiveCaption"));
        listUserOnline.setBackground(javax.swing.UIManager.getDefaults().getColor("inactiveCaption"));
    }
    
    // informa que o servidor foi desconectado
    public void servidorDesconectado(Mensagem msg){
        // informa que desconectou o usuário
        userDesconectado();       
        
        // adiciona quebras de linha para quando o usuário se conectar novamente
        msg.setTexto(msg.getTexto()+"\n\n");
        
        // exibe a mensagem vindo do servidor
        exibeMensagem.exibeMensagemGlobal(msg);
    }
    
    // cria uma aba para exibir mensagens privadas
    protected void criaAbaMensagemPrivada(String nomeUserChatPrivado){
        // se o painel de abas não for visivel, mostra ele
        if(!jtpPainelAbasMensagem.isVisible()){            
            // passa a caixa de mensagens para o painel de abas
            jPanel2.remove(jSPanelChat);
            jtpPainelAbasMensagem.addTab(nomeAbaGeral, jSPanelChat);
            // mostra o painel de abas
            jtpPainelAbasMensagem.setVisible(true);
        }
        
        // verifica se já existe uma caixa de mensagens privada para o user selecionado
        // se não tiver cria uma
        if(jtpPainelAbasMensagem.indexOfTab(nomeUserChatPrivado) < 0){        
            // cria uma caixa de mensagens para mostrar as mensagens privadas
            // define sua key como nome do usuário selecionado
            addNovaCaixaMensagens(nomeUserChatPrivado);

            // cria um novo painel
            JScrollPane panelMsg = new JScrollPane();
            panelMsg.setAutoscrolls(true);
            panelMsg.setBounds(12, 50, 329, 263);
            panelMsg.getVerticalScrollBar().setUnitIncrement(16);
            // adiciona a caixa de mensagens privada do usuário ao painel
            panelMsg.setViewportView(caixasMensagens.get(nomeUserChatPrivado).getCaixaMensagem());

            // adiciona o painel em uma nova aba com o nome do usuário selecionado
            jtpPainelAbasMensagem.addTab(nomeUserChatPrivado, panelMsg);
        }
        
        // deixa a aba privada selecionada no painel de abas
        jtpPainelAbasMensagem.setSelectedIndex(jtpPainelAbasMensagem.indexOfTab(nomeUserChatPrivado));
    }
    
    // cria uma nova caixa de mensagens para mostrar mensagens recebidas
    private void addNovaCaixaMensagens(String nomeCaixa){
        // cria a caixa de mensagem
        CaixaMensagem caixaMsg = new CaixaMensagem();
        
        // adiciona ao conjunto de caixas de mensagens
        caixasMensagens.put(nomeCaixa, caixaMsg);
    }
    
    // rola a barra de rolagem ate a nova mensagem
    protected void rolaScrollBar(JScrollPane jSPane)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // rola a barra de rolagem
                jSPane.getVerticalScrollBar().setValue(jSPane.getVerticalScrollBar().getMaximum());
            }
        });
    }
    
    // cores dos nomes dos usuarios
    protected void estiloNomesUser(String nomeUser){
        // estilo para os nomes dos usuários
        Style style = styleContextChat.addStyle(nomeUser, null);
        StyleConstants.setBold(style, true);
        
        // se for o usuário do chat, ele tem uma cor reservada
        if(nomeUser.equals(nomeUsuario)){
            StyleConstants.setForeground(style, coresNomeUser.get(0));
        }
        else{
            // a cor 0 é reservada do usuário deste chat
            StyleConstants.setForeground(style, coresNomeUser.get(1));
            
            // gira a lista de cores, o proximo usuario pegara a proxima cor da lista            
            Color cr = coresNomeUser.get(1);
            coresNomeUser.remove(1);
            coresNomeUser.add(cr);
        }
    }
    
    // atualiza a lista de usuários online
    public void atualizaUsuariosOnline(Mensagem msg_servidor){
        // pega a lista de usuários online
        ArrayList<String> lista = msg_servidor.getUsuariosOnline();
        
        // remove o cliente da lista de usuários online
        // tem que pegar da caixa de texto, pois a lista de usuários online
        // vem antes da confirmação de conecção do usuário
        lista.remove(txtNome.getText());
        
        // converte a lista para um array de Strings
        String[] listaUser = lista.toArray(new String[lista.size()]);
        
        // exibe a lista de usuários online
        listUserOnline.setListData(listaUser);
        
        // somente ativa o botão de privado se tiver usuários online
        if(lista.size() > 0){
            btnPrivado.setEnabled(true);
        }
        else{
            btnPrivado.setEnabled(false);
        }
        
        // informa nas caixa de msg privada se o usuário saiu do chat
        // e desabilita o botão de enviar caso tenha saído
        informaUserSaiuMensagemPrivada(lista);
    }
    
    // informa nas caixa de msg privada se o usuário saiu do chat
    // e desabilita o botão de enviar caso tenha saído
    private void informaUserSaiuMensagemPrivada(ArrayList lista){
        // pega todos os usuários que tem caixa de mensagem privada
        String[] caixasMsg = caixasMensagens.keySet().toArray(new String[caixasMensagens.keySet().size()]);
        
        for (int i = 0; i < caixasMsg.length; i++) {
            // se não for a caixa de mensagem geral
            if(!caixasMsg[i].equals(nomeAbaGeral)){
                // se o usuário da caixa de msg desconectou
                if(!lista.contains(caixasMsg[i])){
                    // informa que ele saiu do chat
                    Mensagem msgServer = new Mensagem();
                    msgServer.setRemetente(nomeServidor);
                    msgServer.setTexto(caixasMsg[i] + " saiu do chat");
                    msgServer.setAcao(Mensagem.Acao.ENVIAR);
                    msgServer.setUsuariosEnviar(new ArrayList<>(asList(caixasMsg[i])));
                    exibeMensagem.exibeMensagemPrivada(msgServer);
                    
                    // informa para a caixa de mensagem que o user desconectou
                    caixasMensagens.get(caixasMsg[i]).setUsuarioOnline(false);
                    
                    // se a caixa de mensagens selecionada for a do usuráio desconectado
                    // desabilita o botão de enviar
                    habilitaBtnEnviarCaixaPrivada();
                }
                else{
                    // informa para a caixa de mensagem que o user conectou
                    caixasMensagens.get(caixasMsg[i]).setUsuarioOnline(true);
                    
                    // se a caixa de mensagens selecionada for a do usuráio desconectado
                    // desabilita o botão de enviar
                    habilitaBtnEnviarCaixaPrivada();
                }
            }
        }
    }
        
    // se o usuário da caixa de msg privada desconectou, desabilita o botão de enviar
    protected void habilitaBtnEnviarCaixaPrivada(){
        // se alguma caixa de mensagem privada existir
        if(jtpPainelAbasMensagem.isVisible()){
            // se o usuário da caixa de msg privada desconectou
            if(!caixasMensagens.get(jtpPainelAbasMensagem.getTitleAt(jtpPainelAbasMensagem.getSelectedIndex())).isUsuarioOnline()){
                // desabilita o botão de enviar mensagem
                btnEnviar.setEnabled(false);
            }
            else{
                btnEnviar.setEnabled(true);
            }
        }
        else{
            // deixa o botão ativado
            btnEnviar.setEnabled(true);
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getNomeServidor() {
        return nomeServidor;
    }

    public Map<String, CaixaMensagem> getCaixasMensagens() {
        return caixasMensagens;
    }

    public JTabbedPane getJtpPainelAbasMensagem() {
        return jtpPainelAbasMensagem;
    }
    
    public String getNomeAbaGeral() {
        return nomeAbaGeral;
    }

    public JScrollPane getjSPanelChat() {
        return jSPanelChat;
    }

    public String[] getEmojisCodigo() {
        return emojisCodigo;
    }

    public StyleContext getStyleContextChat() {
        return styleContextChat;
    }

    public ExibeMensagem getExibeMensagem() {
        return exibeMensagem;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnMostraBoxEmoji;
    private javax.swing.JButton btnPrivado;
    private javax.swing.JMenuItem jMenuAlterarServidor;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuPortaPadrao;
    private javax.swing.JMenu jMenuServidor;
    private javax.swing.JMenu jMenuSobre;
    private javax.swing.JMenuItem jMenuSobreChat;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelBoxEmoji;
    private javax.swing.JScrollPane jSPBoxEmoji;
    private javax.swing.JScrollPane jSPListaUserOnline;
    private javax.swing.JScrollPane jSPanelChat;
    private javax.swing.JScrollPane jSPtxtMsgUser;
    private javax.swing.JTabbedPane jtpPainelAbasMensagem;
    private javax.swing.JLabel lblInfoConectando;
    private javax.swing.JLabel lblNomeUser;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblUserOnline;
    private javax.swing.JList<String> listUserOnline;
    protected javax.swing.JEditorPane txtMensagemUser;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
