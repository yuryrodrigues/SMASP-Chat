package view;

import java.awt.Color;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 *
 * @author yury-antergos
 */
public class MensagemParaEnviar {
    HtmlCleaner cleaner;;
    
    public MensagemParaEnviar(){
        this.cleaner = new HtmlCleaner();        
    }
    
    /**
     * substitui as imagens dos emoji pelos seus códigos e substitui as tag 'p' por 'span
     * @param msg mensagem a ser limpa
     * @return mensagem limpa
     */
    protected String limpaMsgEnviar(String msg){        
        // formata o HTML da mensagem
        TagNode rootNode = cleaner.clean(msg);

        // busca todas as tags 'img'
        TagNode[] elementosImg = rootNode.getElementsByName("img", true);            
        
        for(int i=0; elementosImg != null && i < elementosImg.length; i++){
            // pega o codigo dos emoji, que esta dentro do atributo alt
            String codigoEmoji = elementosImg[i].getAttributeByName("alt");
            
            // inseri o codigo dentro da tag span(pai da tag img)
            cleaner.setInnerHtml(elementosImg[i].getParent(), ":"+codigoEmoji+":");
            
            // remove a tag img da mensagem
            elementosImg[i].removeFromTree();                
        }
        
        // remove a tag de style, senão será pega como se fosse texto!
        TagNode[] styleTag = rootNode.getElementsByName("style", true);
        
        if(styleTag.length > 0){
            styleTag[0].removeFromTree();
        }
        
        // busca todas as tags 'p'
        TagNode[] elementosP = rootNode.getElementsByName("p", true);            

        for(int i=0; elementosP != null && i < elementosP.length; i++){
            // remove o atributo de estilo
            elementosP[i].removeAttribute("style");               
        }
        
        // substitui as tags 'p' por 'span'
        String htmlMsg = cleaner.getInnerHtml(rootNode.getElementsByName("body", true)[0]).trim();
        htmlMsg = htmlMsg.replaceAll("<p>", "<span style='texto-mensagem'>");
        htmlMsg = htmlMsg.replaceAll("</p>", "</span><br />");
        
        // retorna o texto da msg limpo, sem tags html e com os códigos dos emoji
        return htmlMsg.trim();
    }
    
    /**
     * adiciona o emoji clicado no texto que o usuario esta digitando
     * @param evt evento do botão clicado
     * @param cliGUI janela onde está a caixa de msg do usuário
     */
    protected void addEmojiMsg(java.awt.event.ActionEvent evt, ClienteGUI cliGUI) {
        // oculta a caixa de mensagens
        cliGUI.mostrarCaixaEmoji(false);
        
        // botao do emoji clickado
        JButton btn = (JButton) evt.getSource();
        
        // url da imagem do emoji
        URL urlEmoji = getClass().getResource("/img/emojis2/"+btn.getName()+".png");
        
        // caixa de texto com a mensagem do usuário
        JEditorPane caixaMsgUser = cliGUI.txtMensagemUser;
        
        // pega o documento da caixa de texto do usuário
        HTMLDocument docCaixaMsgUser = (HTMLDocument) caixaMsgUser.getDocument();
        
        try {            
            // estilo do texto que será inserido antes da imagem do emoji
            // depois será substituido pela imagem do emoji
            StyleContext styleContextChat = new StyleContext();
            Style style = styleContextChat.addStyle("EMOJI-MSG-USER", null);
            StyleConstants.setFontFamily(style, "EMOJI-IMG");
            StyleConstants.setFontSize(style, 0);
            StyleConstants.setForeground(style, Color.WHITE);
            
            StyledDocument docStyled = (StyledDocument) caixaMsgUser.getDocument();
            docStyled.insertString(caixaMsgUser.getCaretPosition(), ".", styleContextChat.getStyle("EMOJI-MSG-USER"));
            
            // formata o HTML da mensagem
            TagNode rootNode = cleaner.clean(caixaMsgUser.getText());
            
            // busca a tag 'font' que conterá a imagem do emoji
            TagNode localInserirEmoji = rootNode.findElementByAttValue("face", "EMOJI-IMG", true, true);
            
            // adiciona a imagem do emoji dentro da tag 'font'
            cleaner.setInnerHtml(localInserirEmoji, "<span class='"+System.nanoTime()+"'><img src='"+urlEmoji+"' width='25' height='25' alt='"+btn.getName()+"' /></span>");
            
            // remove os atributos da tag 'font'
            localInserirEmoji.removeAttribute("face");
            localInserirEmoji.removeAttribute("size");
            localInserirEmoji.removeAttribute("color");
            
            // remove a tag 'font', deixando apenas a imagem do emoji
            String htmlMsg = cleaner.getInnerHtml(rootNode).trim();
            htmlMsg = htmlMsg.replaceAll("<font>", "");           
            // o último código inserido é para a próxima palavra digitada
            // pelo usuário não ficar dentro da tag span da imagem do emoji
            htmlMsg = htmlMsg.replaceAll("</font>", "&#32;");
            
            // substitui o texto da mensagem do usuário, pelo texto com o emoji
            caixaMsgUser.setText("<html>"+htmlMsg+"</html>");
            
        } catch (BadLocationException ex) {
            Logger.getLogger(ClienteGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
