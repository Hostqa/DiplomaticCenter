package qa.dcsdr.diplomaticclub.Tools;

import android.util.Base64;

/**
 * Created by Tamim on 6/13/2015.
 * This is for decrypting base64 content.
 */
public class ContentDecrypter {

    public ContentDecrypter() {}

    public String decrypt(String encrypted) {
        byte[] d = Base64.decode(encrypted, Base64.DEFAULT);
        return new String(d);
    }

}

