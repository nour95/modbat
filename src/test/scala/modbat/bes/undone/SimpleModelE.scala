package modbat.bes.undone

import modbat.dsl._
import modbat.examples.SimpleCounter

class SimpleModelE extends Model {
  var counter: SimpleCounter = _

  // transitions
  "reset" -> "zero" := {
    counter = new SimpleCounter()
  }
  "zero" -> "zero" := {
    counter.toggleSwitch
  }
  "zero" -> "one" := {
    counter.inc
  }
  "one" -> "two" := {
    counter.inc
  }
  "zero" -> "two" := {
    counter.inc2
  }
  "two" -> "end" := {
    assert (counter.value == 2)
  }
}
