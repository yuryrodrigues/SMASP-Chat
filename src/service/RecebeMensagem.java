package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.ClienteGUI;

public class RecebeMensagem implements Runnable {

    private ClienteGUI cl_GUI;
    private ObjectInputStream cl_entrada;

    public RecebeMensagem(Socket cl_socket, ClienteGUI cl_GUI) {
        // referencia do servidor que criou esta thread
        this.cl_GUI = cl_GUI;

        try {
            // salva o stream de entrada desta conexao
            this.cl_entrada = new ObjectInputStream(cl_socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(RecebeMensagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        // mensagem vinda do servidor
        Mensagem msg_servidor = null;

        try {
            // pega as mensagens enviadas pelo cliente
            while ((msg_servidor = (Mensagem) cl_entrada.readObject()) != null) {
                // pega a acao que o usuario quer fazer
                Mensagem.Acao acao = msg_servidor.getAcao();

                // verifica o tipo acao que o cliente esta pedindo
                switch (acao) {
                    case CONECTAR:
                        // verifica se o usuario conseguiu se conectar
                        if(msg_servidor.getTexto().equals("NO")){
                            cl_GUI.mostrarCaixaDialogo("Não foi possível entrar no chat. "
                                    + "\nDigite outro nome de usuário");
                        }
                        else {
                            // habilita o chat
                            cl_GUI.userConectado();
                        }
                        break;
                    case DESCONECTAR:
                        // desconecta o usuário
                        cl_GUI.servidorDesconectado(msg_servidor);
                        break;
                    case ENVIAR:
                        // envia a mensagem para os usuários especificados
                        cl_GUI.getExibeMensagem().exibeMensagemPrivada(msg_servidor);
                        break;
                    case ENVIAR_PARA_TODOS:
                        // envia a mensagem para todos os clientes
                        cl_GUI.getExibeMensagem().exibeMensagemGlobal(msg_servidor);
                        break;
                    case USUARIOS_ONLINE:
                        cl_GUI.atualizaUsuariosOnline(msg_servidor);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException ex) {
            // desconecta o usuário
            //servidor.desconecta(msg_servidor.getRemetente());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RecebeMensagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
