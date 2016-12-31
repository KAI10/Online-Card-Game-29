/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KAI10
 */
public class Game implements Runnable{

    private Thread gameThread;
    private ClientThread player[];
    private int callStarter, pt1, pt2;
    private int[] trumpSetter;
    private String trumpCard;
    private String[] inGameCards;
    private boolean trumpShown;

    public Game(ClientThread player[]) throws IOException, ClassNotFoundException, InterruptedException {
        
        //this.player = new ClientThread[4];
        this.player = player;
        System.out.println("Constructor game");
        callStarter = 0;
        pt1 = 0;
        pt2 = 0;
        inGameCards = new String[4];
        trumpSetter = new int[2];
        
        gameThread = new Thread (this, "GAME");
        System.out.println("THREAD: " + gameThread);
        gameThread.start();
    }

    private void deal() {

        String all[];
        all = new String[32];
        System.arraycopy(Deck.playCard, 0, all, 0, all.length);

        int x;
        for (int i = 0; i < 32; i++) {
            Random rand = new Random();
            x = rand.nextInt(32);

            String temp;
            temp = all[i];
            all[i] = all[x];
            all[x] = temp;
        }

        for (int i = 0; i < 8; i++) {
            this.player[0].hand[i] = all[4 * i];
            this.player[1].hand[i] = all[4 * i + 1];
            this.player[2].hand[i] = all[4 * i + 2];
            this.player[3].hand[i] = all[4 * i + 3];
        }
    }

    private void sendCards() throws IOException, ClassNotFoundException, InterruptedException {

        System.out.println(player[0].hand[0] + " " + player[0].hand[1] + " " + player[0].hand[2] + " " + player[0].hand[3] + " ");
        for (int i = 0; i < 4; i++) {
            player[i].resume();
            player[i].write(player[i].hand);
            player[i].write((Integer) player[i].serial);
            player[i].mySuspend();
        }

        System.out.println(player[0].hand[0] + " " + player[0].hand[1] + " " + player[0].hand[2] + " " + player[0].hand[3] + " ");

    }

    private void gameManage() throws IOException, ClassNotFoundException {

        callTrump();
        startGame();

        //print winner
    }

    private void callTrump() throws IOException, ClassNotFoundException {
        int call, pos = -1, max = 0;
        //sending callstarter

        for (int i = 0; i < 4; i++) {
            player[i].resume();
            player[i].write((Integer) callStarter);
            player[i].mySuspend();
        }

        // call receiver and sender
        for (int i = 0; i < 4; i++) {
            player[(callStarter + i) % 4].resume();
            call = (int) player[(callStarter + i) % 4].read();
            System.out.println("CALL = " + call);
            if (call > max) {
                max = call;
                pos = (callStarter + i) % 4;
            }
            player[(callStarter + i) % 4].mySuspend();

            if (i != 3) {  // later it will be 3
                player[(callStarter + i + 1) % 4].resume();   // later 2 will be 4
                player[(callStarter + i + 1) % 4].write((Integer) max);
                player[(callStarter + i + 1) % 4].mySuspend();
            }
        }

        System.out.println("Call sent");

        trumpSetter[0] = pos;
        trumpSetter[1] = max;

        for (int i = 0; i < 4; i++) {

            player[i].resume();
            player[i].write(trumpSetter);
            player[i].mySuspend();
        }

        System.out.println("trump sent");

        player[pos].resume();
        trumpCard = (String) player[pos].read();
        player[pos].mySuspend();

        System.out.println(trumpCard);
    }

