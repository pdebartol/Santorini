package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerController {

    private ArrayList<Player> players;
    private Player currentPlayer;

    public PlayerController() {
        players = new ArrayList<>();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer(){
        return players.get((players.indexOf(currentPlayer)+1)%players.size());
    }

    public void addPlayer(Player p){
        players.add(p);
    }

    public Integer getNumberOfPlayers(){
        return players.size();
    }


    public Player getPlayerByIndex(Integer index){
        return players.get(index);
    }

    public Player getPlayerByUsername(String playerUsername){
        for(Player p: players){
            if(p.getUsername().equals(playerUsername)){
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid Username!");
    }

    public void setCurrentPlayerByIndex(int index) {
        this.currentPlayer = players.get(index);
    }

    public void setCurrentPlayer(Player p){
        this.currentPlayer = p;
    }
}
