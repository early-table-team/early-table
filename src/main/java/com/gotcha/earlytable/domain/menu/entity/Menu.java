package com.gotcha.earlytable.domain.menu.entity;

import com.gotcha.earlytable.domain.file.entity.File;
import com.gotcha.earlytable.domain.file.entity.ImageFile;
import com.gotcha.earlytable.domain.menu.MenuStatus;
import com.gotcha.earlytable.domain.menu.dto.MenuRequestDto;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private String menuContents;

    @Column(nullable = false)
    private Integer menuPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Allergy> allergyList = new ArrayList<>();


    public Menu(String menuName, String menuContents, Integer menuPrice, MenuStatus menuStatus,
                File file, Store store) {

        this.menuName = menuName;
        this.menuContents = menuContents;
        this.menuPrice = menuPrice;
        this.menuStatus = menuStatus;
        this.file = file;
        addStore(store);
    }

    public Menu() {

    }

    public void addStore(Store store) {
        this.store = store;
        store.getMenuList().add(this);
    }

    public void updateMenu(MenuRequestDto menuRequestDto) {
        if(menuRequestDto.getMenuName() != null) {
            this.menuName = menuRequestDto.getMenuName();
        }
        if(menuRequestDto.getMenuContents() != null) {
            this.menuContents = menuRequestDto.getMenuContents();
        }
        if(menuRequestDto.getMenuPrice() != null) {
            this.menuPrice = menuRequestDto.getMenuPrice();
        }
        if(menuRequestDto.getMenuStatus() != null) {
            this.menuStatus = menuRequestDto.getMenuStatus();
        }
    }
}
