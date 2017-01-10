package app.sample.jpa;

import javax.persistence.*;

@Entity
@Table(name = "tbl_myshop")
public class Shop {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String owner;

    protected Shop() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                '}';
    }
}
