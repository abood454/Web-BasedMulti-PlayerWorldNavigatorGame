package websocketdemo;

import CoreGame.*;
import JSON.JsonMaker;
import Operation.Moves;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.gson.Gson;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.*;

public class WebSocketHandler extends AbstractWebSocketHandler {

  public static Map<String, WebSocketSession> Players = new HashMap<>();
  public static Map<String, Player> PlayersObject = new HashMap<>();
  public static Map<String, String> Games = new HashMap<>();
  public static Map<String, MapGame> Maps = new HashMap<>();
  public static Set<String> GamesRunNow = new HashSet<>();
  public static Set<String> GamesStarted = new HashSet<>();
  public static Map<String, List<Player>> PlayersByGame = new HashMap<>();
  public static Map<Player, Player> Fight = new HashMap<>();
  public static Map<Player, String> RockPepper = new HashMap<>();
  public static Map<String, String> Host = new HashMap<>();

  JsonMaker Json = new JsonMaker();
  Moves Operations = new Moves();
  MapGenerator mapGenerator = new MapGenerator();

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message)
      throws IOException, InterruptedException {
    System.out.println("New Text Message Received");
    Gson g = new Gson();
    JsonObject object = g.fromJson(message.getPayload(), JsonObject.class);
    System.out.println(object);

    if (object.get("Method").equals("Connect")) {
      Operations.Connect(session);
    } else if (object.get("Method").equals("Create")) {
      Create(session, object);
    } else if (object.get("Method").equals("JoinGame")) {
      JoinGame(session, object);
    } else if (object.get("Method").equals("ReJoin")) {
        Operations.ReJoin(session, object);
    } else {
      PlayerActions(session, object);
    }
  }

  @Override
  protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message)
      throws IOException {
    System.out.println("New Binary Message Received");
    session.sendMessage(message);
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    super.afterConnectionEstablished(session);

    System.out.println("HI there");
  }

  void Create(WebSocketSession session, JsonObject object) {
    String Game = null;
    try {
      Game = Operations.Create(session, object);
    } catch (IOException e) {
      e.printStackTrace();
    }
    GamesRunNow.add(Game);

    Games.put(object.get("PlayerId").toString(), Game);
    MapGame NewGame = new MapGame();
    NewGame.setGameId(Game);

    NewGame.setHost(object.get("PlayerId").toString());

    if (object.get("Test").equals("No")) {
      mapGenerator.Generate(NewGame);
      mapGenerator.AddPlayer(PlayersObject.get(object.get("PlayerId").toString()) ,NewGame);
    }
    else {
      Test(NewGame);
      AddPlayer(PlayersObject.get(object.get("PlayerId").toString()), NewGame);
        }
    Maps.put(Game, NewGame);
    Host.put(object.get("PlayerId").toString(), Game);
  }

  void JoinGame(WebSocketSession session, JsonObject object) {
    if (!GamesStarted.contains(object.get("GameId").toString())) {
      try {
        Operations.JoinGame(session, object);
      } catch (IOException e) {
        e.printStackTrace();
      }

      AddPlayer(
          PlayersObject.get(object.get("PlayerId").toString()),
          Maps.get(object.get("GameId").toString()));
    }
    else {
      try {
        session.sendMessage(new TextMessage(Json.ItStarted().toString()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  void PlayerActions(WebSocketSession session, JsonObject object)
      throws IOException, InterruptedException {
    if (GamesRunNow.contains(object.get("GameId").toString())) {
      if (object.get("Method").equals("Forward")) {
        Operations.Forward(session, object);

        if (GamesRunNow.contains(object.get("GameId").toString())) Operations.Structure(session, object);

      } else if (object.get("Method").equals("Buy")) {
        Operations.BuyFromSeller(session, object);

      } else if (object.get("Method").equals("EndFight")) {
        Operations.EndFight(session, object);

      } else if (object.get("Method").equals("RockPepperScissor")) {
        Player player1 = PlayersObject.get(object.get("PlayerId").toString());

        if (Fight.get(player1) != null) {
          System.out.println("RockPepperScissor");

          String move = object.get("Move").toString();
          RockPepper.put(player1, move);
        }
      } else if (object.get("Method").equals("StartGame")) {
        String PlayerId = object.get("PlayerId").toString();

        if (Host.get(PlayerId) != null
            && Host.get(PlayerId).equals(object.get("GameId").toString())) {
          Operations.StartGame(object.get("GameId").toString());
        }

      } else if (object.get("Method").equals("Start")) {
        Operations.Structure(session, object);
        Maps.get(object.get("GameId").toString()).StartTimer(100000);
        GamesStarted.add(object.get("GameId").toString());
      } else if (object.get("Method").equals("DropAndLeave")) {
        Operations.DropAndLeave(session, object);
      }
    }
  }

  void Test(MapGame M) {
    Room R1 = new Room(), R2 = new Room(), R3 = new Room(), R4 = new Room(), R5 = new Room();
    Seller s1 = new Seller();
    s1.AddKey(2, 50);
    Door D = new Door(true, -1), D2 = new Door(true, -1);
    Chest c1 = new Chest.Builder(-1, false).AddGold(50.0).build();
    R1.SetTheEastWall(D);
    R1.SetTheWestWall(s1);
    R1.SetTheNorthWall(c1);
    R1.SetTheSouthWall(D2);
    M.AddRoom(R1);
    // R3
    Seller s2 = new Seller();
    s2.AddKey(1, 50);
    R3.SetTheSouthWall(s2);
    R3.SetTheNorthWall(D2);
    M.AddRoom(R3);

    ///////////////////////////////
    Door D3 = new Door(true, -1);
    Painting p1 = new Painting(false, 0, 50);
    R2.SetTheWestWall(D);
    R2.SetTheNorthWall(p1);
    R2.SetTheEastWall(D3);
    M.AddRoom(R2);
    ////////////////////////////////
    Chest c2 = new Chest.Builder(-1, false).AddGold(100.0).build();
    Chest c3 = new Chest.Builder(3, true).AddKey(2).build();
    Mirror m1 = new Mirror(false, 0, 0, true);
    R4.SetTheNorthWall(c2);
    R4.SetTheEastWall(c3);
    R4.SetTheSouthWall(m1);
    R4.SetTheWestWall(D3);
    M.AddRoom(R4);
    /////////////////////////////////////////////////////////
    R5.SetDark(true);
    Door D4 = new Door(false, 3);
    M.getWinningDoors().put(D4, 1);

    R5.SetTheSouthWall(D4);
    R2.SetTheSouthWall(D4);
    M.AddRoom(R5);
    ///////////////////////////////////////////////////////////

    M.MakePairRoom(D, R1.GetRoomNumber(), R2.GetRoomNumber());
    M.MakePairRoom(D2, R1.GetRoomNumber(), R3.GetRoomNumber());
    M.MakePairRoom(D3, R2.GetRoomNumber(), R4.GetRoomNumber());
    M.MakePairRoom(D4, R5.GetRoomNumber(), R2.GetRoomNumber());
  }

  public void AddPlayer(Player player, MapGame map) {
    System.out.println(map);
    map.AddPlayer(player);
    Random rand = new Random();
    int int_random = rand.nextInt(map.getRooms().size() - 1);
    if (map.getPlayers().size() == 1) {
      player.ChangeCurrentRoom(map.getRooms().get(0));
      map.getPeople().put(map.getRooms().get(0), player);
      map.getHere().put(map.getRooms().get(0),1);
    } else {
      player.ChangeCurrentRoom(map.getRooms().get(1));
      map.getPeople().put(map.getRooms().get(1), player);
      map.getHere().put(map.getRooms().get(1),1);
    }

  }
}
