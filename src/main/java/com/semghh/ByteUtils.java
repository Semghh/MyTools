package com.semghh;

public class ByteUtils {



    public static boolean copyByte(byte[] source,byte[] dest,int sStart,int sLen){
        if (sLen>dest.length)return false;
        int end = sStart+sLen;
        int j = 0;
        for (int i = sStart; i < end; i++,j++) {
            dest[j] = source[i];
        }
        return true;
    }

    public static ByteCopyUtil copy(){
        return new ByteCopyUtil();
    }


    public static class ByteCopyUtil {


        private ByteCopyUtil() {
        }

        private byte[] bytes = new byte[1024];

        private int pos = 0;

        public ByteCopyUtil append(byte[] source,int start ,int length){

            if (bytes.length<pos+length){
                byte[] arr = new byte[bytes.length*2];
                ByteUtils.copyByte(bytes,arr,0,pos-1);
                this.bytes = arr;
            }
            for (int i = 0; i < length; i++) {
                bytes[pos++] = source[start+i];
            }
            return this;
        }

        public ByteCopyUtil append(byte[] source){
            return append(source,0,source.length);
        }



        public int getPos() {
            return pos;
        }

        public byte[] get() {
            return bytes;
        }

    }

}
