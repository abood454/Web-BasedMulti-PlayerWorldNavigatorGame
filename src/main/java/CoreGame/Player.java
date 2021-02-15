package CoreGame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Player  {

  private Room CurrentRoom;
  private String ClientId;
  private HashMap<Integer, Integer> Items = new HashMap<>();
  private ArrayList<Room> R = new ArrayList<>();
  private double Money;
  private String PlayerStatus = "North";
  private boolean FlashLight = false;
  private String Name = "";

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public String getClientId() {
    return ClientId;
  }

  public void setClientId(String clientId) {
    ClientId = clientId;
  }

  public Player() {}

  public void SetFlashLight(boolean a) {
    FlashLight = a;
  }

  public void AddItem(int x) {
    Items.put(x, 7);
  }

  public boolean IsThereKey(int x) {
    return Items.get(x) != null;
  }

  public boolean GetFlashLight() {
    return FlashLight;
  }

  public String PlayerFacing() {
    return PlayerStatus;
  }

  public void Change(String s) {
    PlayerStatus = s;
  }

  public String GetBackward() {
    if (PlayerStatus == "North") return "South";
    else if (PlayerStatus == "East") return "West";
    else if (PlayerStatus == "South") return "North";
    else return "East";
  }

  public void ListPlayerItems() {
    System.out.println(CurrentRoom);
    System.out.println("The Player has");
    int i = 0;
    for (HashMap.Entry<Integer, Integer> entry : Items.entrySet()) {
      System.out.println(i + " " + "Key : " + entry.getKey());
      i++;
    }
    if (FlashLight) System.out.println(i + " : " + "FlashLight");
    System.out.println("the end of player's items");
  }

  public double GetPlayerMoney() {
    return Money;
  }

  public void AddMoney(Double a) {
    Money += a;
  }

  public void RemoveMoney(Double a) {
    Money -= a;
  }

  public void Right() {
    if (PlayerStatus .equals("North")) PlayerStatus = "East";
    else if (PlayerStatus .equals("East")) PlayerStatus = "South";
    else if (PlayerStatus .equals("South")) PlayerStatus = "West";
    else PlayerStatus = "North";
  }

  public void Left() {
    if (PlayerStatus .equals( "North")) PlayerStatus = "West";
    else if (PlayerStatus .equals("East")) PlayerStatus = "North";
    else if (PlayerStatus .equals("South")) PlayerStatus = "East";
    else PlayerStatus = "South";
  }

  public Wall PlayerCheck() {
    return CurrentRoom.Action(PlayerStatus);
  }

  private Player(double Money, Room CurrentRoom) {
    this.Money = Money;
    this.CurrentRoom = CurrentRoom;
  }

  public static Player getInstance() {

    return new Player(50, new Room());
  }

  public void ChangeMoney(double M) {
    if (M >= 0) this.Money = M;
  }

  public void ChangeCurrentRoom(Room M) {
    this.CurrentRoom = M;
  }

  public double GetMoney() {
    return Money;
  }

  public Room GetCurrentRoom() {
    return CurrentRoom;
  }

  public void RemoveItem(int x) {
    if (Items.size() > x) Items.remove(x);
  }

  public int GetItem(int x) {
    if (Items.size() > x) return Items.get(x);
    else return -1;
  }

  public int ReturnSize() {
    return Items.size();
  }

  public Room getCurrentRoom() {
    return CurrentRoom;
  }

  public HashMap<Integer, Integer> getItems() {
    return Items;
  }

  public ArrayList<Room> getR() {
    return R;
  }

  public double getMoney() {
    return Money;
  }

  public String getPlayerStatus() {
    return PlayerStatus;
  }

  public boolean isFlashLight() {
    return FlashLight;
  }

  public void setCurrentRoom(Room currentRoom) {
    CurrentRoom = currentRoom;
  }

  public void setItems(HashMap<Integer, Integer> items) {
    Items = items;
  }

  public void setR(ArrayList<Room> r) {
    R = r;
  }

  public void setMoney(double money) {
    Money = money;
  }

  public void setPlayerStatus(String playerStatus) {
    PlayerStatus = playerStatus;
  }

  public void setFlashLight(boolean flashLight) {
    FlashLight = flashLight;
  }

  @Override
  public String toString() {
    return "Player{"
        + "CurrentRoom="
        + CurrentRoom
        + ", Items="
        + Items
        + ", R="
        + R
        + ", Money="
        + Money
        + ", PlayerStatus='"
        + PlayerStatus
        + '\''
        + ", FlashLight="
        + FlashLight
        + '}';
  }
}
