package com.mean.csts.client.util;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @program: CampusSecondhandTradingSystem
 * @description: utility
 * @author: MeanFan
 * @create: 2018-07-22 17:42
 **/
public class Util {
    public static byte[] image2byte(String path){
        byte[] data;
        FileImageInputStream input;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[3145728];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        }
        catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        return data;
    }

}
