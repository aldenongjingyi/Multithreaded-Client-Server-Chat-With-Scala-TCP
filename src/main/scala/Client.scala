import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.io.StdIn

object Client {

  def main(args: Array[String]): Unit = {
    val serverHost = "localhost"
    val serverPort = 4444

    val clientSocket = new Socket(serverHost, serverPort)
    val inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))
    val outputWriter = new PrintWriter(clientSocket.getOutputStream, true)

    try {
      while (true) {
        print("Enter ISBN (type 'exit' to quit): ")
        val userInput = StdIn.readLine()

        if (userInput == null || userInput.equalsIgnoreCase("exit")) {
          return
        }

        outputWriter.println(userInput)
        val serverResponse = inputReader.readLine()
        println(s"Server response: $serverResponse")
      }
    } catch {
      case e: Exception =>
        println("An error occurred: " + e.getMessage)
    } finally {
      inputReader.close()
      outputWriter.close()
      clientSocket.close()
    }
  }
}
