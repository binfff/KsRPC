package com.rpc.loadbalance;

import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * LoadBalance
 *
 * @author gjh
 * @version 1.0
 * @Date 2023-07-22 14:19
 * @description TODO
 */
public class LoadBalance {

    public static URL random(List<URL> urls){
        if (urls == null||urls.size()==0){
            return null;
        }
        Random random = new Random();
        int i = random.nextInt(urls.size());
        return urls.get(i);
    }

}
