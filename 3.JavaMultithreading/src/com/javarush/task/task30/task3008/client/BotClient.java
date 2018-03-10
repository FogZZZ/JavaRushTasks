package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BotClient extends Client {
    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            if (!message.contains(":"))
                return;
            String userName = message.substring(0, message.indexOf(":"));
            String text = message.substring(message.indexOf(":")+2, message.length());

            String reply = "";
            Date date = Calendar.getInstance().getTime();
            if (text.equals("дата")) {
                reply = new SimpleDateFormat("d.MM.YYYY").format(date);
            }
            else if (text.equals("день")) {
                reply = new SimpleDateFormat("d").format(date);
            }
            else if (text.equals("месяц")) {
                reply = new SimpleDateFormat("MMMM").format(date);
            }
            else if (text.equals("год")) {
                reply = new SimpleDateFormat("YYYY").format(date);
            }
            else if (text.equals("время")) {
                reply = new SimpleDateFormat("H:mm:ss").format(date);
            }
            else if (text.equals("час")) {
                reply = new SimpleDateFormat("H").format(date);
            }
            else if (text.equals("минуты")) {
                reply = new SimpleDateFormat("m").format(date);
            }
            else if (text.equals("секунды")) {
                reply = new SimpleDateFormat("s").format(date);
            } else {
                return;
            }

            sendTextMessage("Информация для " + userName + ": " + reply);
        }
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return "date_bot_" + (int)(100*Math.random());
    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }
}
