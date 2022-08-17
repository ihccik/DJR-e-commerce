package com.company;

import com.company.balance.Balance;
import com.company.balance.CustomerBalance;
import com.company.balance.GiftCardBalance;
import com.company.category.Category;
import com.company.discount.Discount;
import com.company.order.Order;
import com.company.order.OrderService;
import com.company.order.OrderServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import static com.company.StaticConstants.DISCOUNT_LIST;
import static com.company.StaticConstants.ORDER_LIST;

public class Main {

    public static void main(String[] args)  {

        DataGenerator.createCustomer();
        DataGenerator.createCategory();
        DataGenerator.createProduct();
        DataGenerator.createBalance();
        DataGenerator.createDiscount();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Select Customer:");
        for(int i = 0; i< StaticConstants.CUSTOMER_LIST.size(); i++){
            System.out.println("Type " + i + " for customer:" + StaticConstants.CUSTOMER_LIST.get(i).getUserName());
        }

        Customer customer = StaticConstants.CUSTOMER_LIST.get(scanner.nextInt());

        Cart cart = new Cart(customer);

        while(true){

            System.out.println("What would you like to do? Just type id for selection");

            for(int i=0;i< prepareMenuOptions().length;i++){
                System.out.println(i + "-" + prepareMenuOptions()[i]);
            }

            int menuSelection = scanner.nextInt();

            switch (menuSelection){
                case 0: //list categories
                    for(Category category : StaticConstants.CATEGORY_LIST){
                        System.out.println("Category Code:" + category.generateCategoryCode() + " category name:" + category.getName());
                    }
                    break;
                case 1: //list products  //product name, product category name
                    try{
                        for(Product product : StaticConstants.PRODUCT_LIST){

                        System.out.println("Product Name: " + "\n" + product.getName() + "\n" + "Product Category Name:" + "\n" + product.getCategoryName());
                        }

                    }catch(Exception e){
                        System.out.println("Product could not printed because category not found for product name:" + e.getMessage().split(",")[1] );
                    }
                    break;
                case 2: //list discounts
                    for(Discount discount : StaticConstants.DISCOUNT_LIST){
                        System.out.println("Discount Name: " + discount.getName() + "discount threshold amount: " + discount.getThresholdAmount());
                    }
                    break;
                case 3://see balance
                    CustomerBalance cBalance = findCustomerBalance(customer.getId());
                    GiftCardBalance gBalance = findGiftCardBalance(customer.getId());
                    double totalBalance = cBalance.getBalance() + gBalance.getBalance();
                    System.out.println("Total Balance:" + totalBalance);
                    System.out.println("Customer Balance:" + cBalance.getBalance());
                    System.out.println("Gift Card Balance:" + gBalance.getBalance());
                    break;
                case 4://add balance
                    CustomerBalance customerBalance = findCustomerBalance(customer.getId());
                    GiftCardBalance giftCardBalance= findGiftCardBalance(customer.getId());
                    System.out.println("Which Account would you like to add?");
                    System.out.println("Type 1 for Customer Balance:" + customerBalance.getBalance());
                    System.out.println("Type 2 for Gift Card Balance:" + giftCardBalance.getBalance());
                    int balanceAccountSelection = scanner.nextInt();
                    System.out.println("How much you would like to add?");
                    double additionalAmount = scanner.nextInt();

                    switch(balanceAccountSelection){
                        case 1:
                            customerBalance.addBalance(additionalAmount);
                            System.out.println("New Customer Balance:" + customerBalance.getBalance());
                            break;
                        case 2:
                            giftCardBalance.addBalance(additionalAmount);
                            System.out.println("New Gift Card Balance:" + giftCardBalance.getBalance());
                            break;
                    }
                    break;
                case 5://place an order
                    Map<Product,Integer> map = new HashMap<>();
                    cart.setProductMap(map);
                    while(true){
                        System.out.println("Which product you want to add to your cart. For exit product selection Type : exit");
                        for(Product product: StaticConstants.PRODUCT_LIST){
                            try {
                                System.out.println(
                                        "id:" + product.getId() + "price:" + product.getPrice() +
                                        "product category" + product.getCategoryName() +
                                                "stock:" + product.getRemainingStock() +
                                                "product delivery due:" + product.getDeliveryDueDate());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());;
                            }
                        }
                        String productId = scanner.next();

                        try {
                            Product product = findProductById(productId);
                            if (!putItemToCartIfStockAvailable(cart, product)) {
                                System.out.println("Stock is insufficient. Please try again");
                                continue;
                            }
                        } catch (Exception e) {
                            System.out.println("Product does not exist. please try again");
                            continue;
                        }

                        System.out.println("Do you want to add more product. Type Y for adding more, N for exit");
                        String decision = scanner.next();
                        if(!decision.equals("Y")){
                            break;
                        }
                    }


                    System.out.println("seems there are discount options. Do you want to see and apply to your cart if it is applicable. For no discount type no");
                    for (Discount discount : DISCOUNT_LIST) {
                        System.out.println("discount id " + discount.getId() + " discount name: " + discount.getName());
                    }
                    String discountId = scanner.next();
                    if (!discountId.equals("no")) {
                        try {
                            Discount discount = findDiscountById(discountId);
                            if (discount.decideDiscountIsApplicableToCart(cart)) {
                                cart.setDiscountId(discount.getId());
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }

                    OrderService orderService = new OrderServiceImpl();
                    String result = orderService.placeOrder(cart);
                    if (result.equals("Order has been placed successfully")) {
                        System.out.println("Order is successful");
                        updateProductStock(cart.getProductMap());
                        cart.setProductMap(new HashMap<>());
                        cart.setDiscountId(null);
                    } else {
                        System.out.println(result);
                    }
                    break;
                case 6://See cart
                    System.out.println("Your Cart");
                    if (!cart.getProductMap().keySet().isEmpty()) {
                        for (Product product : cart.getProductMap().keySet()) {
                            System.out.println("product name: " + product.getName() + " count: " + cart.getProductMap().get(product));
                        }
                    } else {
                        System.out.println("Your cart is empty");
                    }
                    break;
                case 7://see order details
                    printOrdersByCustomerId(customer.getId());
                    break;
                case 8://see your address
                    printAddressByCustomerId(customer);
                    break;
                case 9:
                    System.exit(1);
                    break;
            }


        }

    }

    private static Discount findDiscountById(String discountId) throws Exception {
        for (Discount discount : DISCOUNT_LIST) {
            if (discount.getId().toString().equals(discountId)) {
                return discount;
            }
        }
        throw new Exception("Discount couldn't applied because couldn't found");
    }

    private static void updateProductStock(Map<Product, Integer> map) {
        for (Product product : map.keySet()) {
            product.setRemainingStock(product.getRemainingStock() - map.get(product));
        }
    }

    private static void printOrdersByCustomerId(UUID customerId) {
        for (Order order : ORDER_LIST) {
            if (order.getCustomerId().toString().equals(customerId.toString())) {
                System.out.println("Order status: " + order.getOrderStatus() + " order amount " + order.getPaidAmount() + " order date " + order.getOrderDate());
            }
        }
    }

    private static void printAddressByCustomerId(Customer customer) {
        for (Address address : customer.getAddress()) {
            System.out.println(" Street Name: " + address.getStreetName() +
                    " Street Number: " + address.getStreetNumber() + "ZipCode:  "
                    + address.getZipCode() + " State: " + address.getState());
        }
    }

    private static boolean putItemToCartIfStockAvailable(Cart cart, Product product) {

        System.out.println("Please provide product count:");
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();

        Integer cartCount = cart.getProductMap().get(product);

        if(cartCount !=null && product.getRemainingStock() > cartCount+count){
            cart.getProductMap().put(product,cartCount+count);
            return true;

        }else if(product.getRemainingStock()>=count){
            cart.getProductMap().put(product,count);
            return true;
        }
        return false; //BREAK TILL 3:10 PM

    }

    private static Product findProductById(String productId) throws Exception {
        for(Product product : StaticConstants.PRODUCT_LIST){
            if (product.getId().toString().equals(productId)) {
                return product;
            }
        }
        throw new Exception("Product not found");
    }


    private static CustomerBalance findCustomerBalance(UUID customerId){
        for(Balance customerBalance : StaticConstants.CUSTOMER_BALANCE_LIST){
            if(customerBalance.getCustomerId().toString().equals(customerId.toString())){
                return (CustomerBalance) customerBalance;
            }
        }

        CustomerBalance customerBalance = new CustomerBalance(customerId,0d);
        StaticConstants.CUSTOMER_BALANCE_LIST.add(customerBalance);

        return customerBalance;
    }

    private static GiftCardBalance findGiftCardBalance(UUID customerId){
        for(Balance giftCarBalance : StaticConstants.GIFT_CARD_BALANCE_LIST){
            if(giftCarBalance.getCustomerId().toString().equals(customerId.toString())){
                return  (GiftCardBalance) giftCarBalance;
            }
        }

        GiftCardBalance giftCarBalance = new GiftCardBalance(customerId,0d);
        StaticConstants.GIFT_CARD_BALANCE_LIST.add(giftCarBalance);

        return giftCarBalance;
    }





    private static String[] prepareMenuOptions(){
        return new String[]{"List Categories","List Products","List Discount","See Balance","Add Balance",
                "Place an order","See Cart","See order details","See your address","Close App"};
    }

    // This is DJR-2 ticket

}

// sTARTING COMMIT
//
