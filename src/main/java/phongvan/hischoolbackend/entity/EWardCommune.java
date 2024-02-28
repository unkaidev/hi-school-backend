package phongvan.hischoolbackend.entity;

public enum EWardCommune {
      BA_TRAI("Ba Trại"),
    BA_VI("Ba Vì"),
    CAM_THUONG("Cam Thượng"),
    CAM_LINH("Cẩm Lĩnh"),
    CHAU_SON("Châu Sơn"),
    CHU_MINH("Chu Minh"),
    CO_DO("Cổ Đô"),
    DONG_QUANG("Đông Quang"),
    DONG_THAI("Đồng Thái"),
    KHANH_THUONG("Khánh Thượng"),
    MINH_CHAU("Minh Châu"),
    MINH_QUANG("Minh Quang"),
    PHONG_VAN("Phong Vân"),
    PHU_CHAU("Phú Châu"),
    PHU_CUONG("Phú Cường"),
    PHU_DONG("Phú Đông"),
    PHU_PHUONG("Phú Phương"),
    PHU_SON("Phú Sơn"),
    SON_DA("Sơn Đà"),
    TAN_HONG("Tản Hồng"),
    TAN_LINH("Tản Lĩnh"),
    TAY_DANG("TT Tây Đằng"),
    THAI_HOA("Thái Hòa"),
    THUAN_MY("Thuần Mỹ"),
    THUY_AN("Thụy An"),
    TIEN_PHONG("Tiên Phong"),
    TONG_BAT("Tòng Bạt"),
    VAN_THANG("Vạn Thắng"),
    VAN_HOA("Vân Hòa"),
    VAT_LAI("Vật Lại"),
    YEN_BAI("Yên Bài");

    EWardCommune(String name) {
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }
}
