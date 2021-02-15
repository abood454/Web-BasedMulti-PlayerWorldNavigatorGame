package CoreGame;

import Operation.Moves;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Reminder implements Serializable {
  Timer timer;
  MapGame Map;
  Moves Move = new Moves();

  public Reminder() {}

  public Reminder(int seconds , MapGame mapGame) throws IOException, InterruptedException {
    timer = new Timer();
    timer.schedule(new RemindTask(), seconds * 1000);
    Map = mapGame;
  }

  class RemindTask extends TimerTask {
    public void run() {
      System.out.println("Time's up!");
      Map = Map;
      try {
        Move.TimeOver(Map);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      timer.cancel(); // Terminate the timer thread

    }
  }

  public Timer getTimer() {
    return timer;
  }

  public MapGame getMap() {
    return Map;
  }

  public void setTimer(Timer timer) {
    this.timer = timer;
  }

  public void setMap(MapGame map) {
    Map = map;
  }

  @Override
  public String toString() {
    return "Reminder{" +
            "timer=" + timer +
            ", Map=" + Map +
            '}';
  }
}
