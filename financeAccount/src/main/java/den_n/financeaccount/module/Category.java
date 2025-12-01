package den_n.financeaccount.module;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions = new ArrayList<>();


    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    // Геттеры и сеттеры
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public List<Transaction> getTransactions() {return transactions;}
    public void setTransactions(List<Transaction> transactions) {this.transactions = transactions;}

    @Override
    public String toString() {
        return name;
    }
}
