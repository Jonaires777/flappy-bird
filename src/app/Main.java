import panel.FlappyBird;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        int borderWidht = 360;
        int borderHeight = 640;

        JFrame frame =  new JFrame("Flappy Bird");
        frame.setSize(borderWidht, borderHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);

    }
}