package CoreGame;

import java.util.ArrayList;
import java.util.List;

public class PlayerAction {

  public String IsDoor(MapGame map, Player ActivePlayer) {

    Wall Temp = ActivePlayer.GetCurrentRoom().Action(ActivePlayer.PlayerFacing());

    Room GoRoom = ActivePlayer.GetCurrentRoom();
    Door GoDoor = (Door) Temp;

    if (GoDoor.isOpen() || (ActivePlayer.IsThereKey(GoDoor.GetKey()))) {
      if (map.getWinningDoors().get(GoDoor) != null) return "WIN";

      int NumberOfNextRoom = GoDoor.GetNextRoom(GoRoom.GetRoomNumber());

      if(map.getHere().get(map.GetNextRoom(NumberOfNextRoom)) == 2){
        return "ThereIsFight";
      }

        map.getPeople().put(ActivePlayer.GetCurrentRoom(), null);
      map.getHere()
          .put(ActivePlayer.getCurrentRoom(), map.getHere().get(ActivePlayer.getCurrentRoom()) - 1);
      ActivePlayer.ChangeCurrentRoom(map.GetNextRoom(NumberOfNextRoom));


      map.getHere()
              .put(ActivePlayer.getCurrentRoom(), map.getHere().get(ActivePlayer.getCurrentRoom()) + 1);


      if (map.getPeople().get(ActivePlayer.GetCurrentRoom()) != null
          && map.getHere().get(ActivePlayer.getCurrentRoom())%2 == 0) return "FIGHT";

      map.getPeople().put(ActivePlayer.GetCurrentRoom(), ActivePlayer);
      map.getHere()
          .put(ActivePlayer.getCurrentRoom(), map.getHere().get(ActivePlayer.getCurrentRoom()) + 1);
      List<Integer> items = ActivePlayer.GetCurrentRoom().getItemsOnFloor();

      if (items.size() > 0) {
        for (Integer item : items) {
          ActivePlayer.AddItem(item);
        }
        items.clear();
        return "OpenWithItems";
      }

      System.out.println("You moved to the next room");
      return "OPEN";
    } else {
      System.out.println("Door is locked, the key " + GoDoor.GetKey() + " is needed" + "to unlock");
      return "CLOSE";
    }
  }

  public boolean IsChest(Wall wall, Player ActivePlayer) {
    Chest chest = (Chest) wall;
    if (!chest.IsClose() || (chest.IsClose() && ActivePlayer.IsThereKey(chest.GetNeededKey()))) {
      ActivePlayer.AddMoney(chest.GetGold());
      if (chest.getKey() != 0) ActivePlayer.AddItem(chest.getKey());
      chest.setGold(0.0);
      chest.setKey(0);
      return true;
    }
    return false;
  }

  public void IsMirror(Wall wall, Player ActivePlayer) {
    Mirror mirror = (Mirror) wall;
    ActivePlayer.AddMoney(mirror.GetMoney());
    if (mirror.getKeyName() != 0) ActivePlayer.AddItem(mirror.getKeyName());
    mirror.setMoney(0.0);
    mirror.setKeyName(0);
  }

  public void IsPainting(Wall wall, Player ActivePlayer) {
    Painting painting = (Painting) wall;
    ActivePlayer.AddMoney(painting.GetMoney());
    if (painting.ReturnKeyName() != 0) ActivePlayer.AddItem(painting.ReturnKeyName());
    painting.setMoney(0.0);
    painting.setKeyName(0);
  }

  public ArrayList<Integer> Seller(Player ActivePlayer, MapGame map) {
    Wall Temp = ActivePlayer.GetCurrentRoom().Action(ActivePlayer.PlayerFacing());

    Seller seller = (Seller) Temp;

    return seller.getKeys();
  }



}
