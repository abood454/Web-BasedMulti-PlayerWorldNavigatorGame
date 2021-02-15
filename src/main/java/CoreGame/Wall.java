package CoreGame;

public interface Wall {
     String TypeObject = "wall";
     boolean Visited = false;

     boolean GetVisited();

     String getTypeObject() ;

     void setTypeObject(String typeObject);

     void setVisited(boolean visited) ;
}
