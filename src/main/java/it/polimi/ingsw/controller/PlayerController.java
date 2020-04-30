package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enums.Error;

import java.util.ArrayList;

/**
 * PlayerController handles the players in the current match.
 * @author pierobartolo
 */

public class PlayerController {

    private ArrayList<Player> players;
    private Player currentPlayer;
    String challengerUsername;

    public PlayerController() {
        players = new ArrayList<>();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer(){
        return players.get((players.indexOf(currentPlayer)+1)%players.size());
    }

    public void nextTurn(){
        this.getNextPlayer().setActive(false);
        currentPlayer =  this.getNextPlayer();
        currentPlayer.setActive(true);
    }

    public ArrayList<Error> addPlayer(Player player){
        ArrayList<Error> errors = new ArrayList<>();
        for(Player p: players){
            if(player.getUsername().equals(p.getUsername())) errors.add(Error.LOGIN_USERNAME_NOT_AVAILABLE);
            if(player.getWorkers().get(0).getColor().equals(p.getWorkers().get(0).getColor())) errors.add(Error.LOGIN_COLOR_NOT_AVAILABLE);
            if(players.size() >= 3 && !errors.contains(Error.LOGIN_TOO_MANY_PLAYERS)) errors.add(Error.LOGIN_TOO_MANY_PLAYERS);
        }

        if(errors.isEmpty())
            players.add(player);

        return errors;
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
        if(currentPlayer != null) currentPlayer.setActive(false);
        this.currentPlayer = players.get(index);
        currentPlayer.setActive(true);
    }

    public void setCurrentPlayer(Player p){
        if(currentPlayer != null) currentPlayer.setActive(false);
        this.currentPlayer = p;
        currentPlayer.setActive(true);
    }

    public void setChallengerUsername(String challengerUsername){
        this.challengerUsername = challengerUsername;
    }

    public String getChallengerUsername(){
        return challengerUsername;
    }
}