    private void startGame() throws IOException, ClassNotFoundException {
        int gameStarter = callStarter, winner, pt, pointT1 = 0, pointT2 = 0;

        for (int j = 0; j < 8; j++) {
            pt = 0;

            for (int i = 0; i < 4; i++) {
                player[(i + gameStarter) % 4].resume();
                player[(i + gameStarter) % 4].write((Integer) gameStarter);
                player[(i + gameStarter) % 4].mySuspend();
            }

            for (int i = 0; i < 4; i++) {
                player[(i + gameStarter) % 4].resume();
                //player[(i+gameStarter)%4].write((Integer)(i+gameStarter)%4);
                inGameCards[i] = (String) player[(i + gameStarter) % 4].read();

                /*if (inGameCards[i].equals("TrumpShown")) {
                    trumpShown = true;
                    //this is to be done
                    
                     write trump to all other clients
                     
                    inGameCards[i] = (String) player[(i + gameStarter) % 4].read();
                }*/

                System.out.println(inGameCards[i]);
                // card sent to all others
                player[(i + gameStarter + 1) % 4].resume();
                player[(i + gameStarter + 1) % 4].write(inGameCards[i]);
                player[(i + gameStarter + 1) % 4].mySuspend();
                player[(i + gameStarter + 2) % 4].resume();
                player[(i + gameStarter + 2) % 4].write(inGameCards[i]);
                player[(i + gameStarter + 2) % 4].mySuspend();
                player[(i + gameStarter + 3) % 4].resume();
                player[(i + gameStarter + 3) % 4].write(inGameCards[i]);
                player[(i + gameStarter + 3) % 4].mySuspend();

                player[(i + gameStarter) % 4].mySuspend();
                pt += findPoint(inGameCards[i].charAt(1));
            }

            winner = findWinner(gameStarter);
            if (winner % 2 == 0) {
                pointT1 += pt;
            } else {
                pointT2 += pt;
            }
            gameStarter = winner;

            System.out.println("WINNER: " + gameStarter);
        }

        if (trumpSetter[0] % 2 == 0) {        //sending who won the match
            if (pointT1 >= trumpSetter[1]) {
                pt1++;
                player[0].resume();
                player[0].write((Integer) 1);
                player[0].mySuspend();
                player[2].resume();
                player[2].write((Integer) 1);
                player[2].mySuspend();
                player[1].resume();
                player[1].write((Integer) 0);
                player[1].mySuspend();
                player[3].resume();
                player[3].write((Integer) 0);
                player[3].mySuspend();
            } else {
                pt1--;
                player[0].resume();
                player[0].write((Integer) 0);
                player[0].mySuspend();
                player[2].resume();
                player[2].write((Integer) 0);
                player[2].mySuspend();
                player[1].resume();
                player[1].write((Integer) 1);
                player[1].mySuspend();
                player[3].resume();
                player[3].write((Integer) 1);
                player[3].mySuspend();
            }
        } else {
            if (pointT2 >= trumpSetter[1]) {
                pt2++;
                player[0].resume();
                player[0].write((Integer) 0);
                player[0].mySuspend();
                player[2].resume();
                player[2].write((Integer) 0);
                player[2].mySuspend();
                player[1].resume();
                player[1].write((Integer) 1);
                player[1].mySuspend();
                player[3].resume();
                player[3].write((Integer) 1);
                player[3].mySuspend();
            } else {
                pt2--;
                player[0].resume();
                player[0].write((Integer) 1);
                player[0].mySuspend();
                player[2].resume();
                player[2].write((Integer) 1);
                player[2].mySuspend();
                player[1].resume();
                player[1].write((Integer) 0);
                player[1].mySuspend();
                player[3].resume();
                player[3].write((Integer) 0);
                player[3].mySuspend();
            }
        }

    }

    private int findPoint(char num) {
        if (num == 'J') {
            return 3;
        }
        if (num == '9') {
            return 2;
        }
        if (num == '1' || num == 'A') {
            return 1;
        }
        return 0;
    }

    private int findWinner(int gameStarter) {
        int winner = gameStarter;
        String maxCard = inGameCards[0];
        for (int k = 1; k < 4; k++) {
            /*if (trumpShown) {

             if (inGameCards[k].charAt(0) == maxCard.charAt(0)) {
             if (findPoint(inGameCards[k].charAt(1)) > findPoint(maxCard.charAt(1))) {
             maxCard = inGameCards[k];
             winner = (k + gameStarter) % 4;
             }
             } else if (inGameCards[k].charAt(0) == trumpCard.charAt(0)) {
             maxCard = inGameCards[k];
             }

             } else {*/
            // who gets the hand in case of no point on table is to be fixed
            if (inGameCards[k].charAt(0) == maxCard.charAt(0)) {
                if (findPoint(inGameCards[k].charAt(1)) > findPoint(maxCard.charAt(1))) {
                    maxCard = inGameCards[k];
                    winner = (k + gameStarter) % 4;
                } else if (findPoint(inGameCards[k].charAt(1)) == findPoint(maxCard.charAt(1))) {
                    if (findPoint(inGameCards[k].charAt(1)) == 1) {
                        if (inGameCards[k].charAt(1) == 'A') {
                            maxCard = inGameCards[k];
                            winner = (k + gameStarter) % 4;
                        }
                    } else if (findPoint(inGameCards[k].charAt(1)) == 0) {
                        if (inGameCards[k].charAt(1) == 'K') {
                            maxCard = inGameCards[k];
                            winner = (k + gameStarter) % 4;
                        } else if (inGameCards[k].charAt(1) == 'Q') {
                            if (maxCard.charAt(1) != 'K') {
                                maxCard = inGameCards[k];
                                winner = (k + gameStarter) % 4;
                            }
                        } else if (inGameCards[k].charAt(1) > maxCard.charAt(1)) {
                            maxCard = inGameCards[k];
                            winner = (k + gameStarter) % 4;
                        }
                    }
                }
            }

        }

        //}
        return winner;
    }

    @Override
    public void run() {
        
        try {
            //while ((pt1 != 6 || pt1 != -6) || (pt2 != 6 || pt2 != -6)) {  //tried to repeat the game, but couldn't finish due to read/write problems
            deal();
            sendCards();
            gameManage();
            
            /*callStarter++;
            if (callStarter == 4) {
            callStarter = 0;
            }*/
            //}
        } catch (IOException | ClassNotFoundException | InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
