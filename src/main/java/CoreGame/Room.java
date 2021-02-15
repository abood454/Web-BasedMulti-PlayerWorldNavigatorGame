package CoreGame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Room implements Serializable {

  public Wall[] Walls = new Wall[4];
  private int RoomNumber, North = 0, South = 1, West = 2, East = 3;
  private static int CountRoom;
  private static HashMap<Integer, Room> Map = new HashMap<>();
  private boolean Dark = false;
  private List<Integer> ItemsOnFloor = new ArrayList<>();

  public List<Integer> getItemsOnFloor() {
    return ItemsOnFloor;
  }

  public void setItemsOnFloor(List<Integer> itemsOnFloor) {
    ItemsOnFloor = itemsOnFloor;
  }

  public void setWalls(Wall[] walls) {
    Walls = walls;
  }

  public void setRoomNumber(int roomNumber) {
    RoomNumber = roomNumber;
  }

  public void setNorth(int north) {
    North = north;
  }

  public void setSouth(int south) {
    South = south;
  }

  public void setWest(int west) {
    West = west;
  }

  public void setEast(int east) {
    East = east;
  }

  public static void setCountRoom(int countRoom) {
    CountRoom = countRoom;
  }

  public static void setMap(HashMap<Integer, Room> map) {
    Map = map;
  }

  public void setDark(boolean dark) {
    Dark = dark;
  }

  public int getRoomNumber() {
    return RoomNumber;
  }

  public int getNorth() {
    return North;
  }

  public int getSouth() {
    return South;
  }

  public int getWest() {
    return West;
  }

  public int getEast() {
    return East;
  }

  public static int getCountRoom() {
    return CountRoom;
  }

  public static HashMap<Integer, Room> getMap() {
    return Map;
  }

  public boolean isDark() {
    return Dark;
  }

  public Wall[] getWalls() {
    return Walls;
  }

  public Room() {
    for (int i = 0; i < 4; i++) Walls[i] = new NormalWall();
    RoomNumber = ++CountRoom;
  }

  public Wall Action(String s) {
    if (s.equals("North")) return GetTheNorthWall();
    else if (s.equals("East")) return GetTheEastWall();
    else if (s.equals("South")) return GetTheSouthWall();
    else return GetTheWestWall();
  }

  public String WhatIsThis(String s) {
    String WallType = Action(s).getTypeObject();
    if (WallType.equals("Door")) return "Door";
    else if (WallType.equals("Chest")) return "Chest";
    else if (WallType.equals("Painting")) return "Painting";
    else if (WallType.equals("Seller")) return "Seller";
    else if (WallType.equals("Mirror")) return "Mirror";
    else return "Wall";
  }

  public void SetTheNorthWall(Wall a) {
    Walls[North] = a;
  }

  public Wall GetTheSouthWall() {
    return Walls[South];
  }

  public Wall GetTheNorthWall() {
    return Walls[North];
  }

  public Wall GetTheWestWall() {
    return Walls[West];
  }

  public Wall GetTheEastWall() {
    return Walls[East];
  }

  public boolean IsDark() {
    return Dark;
  }

  public void SetDark(boolean t) {
    Dark = t;
  }

  public void SetTheSouthWall(Wall a) {
    Walls[South] = a;
  }

  public void SetTheWestWall(Wall a) {
    Walls[West] = a;
  }

  public void SetTheEastWall(Wall a) {
    Walls[East] = a;
  }

  public int GetRoomNumber() {
    return RoomNumber;
  }

  @Override
  public String toString() {
    return "Room " + RoomNumber;
  }
}
