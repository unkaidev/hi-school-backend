package phongvan.hischoolbackend.entity;

public enum EProvince {
    NONE("X"),
    HA_NOI("Hà Nội"),
    ;

    EProvince(String name) {
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }
}
