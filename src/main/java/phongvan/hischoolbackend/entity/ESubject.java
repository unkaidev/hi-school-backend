package phongvan.hischoolbackend.entity;

public enum ESubject {    
    SINH_HOAT("Sinh Hoạt Chung"),
    TOAN_10("Toán K10"),
    VAN_10("Văn K10"),
    TIENG_ANH_10("Tiếng Anh K10"),
    TIENG_TRUNG_10("Tiếng Trung K10"),
    GDCD_10("Giáo Dục Công Dân K10"),
    LICH_SU_10("Lịch Sử K10"),
    VAT_LY_10("Vật Lý K10"),
    DIA_LY_10("Địa Lý K10"),
    HOA_HOC_10("Hóa Học K10"),
    SINH_HOC_10("Sinh Học K10"),
    TIN_HOC_10("Tin Học K10"),
    TOAN_11("Toán K11"),
    VAN_11("Văn K11"),
    TIENG_ANH_11("Tiếng Anh K11"),
    TIENG_TRUNG_11("Tiếng Trung K11"),
    GDCD_11("Giáo Dục Công Dân K11"),
    LICH_SU_11("Lịch Sử K11"),
    VAT_LY_11("Vật Lý K11"),
    DIA_LY_11("Địa Lý K11"),
    HOA_HOC_11("Hóa Học K11"),
    SINH_HOC_11("Sinh Học K11"),
    TIN_HOC_11("Tin Học K11"),
    ;

    ESubject(String name) {
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }
}
