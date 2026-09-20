package flamesgame;

public class Planet {
    private String name;
    private String state;

    public Planet(String name, String state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }
}