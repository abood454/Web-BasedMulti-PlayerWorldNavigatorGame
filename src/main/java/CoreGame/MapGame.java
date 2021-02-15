package CoreGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapGame {

  private HashMap<Integer, Room> Graph = new HashMap<>();
  private List<Player> Players = new ArrayList<>();
  private Reminder Timer;
  private List<Room> Rooms = new ArrayList<>();
  private Map<Door, Integer> WinningDoors = new HashMap<>();
  private Map<Room, Player> people = new HashMap<>();
  private Map<Room, Integer> here = new HashMap<>();
  private String GameId;
  private String Host;

  public void setTimer(Reminder timer) {
    Timer = timer;
  }

  public String getHost() {
    return Host;
  }

  public void setHost(String host) {
    Host = host;
  }

  public String getGameId() {
    return GameId;
  }

  public void setGameId(String gameId) {
    GameId = gameId;
  }

  public Map<Room, Player> getPeople() {
    return people;
  }

  public void setPeople(HashMap<Room, Player> people) {
    this.people = people;
  }

  public void AddPlayer(Player player) {
    Players.add(player);
  }

  public void AddWinningDoor(Door door) {
    WinningDoors.put(door, 1);
  }

  public Map<Door, Integer> getWinningDoors() {
    return WinningDoors;
  }

  public List<Player> getPlayers() {
    return Players;
  }


  public Reminder GetTimer() {
    return Timer;
  }

  public void StartTimer(int seconds) throws IOException, InterruptedException {
    Timer = new Reminder(seconds, this);
  }

  public void Loes() {
    System.out.println("GAME OVER");
    System.exit(0);
  }

  public void AddRoom(Room room) {
    Graph.put(room.GetRoomNumber(), room);
    Rooms.add(room);
    here.put(room, 0);
  }

  public Room GetNextRoom(int x) {
    return Graph.get(x);
  }

  public void MakePairRoom(Door Door1, int FristRoom, int SecondRoom) {
    if (Graph.get(FristRoom) != null && Graph.get(SecondRoom) != null) {
      Door1.SetFirstRoom(FristRoom);
      Door1.SetSecondRoom(SecondRoom);
    }
  }

  public HashMap<Integer, Room> getGraph() {
    return Graph;
  }

  public Reminder getTimer() {
    return Timer;
  }



  public List<Room> getRooms() {
    return Rooms;
  }

  @Override
  public String toString() {
    return "Map{"
        + "Graph="
        + Graph
        + ", MyPlayer="
        + Players
        + ", Timer="
        + Timer
        + ", Rooms="
        + Rooms
        + ", WinningDoor="
        + WinningDoors
        + '}';
  }

  public Map<Room, Integer> getHere() {
    return here;
  }

}
