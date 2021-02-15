package CoreGame;

import java.io.Serializable;

public class Chest implements Wall, Serializable {

  private int Key = -1;
  private Double Gold = 0.0;
  private boolean FlashLight = false;
  private boolean Close = false;
  private int HiddenKey = -1;

  public boolean GetFlashLight() {
    return FlashLight;
  }

  public void setKey(int key) {
    Key = key;
  }

  public void setGold(Double gold) {
    if (gold >= 0) Gold = gold;
  }

  public void setFlashLight(boolean flashLight) {
    FlashLight = flashLight;
  }

  public void setClose(boolean close) {
    Close = close;
  }

  public void setHiddenKey(int hiddenKey) {
    HiddenKey = hiddenKey;
  }

  public int getKey() {
    return Key;
  }

  public double GetGold() {
    return Gold;
  }

  public boolean IsClose() {
    return Close;
  }

  public int GetNeededKey() {
    return HiddenKey;
  }

  public Chest(Builder builder) {
    setTypeObject("Chest");
    Key = builder.Key;
    Gold = builder.Gold;
    FlashLight = builder.FlashLight;
    HiddenKey = builder.HiddenKey;
    Close = builder.Close;
  }

  public static class Builder {

    private int Key;
    private boolean Close;

    private Double Gold = 0.0;
    private boolean FlashLight = false;
    private int HiddenKey = -1;

    public Builder(int Key, boolean Close) {
      this.Key = Key;
      this.Close = Close;
    }

    public Builder AddKey(int val) {
      HiddenKey = val;
      return this;
    }

    public Builder AddGold(Double val) {
      Gold = val;
      return this;
    }

    public Builder AddFlashLight(boolean val) {
      FlashLight = val;
      return this;
    }

    public Chest build() {
      return new Chest(this);
    }
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
    return "Chest{"
        + "Key="
        + Key
        + ", Gold="
        + Gold
        + ", FlashLight="
        + FlashLight
        + ", Close="
        + Close
        + ", HiddenKey="
        + HiddenKey
        + '}';
  }
}
