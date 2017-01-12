package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.Mensagem.Acao;

public class EscutaCliente implements Runnable {

    private Servidor servidor;
    private ObjectOutputStream cl_saida;
    private ObjectInputStream cl_entrada;

    public EscutaCliente(Socket cl_socket, Servidor servidor) {
        // referencia do servidor que criou esta thread
        this.servidor = servidor;

        try {
            // salva o stream de saida e entrada desta conexao
            this.cl_saida = new ObjectOutputStream(cl_socket.getOutputStream());
            this.cl_entrada = new ObjectInputStream(cl_socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(EscutaCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        // mensagem vinda do cliente
        Mensagem msg_cliente = null;

        try {
            // pega as mensagens enviadas pelo cliente
            while ((msg_cliente = (Mensagem) cl_entrada.readObject()) != null) {
                // pega a acao que o usuario quer fazer
                Acao acao = msg_cliente.getAcao();

                // verifica o tipo acao que o cliente esta pedindo
                switch (acao) {
                    case CONECTAR:
                        // conecta o usuário
                        servidor.conecta(msg_cliente, cl_saida);
                        break;
                    case DESCONECTAR:
                        // desconecta o usuário
                        servidor.desconecta(msg_cliente.getRemetente());
                        break;
                    case ENVIAR:
                        // envia a mensagem para os usuários especificados
                        servidor.enviaParaAlguns(msg_cliente, msg_cliente.getUsuariosEnviar());
                        break;
                    case ENVIAR_PARA_TODOS:
                        // envia a mensagem para todos os clientes
                        servidor.enviaParaTodos(msg_cliente);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException ex) {
            // desconecta o usuário
            servidor.desconecta(msg_cliente.getRemetente());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EscutaCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
