package com.javarush.task.task30.task3008.client;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClientGuiView {
    private final ClientGuiController controller;

    private final Map<String, Color> usersColors = new HashMap<>();
    private final java.util.Map<String, JTextField> mapOfTextFileds = new HashMap<>();
    private final java.util.Map<String, JTextPane> mapOfChats = new HashMap<>();
    private final java.util.Map<String, JScrollPane> mapOfScrollChats = new HashMap<>();

    private JFrame frame = new JFrame("Чат");
    private JTextField textField = new JTextField(50);
    private JTextPane messages = new JTextPane();
    private JTextPane users = new JTextPane();
    private JScrollPane messagesScroll = new JScrollPane(messages);
    private JScrollPane usersScroll = new JScrollPane(users);

    public ClientGuiView(ClientGuiController controller) {
        this.controller = controller;
        initView();

        mapOfTextFileds.put(null, textField);
        mapOfChats.put(null, messages);
        mapOfScrollChats.put(null, messagesScroll);
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

        //добавляем слушатель для вызова контекстного меню дял приватного чата
        messages.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger())
                    doPop(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    doPop(e);
            }

            private void doPop(MouseEvent e) {
                messages.setCaretPosition(messages.viewToModel(e.getPoint()));
                int offset = messages.getCaretPosition();
                String userName = getClickedName(offset, 0);

                // выходим, если не попали в существющий никнейм
                if (!controller.getModel().getAllUserNames().contains(userName))
                    return;

                // показываем контекстное меню
                new JPopupMenu() {
                    JMenuItem privateDialog = new JMenuItem("Приватный чат с " + userName);
                    {
                        privateDialog.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("Открыт приватный чат с " + userName);
                                controller.startPrivateChat(userName);
                            }
                        });
                        add(privateDialog);
                    }
                }.show(e.getComponent(), e.getX(), e.getY());


            }

            private String getClickedName(int caretPosition, int direction) {
                try {
                    int start = Utilities.getWordStart(messages, caretPosition);
                    int end = Utilities.getWordEnd(messages, caretPosition);
                    String userName = messages.getText(start, end - start);

                    //если попали в пробел(ы) возвращаем пустую строку
                    if (userName.matches("\\s+"))
                        return "";

                    //заполняем имя влево (direction = -1), учитывая все "_"
                    if ((direction == -1 || direction == 0) && start != 0) {
                        if (userName.equals("_")) {
                            String prevPart = getClickedName(start-1, -1);
                            prevPart = prevPart.trim();
                            userName = prevPart + userName;
                        }
                        else if (messages.getText(start - 1, 1).equals("_")) {
                            String prevPart = getClickedName(start - 2, -1);
                            prevPart = prevPart.trim();
                            userName = prevPart + "_" + userName;
                        }

                    }

                    //заполняем имя вправо (direction = 1), учитывая все "_"
                    if ((direction == 1 || direction == 0) && end != messages.getDocument().getLength()) {
                        if (userName.endsWith("_")) {
                            String nextPart = getClickedName(end, 1);
                            nextPart = nextPart.trim();
                            userName = userName + nextPart;
                        }
                        else if (messages.getText(end, 1).equals("_")) {
                            String nextPart = getClickedName(end + 1, 1);
                            nextPart = nextPart.trim();
                            userName = userName + "_" + nextPart;
                        }
                    }

                    return userName;

                } catch (BadLocationException exception) {
                    System.out.println("BadLocationException в getClickedName(int caretPosition)");
                    return null;
                }
            }
        });
    }

    public void startPrivateChat(String userName) {
        JFrame frame = new JFrame("Приватный чат с " + userName);
        JTextField textField = new JTextField(50);
        mapOfTextFileds.put(userName, textField);
        JTextPane messages = new JTextPane();
        mapOfChats.put(userName, messages);
        System.out.println(userName);
        System.out.println(mapOfChats.get(userName));
        JScrollPane messagesScroll = new JScrollPane(messages);
        mapOfScrollChats.put(userName, messagesScroll);

        textField.setEditable(true);
        messages.setPreferredSize(new Dimension(200, 500));
        messages.setEditable(false);


        frame.getContentPane().add(textField, BorderLayout.SOUTH);
        frame.getContentPane().add(messagesScroll, BorderLayout.NORTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                String addressedText = userName + " " + text;
                controller.sendPrivateTextMessage(addressedText);
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
        for (JTextField textField : mapOfTextFileds.values()) {
            textField.setEditable(clientConnected);
        }
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

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException в refreshMessages()");
        }

        messagesScroll.updateUI();
    }

    public void refreshPrivateMessages(String fromUser, boolean isBack) {
        ClientGuiModel model = controller.getModel();
        String newMessage = model.getNewMessage();
        if (isBack)
            newMessage = newMessage.substring(newMessage.indexOf(":")+1, newMessage.length());
        String userName = newMessage.substring(0,newMessage.indexOf(":"));

        JTextPane messages = mapOfChats.get(fromUser);
        JScrollPane messagesScroll = mapOfScrollChats.get(fromUser);

        JScrollBar scrollBar = messagesScroll.getVerticalScrollBar();
        Document doc = messages.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();

        boolean isScrollValueMax = scrollBar.getValue() == scrollBar.getMaximum() - scrollBar.getVisibleAmount();

        StyleConstants.setForeground(set, usersColors.get(userName));
        try {
            System.out.println(model.getNewMessage());
            doc.insertString(doc.getLength(), newMessage + "\n", set);
        } catch (BadLocationException e) {
            System.out.println("BadLocationException в refreshPrivateMessages()");
        }

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException в refreshPrivateMessages()");
        }

        if (isScrollValueMax)
           scrollBar.setValue(scrollBar.getMaximum() - scrollBar.getVisibleAmount());

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException в refreshPrivateMessages()");
        }

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
