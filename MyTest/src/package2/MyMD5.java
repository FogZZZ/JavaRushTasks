package package2;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

public class MyMD5 {
    public static void main(String... args) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(new String("test string"));
        oos.flush();
        //bos.write("md5".getBytes("ASCII"));
        System.out.println(compareMD5(bos, "5a47d12a2e3f9fecf2d9ba1fd98152eb")); //true
        //System.out.println(compareMD5(bos,"1BC29B36F623BA82AAF6724FD3B16718".toLowerCase()));

    }

    public static boolean compareMD5(ByteArrayOutputStream byteArrayOutputStream, String md5) throws Exception {
        int[] s = new int[]{
                7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,
                5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,
                4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,
                6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21};
        int[] K = new int[64];
        for (int i = 0; i < 64; i++) {
            K[i] = (int)(long)( Math.abs(Math.sin(i + 1)) * (1L << 32));
            //System.out.println(i + " " + K[i]);
        }

        int a0 = 0x67452301;
        int b0 = 0xefcdab89;
        int c0 = 0x98badcfe;
        int d0 = 0x10325476;

        byte[] b = byteArrayOutputStream.toByteArray();

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String str = String.format("%8s" , Integer.toBinaryString(Byte.toUnsignedInt(b[i])));
            message.append(str.replace(' ', '0'), str.length()-8,  str.length());
        }

        BigInteger origLength = new BigInteger(String.valueOf(message.length()));
        message.append(1);
        while (message.length() % 512 < 448){
            message.append(0);
        }

        origLength = origLength.mod(new BigInteger("2").pow(64));
        String origLengthInBits = String.format("%64s", origLength.toString(2)).replace(' ', '0');

        message.append(origLengthInBits,  56, 64);
        message.append(origLengthInBits,  48, 56);
        message.append(origLengthInBits,  40, 48);
        message.append(origLengthInBits,  32, 40);
        message.append(origLengthInBits,  24, 32);
        message.append(origLengthInBits,  16, 24);
        message.append(origLengthInBits,  8, 16);
        message.append(origLengthInBits,  0, 8);

        String[] M = new String[16];
        for (int i = 0; i < 16;) {
            M[i] = message.substring(i*32, (++i)*32);
        }

        int A = a0;
        int B = b0;
        int C = c0;
        int D = d0;

        for (int i = 0; i < 64; i++) {
            int F = 0, k = 0;
            if (i <=15) {
                F = (B & C) | ((~B) & D);
                k = i;
            }
            else if (i <=31) {
                F = (D & B) | ((~D) & C);
                k = (5*i + 1) % 16;
            }
            else if (i <=47) {
                F = B ^ C ^ D;
                k = (3*i + 5) % 16;
            } else {
                F = C ^ (B | (~D));
                k =(7*i) % 16;
            }

            String Mk_littleEndian = new StringBuilder().append(M[k], 24, 32).append(M[k], 16, 24).append(M[k], 8, 16).append(M[k], 0, 8).toString();
            F = (int) ((Integer.toUnsignedLong(F) + Integer.toUnsignedLong(A) + Integer.toUnsignedLong(K[i]) +
                    Integer.toUnsignedLong(Integer.parseUnsignedInt(Mk_littleEndian, 2))) % 4294967296L);
            A = D;
            D = C;
            C = B;
            B = (int) ((Integer.toUnsignedLong(B) + Integer.toUnsignedLong((F << s[i]) | (F >>> (32-s[i])))) % 4294967296L);
        }

        a0 = (int) ((Integer.toUnsignedLong(a0) + Integer.toUnsignedLong(A)) % 4294967296L);
        b0 = (int) ((Integer.toUnsignedLong(b0) + Integer.toUnsignedLong(B)) % 4294967296L);
        c0 = (int) ((Integer.toUnsignedLong(c0) + Integer.toUnsignedLong(C)) % 4294967296L);
        d0 = (int) ((Integer.toUnsignedLong(d0) + Integer.toUnsignedLong(D)) % 4294967296L);

        StringBuilder digest = new StringBuilder();
        digest.append(String.format("%32s", Integer.toBinaryString(a0)).replace(' ', '0'));
        digest.append(String.format("%32s", Integer.toBinaryString(b0)).replace(' ', '0'));
        digest.append(String.format("%32s", Integer.toBinaryString(c0)).replace(' ', '0'));
        digest.append(String.format("%32s", Integer.toBinaryString(d0)).replace(' ', '0'));
        return toHexString(digest.toString()).equals(md5);
    }

    private static String toHexString(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 4; j > 0; j--) {
                String sub = input.substring(i*32+(j-1)*8, i*32+j*8);
                sb.append(Integer.toHexString(Integer.valueOf(sub, 2)));
                //sb.append(" ");
            }
        }
        return sb.toString();
    }
}
