package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {

            connection.send(new Message(MessageType.NAME_REQUEST));
            Message message = connection.receive();

            if (message.getType() != MessageType.USER_NAME) {
                return serverHandshake(connection);
            }

            if (message.getData().isEmpty() || connectionMap.containsKey(message.getData())) {
                return serverHandshake(connection);
            }

            connection.send(new Message(MessageType.NAME_ACCEPTED));
            connectionMap.put(message.getData(), connection);
            return message.getData();
        }

        private void sendListOfUsers(Connection connection, String userName) throws IOException {
            for (String name : connectionMap.keySet()) {
                if (!name.equals(userName)) {
                    Message message = new Message(MessageType.USER_ADDED, name);
                    connection.send(message);
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while(true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT) {
                    Message newMessage = new Message(MessageType.TEXT, userName + ": " + message.getData());
                    sendBroadcastMessage(newMessage);
                } else {
                    ConsoleHelper.writeMessage("Принятое сообщение не является текстом.");
                }
            }
        }

        public void run() {
            ConsoleHelper.writeMessage("Установлено новое соединение с удаленным адресом: " +
                    socket.getRemoteSocketAddress());
            String userName = null;

            try (Connection connection = new Connection(socket);)
            {
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                sendListOfUsers(connection, userName);
                serverMainLoop(connection, userName);

            } catch (ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Произошла ошибка при обмене данными с удаленным адресом: " +
                        socket.getRemoteSocketAddress());

            } catch (IOException e) {
                ConsoleHelper.writeMessage("Произошла ошибка при обмене данными с удаленным адресом: " +
                        socket.getRemoteSocketAddress());

            } finally {
                if (userName != null) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                }
                ConsoleHelper.writeMessage("Соединение с удаленным адресом закрыто: "
                        + socket.getRemoteSocketAddress());
            }
        }
    }

    public static void sendBroadcastMessage(Message message) {
        try {
            for (Connection connection : connectionMap.values()) {
                connection.send(message);
            }
        } catch (IOException e) {
            System.out.println("Отправить сообщение не удалось.");
        }
    }



    public static void main(String[] args) {
        int port = ConsoleHelper.readInt();

        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            System.out.println("Сервер запущен.");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Handler(clientSocket).start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
