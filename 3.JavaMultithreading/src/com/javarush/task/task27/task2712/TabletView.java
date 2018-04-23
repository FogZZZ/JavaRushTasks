package com.javarush.task.task27.task2712;

import com.javarush.task.task27.task2712.ad.Advertisement;
import com.javarush.task.task27.task2712.kitchen.Dish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabletView {
    private Tablet tablet;

    private JFrame frame = new JFrame();
    private JButton makeNewOrderButton = new JButton("Сделать Новый Заказ");
    private JButton makeOrderButton = new JButton("Сделать заказ");
    private JPanel orderPanel = new JPanel();

    private Map<JButton, Integer> dishButtons = new HashMap<>();
    private Map<JButton, JButton> orderDishButtons = new HashMap<>();

    public TabletView(Tablet tablet, int num) {
        this.tablet = tablet;
        initStartView(num);

        initMakeOrderButton();
        initOrderPanel();
    }

    private void initStartView(int num) {
        frame.setTitle("Планшет №" + num);
        frame.setSize(600, 400);
        makeNewOrderButton.setPreferredSize(new Dimension(200, 100));

        frame.getContentPane().setLayout(new GridBagLayout());
        frame.add(makeNewOrderButton);
        frame.setVisible(true);

        makeNewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeOrder();
            }
        });
    }

    public void makeOrder() {

        frame.getContentPane().remove(makeNewOrderButton);
        frame.getContentPane().setLayout(new GridBagLayout());

        renewOrderPanel();
        addOrderPanel();
        addMakeOrderButton();

        initDishButtons();

        frame.getContentPane().repaint();
        frame.getContentPane().validate();
    }

    private void toStartView() {
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.add(makeNewOrderButton);
        frame.getContentPane().repaint();
        frame.getContentPane().validate();
    }

    private void initMakeOrderButton() {
        makeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        frame,
                        "Вы желаете сделать заказ?",
                        "Подтверждение Заказа",
                        JOptionPane.YES_NO_OPTION);
                if (n == 0) {
                    toStartView();
                    tablet.createUIOrder();
                    tablet.getCurrentOrder().clear();
                    dishButtons.clear();
                    orderDishButtons.clear();
                }
            }
        });
    }

    private void initOrderPanel() {
        orderPanel.setBackground(Color.LIGHT_GRAY);
        orderPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 1;

        JLabel orderTitleLabel = new JLabel("Ваш заказ:");
        orderPanel.add(orderTitleLabel, c);
    }

    private void renewOrderPanel() {
        Component[] components = orderPanel.getComponents();
        for (Component c : components) {
            if (c instanceof JButton) {
                orderPanel.remove(c);
            }
        }
    }

    private void addOrderPanel() {
        frame.getContentPane().add(orderPanel, new GridBagConstraints(
                3, 0,
                1, 2,
                1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 10, 10),
                0, 0));

        /*int gridx, int gridy,
          int gridwidth, int gridheight,
          double weightx, double weighty,
          int anchor, int fill,
          Insets insets,
          int ipadx, int ipady*/
    }

    private void addMakeOrderButton() {
        frame.getContentPane().add(makeOrderButton, new GridBagConstraints(
                3, 2,
                4, 1,
                1, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(10, 10, 10, 10),
                0, 0));

        /*int gridx, int gridy,
          int gridwidth, int gridheight,
          double weightx, double weighty,
          int anchor, int fill,
          Insets insets,
          int ipadx, int ipady*/
    }

    private void initDishButtons() {
        GridBagConstraints c = new GridBagConstraints(
                0, 0,
                1, 1,
                1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1),
                0,0);

        /*int gridx, int gridy,
          int gridwidth, int gridheight,
          double weightx, double weighty,
          int anchor, int fill,
          Insets insets,
          int ipadx, int ipady*/

        int x = -1;
        int y = -1;
        for (Dish dish : Dish.values()) {
            if ((x = ++x % 3) == 0) {
                y++;
            }
            c.gridx = x;
            c.gridy = y;
            JButton dishButton = new JButton(dish.name());
            dishButtons.put(dishButton, 0);
            frame.getContentPane().add(dishButton, c);

            dishButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int num = dishButtons.get(dishButton) + 1;
                    dishButtons.put(dishButton, num);
                    tablet.getCurrentOrder().put(dish, num);

                    if (num == 1) {
                        JButton orderDishButton = new JButton(dish.name() + "(" + num + ")");
                        orderDishButtons.put(dishButton, orderDishButton);

                        GridBagConstraints c = new GridBagConstraints();
                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.gridwidth = GridBagConstraints.REMAINDER;
                        c.weightx = 1;
                        orderPanel.add(orderDishButton, c);

                        orderDishButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int num = dishButtons.get(dishButton);
                                if (num > 1) {
                                    orderDishButton.setText(dish.name() + "(" + (num-1) + ")");
                                    tablet.getCurrentOrder().put(dish, num-1);
                                } else {
                                    orderPanel.remove(orderDishButton);
                                    tablet.getCurrentOrder().put(dish, 0);
                                }
                                dishButtons.put(dishButton, num-1);

                                orderPanel.repaint();
                                orderPanel.validate();
                            }
                        });

                    } else {
                        orderDishButtons.get(dishButton).setText(dish.name() + "(" + num + ")");
                    }

                    frame.getContentPane().repaint();
                    frame.getContentPane().validate();
                }
            });
        }
    }

    public void playVideos(List<Advertisement> videos) {
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new GridLayout());

        StringBuilder sb = new StringBuilder();
        int totalDuration = 0;
        for (Advertisement video : videos) {
            sb.append(video.toString() + ", ");
            totalDuration += video.getDuration() / 60;
        }
        sb.delete(sb.length()-2, sb.length()-1);

        JLabel text = new JLabel("Playing " + sb + " " + totalDuration + " sec", SwingConstants.CENTER);

        Timer timer = new Timer(1000, null);
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldTime = text.getText();
                int lastSpace = oldTime.lastIndexOf(" ");
                int prevLastSpace = oldTime.lastIndexOf(" ", lastSpace-1);
                oldTime = oldTime.substring(prevLastSpace+1, lastSpace);
                int t = Integer.parseInt(oldTime);

                if (--t < 0) {
                    timer.stop();
                    toStartView();
                }

                text.setText("Playing " + sb + " " + t + " sec");
            }
        };
        timer.addActionListener(listener);

        frame.getContentPane().add(text);

        frame.repaint();
        frame.validate();
        timer.start();
    }
}
