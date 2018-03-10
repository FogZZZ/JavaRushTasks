package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BotClient extends Client {
    public class BotSocketThread extends SocketThread {

        private List<String> jokes = new ArrayList<>();
        {
            jokes.add("Шутка 1");
            jokes.add("Шутка 2");
            jokes.add("Шутка 3");
            jokes.add("Шутка 4");
            jokes.add("Шутка 5");
            jokes.add("Шутка 6");
            jokes.add("Шутка 7");
            jokes.add("Шутка 8");
            jokes.add("Шутка 9");
            jokes.add("Шутка 10");
            jokes.add("Шутка 11");
            jokes.add("Шутка 12");
            jokes.add("Шутка 13");
            jokes.add("Шутка 14");
            jokes.add("Шутка 15");
            jokes.add("Шутка 16");
            jokes.add("Шутка 17");
            jokes.add("Шутка 18");
            jokes.add("Шутка 19");
            jokes.add("Шутка 20");
        }

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды, шутка.");
            new Thread(){
                @Override
                public void run() {
                    Random rand = new Random();
                    while (true) {
                        int randSecs = 60 + (int)(60 * 4 * rand.nextDouble());
                        try {
                            Thread.sleep(randSecs * 1000);
                        } catch (InterruptedException e) {
                            System.out.println("InterruptedException в clientMainLoop()");
                        }
                        sendTextMessage(jokes.get((int)(rand.nextDouble()*jokes.size())));
                    }
                }
            }.start();
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
            }
            else if (text.equals("шутка")) {
                reply = jokes.get((int)(new Random().nextDouble()*jokes.size()));
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
