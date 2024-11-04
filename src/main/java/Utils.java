public interface Utils {

    static String queryMaker(String text) {
        String[] specialCharacters = {".", ",", "!", "?", "+", "-", "*", "/", "<", ">", "=", "&", "|", "%", "~", "'", "\"", " "};

        for (String s : specialCharacters) {
            String hexString = toHexString(s);
            text = text.replace(s, hexString);
        }

        return text;
    }

    private static String toHexString(String s) {
        StringBuilder sb = new StringBuilder("%");
        for (char c : s.toCharArray()) {
            sb.append(String.format("%02x", (int) c));
        }

        return sb.toString().toUpperCase();
    }
}
