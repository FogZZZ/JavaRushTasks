package com.javarush.task.task30.task3008.client;

import java.util.Random;

public class ClientGuiController extends Client {
    private ClientGuiModel model = new ClientGuiModel();
    private ClientGuiView view = new ClientGuiView(this);

    public class GuiSocketThread extends SocketThread {
        @Override
        protected void processIncomingMessage(String message) {
            model.setNewMessage(message);
            view.refreshMessages();
        }

        @Override
        protected void informAboutAddingNewUser(String userName) {
            model.addUser(userName);
            view.refreshUsers();
        }

        @Override
        protected void informAboutDeletingNewUser(String userName) {
            model.deleteUser(userName);
            view.refreshUsers();
        }

        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            view.notifyConnectionStatusChanged(clientConnected);
        }
    }

    @Override
    protected SocketThread getSocketThread() {
        return new GuiSocketThread();
    }

    @Override
    public void run() {
        SocketThread socketThread = getSocketThread();
        socketThread.run();
    }

    @Override
    protected String getServerAddress() {
        //return view.getServerAddress();
        return "localhost";
    }

    @Override
    protected int getServerPort() {
        //return view.getServerPort();
        return 1111;
    }

    @Override
    protected String getUserName() {
        //return view.getUserName();
        return "FogzTest_" + (int)(new Random().nextDouble()*1000);
    }

    public ClientGuiModel getModel() {
        return model;
    }

    /*public void startPrivateChat(String userName) {
        view.startPrivateChat(userName);
    }*/

    public static void main(String[] args) {
        ClientGuiController controller = new ClientGuiController();
        controller.run();
    }
}
