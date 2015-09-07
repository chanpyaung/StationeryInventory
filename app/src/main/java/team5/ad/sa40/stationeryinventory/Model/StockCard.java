package team5.ad.sa40.stationeryinventory.Model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockCard {
    
    private String StockCardSN;
    private Date date;
    private String description;
    private int qty;
    private int balance;
    public static List<StockCard> stockCardList;
    
    public String getStockCardSN(){
        return StockCardSN;
    }

    public Date getDate() {
        return date;
    }
    public String getDescription(){
        return description;
    }
    public int getQty(){
        return qty;
    }
    public int getBalance(){
        return balance;
    }

    public void setStockCardSN(String scid){
         StockCardSN = scid;
    }

    public void setDate(Date d) {
         date = d;
    }
    public void setDescription(String desc){
         description = desc;
    }
    public void setQty(int q){
         qty = q;
    }
    public void setBalance(int bal){
         balance = bal;
    }

    public static List<StockCard> initializeData(String itemID){
        List<StockCard> stockCards = new ArrayList<StockCard>();
        int i = 0;
        StockCard sc = new StockCard();
        sc.setDate(new Date());
        sc.setDescription("Supplier - BANE");
        sc.setQty(500);
        sc.setBalance(550);
        stockCards.add(sc);
        i++;
        StockCard sc2 = new StockCard();
        sc2.setDate(new Date());
        sc2.setDescription("English Department");
        sc2.setQty(-20);
        sc2.setBalance(530);
        stockCards.add(sc2);
        i++;
        StockCard sc3 = new StockCard();
        sc3.setDate(new Date());
        sc3.setDescription("Stock Adjustment 001/08/2015");
        sc3.setQty(-4);
        sc3.setBalance(526);
        stockCards.add(sc3);

        stockCardList = stockCards;
        return stockCards;
    }
    
}
