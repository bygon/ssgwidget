package com.util;

import java.math.BigDecimal;
import java.util.Hashtable;

public class ATools {
	
	public ATools()
    {
    }
	
	public static String moneyFmt(String money)
    {
        if(money == null)
            return "";
        else
            return num2fmtStr(money);
    }
	
	public static void hashnum2fmtStr(String itms, Hashtable h)
	        throws Exception
	    {
	        String outs[] = str2arr(itms);
	        for(int i = 0; i < outs.length; i++)
	        {
	            String strarr[] = (String[])h.get(outs[i]);
	            for(int j = 0; j < strarr.length; j++)
	                strarr[j] = num2fmtStr(strarr[j]);

	            h.put(outs[i], strarr);
	        }

	    }
	
	public static String[] str2arr(String arr)
    {
        if(arr == null)
            return null;
        if(arr.trim().length() == 0)
            return null;
        int i = 0;
        int cnt = 0;
        for(; i < arr.length(); i++)
            if(',' == arr.charAt(i))
                cnt++;

        String xx[] = new String[cnt + 1];
        for(i = 0; i < xx.length; i++)
            xx[i] = "";

        i = 0;
        cnt = 0;
        for(; i < arr.length(); i++)
            if(arr.charAt(i) != ' ' && arr.charAt(i) != '\t' && arr.charAt(i) != '\n')
                if(arr.charAt(i) == ',')
                    cnt++;
                else
                    xx[cnt] = xx[cnt] + arr.charAt(i);

        return xx;
    }
	
	public static String num2fmtStr(String sval)
    {
        String ret;
        String val = sval;
        if(sval.substring(0, 1).equals("-"))
            val = sval.substring(1);
        BigDecimal chk_digit = new BigDecimal(val);
        int point = val.indexOf('.');
        if(point == -1)
            point = val.length();
        ret = "";
        int cnt = 1;
        for(int i = val.length() - 1; i >= 0; i--)
            if(point <= i)
            {
                ret = val.charAt(i) + ret;
            } else
            {
                if(cnt == 4)
                {
                    ret = ',' + ret;
                    cnt = 1;
                }
                cnt++;
                ret = val.charAt(i) + ret;
            }

        return (sval.substring(0, 1).equals("-") ? "-" : "") + ret;       
        
    }
}
