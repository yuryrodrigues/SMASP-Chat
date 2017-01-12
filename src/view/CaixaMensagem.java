package view;

import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author yury-antergos
 */
public class CaixaMensagem {
    private JTextPane caixaMensagem;
    private String ultimoRemetente;
    private boolean usuarioOnline;

    public CaixaMensagem(){
        // cria a nova caixa de mensagens
        caixaMensagem = new JTextPane();
        caixaMensagem.setEditable(false);
        caixaMensagem.setEnabled(true);
        caixaMensagem.setBackground(new java.awt.Color(255, 255, 255));
        caixaMensagem.setMargin(new java.awt.Insets(3, 5, 3, 3));
        caixaMensagem.setContentType("text/html");
        
        // define o documento que conterá os textos da caixa de mensagens        
        caixaMensagem.setDocument(new HTMLDocument());
        caixaMensagem.setEditorKit(new HTMLEditorKit());
        
        // cria os estilos dos textos adiconados em formato html
        caixaMensagem.setText(
            "<html>"
            + "<style type='text/css'>"
            + "body{color:black; font-family: Dialog; font-size: 10px}"
            + "</style>"
            + "<body>"
        );
        
        // define o último remetente como vazio
        ultimoRemetente = "";
        
        // indica que o usuário está online
        usuarioOnline = true;
    }
    
    public JTextPane getCaixaMensagem() {
        return caixaMensagem;
    }

    public String getUltimoRemetente() {
        return ultimoRemetente;
    }

    public void setUltimoRemetente(String ultimoRemetente) {
        this.ultimoRemetente = ultimoRemetente;
    }

    public boolean isUsuarioOnline() {
        return usuarioOnline;
    }

    public void setUsuarioOnline(boolean usuarioOnline) {
        this.usuarioOnline = usuarioOnline;
    }
    
}
