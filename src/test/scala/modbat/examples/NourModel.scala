package modbat.examples

import modbat.dsl._

class NourModel extends Model
{

  // transitions
  "zero" -> "one" := {
    val m = 5;
  }

  "one" -> "two" := {
    val x = 1;
    //if(true)
    //  throw new NullPointerException()

  } nextIf  (() => true) -> "synthetic"
  //catches( "NullPointerException" -> "four" ) // nextIf , maybe NextIf

  "two" -> "five" := {
    val z =0;
  }

  "two" -> "six" := {
    val z1 =0;
  }

  "synthetic" -> "three" := {
    val y = 2;
  } //nextIf(() => true) -> "five"

  "synthetic" -> "four" := {
    val y = 2;
  }




}
