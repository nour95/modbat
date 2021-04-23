package modbat.bes

import modbat.dsl._

class ElevatorModel extends Model {
  var system: ElevatorSystem = _;

  // transitions
  "InitializeSystem" -> "DoorClosed" := {
    system = new ElevatorSystem()
  }

  "DoorClosed" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    system.openDoor();
    assert(system.isDoorOpened)
  } label "open"

  "DoorClosed" -> "DoorClosed" := {
    require(!system.isDoorOpened)
    require(system.getCloseWarningCounter < 4)  //todo maybe not , it should be ok to make a warning??
    system.closeDoor();
    assert(!system.isDoorOpened)
  } label "close"

  "DoorOpened" -> "DoorOpened" := {
    require(system.isDoorOpened)
    require(system.getOpenWarningCounter < 4)  //todo maybe not , it should be ok to make a warning??
    system.openDoor();
    assert(system.isDoorOpened)
  } label "open"

  "DoorOpened" -> "DoorClosed" := {
    require(system.isDoorOpened)
    system.closeDoor();
    assert(!system.isDoorOpened)
  } label "close"

  "DoorClosed" -> "MoveToFloorZero" := {
    require(system.getCurrentFloor == 1)
    val success: Boolean = system.moveElevator(0)
    assert(success)
    assert(system.getCurrentFloor == 0)
    assert(!system.isDoorOpened) //
  } label "moveToZero"

  "DoorClosed" -> "MoveToFloorOne" := {
    require(system.getCurrentFloor == 0)
    val success: Boolean = system.moveElevator(1)
    assert(success)
    assert(system.getCurrentFloor == 1)
    assert(!system.isDoorOpened) //
  } label "moveToOne"

  "MoveToFloorZero" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    require(system.getCurrentFloor == 0)
    system.openDoor();
    assert(system.isDoorOpened)
  } label "open"

  "MoveToFloorOne" -> "DoorOpened" := {
    require(!system.isDoorOpened)
    require(system.getCurrentFloor == 1)
    system.openDoor();
    assert(system.isDoorOpened)
  } label "open"

  "DoorClosed" -> "WarningCheck" := {
    assert(! system.isWarningHappen)
  } label "checkWarning"

  "DoorOpened" -> "WarningCheck" := {
    assert(! system.isWarningHappen)
  } label "checkWarning"

 // "DoorClosed" -> "End" := skip   //todo add warning check here or

  "WarningCheck" -> "End" := skip


}
