package modbat.bes

import modbat.dsl._

class SimpleModel extends Model {
  var counter: SimpleCounterBES = _

  // transitions
  "reset" -> "zero" := {
    counter = new SimpleCounterBES()
  }

  "reset" -> "fourOrFive" := {
    counter = new SimpleCounterBES()
    counter.setValue(choose(4, 5))
    counter.setIncrementBy(choose(1, 2))
  }

  "fourOrFive" -> "fourInc" := {
    require(counter.value == 4)
    require(counter.getIncrementBy == 2)
    counter.inc2()
  }

  "fourOrFive" -> "fiveInc" := {
    require(counter.value == 5)
    require(counter.getIncrementBy == 1)

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
    counter.setIncrementBy(choose(1, 3))
  }

  "zero" -> "one" := {
    require(counter.getIncrementBy == 1)  //todo require flag = true??
    counter.inc()
  }

  "one" -> "two" := {
    require(counter.flag)
    require(counter.getIncrementBy == 1)
    counter.inc()
  }

  "zero" -> "two" := {
    require(counter.getIncrementBy == 2)
    counter.inc2()
  }

  "two" -> "end" := {
    assert (counter.value == 2)
  }
}
