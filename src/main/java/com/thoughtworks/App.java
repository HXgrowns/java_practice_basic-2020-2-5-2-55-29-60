package com.thoughtworks;

import java.util.HashMap;
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
    Map<String, Integer> map = new LinkedHashMap<>();
    map = parseParameter(selectedItems);
    String[] itemIds = getItemIds();
    String[] itemNames = getItemNames();
    double[] itemPrices = getItemPrices();
    double total = 0.0;
    double discountFull = 0.0;
    double discountHalf = 0.0;
    String result = "============= 订餐明细 =============\n";
    for (String key : map.keySet()) {
      for (int i = 0; i < itemIds.length; i++) {
        if(key.equals(itemIds[i])) {
          Integer value = map.get(key);
          total += calculateEverMoney(value, itemPrices[i]);
          discountHalf += discountHalfReduce(key, value, itemPrices[i]);
          result += printSection1(itemNames[i], value, (int)calculateEverMoney(value, itemPrices[i]));
          break;
        }
      }
    }
    discountFull = discountFullReduce(total);

    if (discountFull > discountHalf) {
      result += printSection2("指定菜品半价(黄焖鸡，凉皮)", (int)(total - discountHalf));
      result += printSection3((int)discountHalf);
    } else if (discountFull < discountHalf) {
      result += printSection2("满30减6元", (int)(total - discountFull));
      result += printSection3((int)discountFull);
    } else {
      result += printSection3((int)total);
    }
    return result;
  }

  /**
   * 解析参数，将菜品ID当做key，菜品数量当做value存入map
   * @param selectedItems
   * @return
   */
  public static Map<String, Integer> parseParameter(String selectedItems) {
    String[] parameters = selectedItems.split(",");

    Map<String, Integer> map = new LinkedHashMap<>();
    for (String parameter : parameters) {
      String[] items = parameter.split(" x ");
      String key = items[0];
      Integer value = Integer.valueOf(items[1]);
      map.put(key,value);
    }
    return map;
  }

  /**
   * 计算每个物品的价钱
   * @param itemCount
   * @param itemPrice
   * @return
   */
  public static double calculateEverMoney(Integer itemCount, double itemPrice) {
    return itemCount * itemPrice;
  }

  /**
   * 计算满30减6元的优惠价格
   * @param itemsMoney
   * @return
   */
  public static double discountFullReduce(double itemsMoney) {
    return itemsMoney - Math.floor(itemsMoney / 30) * 6;
  }

  /**
   * 计算指定商品半价的优惠价格
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
   * @param itemName
   * @param itemCount
   * @param itemMoney
   * @return
   */
  public static String printSection1(String itemName, Integer itemCount, int itemMoney) {
    return itemName + " x " + itemCount + " = " + itemMoney + "元\n";
  }

  /**
   * 拼接优惠方式字符串
   * @param discountMathod
   * @param reduceMoney
   * @return
   */
  public static String printSection2(String discountMathod, int reduceMoney) {
    return "-----------------------------------\n"
            + "使用优惠:\n"
            + discountMathod + "，省" + reduceMoney + "元\n";
  }

  /**
   * 拼接总计字符串
   * @param discount
   * @return
   */
  public static String printSection3(int discount) {
    return "-----------------------------------\n"
            + "总计：" + discount +"元\n"
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
}
