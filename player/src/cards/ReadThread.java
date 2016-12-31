/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KAI10
 */
public class ReadThread implements Runnable {

    private Thread t;
    private ObjectInputStream in;
    private Object r;

    ReadThread(InputStream in) throws IOException {

        this.in = new ObjectInputStream(in);
        r = new Object();
        
        System.out.println("In read");
        t = new Thread(this, "read");
        System.out.println("Thread: " + t);
        t.start();
    }

    @Override
    public void run() {

        while (true) {
            try {
                System.out.println("BEFORE");
                r = in.readObject();

                if (All.mode == 0) {
                    System.out.println(r);
                    Client.player.hand = (String[]) r;
                    System.out.println("In mode = 0");
                    All.mode++;

                }
                if (All.mode == 1) {
                    System.out.println("In mode = 1");
                    r = in.readObject();
                    Client.id = (Integer) r;
                    All.status = true;

                } else if (All.mode == 2) {

                    Client.firstCaller = (Integer) r;
                    All.status = true;

                } else if (All.mode == 3) {
                    t.sleep(3000);
                    Client.player.currentCall = (Integer) r;
                    All.status = true;
                    
                } else if (All.mode == 4) {
                    All.setter = (int[]) r;
                    All.status = true;
                    
                } else if (All.mode == 5) {
                    System.out.println("In mode 5");

                    t.sleep(1000);
                    for (int i = 0; i < 4; i++) {
                        All.labels[i].setIcon(null);
                    }

                    if (All.gameCount < 8) {
                        All.gameStarter = (Integer) r;

                        int pos = (Client.id >= All.gameStarter) ? (Client.id - All.gameStarter) : (4 + Client.id - All.gameStarter);

                        System.out.println("POS = " + pos);

                        for (int i = 0; i < pos; i++) {
                            System.out.println("Before read");
                            r = in.readObject();
                            System.out.println("after read");
                            All.singleHandCards = (String) r;
                            if (i==0) All.firstPlayedCard = All.singleHandCards;
                            All.labels[pos - i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/cards/Images/" + All.singleHandCards + ".png")));
                        }

                        All.buttonPress = true;
                        All.rPrompt.setText("PLAY");
                        System.out.println(All.buttonPress);
                        
                        while (All.buttonPress) t.sleep(10);    //for 4th player this thread now cannot make firstplayedcard null prior to playing
                        
                        All.rPrompt.setText(null);
                        for (int i = 0; i < 3 - pos; i++) {
                            r = in.readObject();
                            All.singleHandCards = (String) r;
                            All.labels[3 - i].setIcon(new javax.swing.ImageIcon(getClass().getResource("/cards/Images/" + All.singleHandCards + ".png")));
                        }
                        
                        
                        All.firstPlayedCard = null;
                        All.mode--;
                        All.gameCount++;

                    }
                    if (All.gameCount == 8) {
                        
                        t.sleep(1000);
                        for (int i = 0; i < 4; i++) {
                            All.labels[i].setIcon(null);
                        }/*
                        All.gameCount = 0;                 //tried to repeat the game, but couldn't finish due to read/write problems
                        All.mode = -1;
                        All.repeat.setEnabled(true);
                        All.repeat.setVisible(true);*/
                        r = in.readObject();
                        int cond = (Integer) r;
                        
                        All.rHands.setEnabled(false);
                        All.rHands.setVisible(false);
                        
                        All.rResult.setEnabled(true);
                        All.rResult.setVisible(true);
                        
                        if (cond==1){
                            All.rShowResult.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cards/Images/win.png")));
                        }
                        else{
                            All.rShowResult.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cards/Images/lose.png")));
                        }
                        
                        All.rCredit.setVisible(true);
                        All.rCredit.setEnabled(true);
                        break;
                    }
                }

                All.mode++;

                if (All.mode == 3) {
                    t.sleep(10);     // just to pause for a while so that in callManage mode is updated if necessary
                }

            } catch (IOException | ClassNotFoundException | InterruptedException ex) {
                Logger.getLogger(ReadThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
