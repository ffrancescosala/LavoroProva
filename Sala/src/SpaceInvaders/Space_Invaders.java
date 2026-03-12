package SpaceInvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Space_Invaders extends JPanel implements KeyListener { // sala sei un piccione

    //NAVICELLA
    private int xN, yN;
    private int size = 150;
    private int speed = 12;
    private static boolean esploso = false;

    //PROIETTILE
    private int xP;
    private int yP;
    private int sizeP = 40;
    private int speedP = 20;
    private boolean shooting = false;
    
    //ASTEROIDE
    private int xA;
    private int yA;
    private int sizeA = 130;
    private int speedA = 7;
    private int punteggio = 0;
    private int larghezza;
    
    //MOVIMENTO
    private boolean left, right;

    //IMMAGINI
    private Image sfondo;
    private Image navicella;
    private Image colpo;
    private Image asteroide;
    
    //PER CENTRARE NAVICELLA SOLO ALL'INIZIO
    private boolean navicellaCentrata = false;

    //COSTRUTTORE
    public Space_Invaders() {

        //PERMETTE DI RICEVERE INPUT DA TASTIERA
        setFocusable(true);
        
        //REGISTRA LA CLASSE COME KEYLISTENER
        addKeyListener(this);

        //PERCORSO DELLE IMMAGINI
        sfondo = new ImageIcon("src/SpaceInvaders/background.png").getImage();
        navicella = new ImageIcon("src/SpaceInvaders/navicella.png").getImage();
        colpo = new ImageIcon("src/SpaceInvaders/colpo.png").getImage();
        asteroide = new ImageIcon("src/SpaceInvaders/asteroide.png").getImage();
        
        //METODO PER SPAWNARE UN ASTEROIDE
        spawnAsteroide();
        
        //TIMER CHE AGGIORNA MOVIMENTO OGNI 16ms
        Timer timer = new Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //AGGIORNAMENTO MOVIMENTI
                update();

                //RIDISEGNA LA NAVICELLA, L'ASTEROIDE E IL COLPO
                repaint();
            }
        });

        //AVVIA IL TIMER
        timer.start();
    }

    //METODO UPDATE PER IL MOVIMENTO
    private void update() {

        //ALTEZZA NAVICELLA
        yN = getHeight() - 160;

        //MOVIMENTO NAVICELLA
        if (!esploso) {
            if (left) xN -= speed;
            if (right) xN += speed;
        }
        
        //CONTROLLO NON ESCA DALLO SCHERMO
        if (xN < 0) xN = 0;
        if (xN + size > getWidth()) xN = getWidth() - size;

        //MOVIMENTO PROIETTILE
        if (shooting && esploso == false) {
            yP -= speedP;

        //RESET QUANDO ESCE DALLO SCHERMO
        if (yP < 0) {
            shooting = false;
        }
        }
        
        //MOVIMENTO ASTEROIDE
        yA += speedA;
        
        //CONTEGGIO E RESET DEGLI ASTEROIDI
        if(yA > getHeight()) {
            punteggio = punteggio - 2;
            spawnAsteroide();
        }
        
        //CONTROLLO COLLISIONE PROIETTILE ASTEROIDE
        int margine = 40;

        if (xP + sizeP >= xA + margine &&
            xP <= xA + sizeA - margine &&
            yP + sizeP >= yA + margine &&
            yP <= yA + sizeA - margine) {

            //CONTEGGIO E RESET DEI COLPI
            punteggio++;
            shooting = false;
                
            //RESET ASTEROIDE
            spawnAsteroide(); 
        }
        
        //CONTROLLO COLLISIONE NAVICELLA ASTEROIDE
        int margine2 = 50;

        if (xN + size >= xA + margine2 &&
            xN <= xA + sizeA - margine2 &&
            yN + size >= yA + margine2 &&
            yN <= yA + sizeA - margine2) {

            esploso = true;
        }
    }

    
    //METODO PER SPAWNARE UN ASTEROIDE
    public void spawnAsteroide() {
        yA = -sizeA;        						//FUORI SCHERMO
       
        if(getWidth() == 0) {
            larghezza = 1280;
        } else {
            larghezza = getWidth();
        }
        xA = (int) ((larghezza - 180)  * Math.random()) + 50;    //SPAWN RANDOM
    }
    
    //METODO PAINTCOMPONENT PER LA GRAFICA
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //CENTRA LA NAVICELLA SOLO ALL'INIZIO
        if (navicellaCentrata == false) {
            xN = getWidth() / 2 - size / 2;
            navicellaCentrata = true;
        }

        //SFONDO
        g.drawImage(sfondo, 0, 0, getWidth(), getHeight(), this);
        
        //ASTEROIDE
        g.drawImage(asteroide, xA, yA, sizeA, sizeA, this);

        //NAVICELLA
        g.drawImage(navicella, xN, yN, size, size, this);

        //PROIETTILE
        if(shooting) {
            g.drawImage(colpo, xP, yP, sizeP, sizeP, this);
        }
        
        //SCRITTA GAME OVER
        if (esploso) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 80));
            g.drawString("GAME OVER", getWidth()/2 - 250, getHeight()/2);
        }
    }

    
    //METODO PER LA PRESSIONE SU TASTIERA
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_A) left = true;
        if (e.getKeyCode() == KeyEvent.VK_D) right = true;

        if (e.getKeyCode() == KeyEvent.VK_SPACE && shooting == false) {
            shooting = true;
            xP = xN + 55;
            yP = yN;
        }
    }

    
    //METODO PER RILASCIO SU TASTIERA
    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_A) left = false;
        if (e.getKeyCode() == KeyEvent.VK_D) right = false;
    }

    
    //METODO SOVRASCRITTO PER OBBLIGO
    @Override
    public void keyTyped(KeyEvent e) {}

    
    //METODO MAIN
    public static void main(String[] args) {

    	//CREAZIONE DELLA FINESTRA
        JFrame finestra = new JFrame("Space Invaders");
        finestra.setLayout(new BorderLayout());
        finestra.setExtendedState(JFrame.MAXIMIZED_BOTH);        
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Space_Invaders gioco = new Space_Invaders();

        //PUNTEGGIO
        JLabel labelColpiti = new JLabel("PUNTEGGIO: 0", SwingConstants.CENTER);
        labelColpiti.setFont(new Font("Arial", Font.BOLD, 40));
        labelColpiti.setForeground(Color.WHITE);
        labelColpiti.setBackground(Color.BLACK);
        labelColpiti.setOpaque(true);
        

        finestra.add(labelColpiti, BorderLayout.NORTH);
        finestra.add(gioco, BorderLayout.CENTER);

        finestra.setVisible(true);

        //TIMER
        new Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (esploso == false) {
                    labelColpiti.setText("PUNTEGGIO: " + gioco.punteggio);
                }
            }
        }).start();
    }

}
