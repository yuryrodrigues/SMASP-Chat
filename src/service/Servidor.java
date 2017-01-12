package service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.ServidorGUI;

public class Servidor {

    private String host;
    public static int porta = 12345;
    private ServerSocket servidor;
    private boolean iniciado;
    private Map<String, ObjectOutputStream> clientes = new HashMap<String, ObjectOutputStream>();
    private ServidorGUI obView;    
    private String nomeServer = "servidorChat";

    public Servidor(ServidorGUI obView) {
        // objeto da tela do servidor
        this.obView = obView;
    }

    // inici a o servidor
    public void inicia() {
        // inicia o servidor na porta padrao
        inicia(Servidor.porta);
    }

    public void inicia(int porta) {
        // salva a porta que sera usada para abrir o servidor
        Servidor.porta = porta;

        try {
            // abre o servidor na porta especificada
            servidor = new ServerSocket(Servidor.porta);

            // pega o endereco do servidor
            host = servidor.getInetAddress().getHostAddress();

            // indica que o servidor foi iniciado
            iniciado = true;

            // referencia do objeto do servidor
            Servidor sv = this;

            // recebe conexoes dos clientes
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket cl_socket;
                    EscutaCliente ec;

                    // executa a thread enquanto o servidor estiver ativo
                    while (iniciado) {
                        try {
                            // aceita um cliente
                            cl_socket = servidor.accept();

                            // cria uma nova thread para ficar recebendo as mensagens do cliente
                            ec = new EscutaCliente(cl_socket, sv);
                            new Thread(ec).start();
                        } catch (IOException ex) {
                        }
                    }
                }
            }).start();
        } catch (IOException ex) {
            this.iniciado = false;

            obView.mostrarMensagem("Nâo foi possível iniciar o servidor. \nTente uma porta diferente.");
        }
    }

    // para o servidor
    public void para() {
        try {
            // indica que fechou o servidor
            this.iniciado = false;
            
            // se tiver usuários online, informa que o servidor será fechado
            if(!clientes.isEmpty()){
                Mensagem msgServer = new Mensagem();
                msgServer.setRemetente(this.nomeServer);
                msgServer.setTexto("O servidor não está disponível. Entre novamente mais tarde.");
                msgServer.setAcao(Mensagem.Acao.DESCONECTAR);
                
                enviaParaTodos(msgServer);
            }
            
            // remove todos os usuários do servidor
            clientes.clear();
            
            // fecha o servidor
            this.servidor.close();

            //((RecebeConexoes)recebeConexoes).terminate();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // conecta um cliente
    protected void conecta(Mensagem msg_cl, ObjectOutputStream cl_saida) {
        // verifica se o usuário não escolheu um nome reservado ou 
        // se for o primeiro cliente conectado ou seu nome já não estiver em uso
        if (!msg_cl.getRemetente().equals(this.nomeServer) && (this.clientes.isEmpty() || !this.clientes.containsKey(msg_cl.getRemetente()))) {
            // adiciona o cliente a lista dos online
            addCliente(msg_cl.getRemetente(), cl_saida);

            // envia lista com os usuarios online para os clientes
            enviaListaUserOnline();

            // informa que para o cliente que ele se conectou ao servidor
            Mensagem msgServer = new Mensagem();
            msgServer.setRemetente(this.nomeServer);
            msgServer.setTexto("YES");
            msgServer.setAcao(Mensagem.Acao.CONECTAR);
            envia(msgServer, cl_saida);
        } 
        else {
            // informa para o cliente que nao foi possível se conectar ao servidor
            Mensagem msgServer = new Mensagem();
            msgServer.setRemetente(this.nomeServer);
            msgServer.setTexto("NO");
            msgServer.setAcao(Mensagem.Acao.CONECTAR);
            envia(msgServer, cl_saida);
        }
    }

    // desconecata um cliente do servidor
    protected void desconecta(String cliente) {
        // remove o cliente
        this.clientes.remove(cliente);

        // envia a msg para todos os clientes informando o user saiu do chat
        Mensagem msgServer = new Mensagem();
        msgServer.setRemetente(this.nomeServer);
        msgServer.setTexto(cliente + " saiu do chat");
        msgServer.setAcao(Mensagem.Acao.ENVIAR_PARA_TODOS);
        enviaParaTodos(msgServer);

        // atualiza a lista de clientes online
        enviaListaUserOnline();
    }

    // envia uma mensgagem para um cliente online
    protected void envia(Mensagem msg, ObjectOutputStream cliente) {
        // envia msg para o cliente especificado
        try {
            cliente.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // envia uma mensagem para varios clientes online
    protected void enviaParaAlguns(Mensagem msg, ArrayList<String> cl_enviar) {
        // pega os nomes dos clientes destinatarios da msg
        for (int i = 0; i < cl_enviar.size(); i++) {
            // verifica se o cliente esta online
            if (this.clientes.containsKey(msg.getUsuariosEnviar().get(i))) {
                try {
                    // envia msg para o cliente
                    clientes.get(msg.getUsuariosEnviar().get(i)).writeObject(msg);
                } catch (IOException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // envia uma mensagem para todos os clientes online
    protected void enviaParaTodos(Mensagem msg) {
        // pega todos os clientes online
        for (Map.Entry<String, ObjectOutputStream> cl : this.clientes.entrySet()) {
            try {
                // envia msg para o cliente
                cl.getValue().writeObject(msg);
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // envia a lista atualizada de clientes online
    protected void enviaListaUserOnline() {
        ArrayList<String> userOnline = new ArrayList<>();

        // pega a lista de clientes online
        for (Map.Entry<String, ObjectOutputStream> cl_online : clientes.entrySet()) {
            userOnline.add(cl_online.getKey());
        }

        // mensagem que sera enviada
        Mensagem msgServer = new Mensagem();
        msgServer.setRemetente(this.nomeServer);
        msgServer.setAcao(Mensagem.Acao.USUARIOS_ONLINE);
        // anexa a lista de clientes online
        msgServer.setUsuariosOnline(userOnline);

        // pega todos os usuarios que estao online
        for (Map.Entry<String, ObjectOutputStream> cl : clientes.entrySet()) {
            try {
                //envia a lista com todos os usuarios online
                cl.getValue().writeObject(msgServer);
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getHost() {
        return host;
    }

    public int getPorta() {
        return porta;
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public void addCliente(String cl, ObjectOutputStream obOs) {
        this.clientes.put(cl, obOs);
    }

}
