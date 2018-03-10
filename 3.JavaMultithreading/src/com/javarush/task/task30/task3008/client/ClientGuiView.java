package com.javarush.task.task30.task3008.client;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientGuiView {
    private final ClientGuiController controller;

    private final Map<String, Color> usersColors = new HashMap<>();

    private JFrame frame = new JFrame("Чат");
    private JTextField textField = new JTextField(50);
    private JTextPane messages = new JTextPane();
    private JTextPane users = new JTextPane();
    private JScrollPane messagesScroll = new JScrollPane(messages);
    private JScrollPane usersScroll = new JScrollPane(users);

    public ClientGuiView(ClientGuiController controller) {
        this.controller = controller;
        initView();
    }

    private void initView() {
        textField.setEditable(false);
        messages.setPreferredSize(new Dimension(500, 200));
        messages.setEditable(false);
        users.setPreferredSize(new Dimension(100,200));
        users.setEditable(false);


        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(messagesScroll, BorderLayout.WEST);
        frame.getContentPane().add(usersScroll, BorderLayout.EAST);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.sendTextMessage(textField.getText());
                textField.setText("");
            }
        });
    }

    public String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Введите адрес сервера:",
                "Конфигурация клиента",
                JOptionPane.QUESTION_MESSAGE);
    }

    public int getServerPort() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    "Введите порт сервера:",
                    "Конфигурация клиента",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                return Integer.parseInt(port.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Был введен некорректный порт сервера. Попробуйте еще раз.",
                        "Конфигурация клиента",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public String getUserName() {
        return JOptionPane.showInputDialog(
                frame,
                "Введите ваше имя:",
                "Конфигурация клиента",
                JOptionPane.QUESTION_MESSAGE);
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {
        textField.setEditable(clientConnected);
        if (clientConnected) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Соединение с сервером установлено",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Клиент не подключен к серверу",
                    "Чат",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void refreshMessages() {
        ClientGuiModel model = controller.getModel();
        JScrollBar scrollBar = messagesScroll.getVerticalScrollBar();
        Document doc = messages.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();

        boolean isScrollValueMax = scrollBar.getValue() == scrollBar.getMaximum() - scrollBar.getVisibleAmount();

        StyleConstants.setForeground(set, usersColors.get(model.getUserName()));
        try {
            doc.insertString(doc.getLength(), model.getNewMessage() + "\n", set);
        } catch (BadLocationException e) {
            System.out.println("BadLocationException в refreshMessages()");
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException в refreshMessages()");
        }

        if (isScrollValueMax)
            scrollBar.setValue(scrollBar.getMaximum() - scrollBar.getVisibleAmount());

        messagesScroll.updateUI();
    }

    public void refreshUsers() {
        ClientGuiModel model = controller.getModel();
        Document doc = users.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();

        Random rand = new Random();
        Color userColor;

        users.setText("");
        for (String userName : model.getAllUserNames()) {
            if ((userColor = usersColors.get(userName)) == null ) {
                userColor = new Color(rand.nextFloat()*0.5f+0.2f,
                        rand.nextFloat()*0.5f+0.2f,
                        rand.nextFloat()*0.5f+0.2f);
                usersColors.put(userName, userColor);
            }
            StyleConstants.setForeground(set, userColor);

            try {
                doc.insertString(doc.getLength(), userName + "\n", set);
            } catch (BadLocationException e) {
                System.out.println("BadLocationException в refreshUsers()");
            }
        }
    }
}
