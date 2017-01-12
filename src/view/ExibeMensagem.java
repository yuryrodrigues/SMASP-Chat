package view;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import service.Mensagem;

/**
 *
 * @author yury-antergos
 */
public class ExibeMensagem {
    private ClienteGUI cGUI;
    
    public ExibeMensagem(ClienteGUI clienteGUI){
        this.cGUI = clienteGUI;
    }
    
    // exibe mensagens privadas recebidas
    public void exibeMensagemPrivada(Mensagem msg){
        // nome da aba que deve ser exibida a mensagem
        String nomeAba;
        
        // verifica se a mensagem esta vindo do próprio usuário
        if(msg.getRemetente().equals(cGUI.getNomeUsuario())){
            // pega o nome do destinatário da msg
            // ex, yury -> ugo, sendo yury o próprio usuário, então a aba será "ugo"
            nomeAba = msg.getUsuariosEnviar().get(0);
        }
        // ou se esta vindo do servidor e deve ser exibida na caixa de msg privada
        else if(msg.getRemetente().equals(cGUI.getNomeServidor())){
            nomeAba = msg.getUsuariosEnviar().get(0);
        }
        else{
            nomeAba = msg.getRemetente();
        }
        
        // verifica se existe uma caixa de mensagens privadas deste remetente
        if(!cGUI.getCaixasMensagens().containsKey(nomeAba)){
            // se não tiver, cria uma e exibe ela em uma aba do painel
            cGUI.criaAbaMensagemPrivada(nomeAba);
        }
        
        // pega a caixa de mensagem privada
        CaixaMensagem caixaMsgPrivada =  cGUI.getCaixasMensagens().get(nomeAba);
        
        // pega o painel com as abas(e caixas) de mensagens privadas
        JTabbedPane jtpPainelAbasMensagem = cGUI.getJtpPainelAbasMensagem();
        
        // verifica se deve rolar a caixa de mensagens para mostrar a que será exibida
        JScrollPane jSP = (JScrollPane) jtpPainelAbasMensagem.getComponentAt(jtpPainelAbasMensagem.indexOfTab(nomeAba));
        boolean deveRolarCaixa = deveRolarCaixa(jSP); 
        
        // exibe a mensagem na caixa de mensagens
        exibeMsgChat(msg, caixaMsgPrivada);
        
        // rola a caixa de mensagens para o final        
        if(deveRolarCaixa){
            cGUI.rolaScrollBar(jSP);
        }
        
        // se o usuário não estiver na aba da mensagem privada
        if(jtpPainelAbasMensagem.getSelectedIndex() != jtpPainelAbasMensagem.indexOfTab(nomeAba)){
            // altera a cor da aba, para indicar que chegou uma nova mensagem
            jtpPainelAbasMensagem.setBackgroundAt(jtpPainelAbasMensagem.indexOfTab(nomeAba), Color.yellow);
        }
    }
    
    // exibe as mensagens globais recebidas
    public void exibeMensagemGlobal(Mensagem msg){
        JScrollPane jSP;
        
        // pega a caixa de mensagem global
        CaixaMensagem caixaMsgGlobal =  cGUI.getCaixasMensagens().get(cGUI.getNomeAbaGeral());
        
        // pega o painel com as abas(e caixas) de mensagens privadas
        JTabbedPane jtpPainelAbasMensagem = cGUI.getJtpPainelAbasMensagem();
        
        // se tiver chat privado aberto
        if(jtpPainelAbasMensagem.isVisible()){
            // se o usuário não estiver na aba da mensagem global
            if(jtpPainelAbasMensagem.getSelectedIndex() != jtpPainelAbasMensagem.indexOfTab(cGUI.getNomeAbaGeral())){
                // altera a cor da aba, para indicar que chegou uma nova mensagem
                jtpPainelAbasMensagem.setBackgroundAt(jtpPainelAbasMensagem.indexOfTab(cGUI.getNomeAbaGeral()), Color.yellow);
            }
            
            // barra de rolagem da aba da mensagem privada
            jSP = (JScrollPane) jtpPainelAbasMensagem.getComponentAt(jtpPainelAbasMensagem.indexOfTab(cGUI.getNomeAbaGeral()));
        }
        else{
            // barra de rolagem da aba da mensagem global
            jSP = cGUI.getjSPanelChat();
        }
        
        // verifica se deve rolar a caixa de mensagens para mostrar a que será exibida
        boolean deveRolarCaixa = deveRolarCaixa(jSP);  
        
        // exibe a mensage na caixa de mensagens
        exibeMsgChat(msg, caixaMsgGlobal);
        
        // rola a caixa de mensagens para o final        
        if(deveRolarCaixa){
            cGUI.rolaScrollBar(jSP);
        }
    }
    
