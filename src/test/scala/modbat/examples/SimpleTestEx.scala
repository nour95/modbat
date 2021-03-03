package modbat.examples

import org.scalatest._
import modbat.mbt.ModbatTestHarness

//todo if I need to have a package then I need to add it to gradle (gradle test failed bec. log folder can't be found)
class SimpleTestEx  extends fixture.FlatSpec with fixture.TestDataFixture with Matchers
{
  "SimpleModelTest5" should "pass" in {
    td =>
      ModbatTestHarness.test(Array("-s=1", "-n=30", "--no-redirect-out","--search=exhaustive", "--log-level=fine", "modbat.examples.SimpleModel"), (() => ModbatTestHarness.setExamplesJar()), td)
  }

  "SimpleModelTest6" should "pass" in {
    td =>
      ModbatTestHarness.test(Array("-s=49a846e52b813972", "-n=1", "--no-redirect-out", "modbat.examples.SimpleModel"), (() => ModbatTestHarness.setExamplesJar()), td)
  }

  "SimpleModelTest7" should "pass" in { td =>
    ModbatTestHarness.test(Array("-s=88af43883571af0c", "-n=1", "--no-redirect-out", "modbat.examples.SimpleModel"), (() => ModbatTestHarness.setExamplesJar()), td)
  }

  "SimpleModelTest8" should "pass" in { td =>
    ModbatTestHarness.test(Array("-s=88af43883571af0c", "-n=1", "--no-redirect-out", "modbat.examples.SimpleModel", "--print-stack-trace"), (() => ModbatTestHarness.setExamplesJar()), td)
  }
}
