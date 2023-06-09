package dk.dtu.compute.se.pisd.roborally.webApplication;

import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.AllPlayersTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.webApplication.api.model.testUser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Client {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String getUsers() throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/user"))
                .setHeader("User-Agent", "Product Client")
                .header("Content-Type", "application/json")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
        return result;
    }

    public static String getUserById(int id) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/user?id=" + id))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            Gson gson = new Gson();
            testUser p = gson.fromJson(result, testUser.class);
            return p.getName();
        } catch (Exception e) {
            return null;
        }
    }


    public static String getMapName(){
        System.out.println("testing1");
        try{
            System.out.println("testing2");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/map"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            System.out.println("testing3");
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("testing4");
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            System.out.println("testing5: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("fail1");
            return null;
        }
    }

    public static boolean setMapName(String name) {
        try{
            String productJSON = new Gson().toJson(name);
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(productJSON))
                    .uri(URI.create("http://localhost:8080/map"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("added")? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean addPlayer(Player p) {



        try{
//            String productJSON = new Gson().toJson(p);
//            System.out.println(productJSON);

            PlayerTemplate playerTemplate = new PlayerTemplate();
            Space space = p.getSpace();
            playerTemplate.x = space.x;
            playerTemplate.y = space.y;
            playerTemplate.playerName = space.getPlayer().getName();
            playerTemplate.color = space.getPlayer().getColor();
            playerTemplate.playerHeading = space.getPlayer().getHeading();
            playerTemplate.checkpoints = space.getPlayer().getCheckpoints();

            for (CommandCardField card: space.getPlayer().getCardsList()) {
                playerTemplate.cards.add(card.getCard());
            }
            for (CommandCardField card: space.getPlayer().getProgramList()) {
                playerTemplate.program.add(card.getCard());
            }
            String productJSON = new Gson().toJson(playerTemplate);
            System.out.println("temp: " + productJSON);

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(productJSON))
                    .uri(URI.create("http://localhost:8080/addPlayer"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("added")? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    public static int permToJoin() {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/perm"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return Integer.parseInt(result);
        } catch (Exception e) {
            return -1;
        }
    }

    public static void getAllPlayers(Board board){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/getAllPlayer"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);

            System.out.println("result getAllPlayers: " + result);
            Gson gson = new Gson();

            AllPlayersTemplate template = gson.fromJson(result, AllPlayersTemplate.class);

            for (PlayerTemplate playerTemplate: template.players) {
                Space space = board.getSpace(playerTemplate.x, playerTemplate.y);
                if(space != null){
                    Player player = new Player(board, playerTemplate.color, playerTemplate.playerName, playerTemplate.program, playerTemplate.cards);
                    player.setHeading(playerTemplate.playerHeading);
                    player.setCheckpoints(playerTemplate.checkpoints);
                    board.addPlayer(player);
                    space.setPlayer(player);
                }
            }
//            List<Player> p = gson.fromJson(result, ArrayList.class);
//            return p;
        } catch (Exception e) {
//            return null;
        }
    }

    public static Player getCurrentPlayer(){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/currPlayer"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            System.out.println("result: " + result);
            Gson gson = new Gson();
            Player p = gson.fromJson(result, Player.class);
            System.out.println("getname: " + p.getName());


//            for (PlayerTemplate playerTemplate: template.players) {
//                Space space = result.getSpace(playerTemplate.x, playerTemplate.y);
//                if(space != null){
//                    Player player = new Player(board, playerTemplate.color, playerTemplate.playerName, playerTemplate.program, playerTemplate.cards);
//                    player.setHeading(playerTemplate.playerHeading);
//                    player.setCheckpoints(playerTemplate.checkpoints);
//                    board.addPlayer(player);
//                    space.setPlayer(player);
//                }
//            }

            return p;
        } catch (Exception e) {
            return null;
        }
    }

    public static Phase getPhase(){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/phase"))
                    .setHeader("User-Agent", "Product Client")
                    .header("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            Gson gson = new Gson();
            Phase p = gson.fromJson(result, Phase.class);
            return p;
        } catch (Exception e) {
            return null;
        }
    }

}
