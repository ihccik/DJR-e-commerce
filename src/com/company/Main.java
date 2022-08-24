package com.company;

import com.company.balance.Balance;
import com.company.balance.CustomerBalance;
import com.company.balance.GiftCardBalance;
import com.company.category.Category;
import com.company.discount.Discount;
import com.company.order.Order;
import com.company.order.OrderService;
import com.company.order.OrderServiceImpl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.company.StaticConstants.*;

public class Main {


    static {

        DataGenerator.createCustomer();
        DataGenerator.createCategory();
        DataGenerator.createProduct();
        DataGenerator.createBalance();
        DataGenerator.createDiscount();

    }
    private static Customer customer;
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Select Customer:");

            System.out.println("Type 0 for creating new customer");

            for (int i = 0; i < StaticConstants.CUSTOMER_LIST.size(); i++) {
                System.out.println("Type " + (i + 1) + " for customer:" + StaticConstants.CUSTOMER_LIST.get(i).getUserName());
            }

            int costumerChoice = scanner.nextInt();

            customer = null;

            if (costumerChoice == 0) {


                createNewCustomer();
                customer = CUSTOMER_LIST.get(CUSTOMER_LIST.size() - 1);
                break;
            }

            try {
                customer = CUSTOMER_LIST.get(costumerChoice - 1);
                break;

            } catch (IndexOutOfBoundsException c) {
                System.err.println("The user doesn't exist. Please, try again.");
            }

        }
        Cart cart = new Cart(customer);

        while (true) {

            System.out.println("What would you like to do? Just type id for selection");

            for (int i = 0; i < prepareMenuOptions().length; i++) {
                System.out.println(i + "-" + prepareMenuOptions()[i]);
            }

            int menuSelection = scanner.nextInt();

            switch (menuSelection) {
                case 0: //list categories
                    for (Category category : StaticConstants.CATEGORY_LIST) {
                        System.out.println(
                            "Category Code:" + category.generateCategoryCode() + " category name:"
                                + category.getName());
                    }
                    break;
                case 1: //list products  //product name, product category name
                    try {
                        for (Product product : StaticConstants.PRODUCT_LIST) {
                            System.out.println("Product Name: " + "\n" + product.getName() + "\n" + "Product Category Name:" + "\n" + product.getCategoryName());
                        }
                    } catch (Exception e) {
                        System.out.println(
                            "Product could not printed because category not found for product name:"
                                + e.getMessage().split(",")[1]);
                    }
                    break;
                case 2: //list discounts
                    for (Discount discount : StaticConstants.DISCOUNT_LIST) {
                        System.out.println(
                            "Discount Name: " + discount.getName() + "discount threshold amount: "
                                + discount.getThresholdAmount());
                    }
                    break;
                case 3://see balance
                    CustomerBalance cBalance = findCustomerBalance(customer.getId());
                    GiftCardBalance gBalance = findGiftCardBalance(customer.getId());
                    double totalBalance = cBalance.getBalance() + gBalance.getBalance();
                    System.out.println("Total Balance:" + totalBalance);
                    System.out.println("Customer Balance:" + cBalance.getBalance());
                    System.out.println("Gift Card Balance:" + gBalance.getBalance());
                    System.out.println("Would you like to transfer between your balances?");
                    System.out.println("Type 0 for NO");
                    System.out.println("Type 1 for transferring from Customer Balance to Gift Card Balance");
                    System.out.println("Type 2 for transferring from Gift Card Balance to Customer Balance");

                    int selection = scanner.nextInt();

                    if (selection == 0) {
                        continue;
                    } else if (selection == 1) {
                        System.out.println("How much would you like to transfer? Customer Balance --> Gift Card");
                        double transferringAmount = scanner.nextDouble();
                        if (transferringAmount > cBalance.getBalance()) {
                            System.err.println("Invalid Value: " + transferringAmount);
                            System.exit(0);
                        }
                        System.out.println("Total Balance:" + totalBalance);
                        cBalance.addBalance((-1)*transferringAmount);
                        System.out.println("New Customer Balance:" + cBalance.getBalance());
                        gBalance.addBalance((1)*transferringAmount);
                        System.out.println("New Gift Card Balance:" + gBalance.getBalance());
                    } else {
                        System.out.println("How much would you like to transfer? Gift Card --> Customer Balance");
                        double transferringAmount = scanner.nextDouble();
                        if (transferringAmount > gBalance.getBalance()) {
                            System.err.println("Invalid Value: " + transferringAmount);
                            System.exit(0);
                        }
                        System.out.println("Total Balance:" + totalBalance);
                        cBalance.addBalance((1)*transferringAmount);
                        System.out.println("New Customer Balance:" + cBalance.getBalance());
                        gBalance.addBalance((-1)*transferringAmount);
                        System.out.println("New Gift Card Balance:" + gBalance.getBalance());

                    }
                    break;
                case 4://add balance
                    CustomerBalance customerBalance = findCustomerBalance(customer.getId());
                    GiftCardBalance giftCardBalance = findGiftCardBalance(customer.getId());
                    System.out.println("Which Account would you like to add?");
                    System.out.println(
                        "Type 1 for Customer Balance:" + customerBalance.getBalance());
                    System.out.println(
                        "Type 2 for Gift Card Balance:" + giftCardBalance.getBalance());
                    int balanceAccountSelection = scanner.nextInt();
                    System.out.println("How much you would like to add?");
                    double additionalAmount = scanner.nextInt();

                    switch (balanceAccountSelection) {
                        case 1:
                            customerBalance.addBalance(additionalAmount);
                            System.out.println(
                                "New Customer Balance:" + customerBalance.getBalance());
                            break;
                        case 2:
                            giftCardBalance.addBalance(additionalAmount);
                            System.out.println(
                                "New Gift Card Balance:" + giftCardBalance.getBalance());
                            break;
                    }
                    break;
                case 5://place an order
                    Map<Product, Integer> map = new HashMap<>();
                    cart.setProductMap(map);
                    while (true) {
                        System.out.println(
                            "Which product you want to add to your cart. For exit product selection Type : exit");
                        for (Product product : StaticConstants.PRODUCT_LIST) {
                            try {
                                System.out.println(
                                        "product name: "+ product.getName()+ "\n"+"\n"+
                                         "id: " + product.getId()+ "\n" + "\n"+
                                         "price: " + product.getPrice() +"\n"+ "\n"+
                                         "product category: " + product.getCategoryName() + "\n"+ "\n"+
                                         "stock: " + product.getRemainingStock() + "\n"+ "\n"+
                                         "product delivery due: " + product.getDeliveryDueDate()+ "\n");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                                ;
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

                        System.out.println(
                            "Do you want to add more product. Type Y for adding more, N for exit");
                        String decision = scanner.next();
                        if (!decision.equals("Y")) {
                            break;
                        }
                    }

                    System.out.println(
                        "seems there are discount options. Do you want to see and apply to your cart if it is applicable. For no discount type no");
                    for (Discount discount : DISCOUNT_LIST) {
                        System.out.println("discount id " + discount.getId() + " discount name: "
                            + discount.getName());
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
                            System.out.println("product name: " + product.getName() + " count: "
                                + cart.getProductMap().get(product));
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
                    while (true) {
                        printPhoneNumberMenu();
                        int userChoice = scanner.nextInt();
                        if (userChoice == 1) {
                            System.out.println("Write the phone number to add (only digits)");
                            customer.getPhoneNumbers().add(scanner.nextLong());
                            //continue;

                        } else if (userChoice == 2) {
                            //todo @Glenio your ticket for editing an existing number
                            boolean isNum = true;
                            int editNumberId = 0;
                            scanner.nextLine();
                            while (isNum) {

                                System.out.println("Select phone number that you want to edit, use id number:\n");

                                listPhoneNumbers();

                                String input = scanner.nextLine();
                                if (!input.matches("^[0-9]+$")) {

                                    System.err.println("*********** Wrong Input, please try again *************\n");

                                    continue;

                                } else {

                                    if (Integer.parseInt(input) > (customer.getPhoneNumbers().size() - 1)) {
                                        System.out.println("Id invalid, please try again:\n");
                                        continue;
                                    }
                                }

                                while (true) {
                                    System.out.println("Enter new phone number");
                                    if (!scanner.hasNextInt()) {
                                        scanner.nextLine();
                                        System.err.println(
                                            "*********** Wrong Input, please try again *************\n");
                                        continue;
                                    } else {
                                        Long newPhoneNumber = scanner.nextLong();
                                        customer.getPhoneNumbers()
                                            .set(editNumberId, newPhoneNumber);

                                        isNum = false;
                                        break;

                                    }
                                }
                            }

                            System.out.println("Phone " + editNumberId + " has been updated to :" + customer.getPhoneNumbers().get(editNumberId));

                            //continue;

                        } else if (userChoice == 3) {
                            //todo for deleting a phone number
                            // continue;
                        } else if (userChoice == 4) {
                            break;
                        }
                    }
                    break;
                case 10:
                    transferGiftCard(customer.getId());
                    break;
                case 11:
                    System.exit(1);
                    break;

            }
        }
    }

    private static void transferGiftCard (UUID id){
        GiftCardBalance gBalance = findGiftCardBalance(id);
        System.out.println("Gift Card Balance:" + gBalance.getBalance());

        if (gBalance.getBalance() == 0) {
            System.out.println("Sorry. You don`t have any balance in your account. ");
            System.exit(0); // how to go back to options menu?
        }
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the amount to be transferred");
        double transferAmount = scanner.nextDouble();

        while (transferAmount > gBalance.getBalance()) {
            System.out.println(
                "You don`t have enough balance. Please enter less than your gift card balance:"
                    + gBalance.getBalance());
            System.out.println("Please enter the amount to be transferred");
            transferAmount = scanner.nextDouble();

        }

        System.out.println(StaticConstants.CUSTOMER_LIST.stream()
            .collect(Collectors.toMap(Customer::getUserName, Customer::getId)));

        System.out.println("Please enter the User ID of the target user");
        UUID targetId = UUID.fromString(scanner.next());

        System.out.println(targetId);

        addGiftCardBalance(targetId, transferAmount);

        gBalance.setBalance(gBalance.getBalance() - transferAmount);
        System.out.println("Your new gift card balance:" + gBalance.getBalance());
    }

    private static void addGiftCardBalance (UUID targetId,double transferAmount){

        if (GIFT_CARD_BALANCE_LIST.stream().filter(p -> p.getCustomerId().equals(targetId))
            .count() == 1) {
            GiftCardBalance targetBalance = (GiftCardBalance) GIFT_CARD_BALANCE_LIST.stream()
                .filter(p -> p.getCustomerId().equals(targetId)).findFirst().get();
            targetBalance.setBalance(targetBalance.getBalance() + transferAmount);
        } else {
            GIFT_CARD_BALANCE_LIST.add(new GiftCardBalance(targetId, transferAmount));
        }
    }

    private static void printPhoneNumberMenu () {

        listPhoneNumbers();
        System.out.println("1 for Add new phone number");
        System.out.println("2 for Edit a phone number");
        System.out.println("3 for Delete a phone number");
        System.out.println("4 for return to main menu");
    }

    private static void listPhoneNumbers () {
        if (customer.getPhoneNumbers() == null || customer.getPhoneNumbers().isEmpty()) {
            System.out.println("There is no phone related to your account");
        } else {
            System.out.println("Phone numbers:");
            for (int i = 0; i < customer.getPhoneNumbers().size(); i++) {
                System.out.println("(id:" + i + ")   " + customer.getPhoneNumbers().get(i));
            }
        }
    }

    private static void createNewCustomer() {

        Customer newCustomer = new Customer(UUID.randomUUID());

        System.out.println("Please enter the user name:");

        newCustomer.setUserName(new Scanner(System.in).nextLine());

        System.out.println("Please enter your email address:");

        newCustomer.setEmail(new Scanner(System.in).nextLine());

        CUSTOMER_LIST.add(newCustomer);
    }

    private static Discount findDiscountById(String discountId) throws Exception {
        for (Discount discount : DISCOUNT_LIST) {
            if (discount.getId().toString().equals(discountId)) {
                return discount;
            }
        }
        throw new Exception("Discount couldn't applied because couldn't found");
    }

    private static void updateProductStock (Map < Product, Integer > map){
        for (Product product : map.keySet()) {
            product.setRemainingStock(product.getRemainingStock() - map.get(product));
        }
    }

    private static void printOrdersByCustomerId (UUID customerId){
        for (Order order : ORDER_LIST) {
            if (order.getCustomerId().toString().equals(customerId.toString())) {
                System.out.println(
                    "Order status: " + order.getOrderStatus() + " order amount "
                        + order.getPaidAmount() + " order date " + order.getOrderDate());
            }
        }
    }

    private static void printAddressByCustomerId (Customer customer){
        for (Address address : customer.getAddress()) {
            System.out.println(" Street Name: " + address.getStreetName() +
                " Street Number: " + address.getStreetNumber() + "ZipCode:  "
                + address.getZipCode() + " State: " + address.getState());
        }
    }

    private static boolean putItemToCartIfStockAvailable (Cart cart, Product product){

        System.out.println("Please provide product count:");
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();

        Integer cartCount = cart.getProductMap().get(product);

        if (cartCount != null && product.getRemainingStock() > cartCount + count) {
            cart.getProductMap().put(product, cartCount + count);
            return true;

        } else if (product.getRemainingStock() >= count) {
            cart.getProductMap().put(product, count);
            return true;
        }
        return false;
    }

    private static Product findProductById(String productId) throws Exception {
        for (Product product : StaticConstants.PRODUCT_LIST) {
            if (product.getId().toString().equals(productId)) {
                return product;
            }
        }
        throw new Exception("Product not found");
    }

    private static CustomerBalance findCustomerBalance(UUID customerId) {
        for (Balance customerBalance : StaticConstants.CUSTOMER_BALANCE_LIST) {
            if (customerBalance.getCustomerId().toString().equals(customerId.toString())) {
                return (CustomerBalance) customerBalance;
            }
        }

        CustomerBalance customerBalance = new CustomerBalance(customerId, 0d);
        StaticConstants.CUSTOMER_BALANCE_LIST.add(customerBalance);

        return customerBalance;
    }

    private static GiftCardBalance findGiftCardBalance(UUID customerId) {
        for (Balance giftCarBalance : StaticConstants.GIFT_CARD_BALANCE_LIST) {
            if (giftCarBalance.getCustomerId().toString().equals(customerId.toString())) {
                return (GiftCardBalance) giftCarBalance;
            }
        }

        GiftCardBalance giftCarBalance = new GiftCardBalance(customerId, 0d);
        StaticConstants.GIFT_CARD_BALANCE_LIST.add(giftCarBalance);

        return giftCarBalance;
    }

    private static String[] prepareMenuOptions () {
        return new String[]{"List Categories", "List Products", "List Discount",
            "See Balance", "Add Balance",
            "Place an order", "See Cart", "See order details", "See your address",
            "Phone Numbers", "Transfer Gift Card", "Close App"};
    }

}