package phongvan.hischoolbackend.entity;

public enum ECity {
    HA_NOI("Hà Nội"),
    ;
    ECity(String name) {
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }
}
