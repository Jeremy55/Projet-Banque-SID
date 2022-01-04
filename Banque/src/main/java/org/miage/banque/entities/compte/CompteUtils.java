package org.miage.banque.entities.compte;

public class CompteUtils {

    public static String randomIban(String pays){
        StringBuilder sb = new StringBuilder();
        sb.append(pays.substring(0,2).toUpperCase());
        for(int i = 0; i < 30; i++)
        {
            sb.append(String.valueOf((int)(Math.random() * 10)));
        }
        return sb.toString();
    }
}
