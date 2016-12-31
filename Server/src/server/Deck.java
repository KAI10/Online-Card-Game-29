/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author KAI10
 */
public interface Deck {
    
    public final int TOTAL = 32; 
    
    public final String [] playCard ={"C7", "C8","CQ","CK","C10","CA","C9","CJ",
                                      "D7", "D8","DQ","DK","D10","DA","D9","DJ",
                                      "H7", "H8","HQ","HK","H10","HA","H9","HJ",
                                      "S7", "S8","SQ","SK","S10","SA","S9","SJ"};
    
    public final String [] trumpCard = {"C2", "D2", "H2", "S2", "Joker"};
    
}
