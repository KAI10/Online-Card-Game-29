/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cards;

/**
 *
 * @author KAI10
 */
public class Player {

    
    public String hand[];
    
    public int currentCall;
    
    
    Player(){
        hand = new String[8];
    }

    public void sortHand() {
        String suitePrecedence = "HCDS", cardPrecedence = "78QK1A9J", temp;
        for (int i=0; i<8; i++){
            temp = hand[i];
            for (int j=i+1; j<8; j++){
                if (suitePrecedence.indexOf(hand[j].charAt(0)) < suitePrecedence.indexOf(temp.charAt(0))){
                    hand[i] = hand[j];
                    hand[j] = temp;
                    temp = hand[i];
                }
                else if (suitePrecedence.indexOf(hand[j].charAt(0)) == suitePrecedence.indexOf(temp.charAt(0))){
                    if (cardPrecedence.indexOf(hand[j].charAt(1)) > cardPrecedence.indexOf(temp.charAt(1))){
                        hand[i] = hand[j];
                        hand[j] = temp;
                        temp = hand[i];
                    }
                }
            }
        }
    }
    
}
