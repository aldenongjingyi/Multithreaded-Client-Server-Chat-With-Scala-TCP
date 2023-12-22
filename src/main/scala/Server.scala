import java.net.{ServerSocket, Socket}
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Server {

  val priceMap: Map[String, Double] = Map(
    "123456789" -> 50.0,
    "987654321" -> 75.0,
    "111222333" -> 30.0,
    "444555666" -> 60.0,
    "777888999" -> 25.0,
    "112233445" -> 90.0,
    "556677889" -> 40.0,
    "998877665" -> 55.0,
    "223344556" -> 70.0,
    "667788990" -> 80.0
  )

  def main(args: Array[String]): Unit = {
    val serverPort = 4444
    val serverSocket = new ServerSocket(serverPort)
    println("Server is running. Waiting for clients...")

    println("Available ISBNs:")
    priceMap.keys.foreach(isbn => println(s" - $isbn"))

    while (true) {
      val clientSocket = serverSocket.accept()
      Future {
        val clientHandler = new ClientHandler(clientSocket)
        clientHandler.handle()
      }
    }
  }
}

class ClientHandler(clientSocket: Socket) {

  def handle(): Unit = {
    val clientAddress = s"${clientSocket.getInetAddress}:${clientSocket.getPort}"
    println(s"Accepted connection from $clientAddress")

    val reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream))
    val writer = new PrintWriter(clientSocket.getOutputStream, true)

    try {
      while (true) {
        print("Enter ISBN (type 'exit' to close connection): ")
        val isbn = reader.readLine()

        if (isbn == null || isbn.equalsIgnoreCase("exit")) {
          println(s"Closing connection with $clientAddress")
          return
        }

        val price = Server.priceMap.getOrElse(isbn, 0.0)
        writer.println(s"The price for ISBN $isbn is $$ $price")
      }
    } catch {
      case e: Exception =>
        println(s"An error occurred with $clientAddress: ${e.getMessage}")
    } finally {
      reader.close()
      writer.close()
      clientSocket.close()
    }
  }
}
