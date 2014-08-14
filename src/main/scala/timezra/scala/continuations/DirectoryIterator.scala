package timezra.scala.continuations

import java.io.File

class DirectoryIterator {
  def findFiles(root: File, startsWith: String): Iterable[File] = {
    def processDirectory(dir: File): Iterable[File] = {
      val files = dir.listFiles.map(f ⇒ f.getCanonicalFile())
      files.foldLeft(List[File]())((agg, f) =>
        if (f.isDirectory) {
          agg ++ processDirectory(f)
        } else if (f.getName().startsWith(startsWith)) {
          agg :+ f
        } else {
          agg
        })
    }
    processDirectory(root)
  }

  def findFiles2(root: File, startsWith: String): Iterable[File] = {
    new Files(root, startsWith)
  }

  class Files(root: File, startsWith: String) extends Iterable[File] {

    override def iterator(): Iterator[File] = {
      return new Iterator[File]() {

        var cont: (Unit ⇒ Unit) = null
        var nextFile: File = null

        import scala.util.continuations._
        def processDirectory(dir: File): Unit @cpsParam[Unit, Unit] = {
          val files = dir.listFiles.map(f ⇒ f.getCanonicalFile())
          var i = 0
          while (i < files.length) {
            val f = files(i)
            i += 1
            if (f.isDirectory) {
              processDirectory(f)
            } else if (f.getName().startsWith(startsWith)) {
              shift {
                k: (Unit ⇒ Unit) ⇒
                  {
                    cont = k
                  }
              }
              nextFile = f
            }
          }
          cont = null
        }

        reset {
          processDirectory(root)
        }

        def hasNext: Boolean = cont != null
        def next(): File = { cont(); return nextFile; }
      }
    }
  }
}

object DirectoryIterator {
  def apply(): DirectoryIterator = new DirectoryIterator()
}