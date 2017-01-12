package service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.ClienteGUI;

public class Cliente {

    public static String HOST_PADRAO    = "127.0.0.1";
    private String host                 = HOST_PADRAO;
    public static int PORTA_PADRAO      = 12345;
    private int porta                   = PORTA_PADRAO;
    private Socket socket;
    private ObjectOutputStream saida;
    private ClienteGUI obView;

    public Cliente(ClienteGUI obView) {
        // objeto da tela do chat
        this.obView = obView;
    }

    public boolean conecta() {
        // conecta ao servidor e porta padrão
        return conecta(host, porta);
    }
    
    public boolean conecta(int porta) {
        // conecta ao servidor padrão e a porta especificada
        return conecta(this.host, porta);
    }

    public boolean conecta(String host, int porta) {
        this.host = host;
        this.porta = porta;

        try {
            // conecta o cliente no servidor especificado
            socket = new Socket(this.host, this.porta);
            // pega a stream de saída de mensagens
            saida  = new ObjectOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            
            return false;
        }
    }

    // fecha a conecção com servidor
    public void desconecta() {        
        try {
            saida.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // envia mensagens para o servidor
    public void envia(Mensagem msg) {
        try {
            saida.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // getter
    public String getHost() {
        return host;
    }

    public int getPorta() {
        return porta;
    }

    public ObjectOutputStream getSaida() {
        return saida;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }
    
    
}
