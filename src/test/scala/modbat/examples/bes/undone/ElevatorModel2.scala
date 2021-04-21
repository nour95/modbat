package modbat.examples.bes.undone

import modbat.dsl._
import modbat.examples.bes.ElevatorSystem2

class ElevatorModel2 extends Model {
  var system: ElevatorSystem2 = new ElevatorSystem2();

  // transitions
  "DoorClosed" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    require(system.getCurrentSpeed == 0.0)  //todo no need for speed
    system.openDoor();
    assert(system.isDoorOpened)
  }
  "DoorOpened" -> "DoorClosed" := {
    require(system.isDoorOpened)
    require(system.getCurrentSpeed == 0.0)
    system.closeDoor();
    assert(!system.isDoorOpened) //todo??
  }
  "DoorClosed" -> "MovingElevator" := {
    require(!system.isDoorOpened)
    require(system.getCurrentSpeed == 0.0)


    system.closeDoor();
    assert(!system.isDoorOpened) //todo??
  }

  "DoorClosed" -> "End" := skip

}
