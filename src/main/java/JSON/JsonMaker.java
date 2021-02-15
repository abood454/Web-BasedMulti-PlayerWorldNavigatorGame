package JSON;

import CoreGame.Player;
import CoreGame.Room;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.google.gson.Gson;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class JsonMaker {

  private final Gson gson = new Gson();

  public JSONObject ConnectMethod() {
    JSONObject obj = new JSONObject();

    obj.put("Method", "Connect");
    String uuid = UUID.randomUUID().toString().replace("-", "");
    obj.put("PlayerId", uuid);
    System.out.println(obj);
    return obj;
  }

  public JSONObject CreateMethod() {
    JSONObject obj = new JSONObject();

    obj.put("Method", "Create");
    String uuid = UUID.randomUUID().toString().replace("-", "");
    obj.put("GameId", uuid);
    System.out.println(obj);
    return obj;
  }

  public JSONObject Join(String NewGame) {
    JSONObject obj = new JSONObject();
    obj.put("Method", "Join");
    obj.put("State", "Yes");
    obj.put("GameId", NewGame);
    return obj;
  }

  public JSONObject PreviousPlayer(String playid, String gameId) {
    JSONObject obj = new JSONObject();

    obj.put("Method", "ReJoin");
    obj.put("GameId", gameId);
    obj.put("PlayerId", playid);

    return obj;
  }

  public JSONObject MoveMethod(boolean state) {
    JSONObject obj = new JSONObject();
    obj.put("Method", "Move");
    if (state) obj.put("State", "Yes");
    else obj.put("State", "No");
    return obj;
  }

  public JSONObject ItStarted(){
    JSONObject obj = new JSONObject();
    obj.put("Method", "ItStarted");
    obj.put("State", "Yes");
    return obj;
  }

  public JSONObject ClosedChest(boolean state){
    JSONObject obj = new JSONObject();
    obj.put("Method", "ClosedChest");
    if(state)
    obj.put("State", "Yes");
    else obj.put("State", "No");

    return obj;
  }


  public JSONObject StructureMethod(Room room) {

    JSONObject obj = new JSONObject();

    obj.put("Method", "Structure");
    System.out.println(room);
    obj.put("North", room.GetTheNorthWall().getTypeObject());
    obj.put("East", room.GetTheEastWall().getTypeObject());
    obj.put("South", room.GetTheSouthWall().getTypeObject());
    obj.put("West", room.GetTheWestWall().getTypeObject());

    return obj;
  }

  public JSONObject FightMethod() {

    JSONObject obj = new JSONObject();

    obj.put("Method", "Fight");

    return obj;
  }


  public JSONObject GameOverMethod() {

    JSONObject obj = new JSONObject();

    obj.put("Method", "GameOver");

    return obj;
  }

  public JSONObject WinMethod() {

    JSONObject obj = new JSONObject();

    obj.put("Method", "WINNER!");

    return obj;
  }

  public JSONObject UpdateMoneyMethod(int MoneyByPlayer) {
    JSONObject obj = new JSONObject();

    obj.put("Method", "Update");

    obj.put("Money", MoneyByPlayer);

    return obj;
  }

  public JSONObject UpdatePlayers(List<Player> players) {

    JSONObject obj = new JSONObject();

    obj.put("Method", "UpdatePlayers");
    List<String> temp = new ArrayList<>();
    for (Player player : players) temp.add(player.getName());

    String arr = gson.toJson(temp);
    obj.put("State", arr);

    return obj;
  }

  public JSONObject StartGame() {

    JSONObject obj = new JSONObject();

    obj.put("Method", "StartGame");

    return obj;
  }

  public JSONObject PlayerItems(Player player) {

    JSONObject obj = new JSONObject();

    obj.put("Method", "PlayerItems");

    List<String> temp = new ArrayList<>();
    HashMap<Integer, Integer> Items = player.getItems();

    for (HashMap.Entry<Integer, Integer> entry : Items.entrySet()) {
      temp.add(entry.getKey().toString());
    }

    String arr = gson.toJson(temp);
    obj.put("State", arr);
    obj.put("Money", player.getMoney());

    return obj;
  }

  public JSONObject YouWinFight(){
    JSONObject obj = new JSONObject();

    obj.put("Method", "YouWinFight");

    return obj;
  }

  public JSONObject ThereIsFight(){

    JSONObject obj = new JSONObject();

    obj.put("Method", "ThereIsFight");

    return obj;
  }

}
