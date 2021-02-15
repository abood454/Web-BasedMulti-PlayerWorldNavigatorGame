package CoreGame;

import java.io.Serializable;
import java.util.ArrayList;

public class Seller implements Wall  {

    private ArrayList<Integer> Keys = new ArrayList<>();
    private ArrayList<Double> Prices = new ArrayList<>();
    private boolean FlashLight = false;
    private double FlashLightPrice = 0;
    private String TypeObject = "wall";
    private boolean Visited = false;




    public void List(){
        int i;
        for( i= 0; i < Keys.size(); i++){
      System.out.println(i + ": the Key" + Keys.get(i) + " Price is " +Prices.get(i));
        }
    if (FlashLight) System.out.println(i + ": the FlashLight" + Keys.get(i) + " Price is " +FlashLightPrice);
    }

    public int GetKey(int i){
        if(i < Keys.size())
        return Keys.get(i);

        return -1;
    }

    public void RemoveItem(int i){
        Keys.remove(i);
        Prices.remove(i);
    }


    public double GetPrice(int i){
        if(i < Prices.size())
            return Prices.get(i);

        return -1;
    }






   public Seller() {
        setTypeObject("Seller");
    }

    public void AddKey(int key, double price) {
        Keys.add(key);
        Prices.add(price);
    }

    public void AddFlashLight(double price) {
        FlashLight = true;
        FlashLightPrice = price;
    }

    public ArrayList<Integer> getKeys() {
        return Keys;
    }

    public ArrayList<Double> getPrices() {
        return Prices;
    }

    public boolean isFlashLight() {
        return FlashLight;
    }

    public double getFlashLightPrice() {
        return FlashLightPrice;
    }

    public void setKeys(ArrayList<Integer> keys) {
        Keys = keys;
    }

    public void setPrices(ArrayList<Double> prices) {
        Prices = prices;
    }

    public void setFlashLight(boolean flashLight) {
        FlashLight = flashLight;
    }

    public void setFlashLightPrice(double flashLightPrice) {
        FlashLightPrice = flashLightPrice;
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
