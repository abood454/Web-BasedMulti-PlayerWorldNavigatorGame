package websocketdemo;

import CoreGame.MapGame;
import CoreGame.MapGenerator;
import CoreGame.Player;
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
      mapGenerator.AddPlayer(PlayersObject.get(object.get("PlayerId").toString()), NewGame);
    } else {
      mapGenerator.Test(NewGame);
      mapGenerator.AddPlayerTest(PlayersObject.get(object.get("PlayerId").toString()), NewGame);
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

      if ( Maps.get(object.get("GameId").toString()).getRooms().size() > 20) {
        mapGenerator.AddPlayer(
            PlayersObject.get(object.get("PlayerId").toString()),
            Maps.get(object.get("GameId").toString()));
      } else {
        mapGenerator.AddPlayerTest(
            PlayersObject.get(object.get("PlayerId").toString()),
            Maps.get(object.get("GameId").toString()));
      }
    } else {
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

        if (GamesRunNow.contains(object.get("GameId").toString()))
          Operations.Structure(session, object);

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
}
