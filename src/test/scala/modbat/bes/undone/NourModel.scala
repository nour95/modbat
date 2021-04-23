package modbat.bes.undone

import modbat.dsl._

class NourModel extends Model
{

  // transitions
  "one" -> "two" := {
    val x = 1;
    if(true)
      throw new NullPointerException()

  } catches( "NullPointerException" -> "four" ) // nextIf , maybe NextIf

  "one" -> "three" := {
    val z =0;
    choose(3, 7)
  }

  "three" -> "four" := {
    val z1 =0;
  }

  "four" -> "five" := {
    val y = 2;
  }




}
