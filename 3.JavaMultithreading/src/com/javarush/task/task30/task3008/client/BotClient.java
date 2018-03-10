package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BotClient extends Client {
    public class BotSocketThread extends SocketThread {
        //да, шутки дурацкие
        private List<String> jokes = new ArrayList<>();
        {
            jokes.add("Вызовите гомеопата! Тут астрологу плохо!");
            jokes.add("Оля каждое утро приносила своему мужу кофе в постель, потому что если она не успевала, утро начиналось с пива.");
            jokes.add("Народная медицина - это когда люди, живущие в среднем 70 лет, лечатся по рецептам людей, живших в среднем 30 лет.");
            jokes.add("- Ты вёл себя как джентльмен. Спасибо, что не лапал.\n- Не за что");
            jokes.add("Важная информация к размышлению: макароны с котлетами - это просто другое агрегатное состояние пельменей.");
            jokes.add("- Чем отличается весёлый байкер от грустного?\n- У весёлого байкера зубы в мошках!");
            jokes.add("Россия обладает 92% мировых запасов сериалов про ментов.");
            jokes.add("Когда Фёдору Емельяненко становится скучно, он берет микрокредит и дожидается коллекторов.");
            jokes.add("Группа туристов в тайге потеряла 15 литров спирта. Две недели людям пришлось существовать на еде и воде…");
            jokes.add("По статистике, каждый четвёртый россиянин встречается в 2 раза реже, чем каждый второй.");
            jokes.add("Девушки очень добры. Они могут простить парня, даже если он ни в чем не виноват.");
            jokes.add("Конфеты Аленка с перцем помогут вам узнать, матерится ли ваш ребенок.");
            jokes.add("Однажды ты спросишь меня, что я люблю больше: тебя или заниматься ерундой, я промолчу и ты уйдешь, так и не узнав, что я засунул себе в уши фисташки.");
            jokes.add("- Ура, пятница! - радостно закричал мозг... И унес жопу в неизвестном направлении.");
            jokes.add("Новая туалетная бумага с дырочками - всё в твоих руках!");
            jokes.add("Одноглазую девочку больше не интересует, кто живет в скворечнике.");
            jokes.add("В детском саду Оля была настолько милым ребенком, что двойное убийство так и не раскрыли.");
            jokes.add("Библиотекарша чихнула, и её сопля попала в книгу рекордов Гиннеса.");
            jokes.add("Маленькую Лизу, глухую на одно ухо, мама ласково называла Моно Лиза.");
            jokes.add("Хочу работать в секс-шопе. Там хоть я спокойно могу сказать: \"За каким хуем вы сюда приперлись\"?");
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
