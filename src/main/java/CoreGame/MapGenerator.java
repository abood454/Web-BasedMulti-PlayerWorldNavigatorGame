package CoreGame;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapGenerator {

    Random rand = new Random();


    public void Generate(MapGame mapGame){

        Room [] rooms = new Room[55];
        for(int i = 0; i <55; i++){
            rooms[i]= new Room();
            mapGame.AddRoom(rooms[i]);
        }

        FillRooms(rooms);

        List<Integer> arr = new ArrayList<>();

        for(int j = 0; j < 44; j++){
            arr.add(j);
        }

        FillDoors(mapGame ,arr,rooms);


        for(int j = 0; j < 44; j++){
            arr.add(j);
        }

        Collections.shuffle(arr);



        Collections.shuffle(arr);

        FillKeys(mapGame ,arr,rooms);






    }

    public void AddPlayer(Player player, MapGame map){
        map.AddPlayer(player);
        Random rand = new Random();
        int int_random = rand.nextInt(43);
        player.ChangeCurrentRoom(map.getRooms().get(int_random));
        map.getPeople().put(map.getRooms().get(int_random), player);
        map.getHere().put(map.getRooms().get(int_random),1);

    }

    public void FillRooms(Room [] rooms){

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
    }

    public void FillDoors(MapGame mapGame , List<Integer> arr , Room [] rooms){
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

    }
    public void FillKeys(MapGame mapGame , List<Integer> arr , Room [] rooms){
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
        FillKeysPlaces(mapGame , arr, rooms , key);
    }

    void FillKeysPlaces(MapGame mapGame , List<Integer> arr , Room [] rooms , int key){
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
                key--;
            }while (wall.getTypeObject().equals("NormalWall"));
            arr.remove(int_random);

        }
    }



    public void Test(MapGame M) {
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

    public void AddPlayerTest(Player player, MapGame map) {
        System.out.println(map);
        map.AddPlayer(player);

        if (map.getPlayers().size() == 1) {
            player.ChangeCurrentRoom(map.getRooms().get(0));
            map.getPeople().put(map.getRooms().get(0), player);
            map.getHere().put(map.getRooms().get(0),map.getHere().get(map.getRooms().get(1)) + 1);
        } else {
            player.ChangeCurrentRoom(map.getRooms().get(1));
            map.getPeople().put(map.getRooms().get(1), player);
            map.getHere().put(map.getRooms().get(1), map.getHere().get(map.getRooms().get(1)) + 1);
        }

    }

}
