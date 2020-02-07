package com.thoughtworks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("请点餐（菜品Id x 数量，用逗号隔开）：");
        String selectedItems = scan.nextLine();
        String summary = bestCharge(selectedItems);
        System.out.println(summary);
    }

    /**
     * 接收用户选择的菜品和数量，返回计算后的汇总信息
     *
     * @param selectedItems 选择的菜品信息
     */
    public static String bestCharge(String selectedItems) {
        Map<String, Integer> itemId2count = parseParameter(selectedItems);
        String[] itemIds = getItemIds();
        String[] itemNames = getItemNames();
        double[] itemPrices = getItemPrices();
        double totalSpend = 0.0;
        double discountHalf = 0.0;
        String result = "============= 订餐明细 =============\n";
        for (String itemId : itemId2count.keySet()) {
            Integer itemCount = itemId2count.get(itemId);
            for (int i = 0; i < itemIds.length; i++) {
                if (itemId.equals(itemIds[i])) {
                    totalSpend += calculatEverySpend(itemCount, itemPrices[i]);
                    discountHalf += discountHalfReduce(itemId, itemCount, itemPrices[i]);
                    result += printSelectedItems(itemNames[i], itemCount, calculatEverySpend(itemCount, itemPrices[i]));
                    break;
                }
            }
        }
        double discountFull = discountFullReduce(totalSpend);

        if (discountFull == discountHalf && discountFull == totalSpend) {
          result += printTotalSpend(totalSpend);
        } else if (discountFull > discountHalf) {
            result += printDiscountMethod("指定菜品半价(黄焖鸡，凉皮)", (totalSpend - discountHalf));
            result += printTotalSpend(discountHalf);
        } else {
            result += printDiscountMethod("满30减6元", (totalSpend - discountFull));
            result += printTotalSpend(discountFull);
        }
        return result;
    }

    /**
     * 解析参数
     *
     * @param selectedItems
     * @return
     */
    public static Map<String, Integer> parseParameter(String selectedItems) {
        String[] parameters = selectedItems.split(",");
        Map<String, Integer> map = new LinkedHashMap<>();
        for (String parameter : parameters) {
            String[] items = parameter.split(" x ");
            String itemId = items[0];
            Integer itemCount = Integer.valueOf(items[1]);
            map.put(itemId, itemCount);
        }
        return map;
    }

    /**
     * 计算每个物品的价钱
     *
     * @param itemCount
     * @param itemPrice
     * @return
     */
    public static double calculatEverySpend(Integer itemCount, double itemPrice) {
        return itemCount * itemPrice;
    }

    /**
     * 计算满30减6元的优惠价格
     *
     * @param itemsMoney
     * @return
     */
    public static double discountFullReduce(double itemsMoney) {
        return itemsMoney - Math.floor(itemsMoney / 30) * 6;
    }

    /**
     * 计算指定商品半价的优惠价格
     *
     * @param itemId
     * @param itemCount
     * @param itemPrice
     * @return
     */
    public static double discountHalfReduce(String itemId, Integer itemCount, double itemPrice) {
        String[] halfPrices = getHalfPriceIds();
        for (int i = 0; i < halfPrices.length; i++) {
            if (itemId.equals(halfPrices[i])) {
                return itemCount * itemPrice / 2;
            }
        }
        return itemCount * itemPrice;
    }

    /**
     * 拼接购买商品列表字符串
     *
     * @param itemName
     * @param itemCount
     * @param itemMoney
     * @return
     */
    public static String printSelectedItems(String itemName, Integer itemCount, double itemMoney) {
        return itemName + " x " + itemCount + " = " + doubleTrans(itemMoney) + "元\n";
    }

    /**
     * 拼接优惠方式字符串
     *
     * @param discountMathod
     * @param reduceMoney
     * @return
     */
    public static String printDiscountMethod(String discountMathod, double reduceMoney) {
        return "-----------------------------------\n"
                + "使用优惠:\n"
                + discountMathod + "，省" + doubleTrans(reduceMoney) + "元\n";
    }

    /**
     * 拼接总计字符串
     *
     * @param discount
     * @return
     */
    public static String printTotalSpend(double discount) {
        return "-----------------------------------\n"
                + "总计：" + doubleTrans(discount) + "元\n"
                + "===================================";
    }

    /**
     * 获取每个菜品依次的编号
     */
    public static String[] getItemIds() {
        return new String[]{"ITEM0001", "ITEM0013", "ITEM0022", "ITEM0030"};
    }

    /**
     * 获取每个菜品依次的名称
     */
    public static String[] getItemNames() {
        return new String[]{"黄焖鸡", "肉夹馍", "凉皮", "冰粉"};
    }

    /**
     * 获取每个菜品依次的价格
     */
    public static double[] getItemPrices() {
        return new double[]{18.00, 6.00, 8.00, 2.00};
    }

    /**
     * 获取半价菜品的编号
     */
    public static String[] getHalfPriceIds() {
        return new String[]{"ITEM0001", "ITEM0022"};
    }

    /**
     * double去掉.0
     *
     * @param d
     * @return
     */
    public static String doubleTrans(double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }
}
