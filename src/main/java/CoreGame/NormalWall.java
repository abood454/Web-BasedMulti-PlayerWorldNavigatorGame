package CoreGame;

public class NormalWall implements Wall {
    String TypeObject = "wall";
    boolean Visited = false;

    NormalWall(){
        TypeObject = "NormalWall";
    }

    @Override
    public boolean GetVisited() {
        return false;
    }

    @Override
    public String getTypeObject() {
        return TypeObject;
    }

    @Override
    public void setTypeObject(String typeObject) {
     TypeObject = typeObject;
    }

    @Override
    public void setVisited(boolean visited) {
        Visited = visited ;
    }
}