    // mostra as mensagens recebidas no chat
    public void exibeMsgChat(Mensagem msg, CaixaMensagem caixaMensagem){
        // pega o documento HTML da caixa de mensagem
        HTMLDocument documentCaixaMsg = (HTMLDocument) caixaMensagem.getCaixaMensagem().getDocument();
        
        // verifica se existe um estilo para o nome do usuário
        // e se a mensagem não foi enviada pelo servidor
        if(cGUI.getStyleContextChat().getStyle(msg.getRemetente()) == null && !msg.getRemetente().equals(cGUI.getNomeServidor())){
            // se não tiver, cria um
            cGUI.estiloNomesUser(msg.getRemetente());
        }
        
        try {            
            URL file;
            // procura por todos os emojis
            for(int i=0; i<cGUI.getEmojisCodigo().length;i++){
                // pega a url da imagem do imoji
                file = getClass().getResource("/img/emojis2/"+cGUI.getEmojisCodigo()[i]+".png");
                
                // substitui o código pela imagem do emoji
                msg.setTexto(msg.getTexto().replaceAll(":"+cGUI.getEmojisCodigo()[i]+":", "<img src='"+file+"' width='25' height='25'>"));
            }
            
            // se a caixa de mensagens não estiver vazia
            if(!caixaMensagem.getUltimoRemetente().isEmpty()){                
                // se a mensagem atual esta sendo enviada pelo servidor
                //    e a mensagem anterior não foi enviada pelo servidor
                // ou se a mensagem esta sendo enviado por um usuario
                //    e a mensagem anterior foi enviada pelo servidor
                // ou se é a informação de boas vindas ao chat
                // ou se é a informação que algum usuário desconectou
                /**** as mensagens do usuário já vem com uma quebra de linha! ****/
                if((msg.getRemetente().equals(cGUI.getNomeServidor()) && !caixaMensagem.getUltimoRemetente().equals(cGUI.getNomeServidor())) || (!msg.getRemetente().equals(cGUI.getNomeServidor()) && caixaMensagem.getUltimoRemetente().equals(cGUI.getNomeServidor())) || msg.getAcao().equals(Mensagem.Acao.DESCONECTAR) || msg.getAcao().equals(Mensagem.Acao.CONECTAR)){
                    // adiciona uma linha em branco antes da mensagem
                    documentCaixaMsg.insertAfterEnd(documentCaixaMsg.getCharacterElement(documentCaixaMsg.getLength()),"<br />");
                }                
            }
            
            // se quem enviou a mensagem foi o servidor
            if(msg.getRemetente().equals(cGUI.getNomeServidor())){  
                // escreve a mensagem no chat
                documentCaixaMsg.insertAfterEnd(documentCaixaMsg.getCharacterElement(documentCaixaMsg.getLength()), msg.getTexto()+"<br />");
            }
            else{
                // pega o documento da caixa de mensagens
                // para poder adicionar os estilos criados para cada usuario
                StyledDocument docStyled = (StyledDocument) documentCaixaMsg;
                
                // escreve o nome do remetente
                docStyled.insertString(docStyled.getLength(), msg.getRemetente()+": ", cGUI.getStyleContextChat().getStyle(msg.getRemetente()));               
                
                // escreve a mensagem no chat
                documentCaixaMsg.insertAfterEnd(documentCaixaMsg.getCharacterElement(documentCaixaMsg.getLength()), msg.getTexto());
            }
            
            // salva o nome do usuario que enviou a última mensagem
            caixaMensagem.setUltimoRemetente(msg.getRemetente());            
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(ClienteGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // verifica a barra de rolagem está no final do chat, ou seja,
    // se o usuário não está lendo mensagens anteriores 
    private boolean deveRolarCaixa(JScrollPane jSPane)
    {        
        // caixa do chat
        JScrollBar sb = jSPane.getVerticalScrollBar();
        // soma o tamanho da caixa de texto visível mais o valor da barra de rolagem
        int min = sb.getValue() + sb.getVisibleAmount();
        // tamanho total da barra de rolagem, ou seja, das mensagens exibidas
        int max = sb.getMaximum();
        
        // se a barra de rolagem estiver no fundo da caixa de texto retorna true
        return min == max;
    }
}
