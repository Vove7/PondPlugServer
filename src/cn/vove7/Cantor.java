package cn.vove7;

import java.util.ArrayList;
import java.util.Collections;
/**
 * Created by Vove on 2017/6/14.
 * Cantor展开，逆展开
 */
public class Cantor {
    private int fac[] = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880};

    public static void main(String[] args){
        Cantor cantor= new Cantor();
        for(int i=1;i<=4;i++){
            for(int j=1;j<5;j++){
                for(int k=1;k<=4;k++){
                    for(int l=1;l<5;l++){
                        int c=cantor.cantor(new int[]{i,j,k,l});
                        System.out.println(i+""+j+""+k+""+l+"       "+c);
                    }
                }
            }
        }
        Integer a[]=new Integer[]{1,2,3,4};
        ArrayList<Integer> aa= new ArrayList<>();
        Collections.addAll(aa, a);
        System.out.println(aa.toString());

    }

    public int cantor(int[] a) {
        int k=a.length;
        int i, j, tmp, num = 0;
        for (i = 0; i < k; i++) {
            tmp = 0;
            for (j = i + 1; j < k; j++)
                if (a[j] < a[i]) tmp++;
            num += fac[k - i - 1] * tmp;
        }
        return num;
    }

    public int[] uncantor(int x, int k) {
        int res[] = new int[9];
        int i, j, l, t;
        boolean h[] = new boolean[12];
        for (i = 1; i <= k; i++) {
            t = x / fac[k - i];
            x -= t * fac[k - i];
            for (j = 1, l = 0; l <= t; j++)
                if (!h[j]) l++;
            j--;
            h[j] = true;
            res[i - 1] = j;
        }
        return res;
    }
}
