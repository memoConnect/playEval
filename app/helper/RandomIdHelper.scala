package helper

/**
 * Created by Weily on 08.01.14.
 */
object RandomIdHelper {
  // Random generator
  val random = new scala.util.Random
  def randomString(n: Int): String = {
    def alphabet: String = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    //TODO check whether random.setSeed is needed
    Stream.continually(random.nextInt(alphabet.size)).map(alphabet).take(n).mkString
  }
}
