import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;


public class Gamepanel extends JPanel implements ActionListener {

    static final int screenWidth = 600;
    static final int screenHeight = 600;
    static final int foodSize = 20;
    static final int maxFoodUnits = (screenHeight*screenWidth)/foodSize;
    static final int delay = 100;
    final int[] x = new int[maxFoodUnits];
    final int[] y = new int[maxFoodUnits];
    int bodyParts = 6;
    int foodEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random = new Random();


    Gamepanel() {
        Random random = new Random();
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new ActionL());
        startGame();
    }
    public class ActionL extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction!='R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction!='L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction!='D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction!='U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    public void startGame(){
        newFood();
        running = true;
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running){
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,foodSize,foodSize);

            //drawing the head
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.cyan);
                    g.fillRect(x[i],y[i],foodSize,foodSize);
                }else{
                    g.setColor(Color.lightGray);
                    g.fillRect(x[i],y[i],foodSize,foodSize);
                }
            }
        }else{
            gameOver(g);
        }
    }


    public void move(){
        for (int i = bodyParts; i >0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case 'U':
                y[0]= y[0]-foodSize;
                break;
            case 'D':
                y[0]= y[0]+foodSize;
                break;
            case 'L':
                x[0]= x[0]-foodSize;
                break;
            case 'R':
                x[0]= x[0]+foodSize;
                break;
        }
    }
    public void newFood(){
        appleX = random.nextInt(screenWidth/foodSize)*foodSize;
        appleY = random.nextInt(screenHeight/foodSize)*foodSize;
    }

    public void eat(){
        if ((x[0]==appleX)&&(y[0]==appleY)) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollisions(){
        for (int i = bodyParts; i > 0 ; i--) {
            if ((x[0]==x[i])&&(y[0]==y[i])) {
                running=false;
            }
        }

        if (x[0] < 0) {
            running = false;
        }

        if (x[0] > screenWidth) {
            running = false;
        }

        if (y[0] < 0) {
            running = false;
        }

        if (y[0] > screenHeight) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,20));
        g.drawString("Game over",screenHeight/2,screenWidth/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            eat();
            checkCollisions();
        }
        repaint();
    }
}
