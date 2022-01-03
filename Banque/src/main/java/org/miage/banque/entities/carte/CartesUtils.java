package org.miage.banque.entities.carte;

public class CartesUtils {

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++)
        {
            sb.append(String.valueOf((int)(Math.random() * 10)));
        }
        return sb.toString();
    }
}
