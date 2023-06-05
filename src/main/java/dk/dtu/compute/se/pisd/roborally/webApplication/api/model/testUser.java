package dk.dtu.compute.se.pisd.roborally.webApplication.api.model;

/**
 * For testing
 */
public class testUser {

    private int id;
    private String name;
    private int temp;

    public testUser(int id, String name, int temp) {
        this.id = id;
        this.name = name;
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
