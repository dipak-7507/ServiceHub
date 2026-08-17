package com.servicehub.entity;
import jakarta.persistence.*;


@Entity
public class ServiceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false, unique = true)

    private String categoryName;

    public ServiceCategory() {

    }
    public ServiceCategory(String categoryName){
        this.categoryName = categoryName;
    }
    public Long getId(){
        return id;
    }
    public String getCategoryName(){
        return categoryName;
    }
    public void setId(Long id){
        this.id = id;
    }
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

}
