package phongvan.hischoolbackend.entity;

public enum EGroup {
    TOAN_LY_HOA("Toán-Lý-Hóa"),
    VAN_SU_DIA("Văn-Sử-Địa"),
    NGOAI_NGU("Ngoại ngữ"),
    GIAODUC_THECHAT("Giáo dục thể chất"),
    KHOAHOC_UNGDUNG("Khoa học ứng dụng"),
    KHOAHOC_XAHOI("Khoa học xã hội"),
    BGH("Ban giám hiệu");


    EGroup(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
