import java.util.ArrayList;

public class Fruits {

    private fruitNameEnum name;
    private Double degrading;
    private Integer saleValue;

    public Fruits(fruitNameEnum name, Double degrading, Integer saleValue) {
        this.name = name;
        this.degrading = degrading;
        this.saleValue = saleValue;

    }

    public fruitNameEnum getName() {
        return name;
    }

    public Double getDegrading() {
        return degrading;
    }

    public Integer getSaleValue() {
        return saleValue;
    }

}

