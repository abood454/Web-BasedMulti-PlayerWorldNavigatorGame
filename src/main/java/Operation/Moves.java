package Operation;

import CoreGame.*;
import JSON.JsonMaker;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import websocketdemo.WebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Moves {

    JsonMaker Json = new JsonMaker();
    PlayerAction playerAction = new PlayerAction();
    Gson gson = new Gson();



    public void Connect(WebSocketSession session ) throws IOException {

       JSONObject s = Json.ConnectMethod();

       System.out.println(s);
       String z = s.toString();

       TextMessage message1 = new TextMessage(z);

       WebSocketHandler.Players.put(s.get("PlayerId").toString() , session );
       Player p = new Player();
       p.setMoney(30);

       WebSocketHandler.PlayersObject.put(s.get("PlayerId").toString() , p);
       p.setClientId(s.get("PlayerId").toString());
       session.sendMessage(message1);

   }

    public String Create(WebSocketSession session , JsonObject object) throws IOException {

        System.out.println("Create");
        JSONObject s = Json.CreateMethod();
        System.out.println(s);
        String z = s.toString();
        TextMessage message1 = new TextMessage(z);
        session.sendMessage(message1);
        List<Player> players =  new ArrayList<>();
         players.add(WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString()));
        WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString())
                .setName(object.get("Name").toString());
        WebSocketHandler.PlayersByGame.put(s.get("GameId").toString() , players);
        return s.get("GameId").toString();

    }

    public void JoinGame(WebSocketSession session , JsonObject object) throws IOException{

        String NewGame = object.get("GameId").toString();

             if(WebSocketHandler.GamesRunNow.contains(NewGame)){
                WebSocketHandler.Games.put( object.get("PlayerId").toString() ,NewGame);

                JSONObject s = Json.Join(NewGame);
                String z = s.toString();

                TextMessage message1 = new TextMessage(z);
                List<Player> players =  WebSocketHandler.PlayersByGame.get(NewGame);

                 players.add(WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString()));
                 WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString())
                         .setName(object.get("Name").toString());

            session.sendMessage(message1);
            }
    }

   public void Forward(WebSocketSession session , JsonObject object) throws IOException, InterruptedException {
      MapGame Map = WebSocketHandler.Maps.get(object.get("GameId").toString());
      Player player = WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString());

      String facing = object.get("facing").toString();
      Wall wall = player.GetCurrentRoom().Action(facing);
      System.out.println("the  " + wall.getTypeObject());
       player.setPlayerStatus(facing);
       TextMessage message1;

       switch (wall.getTypeObject()) {
           case "Door":
               String move = playerAction.IsDoor(Map, player);
               switch (move) {
                   case "OPEN":
                       message1 = new TextMessage(Json.MoveMethod(true).toString());
                       session.sendMessage(message1);
                       Structure(session, object);
                       break;
                   case "OpenWithItems":
                       message1 = new TextMessage(Json.MoveMethod(true).toString());
                       session.sendMessage(new TextMessage(Json.PlayerItems(player).toString()));
                       session.sendMessage(message1);
                       Structure(session, object);
                       break;
                   case "FIGHT":
                       Fight(object);
                       break;
                   case "WIN":
                       Win(Map, player);
                       break;
                   case "ThereIsFight":
                       session.sendMessage(new TextMessage( Json.ThereIsFight().toString()));
                       break;
                   default:
                       message1 = new TextMessage(Json.MoveMethod(false).toString());
                       session.sendMessage(message1);
                       break;
               }

               break;
           case "Chest":
               System.out.println("here");
               boolean bool = playerAction.IsChest(wall, player);
               if (bool) {
                   session.sendMessage(new TextMessage(Json.ClosedChest(false).toString()));
                   session.sendMessage(new TextMessage(Json.PlayerItems(player).toString()));
               } else {
                   session.sendMessage(new TextMessage(Json.ClosedChest(true).toString()));
               }
               break;
           case "Seller":
               ArrayList<Integer> arr = playerAction.Seller(player, Map);

               String obj = gson.toJson(arr);
               JSONObject obj2 = new JSONObject();
               obj2.put("Method", "Seller");
               obj2.put("State", obj);

               message1 = new TextMessage(obj2.toString());
               session.sendMessage(message1);
               break;
           case "Mirror":
               playerAction.IsMirror(wall, player);
               session.sendMessage(new TextMessage(Json.PlayerItems(player).toString()));
               break;
           case "Painting":
               playerAction.IsPainting(wall, player);
               session.sendMessage(new TextMessage(Json.PlayerItems(player).toString()));
               break;
       }


   }

   void Win(MapGame mapGame , Player player) throws IOException, InterruptedException {
        WebSocketHandler.Players.get(player.getClientId()).sendMessage(new TextMessage(
                Json.WinMethod().toString()));

       List<Player> players = WebSocketHandler.PlayersByGame.get(mapGame.getGameId());
       for (Player value : players) {
           if (!value.getClientId().equals(player.getClientId()))
               WebSocketHandler.Players.get(value.getClientId()).sendMessage(new TextMessage(
                       Json.GameOverMethod().toString()));
       }
       WebSocketHandler.Players.get(player.getClientId()).close();

       Clear(mapGame);
   }

    public void TimeOver(MapGame mapGame ) throws IOException, InterruptedException {
        List<Player> players = WebSocketHandler.PlayersByGame.get(mapGame.getGameId());
        for (Player player : players) {
            WebSocketHandler.Players.get(player.getClientId()).sendMessage(new TextMessage(
                    Json.GameOverMethod().toString()));
        }
        Clear(mapGame);
    }

     void Clear (MapGame mapGame) throws IOException, InterruptedException {
       WebSocketHandler.PlayersByGame.remove(mapGame.getGameId());
       List<Player> players = mapGame.getPlayers();

         for (Player player : players) {
             WebSocketHandler.Players.get(player.getClientId()).close();
             WebSocketHandler.Players.remove(player.getClientId());
             WebSocketHandler.PlayersObject.remove(player.getClientId());
         }

        WebSocketHandler.Host.remove(mapGame.getHost());
        WebSocketHandler.GamesRunNow.remove(mapGame.getGameId());
        WebSocketHandler.Maps.remove(mapGame.getGameId());

   }



    public void BuyFromSeller(WebSocketSession session , JsonObject object) throws IOException {
        Player player = WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString());

        Wall Temp = player.GetCurrentRoom().Action(player.PlayerFacing());

        if(Temp.getTypeObject().equals("Seller") && isInteger(object.get("Key").toString())){
            Seller seller = (Seller)Temp;
      System.out.println(object.get("Key").toString());
            int KeyNumber =Integer.parseInt(object.get("Key").toString()) - 1;
            double Money = player.GetMoney();

            if(seller.getKeys().size() >= KeyNumber && Money >= 20){
                player.AddItem(seller.GetKey(KeyNumber));
                player.setMoney(Money - 20);
                session.sendMessage(new TextMessage(Json.PlayerItems(player).toString()));
            }
        }
        else{
            TextMessage message1 = new TextMessage("YOU CAN'T");
            session.sendMessage(message1);}

    }

    public  void Structure(WebSocketSession session , JsonObject object)  throws IOException{

        Player player = WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString());

         if(player != null){

        Json.StructureMethod(player.GetCurrentRoom());
        String JsonString = Json.StructureMethod(player.GetCurrentRoom()).toString();
        TextMessage message1 = new TextMessage(JsonString);
        session.sendMessage(message1);

    }
}


    public void DropAndLeave(WebSocketSession session , JsonObject object) {
        MapGame Map = WebSocketHandler.Maps.get(object.get("GameId").toString());
        Player player = WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString());

        Room room = player.GetCurrentRoom();
        java.util.Map<Integer , Integer> mp =  player.getItems();

        for (java.util.Map.Entry<Integer, Integer> set : mp.entrySet()) {
            room.getItemsOnFloor().add(set.getKey());
        }

    }

   public void Fight(JsonObject object) throws IOException {
       MapGame Map = WebSocketHandler.Maps.get(object.get("GameId").toString());
       Player player1 = WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString());

       Player player2 = Map.getPeople().get(player1.GetCurrentRoom());
       WebSocketSession session1 = WebSocketHandler.Players.get(player1.getClientId());
       WebSocketSession session2 = WebSocketHandler.Players.get(player2.getClientId());



       if(player1.getMoney() + player1.getItems().size()*10 > player2.getMoney() + player2.getItems().size()*10){

           session1.sendMessage(new TextMessage(Json.YouWinFight().toString()));
           session2.sendMessage(new TextMessage(Json.GameOverMethod().toString()));
           object.put("PlayerId" , "NO");
           PlayerDead(player2 , player1 , Map,object);
           UpdateAllPlayers(player2.GetMoney(),object.get("GameId").toString() );

       }
       else if(player1.getMoney() + player1.getItems().size()*10 < player2.getMoney() + player2.getItems().size()*10){


           session2.sendMessage(new TextMessage(Json.YouWinFight().toString()));
           session1.sendMessage(new TextMessage(Json.GameOverMethod().toString()));
           object.put("PlayerId" , "NO");
           PlayerDead(player1 , player2 ,Map,object);
           UpdateAllPlayers(player1.GetMoney(),object.get("GameId").toString() );

       }
       else{
           TextMessage message1 = new TextMessage(Json.FightMethod().toString());
           session1.sendMessage(message1);
           session2.sendMessage(message1);
           WebSocketHandler.Fight.put(player1,player2);
           WebSocketHandler.Fight.put(player2,player1);
       }



   }

   void UpdateAllPlayers(Double Money , String GameId) throws IOException {
       List<Player> players = WebSocketHandler.PlayersByGame.get(GameId);

       int MoneyByPlayer =(int)(Money/players.size());

       for (Player player : players) {
           player.setMoney(player.GetMoney() + MoneyByPlayer);
           WebSocketHandler.Players.get(player.getClientId()).sendMessage(
                   new TextMessage(Json.PlayerItems(player).toString()));

       }
   }


    public void EndFight(WebSocketSession session, JsonObject object) throws IOException {
       MapGame Map = WebSocketHandler.Maps.get(object.get("GameId").toString());
       Player player1 = WebSocketHandler.PlayersObject.get(object.get("PlayerId").toString());


           Player player2 = WebSocketHandler.Fight.get(player1);

           WebSocketSession session1 = WebSocketHandler.Players.get(object.get("PlayerId").toString());
           WebSocketSession session2 = WebSocketHandler.Players.get(player2.getClientId());

           String move1 = WebSocketHandler.RockPepper.get(player1);
           String move2 = WebSocketHandler.RockPepper.get(player2);

           if(move1 == null && move2 != null){
               session1.sendMessage( new TextMessage(Json.GameOverMethod().toString()));
               PlayerDead(player1 , player2,Map, object);
               UpdateAllPlayers(player1.GetMoney(),object.get("GameId").toString() );
           }
        if(move2 == null && move1 != null){
            session2.sendMessage( new TextMessage(Json.GameOverMethod().toString()));
            PlayerDead(player2 , player1,Map, object);
            UpdateAllPlayers(player2.GetMoney(),object.get("GameId").toString() );
        }
        if(move2 == null && move1 == null){
            TextMessage message1 = new TextMessage(Json.GameOverMethod().toString());
            session1.sendMessage(message1);
            session2.sendMessage(message1);
        }

    if (move1 != null && move2 != null) {
      if (move1.equals(move2)) {
        TextMessage message1 = new TextMessage(Json.FightMethod().toString());
          WebSocketHandler.Players.get(object.get("PlayerId").toString()).sendMessage(message1);
      } else if (move1.equals("Rock")) {
        if (move2.equals("Scissor")) {
          PlayerDead(player2, player1, Map, object);
          UpdateAllPlayers(player2.GetMoney(), object.get("GameId").toString());

        } else {
          PlayerDead(player1, player2, Map, object);
          UpdateAllPlayers(player1.GetMoney(), object.get("GameId").toString());
        }
      } else if (move1.equals("Scissor")) {
        if (move2.equals("Pepper")) {
          PlayerDead(player2, player1, Map, object);
          UpdateAllPlayers(player2.GetMoney(), object.get("GameId").toString());

        } else {
          PlayerDead(player1, player2, Map, object);
          UpdateAllPlayers(player1.GetMoney(), object.get("GameId").toString());
        }
      } else if (move1.equals("Pepper")) {
        if (move2.equals("Rock")) {
          PlayerDead(player2, player1, Map, object);
          UpdateAllPlayers(player2.GetMoney(), object.get("GameId").toString());

        } else {
          PlayerDead(player1, player2, Map , object);
          UpdateAllPlayers(player1.GetMoney(), object.get("GameId").toString());
        }
      }
}
   }

   public void PlayerDead(Player player1, Player player2 , MapGame mapGame , JsonObject object) throws IOException {
       WebSocketSession session1 = WebSocketHandler.Players.get(player2.getClientId());

       mapGame.getPeople().put(player2.GetCurrentRoom(), player2);
       mapGame.getHere()
               .put(player1.getCurrentRoom(), 1);
    System.out.println(mapGame.getHere().get(player1.getCurrentRoom()));
       Map<Integer , Integer> mp =  player1.getItems();
       for (Map.Entry<Integer, Integer> set : mp.entrySet()) {
           player2.getItems().put(set.getKey(), set.getValue());
       }
       session1.sendMessage(new TextMessage(Json.PlayerItems(player2).toString()));
       WebSocketHandler.PlayersByGame.get(mapGame.getGameId()).remove(player1);
       if(player1.getClientId().equals(object.get("PlayerId").toString())){
           WebSocketHandler.Players.get(object.get("PlayerId").toString()).sendMessage(new
                   TextMessage(Json.GameOverMethod().toString()));
    }
       if(player2.getClientId().equals(object.get("PlayerId").toString())){
           WebSocketHandler.Players.get(object.get("PlayerId").toString()).sendMessage(new
                   TextMessage(Json.YouWinFight().toString()));
       }

   }


    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }


    public void ReJoin(WebSocketSession session, JsonObject object) throws IOException {
       WebSocketHandler.Players.remove(object.get("PlayerId").toString());
       WebSocketHandler.Players.put(object.get("PlayerId").toString() , session);
       session.sendMessage( new TextMessage("YES YES YES"));
       if(object.get("TellMe").toString().equals("True"))
       HowArePlaying(object.get("GameId").toString());
    }

    public void HowArePlaying(String GameId) throws IOException {

       List<Player> players = WebSocketHandler.PlayersByGame.get(GameId);

       TextMessage message1 = new TextMessage(Json.UpdatePlayers(players).toString());

        for (Player player : players) {

            WebSocketHandler.Players.get(player.getClientId()).sendMessage(message1);
        }

    }

    public void StartGame(String GameId) throws IOException {
        List<Player> players = WebSocketHandler.PlayersByGame.get(GameId);

        TextMessage message1 = new TextMessage(Json.StartGame().toString());

        for (Player player : players) {

            WebSocketHandler.Players.get(player.getClientId()).sendMessage(message1);
        }
    }

}
