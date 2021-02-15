package CoreGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapGenerator {

    public void Generate(MapGame mapGame){

        Room [] rooms = new Room[55];
        for(int i = 0; i <55; i++){
            rooms[i]= new Room();
            mapGame.AddRoom(rooms[i]);
        }



        Random rand = new Random();

        for(int i = 0; i < 55; i++){
            for(int j = 1 ; j < 3; j++){
                int int_random = rand.nextInt( 5);

              if(int_random == 0)
                  rooms[i].Walls[j]  = new Seller();
                else if(int_random == 1)
                  rooms[i].Walls[j] = new Chest.Builder(-1, false).build();
                else if(int_random == 2)
                  rooms[i].Walls[j] = new Mirror(false , -1,0.0,false);
                else if(int_random == 3)
                  rooms[i].Walls[j] = new Painting(false , -1 ,0.0);
                else
                  rooms[i].Walls[j]  = new NormalWall();
            }
            rooms[i].Walls[0] = new Door(true,-1);
            rooms[i].Walls[3]  = new Door(true,-1);

        }

        List<Integer> arr = new ArrayList<>();
        for(int j = 0; j < 44; j++){
            arr.add(j);
        }
        Collections.shuffle(arr);
        for(int i = 0; i < 43; i++){
            Door door = (Door)rooms[i].GetTheEastWall();
            int int_random = rand.nextInt( arr.size() - 1);
            mapGame.MakePairRoom(door , rooms[i].GetRoomNumber() ,rooms[int_random].GetRoomNumber() );
            arr.remove(int_random);
        }

        for(int j = 0; j < 44; j++){
            arr.add(j);
        }

        Collections.shuffle(arr);

        for(int i = 0; i < 43; i++){
            Door door = (Door)rooms[i].GetTheNorthWall();
            int int_random = rand.nextInt( arr.size() - 1);
            mapGame.MakePairRoom(door , rooms[i].GetRoomNumber() ,rooms[int_random].GetRoomNumber() );
            arr.remove(int_random);
        }

        for(int j = 0; j < 44; j++){
            arr.add(j);
        }

        Collections.shuffle(arr);
        int key = 1;
        for(int i = 44; i < 55-1; i+=2){

            Door door = (Door)rooms[i].GetTheNorthWall();
            door.setOpen(false);
            door.setKey(key++);
            Door door1 = (Door)rooms[i].GetTheEastWall();
            door1.setOpen(false);
            door1.setKey(key);

            int int_random = rand.nextInt( arr.size() - 1);
            mapGame.MakePairRoom(door , rooms[i].GetRoomNumber() ,rooms[int_random].GetRoomNumber() );
            mapGame.MakePairRoom(door1 , rooms[i].GetRoomNumber() ,rooms[i+1].GetRoomNumber() );

            Door door2 = (Door)rooms[i].GetTheEastWall();

            for(int k = 0 ; k < 2 ; k++){
            for(int j = 0; j < 3; j++){
                Wall wall; Seller S; Chest C; Painting P; Mirror M;
                do{
                 int_random = rand.nextInt( arr.size() - 1);
                  wall = rooms[int_random].GetTheWestWall();
                  if(wall.getTypeObject().equals("Seller")){
                       S = (Seller)wall;
                       S.AddKey(key - 1, 20.0);}
                  else if(wall.getTypeObject().equals("Chest")){
                      C = (Chest)wall;
                      C.setKey(key - 1);
                  }
                  else if(wall.getTypeObject().equals("Mirror")){
                      M = (Mirror)wall;
                      M.setKey(true);
                      M.setKeyName(key - 1);
                  }
                  else if(wall.getTypeObject().equals("Painting")){
                      P = (Painting)wall;
                      P.setKeyName(key - 1);
                  }


                }while (wall.getTypeObject().equals("NormalWall"));
                arr.remove(int_random);
            }
            key++;
        }
        }
        arr.clear();
        for(int j = 0; j < 44; j++){
            arr.add(j);
        }

        Collections.shuffle(arr);

        for(int i = 0; i<20; i++){
            Wall wall; Seller S; Chest C; Painting P; Mirror M;
            int int_random;
            int Money = rand.nextInt(20);
            do{
                 int_random = rand.nextInt( arr.size()-1);
                wall = rooms[int_random].GetTheWestWall();
                if(wall.getTypeObject().equals("Chest")){
                    C = (Chest)wall;
                    C.setKey(key);
                }
                else if(wall.getTypeObject().equals("Mirror")){
                    M = (Mirror)wall;
                    M.setKey(true);
                    M.setKeyName(key);
                }
                else if(wall.getTypeObject().equals("Painting")){
                    P = (Painting)wall;
                    P.setKeyName(key);
                }


            }while (wall.getTypeObject().equals("NormalWall"));
            arr.remove(int_random);

        }




    }
    public void AddPlayer(Player player, MapGame map){
        map.AddPlayer(player);
        Random rand = new Random();
        int int_random = rand.nextInt(43);
        player.ChangeCurrentRoom(map.getRooms().get(int_random));
        map.getPeople().put(map.getRooms().get(int_random), player);
        map.getHere().put(map.getRooms().get(int_random),1);

    }

}
