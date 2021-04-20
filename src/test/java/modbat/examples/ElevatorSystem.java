package modbat.examples;

public class ElevatorSystem
{

    private boolean doorOpened = false;
    private int currentFloor = 0;

    public ElevatorSystem() { }

    public boolean moveElevator(int requestingFloor)       //todo have a similar method that call this method and that have a while(doorOpened){wait}??
    {
        if (doorOpened == false) {             //todo this is always false
            currentFloor = requestingFloor;
            return true;
        }
        else
            return false;
    }

    public void openDoor()
    {
        doorOpened = true;
    }

    public void closeDoor()
    {
        doorOpened = false;
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

    public void setDoorOpened(boolean doorOpened) {
        this.doorOpened = doorOpened;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void requestElevator(int requestingFloor)
    {
        if (requestingFloor != currentFloor)
            moveElevator(requestingFloor);

        //openDoor();
    }


    private void waitUntilReachingDistance()
    {
        for (int i = 0; i < 1000000; i++){}
    }





}
