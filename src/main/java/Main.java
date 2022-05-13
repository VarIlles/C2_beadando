import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.InputMismatchException;
import java.util.Random;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static String userChoice;
    static Double yourCredit = setYourCredit();
    static Double profitAcquired = 0.0;
    static Double transactionCount = 0.0;
    static Double finalCredit = 0.0;

    static ArrayList<Receipt> receipts = new ArrayList<>();
    static ArrayList<Fruits> fruits = new ArrayList<>();

    static final Fruits alma = new Fruits(fruitNameEnum.Alma, 0.2, 80);
    static final Fruits narancs = new Fruits(fruitNameEnum.Narancs, 0.05, 40);
    static final Fruits kiwi = new Fruits(fruitNameEnum.Kiwi, 0.4, 160);
    static final Fruits szolo = new Fruits(fruitNameEnum.Szolo, 0.3, 110);
    static final Fruits banan = new Fruits(fruitNameEnum.Banan, 0.075, 45);
    static final Fruits szilva = new Fruits(fruitNameEnum.Szilva, 0.05, 40);
    static final Fruits korte = new Fruits(fruitNameEnum.Korte, 0.2, 60);

    static ArrayList<Farmers> farmers = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static Integer marketDayId = random.nextInt(7);

    public static void main(String[] args) {
        XmlToFarmer();
        XmlToReceipt();

        //<initial boot header>

        System.out.println("Gyümölcs Piac Szoftver");
        System.out.println("Üdvözöljük a platformon!");

        //<menu>

        fillFruitsData();
        menu();

    }

    public static int indexByFarmerName(String name){
        int a = -1;
        for (int i = 0; i < farmers.size(); i++){
            if (farmers.get(i).getName().equals(name)){
                a = i;
            }
        }
        return a;
    }

    public static int indexByFruitName(fruitNameEnum name){
        int a = -1;
        for (int i = 0; i < fruits.size(); i++){
            if (fruits.get(i).getName().equals(name)){
                a = i;
            }
        }
        return a;
    }

    public static final void fillFruitsData(){
        fruits.add(alma);
        fruits.add(narancs);
        fruits.add(kiwi);
        fruits.add(szolo);
        fruits.add(banan);
        fruits.add(szilva);
        fruits.add(korte);
    }

    public static Double marketProfit (Farmers farmer, Integer dayOfSale){
        Double profit=0.0;

        Double price = Double.valueOf(farmer.getEstimatedIncome()/farmer.getAmount());
        profit = marketPrice(farmer, farmer.getFruitForSale(), dayOfSale)-price;

        return profit;
    }

    public static Double marketPrice(Farmers farmer, Fruits fruit, Integer dayOfSale){

        Double fruitPrice = 0.0;
        Double a = 1.0;
        Integer dayDiff;

        dayDiff = dayOfSale - farmer.getSaleDay();
        if (farmer.getFruitForSale().getName().equals(fruit.getName())) {
            if (dayDiff < 0) {
                a = 0.0;
            } else {
                a = 1 - (dayDiff * fruit.getDegrading());
            }

            fruitPrice = a * fruit.getSaleValue();

        }
        else{
            fruitPrice=0.0;
        }


        return fruitPrice;
    }

    private static void createChild(Document document, Element parent, String tagName, String text) {
        Element element = document.createElement(tagName);
        element.setTextContent(text);
        parent.appendChild(element);
    }

    public static Double setYourCredit(){

        try{
            receipts.get((receipts.size()-1)).getFinalCredit();
        }
        catch (Exception exception){
            yourCredit=1000.00;
        }

        return yourCredit;
    }

    //<Menu>

    public static void menu(){
        //<cycle so the program only stops when the user chooses>
        int stop = 0;
        while (stop != 1) {

        //<recurring user interaction choice>
        System.out.println("\nAz alábbi menüpontok közül választhat, a menüpont melletti zárójelben szereplő szám beírásával. Melyik opciót szeretné?");
        System.out.println("Menü: " +
                "\n(0) Egyenleg megtekintése," +
                "\n(1) Farmerek listájának megtekintése," +
                "\n(2) Gyümölcsök tuldajdonságainak megtekintése," +
                "\n(3) Nyugták listájának megtekintése," +
                "\n(4) Farmer hozzáadása a listához," +
                "\n(5) Farmer eltávolítása a listából," +
                "\n(6) Farmer tulajdonságának módosítása," +
                "\n(7) Automatikus kereskedés a farmerekkel," +
                "\n(8) Program bezárása (Automatikus mentéssel)");

            //<evaluate user input>

            userChoice = scanner.next();
            switch (userChoice) {
                case "0" -> {
                    System.out.println("Egyenleg megtekintése");
                    walletMenu();
                }
                case "1" -> {
                    System.out.println("Farmerek listájának megtekintése");
                    listFarmersMenu();
                }
                case "2" -> {
                    System.out.println("Gyümölcsök tuldajdonságainak megtekintése");
                    listFruitsMenu();
                }
                case "3" -> {
                    System.out.println("Nyugták listájának megtekintése");
                    listReceiptsMenu();
                }
                case "4" -> {
                    System.out.println("Farmer hozzáadása a listához");
                    addFarmerMenu();
                }
                case "5" -> {
                    System.out.println("Farmer eltávolítása a listából");
                    deleteFarmerMenu();
                }
                case "6" -> {
                    System.out.println("Farmer tulajdonságának módosítása");
                    updateFarmerMenu();
                }
                case "7" -> {
                    System.out.println("Automatikus kereskedés a farmerekkel");
                    if (receipts.size() == 0) {
                        tradeMenu(1000.00, farmers);
                    }
                    else{tradeMenu(receipts.get((receipts.size()-1)).getFinalCredit(), farmers);
                    }
                }
                case "8" -> {
                    System.out.println("Program bezárása (Automatikus mentés .xml formátumban)");
                    saveFarmersXml();
                    saveReceiptsXml();
                    stop = 1;
                }
                default -> System.out.println("Csak a listában szereplő számok egyikét adhatja meg");
            }
        }

    }

    public static void saveFarmersXml() {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element rootElement = document.createElement("farmers");
            document.appendChild(rootElement);

            //<create tags in xml>

            for (Farmers farmer : farmers) {
                Element farmerElement = document.createElement("farmer");
                rootElement.appendChild(farmerElement);
                createChild(document, farmerElement, "name", farmer.getName());
                createChild(document, farmerElement, "estimtedIncome", String.valueOf(farmer.getEstimatedIncome()));
                createChild(document, farmerElement, "fruitname", String.valueOf(farmer.getFruitForSale().getName()));
            }

            //<format xml>

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream("src/main/resources/farmers.xml"));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveReceiptsXml(){
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element rootElement = document.createElement("receipts");
            document.appendChild(rootElement);

            //<create tags in xml>

            for (Receipt receipt : receipts) {
                Element farmerElement = document.createElement("receipt");
                rootElement.appendChild(farmerElement);
                createChild(document, farmerElement, "profitAcquired", receipt.getProfitAcquired().toString());
                createChild(document, farmerElement, "transactionCount", String.valueOf(receipt.getTransactionCount()));
                createChild(document, farmerElement, "starterCredit", String.valueOf(receipt.getStarterCredit()));
                createChild(document, farmerElement, "finalCredit", String.valueOf(receipt.getFinalCredit()));
            }

            //<format xml>

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream("src/main/resources/receipts.xml"));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void walletMenu(){

        System.out.println("Az Ön egyenlege: " + yourCredit);

    }

    public static void listFarmersMenu(){

        if (farmers.size() > 0) {
            for (int i = 0; i < farmers.size(); i++) {
                System.out.println("farmer neve: " + farmers.get(i).getName() + " , termesztett gyümölcs: "
                        + farmers.get(i).getFruitForSale().getName() +
                        " , kívánt napi kereset: " + farmers.get(i).getEstimatedIncome());
            }
        }
        else{
            System.out.println("A farmerek listájában nem szerepelnek farmerek. Adjon hozzá legalább egy farmert," +
                    " a kívánt művelet elvégzéséhez!");
            System.out.println("Visszatérés a menübe");
            menu();
        }
    }

    public static void listFruitsMenu(){

        for (int i = 0; i < fruits.size(); i++) {
            System.out.println("gyümölcs neve: " + fruits.get(i).getName() +
                    " , napi romlás mértéke: " + fruits.get(i).getDegrading()*100 +
                    "% , piaci ára: " + fruits.get(i).getSaleValue());
        }

    }

    public static void listReceiptsMenu(){
        if (receipts.size()>0) {
            for (int i = 0; i < receipts.size(); i++) {
                System.out.println("Profit: " + receipts.get(i).getProfitAcquired() +
                        " , Tranzakciók száma: " + receipts.get(i).getTransactionCount() +
                        " , Kereskedés előtti egyenleg , : " + receipts.get(i).getStarterCredit() +
                        " , Kereskedés utáni egyenleg: " + receipts.get(i).getFinalCredit());
            }
        }
        else{
            System.out.println("Még nem történt tranzakció");
            menu();
        }

    }

    public static void updateFarmerMenu() {

        if (farmers.size() > 0) {

            int stop = 0;

            System.out.println("Adja meg a módosítani kívánt farmer nevét!");
            String name = scanner.next();

            if (indexByFarmerName(name) == -1) {
                System.out.println("Nem szerepel a megadott név a listában.");
                updateFarmerMenu();
            }
            else {
                try {
                    while (stop != 1) {

                        System.out.println("Adja meg melyik tulajdonságát kívánja módosítani a kiválasztott farmernek! (név (1), termesztett gyümölcs (2), napi kereset (3))");
                        Integer attribute = scanner.nextInt();
                        switch (attribute) {
                            case 1 -> {
                                System.out.println("Adja meg az új nevet!");
                                String newName = scanner.next();
                                farmers.get(indexByFarmerName(name)).setName(newName);
                                stop = 1;
                                break;
                            }
                            case 2 -> {
                                System.out.println("Adja meg az új termesztett gyümölcsöt! " +
                                        "(Alma, Narancs, Kiwi, Szolo, Banan, Szilva, Korte)");
                                fruitNameEnum newFruit = fruitNameEnum.valueOf(scanner.next());
                                farmers.get(indexByFarmerName(name)).setFruitForSale(fruits.get(indexByFruitName(newFruit)));
                                stop = 1;
                                break;
                            }
                            case 3 -> {
                                System.out.println("Adja meg az új napi keresetet!");
                                Integer income = scanner.nextInt();
                                farmers.get(indexByFarmerName(name)).setEstimatedIncome(income);
                                stop = 1;
                                break;
                            }
                            default -> {
                                System.out.println("1, 2 vagy 3 a választható szám.");
                                stop = 1;
                                updateFarmerMenu();
                                break;
                            }
                        }
                    }
                }
                catch (IllegalArgumentException exception) {
                    System.out.println("Legközelebb a zárójelben szereplő listából válasszon!");
                    updateFarmerMenu();
                }
                catch (InputMismatchException exception) {
                    System.out.println("Legközelebb csak számkaraktereket adjon meg");
                    updateFarmerMenu();
                }
            }
        }
        else{
            System.out.println("A farmerek listájában nem szerepelnek farmerek. Adjon hozzá legalább egy farmert, a kívánt művelet elvégzéséhez!");
            System.out.println("Visszatér a menübe");
            menu();
        }

    }

    public static void deleteFarmerMenu() {

        if (farmers.size()>0) {

            System.out.println("Adja meg a törölni kívánt farmer nevét!");
            String name = scanner.next();

            if (farmers.get(indexByFarmerName(name)).getName().equals(name)){
                farmers.remove(indexByFarmerName(name));
            }
            else{
                System.out.println("Nem szerepel a listában farmer a megadott névvel.");
                menu();
            }
        }
        else{
            System.out.println("A farmerek listájában nem szerepelnek farmerek. Adjon hozzá legalább egy farmert, a kívánt művelet elvégzéséhez!");
            menu();
        }
    }

    public static void addFarmerMenu() {

        boolean isIt = false;
        String s;

        try {
            System.out.println("\nAdja meg a farmer nevét!");
            String name = scanner.next();
            System.out.println("\nAdja meg a farmer kívánt jövedelmét!");
            Integer income = scanner.nextInt();
            System.out.println("\nAdja meg a farmer által termesztett gyümölcs nevét! " +
                    "(Alma, Narancs, Kiwi, Szolo, Banan, Szilva, Korte)");

            while (!isIt) {
                s = scanner.next();
                switch (s) {
                    case "Alma" -> {
                        farmers.add(new Farmers(name, income, alma));
                        isIt = true;
                    }
                    case "Narancs" -> {
                        farmers.add(new Farmers(name, income, narancs));
                        isIt = true;
                    }
                    case "Kiwi" -> {
                        farmers.add(new Farmers(name, income, kiwi));
                        isIt = true;
                    }
                    case "Szolo" -> {
                        farmers.add(new Farmers(name, income, szolo));
                        isIt = true;
                    }
                    case "Banan" -> {
                        farmers.add(new Farmers(name, income, banan));
                        isIt = true;
                    }
                    case "Szilva" -> {
                        farmers.add(new Farmers(name, income, szilva));
                        isIt = true;
                    }
                    case "Korte" -> {
                        farmers.add(new Farmers(name, income, korte));
                        isIt = true;
                    }
                    default -> {
                        System.out.println("Ellenőrizze, hogy helyesen írta be a gyümölcs nevét, szerepel a gyümöécs neve a listában!");
                    }
                }
            }
        }
        catch (InputMismatchException exception){
            System.out.println("Csak számkaraktereket adjon meg a farmer jövedelméhez.");
            menu();
        }
}

    public static void tradeMenu(Double starterCredit, ArrayList<Farmers> farmers){

        System.out.println("Kereskedés");
        System.out.println("Kereskedés előtti egyenleged: " + starterCredit);
        transactionCount=0.0;
        Double start = starterCredit;

        if (farmers.size() > 0) {

            for (Farmers farmer : farmers) {farmer.setAmount(random.nextInt(40,60));}
            for (Farmers farmer : farmers) {farmer.setSaleDay(random.nextInt(0,6));}
            marketDayId = random.nextInt(7);

            ArrayList<Double> profits = new ArrayList<>();
            ArrayList<String> farmerNames = new ArrayList<>();
            Double a = 0.0;
            Double d1;
            Double d2;
            String f1;
            String f2;
            String f;
            Double revenue = 0.0;

            //<GET ALL PROFIT VALUES FOR FARMERS>

            for (int i = 0; i < farmers.size(); i++) {
                farmerNames.add(farmers.get(i).getName());
                profits.add(marketProfit(farmers.get(i), marketDayId));
            }

            //<ORDER THEM IN A CONDESCENDING ORDER>

            for (int i = 0; i < profits.size(); i++) {
                for (int j = i + 1; j < profits.size(); j++) {

                    d1 = profits.get(i);
                    d2 = profits.get(j);
                    f1 = farmers.get(i).getName();
                    f2 = farmers.get(j).getName();

                    if (Double.compare(d1, d2) < 0) {

                        f = f1;
                        f1 = f2;
                        f2 = f;

                        a = d1;
                        d1 = d2;
                        d2 = a;

                        profits.remove(i);
                        profits.add(i, d1);
                        profits.remove(j);
                        profits.add(j, d2);

                        farmerNames.remove(i);
                        farmerNames.add(i, f1);
                        farmerNames.remove(j);
                        farmerNames.add(j, f2);

                    }
                }
            }

            //<trade>

            int i = 0;
            Integer fruitRemaining = farmers.get(indexByFarmerName(farmerNames.get(i))).getAmount();

            while (i < farmerNames.size() &&
                    profits.get(i) > 0 &&
                    !(starterCredit < Double.valueOf(farmers.get(indexByFarmerName(farmerNames.get(i))).getEstimatedIncome()) /
                            (farmers.get(indexByFarmerName(farmerNames.get(i))).getAmount()))) {

                starterCredit -= Double.valueOf(farmers.get(indexByFarmerName(farmerNames.get(i))).getEstimatedIncome()) /
                              (farmers.get(indexByFarmerName(farmerNames.get(i))).getAmount());

                fruitRemaining--;

                revenue += marketPrice(farmers.get(indexByFarmerName(farmerNames.get(i))),
                           farmers.get(indexByFarmerName(farmerNames.get(i))).getFruitForSale(), marketDayId);

                transactionCount++;

                if (fruitRemaining == 0) {
                    i++;
                    if (i < farmerNames.size()) {
                        fruitRemaining = farmers.get(indexByFarmerName(farmerNames.get(i))).getAmount();
                    }
                }
            }

            finalCredit = starterCredit + revenue;
            profitAcquired = finalCredit - starterCredit;

            System.out.println("Kereskedés utáni egyenleged " + finalCredit);
            System.out.println("Tranzakciók száma: " + transactionCount);
            System.out.println("Profitod: " + profitAcquired);

            if (profitAcquired > 0) {
                receipts.add(new Receipt(profitAcquired,transactionCount,starterCredit,finalCredit));
                yourCredit = finalCredit;
                starterCredit = start;
            }
        }
        else{
            System.out.println("A farmerek listájában nem szerepelnek farmerek. Adjon hozzá legalább egy farmert, a művelet elvégzéséhez!");
            addFarmerMenu();
        }
    }

    public static void XmlToFarmer() {
        fillFruitsData();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("src/main/resources/farmers.xml");
            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;
            for (int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfFarmersTag = node.getChildNodes();
                    String name = "", estimatedIncome = "", fruitname = fruitNameEnum.Alma.toString();
                    for (int j = 0; j < childNodesOfFarmersTag.getLength(); j++) {
                        Node childNodeOffarmerTag = childNodesOfFarmersTag.item(j);
                        if (childNodeOffarmerTag.getNodeType() == Node.ELEMENT_NODE) {
                            switch (childNodeOffarmerTag.getNodeName()) {
                                case "name" -> name = childNodeOffarmerTag.getTextContent();
                                case "estimtedIncome" -> estimatedIncome = childNodeOffarmerTag.getTextContent();
                                case "fruitname" -> fruitname = childNodeOffarmerTag.getTextContent();
                            }
                        }
                    }
                    farmers.add(new Farmers(name,Integer.valueOf(estimatedIncome),
                            fruits.get(indexByFruitName(fruitNameEnum.valueOf(fruitname)))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void XmlToReceipt() {
        try {
            DocumentBuilderFactory documentBuilderFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("src/main/resources/receipts.xml");
            Element rootElement = document.getDocumentElement();
            NodeList childNodeList = rootElement.getChildNodes();
            Node node;
            for (int i = 0; i < childNodeList.getLength(); i++) {
                node = childNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodesOfReceiptTag = node.getChildNodes();
                    String profitAcquired = "", transactionCount = "", starterCredit = "", finalCredit = "";
                    for (int j = 0; j < childNodesOfReceiptTag.getLength(); j++) {
                        Node childnodeofreceiptsTag = childNodesOfReceiptTag.item(j);
                        if (childnodeofreceiptsTag.getNodeType() == Node.ELEMENT_NODE) {
                            switch (childnodeofreceiptsTag.getNodeName()) {
                                case "profitAcquired" -> profitAcquired = childnodeofreceiptsTag.getTextContent();
                                case "transactionCount" -> transactionCount = childnodeofreceiptsTag.getTextContent();
                                case "starterCredit" -> starterCredit = childnodeofreceiptsTag.getTextContent();
                                case "finalCredit" -> finalCredit = childnodeofreceiptsTag.getTextContent();
                            }
                        }
                    }
                    receipts.add(new Receipt(Double.parseDouble(profitAcquired), Double.parseDouble(transactionCount),
                            Double.parseDouble(starterCredit), Double.parseDouble(finalCredit)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}