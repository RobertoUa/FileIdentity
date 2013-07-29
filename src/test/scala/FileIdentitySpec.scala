import extension.FileReader
import filetype.{FileRemote, FileLocal}
import java.io.{File, ByteArrayInputStream}
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers._
import scala.util.Properties

class FileIdentitySpec extends FunSpec {

  describe("FileIdentity") {
    it("should work for a js file") {
      val file = new FileLocal("files/common.js")
      val remoteFile = new FileRemote("http://st2.vk.me/js/al/common.js")
      file compareTo remoteFile should be(true)
    }

    it("should work for a binary file") {
      val file = new FileLocal("files/miranda-im-v0.10.15-unicode.exe")
      val remoteFile = new FileRemote("http://files.miranda-im.org/stable/0.10.15.0/miranda-im-v0.10.15-unicode.exe")
      file compareTo remoteFile should be(true)
    }

    it("should fail for different files") {
      val file = new FileLocal("files/miranda-im-v0.10.15-unicode.exe")
      val remoteFile = new FileRemote("http://st2.vk.me/js/al/common.js")
      file compareTo remoteFile should not be (true)
    }
  }

  describe("md5 sum") {
    it("should compute correct md5sum") {
      val sum = "568c9870ae8fe5931a1f52720921d9c9"
      val fileIdentity = new FileLocal("files/sound.mp3")
      val hash = fileIdentity.hash
      sum should equal(hash)

    }
    it("shouldn't compute correct md5sum for different file") {
      val sum = "568c9870ae8fe5931a1f52720921d9c9"
      val fileIdentity = new FileLocal("files/ou2.ogv")
      val hash = fileIdentity.hash
      sum should not equal(hash)

    }
  }

  describe("files extensions") {
    it("should convert inputStream to byte array") {
      val string = "Some string"
      val reader = new FileReader {
        def getInputStream = new ByteArrayInputStream(string.getBytes("UTF-8"))
      }
      reader.byteArray should equal(string.map(_.toByte).toArray)
    }
  }

  describe("actor system") {
    it("should work in multithread") {
      val files = Map("files/common.js" -> "http://st2.vk.me/js/al/common.js",
        "files/miranda-im-v0.10.15-unicode.exe" -> "http://files.miranda-im.org/stable/0.10.15.0/miranda-im-v0.10.15-unicode.exe")

      val result = FileIdentity.compareAndResult(files)

      files.keySet.subsetOf(result.keySet) should be(true)
      result.forall(_._2) should be(true)
    }

    it("should be asynchronous") {
      val folder = new File(Properties.userHome + "/Downloads")
      val files = folder.listFiles().filter(_.isFile)
        .flatMap(x => Map(x.getPath -> ("file://" + x.getPath))).toMap.take(25)

      val result = FileIdentity.compareAndResult(files)

      files.keySet.subsetOf(result.keySet) should be(true)
      result.forall(_._2) should be(true)
    }
  }
}
