package pages;

public class CheckoutData {
    private static String name;
    private static String country;
    private static String city;
    private static String card;
    private static String month;
    private static String year;

    public static void setFormData(String name, String country, String city, String card, String month, String year) {
        CheckoutData.name = name;
        CheckoutData.country = country;
        CheckoutData.city = city;
        CheckoutData.card = card;
        CheckoutData.month = month;
        CheckoutData.year = year;
    }

    public static String getName(){
        return name;
    }

    public static String getCountry() {
        return country;
    }

    public static String getCity(){
        return city;
    }

    public static String getCard(){
        return card;
    }

    public static String getMonth(){
        return month;
    }

    public static String getYear(){
        return year;
    }

    public static void clear() {
        name = null;
        country = null;
        city = null;
        card = null;
        month = null;
        year = null;
    }
}
