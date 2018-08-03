class SubstringInserter {
    static boolean insertSubstrings(String string, Tree234<String> tree234){
        return !string.trim().isEmpty() && insertSubstrings(string.toLowerCase(), string.length(),1, tree234);
    }

    private static boolean insertSubstrings(String string, int stringLength, int substringSize, Tree234<String> tree234){
        if(substringSize == stringLength){
            tree234.add(string);
            return true;
        }else{
            for(int i = 0, j = substringSize + 1; j <= stringLength; ++i, ++j){
                tree234.add(string.substring(i,j));
            }
            return insertSubstrings(string, stringLength, ++substringSize, tree234);
        }
    }
}