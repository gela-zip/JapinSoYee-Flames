package flamesgame;
import java.util.Random;

public class GameLogic {
    private boolean randomResult = false;
    private int userPoints = 0;
    
    private Planet[] planets = {
        new Planet("Fomalhaut", "UNSAFE"),
        new Planet("LHS 1140 b", "SAFE"),
        new Planet("Alpha Wolf", "UNSAFE"),
        new Planet("Mercury", "UNSAFE"),
        new Planet("ERIS", "SAFE"),
        new Planet("Slytherin", "UNSAFE")
    };
    
    public boolean isRandomResult() {
        return randomResult;
    }

    public int getUserPoints() {
        return userPoints;
    }
    
    public void resetGame() {
        userPoints = 0;
        randomResult = false;
    }
    
    public int getTally(String name1, String name2) {
        randomResult = false; //reset guard
        
        name1 = name1.trim().toUpperCase().replace(" ","");
        name2 = name2.trim().toUpperCase().replace(" ","");
        int tally = 0;
        
        //count matching letters from first name
        for (int i = 0; i < name1.length(); i++) {
            char letter = name1.charAt(i);
            if (name2.contains(String.valueOf(letter)))
                tally++;
        }
        
        //count mathcing letters from second name
        for (int i = 0; i < name2.length(); i++) {
            char letter = name2.charAt(i);
            if (name1.contains(String.valueOf(letter)))
                tally++;
        }
        
        //if no letters match, lance cn decide for them
        if (tally == 0){
            randomResult = true;
            Random random = new Random();
            tally = random.nextInt(6) + 1;
        }
                 
        return tally;
    }
    
    public String flamesResult(int tally){
        String flames = "FLAMES";
        int index = (tally - 1) % flames.length();
        char result = flames.charAt(index);
        
        switch (result) {
            case 'F': return "Friends";
            case 'L': return "Lovers";
            case 'A': return "Acquaintances";
            case 'M': return "Married";
            case 'E': return "Enemies";
            case 'S': return "Soulmates";
            default: return ""; //huhu idk what to put here i hope it NEVER gets here
        }
    }

    public void calculateRelationshipPoints(String flamesResult, String expectation) {
        userPoints = 0; //reset
        
        if (flamesResult.equalsIgnoreCase(expectation)){
            userPoints += 50;
        } else {
            userPoints += 25;
        }
    }

    public Planet choosePlanet() { //gaiz instead nga modifier, rng nalang ang planet HAHAH
        Random random = new Random();
        int index = random.nextInt(planets.length);
        return planets[index];
    }

    public void calculatePlanetPoints(Planet planet) {
        if (planet.getState().equalsIgnoreCase("SAFE")) {
            userPoints += 50;
        } else {
            userPoints += 25;
        }
    }
    
    public boolean isGameWon() {
        if (userPoints == 100){
            return true;
        } else {
            return false;
        }
    }
}
