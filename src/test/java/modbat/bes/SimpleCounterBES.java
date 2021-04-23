package modbat.bes;

public class SimpleCounterBES {
  int count = 0;
  boolean flag = true;
  int incrementBy = 2;


  public void toggleSwitch() {
    flag = !flag;
  }

  public void inc() {
    if (flag && incrementBy == 1) {
      count += 1;
    }
  }

  public void inc2() {
    count += incrementBy;
  }

  public int getIncrementBy() {
    return incrementBy;
  }

  public void setIncrementBy(int incrementBy) {
    this.incrementBy = incrementBy;
  }

  public int value() {
    return count;
  }

  public void setValue ( int value ) {
     count = value ;
  }

  public static void main(String[] args) {

  }


}
