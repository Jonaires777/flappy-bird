package panel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.swing.*;
public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int borderWidth = 360;
    int borderHeight = 640;

    //Images
    Image backGroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    //Bird
    int birdX = borderWidth/8;
    int birdY = borderHeight/2;
    int birdWidht = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int width = birdWidht;
        int height = birdHeight;
        Image img;

        Bird(Image image){
            this.img = image;
        }
    }

    //Pipes
    int pipeX = borderWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image image){
            this.img = image;
        }
    }

    //Game logic
    Bird bird;
    int velocityY = 0;
    int velocityX = -4; //moves pipe to the left
    int gravity = 1;
    Timer gameLoop;
    Timer placePipesTimer;
    ArrayList<Pipe> pipes;
    Random random = new Random();
    boolean gameOver = false;
    double score = 0;

    public FlappyBird(){
        setPreferredSize(new Dimension(borderWidth, borderHeight));
        setFocusable(true);
        addKeyListener(this);

        //loading images
        backGroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/flappybirdbg.png"))).getImage();
        birdImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/flappybird.png"))).getImage();
        topPipeImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/toppipe.png"))).getImage();
        bottomPipeImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/assets/bottompipe.png"))).getImage();

        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes(){
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = borderHeight/4;

        Pipe toppipe = new Pipe(topPipeImage);
        toppipe.y = randomPipeY;
        pipes.add(toppipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = toppipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(backGroundImage, 0, 0, borderWidth, borderHeight, null);
        graphics.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            graphics.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            graphics.drawString("Game Over: " + String.valueOf((int)score), 10, 35);
        } else {
            graphics.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();        }
    }

    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //pipes
        for (Pipe pipe: pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > borderHeight) {
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b){
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;
            if (gameOver) {
                //restart the game by reseting their conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
