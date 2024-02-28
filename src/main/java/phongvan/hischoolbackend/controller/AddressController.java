package phongvan.hischoolbackend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import phongvan.hischoolbackend.entity.ECity;
import phongvan.hischoolbackend.entity.EDistrict;
import phongvan.hischoolbackend.entity.EProvince;
import phongvan.hischoolbackend.entity.EWardCommune;

@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @GetMapping("/allWardCommune")
    public String[] getAllWardCommune(){
        EWardCommune[] wardCommunes = EWardCommune.values();
        String[] names = new String[wardCommunes.length];
        for (int i = 0; i < wardCommunes.length; i++) {
            names[i] = wardCommunes[i].getName();
        }
        return names;
    }
    @GetMapping("/allDistrict")
    public String[] getAllDistrict(){
        EDistrict[] districts = EDistrict.values();
        String[] names = new String[districts.length];
        for (int i = 0; i < districts.length; i++) {
            names[i] = districts[i].getName();
        }
        return names;
    }
    @GetMapping("/allProvince")
    public String[] getAllProvince(){
        EProvince[] provinces = EProvince.values();
        String[] names = new String[provinces.length];
        for (int i = 0; i < provinces.length; i++) {
            names[i] = provinces[i].getName();
        }
        return names;
    }
    @GetMapping("/allCity")
    public String[] getAllCity(){
        ECity[] cities = ECity.values();
        String[] names = new String[cities.length];
        for (int i = 0; i < cities.length; i++) {
            names[i] = cities[i].getName();
        }
        return names;
    }
}
