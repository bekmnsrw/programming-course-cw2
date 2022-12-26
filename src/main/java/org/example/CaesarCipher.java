package org.example;

public class CaesarCipher {
    public String cipher(String message, int offset) {
        StringBuilder result = new StringBuilder();

        for (char character : message.toCharArray()) {
            if (character != ' ') {
                int originalAlphabetPosition = character - 'a';
                int newAlphabetPosition = (originalAlphabetPosition + offset) % 26;
                char newCharacter = (char) ('a' + newAlphabetPosition);
                result.append(newCharacter);
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public String decipher(String message, int offset) {
        return cipher(message, 26 - (offset % 26));
    }

    public static void main(String[] args) {
        CaesarCipher caesarCipher = new CaesarCipher();
//        System.out.println(caesarCipher.cipher("hello server", 10));
//        System.out.println(caesarCipher.decipher("mjqqt btwqi", 5));
        String value = "rovvy cobfob 120";
        System.out.println(value.substring(value.lastIndexOf(" " + 1), value.length()));
    }
}
