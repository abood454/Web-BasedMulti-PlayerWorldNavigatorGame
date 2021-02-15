package CoreGame;


public class Mirror implements Wall {

  private boolean Key;
  private int KeyName;
  private double Money;
  private boolean FlashLight;
  private String TypeObject = "wall";
  private boolean Visited = false;


  public Mirror(boolean Key, int KeyName, double money, boolean Flash) {
    setTypeObject("Mirror");
    this.Key = Key;
    this.KeyName = KeyName;
    Money = money;
    FlashLight = Flash;
  }

  public boolean GetFlashLight() {
    return FlashLight;
  }

  public double GetMoney() {
    return Money;
  }

  public int ReturnKeyName() {
    if (IsThereKey()) return KeyName;
    else return 0;
  }

  public boolean IsThereKey() {
    return Key;
  }

  public void setKey(boolean key) {
    Key = key;
  }

  public void setKeyName(int keyName) {
    KeyName = keyName;
  }

  public void setMoney(double money) {
    if (money >= 0) Money = money;
  }

  public void setFlashLight(boolean flashLight) {
    FlashLight = flashLight;
  }

  public boolean isKey() {
    return Key;
  }

  public int getKeyName() {
    return KeyName;
  }

  public double getMoney() {
    return Money;
  }

  public boolean isFlashLight() {
    return FlashLight;
  }



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
}
