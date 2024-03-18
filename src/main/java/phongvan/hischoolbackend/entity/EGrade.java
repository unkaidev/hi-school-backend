package phongvan.hischoolbackend.entity;

public enum EGrade {
    ALL("Toàn Khối"),
    K_10("Khối 10"),
    K_11("Khối 11"),
    K_12("Khối 12");

    EGrade(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
