import java.util.ArrayList;
import java.util.Random;

public class Farmers {

    Random random = new Random();
    private String name;
    private Integer estimatedIncome;
    private Fruits fruitForSale;
    private Integer amount;
    private Integer saleDay;


    public Farmers(String name, Integer estimatedIncome, Fruits fruitForSale) {
        this.name = name;
        this.estimatedIncome = estimatedIncome;
        this.fruitForSale = fruitForSale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEstimatedIncome() {
        return estimatedIncome;
    }

    public void setEstimatedIncome(Integer estimatedIncome) {
        this.estimatedIncome = estimatedIncome;
    }

    public Fruits getFruitForSale() {
        return fruitForSale;
    }

    public void setFruitForSale(Fruits fruitForSale) {
        this.fruitForSale = fruitForSale;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {this.amount = amount;}

    public Integer getSaleDay() {
        return saleDay;
    }

    public void setSaleDay(Integer saleDay) {
        this.saleDay = saleDay;
    }
}
