/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

/**
 *
 * @author Boo
 */
public class LobbyPublic {
    private String lobbyName;
    private String lobbyID;
    private String isLobbySecure;
    public LobbyPublic(String tempName, String tempID, String tempSecure){
        lobbyName = tempName;
        lobbyID = tempID;
        isLobbySecure = tempSecure;
    }
    
    public String getLobbyName(){
        return lobbyName;
    }
    public String getLobbyID(){
        return lobbyID;
    }
    public String getLobbySecure(){
        return isLobbySecure;
    }
}
