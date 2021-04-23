package modbat.examples

import modbat.dsl._

class SimpleModelE extends Model {
  var counter: SimpleCounter = _

  // transitions
  "reset" -> "reset" := {
    counter = new SimpleCounter()
  }

  "reset" -> "zero" := {
    counter = new SimpleCounter()
  }

  "reset" -> "fourOrFive" := {
    counter = new SimpleCounter()

  }

  "fourOrFive" -> "fourInc" := {
    counter.inc2()
  }

  "fourOrFive" -> "fiveInc" := {
    counter.inc()
  }

  "fourInc" -> "end" := {
    assert (counter.value == 6)
  }

  "fiveInc" -> "end" := {
    assert (counter.value == 6)
  }


  "zero" -> "zero" := {
    counter.toggleSwitch()
  }

  "zero" -> "one" := {
    counter.inc()
  }

  "one" -> "two" := {
    counter.inc()
  }

  "zero" -> "two" := {
    counter.inc2()
  }

  "two" -> "end" := {
    assert (counter.value == 2)
  }
}
