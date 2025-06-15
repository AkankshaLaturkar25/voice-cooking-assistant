import java.io.*;
import java.net.*;

public class RecipeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Voice Recipe Server running at http://localhost:" + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream()
        ) {
            // Read request line
            String line = in.readLine();
            if (line == null || !line.startsWith("POST")) {
                socket.close();
                return;
            }

            // Read headers
            while (!(line = in.readLine()).isEmpty()) {
                // Just read and ignore headers here
            }

            // Read body - assuming small content
            StringBuilder body = new StringBuilder();
            while (in.ready()) {
                body.append((char) in.read());
            }

            String query = body.toString().toLowerCase();
            String recipe = getRecipeResponse(query);

            // Send HTTP response with CORS header
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                  "Content-Type: text/plain\r\n" +
                                  "Access-Control-Allow-Origin: *\r\n" +   // CORS enabled here
                                  "\r\n" +
                                  recipe;
            out.write(httpResponse.getBytes());
            out.flush();

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getRecipeResponse(String query) {
        if (query.contains("pizza")) {
            return "To make pizza: Mix dough, add tomato sauce, mozzarella, and toppings. Bake at 220°C for 15 minutes.";
        } else if (query.contains("pasta")) {
            return "To make pasta: Boil pasta, sauté garlic, add tomato sauce and herbs. Mix and serve hot.";
        } else if (query.contains("omelette")) {
            return "To make omelette: Beat eggs with salt and pepper, pour into pan, add vegetables or cheese. Cook until firm.";
        } else if (query.contains("sandwich")) {
            return "To make a sandwich: Take bread slices, add cheese, tomato, cucumber, lettuce, and sauce. Toast if desired.";
        } else if (query.contains("salad")) {
            return "To make salad: Chop lettuce, cucumber, tomatoes, and onions. Toss with olive oil, lemon juice, and salt.";
        } else if (query.contains("noodles")) {
            return "To make noodles: Boil noodles, stir-fry with vegetables and soy sauce. Add chili sauce if spicy is preferred.";
        } else if (query.contains("fried rice")) {
            return "To make fried rice: Cook rice, stir-fry with eggs, vegetables, and soy sauce. Mix well and serve.";
        } else if (query.contains("pancake")) {
            return "To make pancakes: Mix flour, egg, milk, and sugar. Pour batter into pan and cook on both sides.";
        } else if (query.contains("burger")) {
            return "To make burger: Grill patty, place on bun with lettuce, tomato, cheese, and sauce. Serve with fries.";
        } else if (query.contains("biryani")) {
            return "To make biryani: Cook basmati rice, layer with spiced chicken or vegetables, and steam with herbs and saffron.";
        } else {
            return "Sorry, I don't know that recipe yet. Try asking for pizza, pasta, omelette, or more.";
        }
    }
}
