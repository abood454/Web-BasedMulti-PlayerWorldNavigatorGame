package CoreGame;

import java.io.Serializable;

public class Door implements Wall, Serializable {

  private boolean Open;
  private int Key, FirstRoom, SecondRoom;
  private static int CountRoom = 0;
  private int RoomId;

  public Door() {
    setTypeObject("Door");
  }

  public Door(boolean Open, int Key) {
    setTypeObject("Door");
    this.Open = Open;
    this.Key = Key;
    RoomId = ++CountRoom;
  }

  public int GetKey() {
    return Key;
  }

  public void SetOpen(boolean t) {
    Open = false;
  }

  public void SetFirstRoom(int i) {
    FirstRoom = i;
  }

  public int GetNextRoom(int i) {
    if (FirstRoom == i)
      return SecondRoom;
    else
      return FirstRoom;
  }

  public int GetSecondRoom() {
    return SecondRoom;
  }

  public void SetSecondRoom(int i) {
    SecondRoom = i;
  }

  public boolean IsOpen() {
    return Open;
  }

  public boolean isOpen() {
    return Open;
  }

  public int getKey() {
    return Key;
  }

  public int getFirstRoom() {
    return FirstRoom;
  }

  public int getSecondRoom() {
    return SecondRoom;
  }

  public static int getCountRoom() {
    return CountRoom;
  }

  public int getRoomId() {
    return RoomId;
  }

  public void setOpen(boolean open) {
    Open = open;
  }

  public void setKey(int key) {
    Key = key;
  }

  public void setFirstRoom(int firstRoom) {
    FirstRoom = firstRoom;
  }

  public void setSecondRoom(int secondRoom) {
    SecondRoom = secondRoom;
  }

  public static void setCountRoom(int countRoom) {
    CountRoom = countRoom;
  }

  public void setRoomId(int roomId) {
    RoomId = roomId;
  }

  private String TypeObject = "wall";
  private boolean Visited = false;

  public boolean GetVisited() {
    return Visited;
  }

  public String getTypeObject() {
    return TypeObject;
  }

  public void setTypeObject(String typeObject) {
    TypeObject = typeObject;
  }

  public void setVisited(boolean visited) {
    Visited = visited;
  }

  @Override
  public String toString() {
    return "Door{"
        + "Open="
        + Open
        + ", Key="
        + Key
        + ", FirstRoom="
        + FirstRoom
        + ", SecondRoom="
        + SecondRoom
        + ", RoomId="
        + RoomId
        + '}';
  }
}
