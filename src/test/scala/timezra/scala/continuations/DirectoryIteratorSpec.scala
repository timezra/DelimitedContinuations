package timezra.scala.continuations

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import java.io.File
import java.util.Locale
import org.scalatest.FlatSpecLike
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class DirectoryIteratorSpec extends FlatSpecLike with Matchers with BeforeAndAfterAll {
  it should "collect the files" in {
    val di = DirectoryIterator()

    val allFiles = di findFiles (new File("src/test/resources/acyclicFolder"), "test")
    
    allFiles.count(_ => true) shouldBe (2)
  }
}