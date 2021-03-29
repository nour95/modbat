package modbat.examples

import modbat.dsl._

class ElevatorModel extends Model {
  var system: ElevatorSystem = new ElevatorSystem();

  // transitions
  "DoorClosed" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    require(system.getCurrentSpeed == 0.0)
    system.openDoor();
    assert(system.isDoorOpened) //todo??
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
