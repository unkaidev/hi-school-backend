package phongvan.hischoolbackend.entity;

public enum EDistrict {
    BA_VI("Ba Vì"),
    ;

    EDistrict(String name) {
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }
}
