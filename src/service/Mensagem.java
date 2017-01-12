package service;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author yury-antergos
 */
public class Mensagem implements Serializable {
    // remetente do usuario que esta enviando a mensagem
    private String remetente;
    // texto da mensagem
    private String texto;
    // nomes dos destinatarios
    private ArrayList<String> usuariosEnviar;
    // acao que deve ser tomada pelo servidor ou cliente
    private Acao acao;
    // lista com os usuarios que estao online
    private ArrayList<String> usuariosOnline;
    
    public enum Acao {
        CONECTAR, DESCONECTAR, ENVIAR, ENVIAR_PARA_TODOS, USUARIOS_ONLINE;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String nome) {
        this.remetente = nome;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public ArrayList<String>getUsuariosEnviar() {
        return usuariosEnviar;
    }

    public void setUsuariosEnviar(ArrayList<String> usuariosEnviar) {
        this.usuariosEnviar = usuariosEnviar;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public ArrayList<String> getUsuariosOnline() {
        return usuariosOnline;
    }

    public void setUsuariosOnline(ArrayList<String> usuariosOnline) {
        this.usuariosOnline = usuariosOnline;
    }
}
