public class Receipt {
    private Double profitAcquired = 0.0;
    private Double transactionCount = 0.0;
    private Double starterCredit = 0.00;
    private Double finalCredit = 1000.0;

    public Receipt(Double profitAcquired,Double transactionCount,Double starterCredit,Double finalCredit) {
        this.profitAcquired=profitAcquired;
        this.transactionCount=transactionCount;
        this.starterCredit=starterCredit;
        this.finalCredit=finalCredit;
    }

    public Double getProfitAcquired() {
        return profitAcquired;
    }

    public void setProfitAcquired(Double profitAcquired) {
        this.profitAcquired = profitAcquired;
    }

    public Double getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Double transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Double getStarterCredit() {
        return starterCredit;
    }

    public void setStarterCredit(Double starterCredit) {
        this.starterCredit = starterCredit;
    }

    public Double getFinalCredit() {
        return finalCredit;
    }

    public void setFinalCredit(Double finalCredit) {
        this.finalCredit = finalCredit;
    }
}
