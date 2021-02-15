package CoreGame;

import java.io.Serializable;

public class Painting implements Wall, Serializable {

  private boolean Key;
  private int KeyName;
  private double Money;

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

  public Painting(boolean Key, int KeyName, double money) {
    setTypeObject("Painting");
    this.Key = Key;
    this.KeyName = KeyName;
    if (money >= 0) Money = money;
    else Money = 0;
  }

  public double GetMoney() {
    return Money;
  }

  public int ReturnKeyName() {
    if (IsThereKey()) return KeyName;
    else return 0;
  }

  public void setKeyName(int keyName) {
    KeyName = keyName;
  }

  public void setMoney(double money) {
    Money = money;
  }

  public boolean IsThereKey() {
    return Key;
  }

  @Override
  public String toString() {
    return "Painting{" +
            "Key=" + Key +
            ", KeyName=" + KeyName +
            ", Money=" + Money +
            ", TypeObject='" + TypeObject + '\'' +
            ", Visited=" + Visited +
            '}';
  }
}
