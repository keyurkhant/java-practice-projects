// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import com.sun.media.sound.ModelWavetable;

import java.util.HashMap;
import java.util.Map;


class Solution {
    public int solution(String[] A, String S) {
        int count = 0;
        Map<Character, Integer> map = new HashMap();
        for (char c : S.toCharArray()) {
            map.put(c, map.get(c) == null ? 1 : map.get(c) + 1);
        }
        for (String elem : A) {
            Map<Character, Integer> tempMap = new HashMap<>();
            tempMap.putAll(map);
            boolean flag = true;
            for(char c: elem.toCharArray()) {
                if (tempMap.get(c) == null || tempMap.get(c) <= 0) {
                    flag = false;
                    break;
                }
                tempMap.put(c, tempMap.get(c) - 1);
            }
            if (flag) count++;
        }
        return count;
    }

    public static void main(String[] arg) {
        Solution obj = new Solution();
        String[] A = {"az", "azz", "zza", "zzz", "zzzz"};
        String S = "azzz";
        System.out.println(obj.solution(A, S));
    }
}