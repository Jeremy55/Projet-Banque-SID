package org.miage.banque.entities.carte;

public class CarteUtils {

    private static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++)
        {
            sb.append(String.valueOf((int)(Math.random() * 10)));
        }
        return sb.toString();
    }

    public static String generateNumeroCarte() {
        return randomString(16);
    }

    public static String generateCodeCarte(){
        return randomString(4);
    }

    public static String generateCryptogramme() {
        return randomString(3);
    }

}
