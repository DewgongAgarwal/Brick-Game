//importing header files
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;

public class Bricks extends Applet implements KeyListener, ActionListener
{
    //controlling class
    BrickWall bw;
    Timer t;
    Ball b;
    Paddle p;
    Label lb;
    Dimension d;
    Graphics buffer;
    Image img;
    int points;
    public void init()//default function init
    {
        resize(500, 500);//resizing the applet
        setLayout(null);
        p = new Paddle();
        b = new Ball();
        lb = new Label("                                            ");
        lb.setBounds(240,455,200,35);
        add(lb);
        points = 0;
        d = getSize();
        img = createImage(d.width, d.height);
        buffer = img.getGraphics();
        bw = new BrickWall((int)(Math.random()*10) + 1,(int)(Math.random()*10) + 1);
        addKeyListener(this);
        setFocusable(true);
        t = new Timer(25,this);
        t.start();
    }

    public void paint(Graphics g)
    {
        buffer.clearRect(0,0,d.width,d.height);
        bw.drawWall(buffer);
        b.drawBall(buffer);
        p.drawPaddle(buffer);
        g.drawImage(img, 0, 0, d.width, d.height,this);
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void actionPerformed(ActionEvent ae)
    {
        if (bw.check())
        repaint();
        b.move();
        int i, j;
        int x = p.getX();
        int y = p.getY();
        int x1 = b.getX(), y1 = b.getY();
        Rectangle r1 = new Rectangle (x1,y1,30,30);
        Rectangle r3 = new Rectangle (x, y, 100, 5);
        if (b.getY() >= 500)//if the ball crosses the paddle
        {
            lb.setText("Points = "+points+". GAME OVER");                
            return;
        }
        
        if(!(bw.check()))//if all the bricks have been destroyed
        {
            lb.setText("Points = "+points+". YOU WIN !!");
            return;
        }
        
        if (r1.intersects(r3))//collision between ball and paddle
        {
            b.setDX(b.getDX() + (int)(Math.round(Math.random())));
            b.setDY(b.getDY() + (int)(Math.round(Math.random())));
            b.setDY(-b.getDY());            
        }
        
        for(i = 0; i < bw.rows; i++)
        {
            for (j = 0; j < bw.cols; j++)
            {
                if (bw.arr[i][j] == 0)
                continue;
                Rectangle r2 = new Rectangle (bw.getX(j), bw.getY(i), bw.width, bw.height);
                if (r1.intersects(r2))//if the ball collides with a brick
                {                  
                     points += 1;
                     lb.setText(points + "");
                     bw.arr[i][j] -= 1;;
                     b.setDY(-b.getDY());
                     return;                   
                }   
            }
        }   

    }


    public void keyTyped(KeyEvent ke)
    {
    }
    
    public void keyPressed(KeyEvent ke)
    {
        int x = ke.getKeyCode();
        switch(x)
        {
            case KeyEvent.VK_RIGHT :
                p.moveRight();
                break;
            case KeyEvent.VK_LEFT :
                p.moveLeft();
                break;
        }
    }

    public void keyReleased(KeyEvent ke)
    {
    }
}

class BrickWall//contains functions to generate and paint the bricks
{
    int rows,cols,width,height,h_gap,v_gap;
    int x , y , arr[][];
    int status ; 
    public BrickWall(int r , int c)
    { 
        width=40 ; height=15 ;
        h_gap = 5 ; v_gap= 8 ;
        rows = r ; cols = c ;
        arr=new int [rows] [cols] ;
        x = 60 ; y =50 ;
        int i,j;
        for(i=0;i<rows;i++)
        { 
            for(j=0;j<cols;j++)
            {
                arr[i][j] = (int)(Math.random()*3 + 1);
            }
        }
    }

    public void drawWall(Graphics g)
    {
        int i,j;
        for(i=0;i<rows;i++)
        { 
            for(j=0;j<cols;j++)
            {
                if(arr[i][j] > 0)
                g.setColor(new Color((arr[i][j] + 100*arr[i][j]) %256,100*(arr[i][j] - 1) %256, 100*(arr[i][j] - 1) %256) );
                if(arr[i][j]>=1)
                {
                    g.fillRect(j*(width+h_gap) + x,i*(height+v_gap)+y,width,height);
                }
            }
        }
    }

    public boolean check()//to check if there is at least one non destroyed brick
    {
        for (int i = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++)
            {
                if (this.arr[i][j] >= 1)
                return true;
            }
        }
        return false;
    }

    public int getX( int c)
    {
        return (c*(width+h_gap)+x);
    }

    public int getY(int r )
    {
        return (r*(height+v_gap)+y);
    }
}

class Ball
{
    private int x, y, dx, dy;
    Ball()
    {
        x = 0;
        y = 150;
        dx = 5;
        dy = 5;
    }

    public void drawBall(Graphics g)
    {
        g.setColor(Color.green);
        g.fillOval(x,y,30,30);
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }
    
    public int getDY()
    {
        return this.dy;
    }

    public int getDX()
    {
        return this.dx;
    }

    public void setDY(int dy)
    {
        this.dy = dy;
    }

    public void setDX(int dx)
    {
        this.dx = dx;
    }

    public void move()
    {
        x += dx;
        y += dy;
        if (x <= 0 || x >= 470)
        {
            dx = -dx;
        }
        if (y <= 0)
        {
            dy = -dy;
        } 
    }
}

class Paddle
{
    private int x, y;
    public Paddle()
    {
        y = 420;
        x = 0;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public void drawPaddle(Graphics g)
    {
        g.setColor(Color.red);
        g.fillRect(x, y, 100, 20);
    }

    public void moveRight()
    {
        if (x >= 400)
            return;
        x += 10;
    }

    public void moveLeft()
    {
        if (x <= 0)
            return;
        x -= 10;
    }
}