package com.show.blue;

import android.graphics.BitmapFactory;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

/**
 * Created by apple on 16/9/15.
 */
public class ApplicationTest {

    private void write(String path, String config, String channelid) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            fos = new FileOutputStream(file);
            int start = config.indexOf("<channel_id>");
            int end = config.lastIndexOf("</channel_id>");
            String temps = config.substring(start, end);
            config = config.replace(temps, "<channel_id>" + channelid);
            int sdkstart = config.indexOf("<sdk_channel_id>");
            int sdkend = config.lastIndexOf("</sdk_channel_id>");
            temps = config.substring(sdkstart, sdkend);
            config = config.replace(temps, "<sdk_channel_id>" + channelid);
            byte[] temp = config.getBytes();
            fos.write(temp);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String read(String path) {
        FileInputStream fileInputStream = null;
        BufferedReader br = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            fileInputStream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fileInputStream,
                    "UTF-8"));
            String str = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            String string = stringBuilder.toString();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return null;
    }

    public static boolean checkChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();
        return result;
    }

    @Test
    public void testSubstract() throws Exception {
//        unzip("cd /Users/apple/Downloads/");


//        Iterator i = mock(Iterator.class);
//        when(i.next()).thenReturn("Hello").thenReturn("World");
//        //act
//        String result = i.next() + " " + i.next();
//        //verify
//        verify(i, times(2)).next();
//        //assert
//        assertEquals("Hello World", result);
////        List<Integer> listtemp = listitme.subList(start, listitme.size());
////        System.out.print("====\n");
////        for (Integer integer : listtemp) {
////            System.out.print("====" + integer);
////        }
////        System.out.print(start + "   ====start  \n   ");
////        int[] result = sortBubble(ssf);
////        for (int s : result) {
////            System.out.print(s + " ");
////        }
//        Collections.addAll()
//        TreeNode root = main();

//        preOrderTraverse(root);
//        inOrderTraverse(root);
//        endOrderTraverse(root);
//        iterativePreorder(root);
        int[] args = new int[]{1, 2, 3, 4, 5, 78, 2, 3, 4, 0, 1, 2, 11, 23};
        insertSort(args);
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }

    public void insertSort(int[] arr) {
        if (arr == null || arr.length == 0)
            return;

        for (int i = 1; i < arr.length; i++) { //假设第一个数位置时正确的；要往后移，必须要假设第一个。

            int j = i;
            int target = arr[i]; //待插入的

            //后移
            while (j > 0 && target < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }

            //插入
            arr[j] = target;
        }

    }

    void sorts(int[] args, int start, int end) {

        if (end - start > 1) {

        }


    }

    interface C {
        void show(A a);

        void show();
    }

    class A {
        void show(A a) {
            System.out.print("===a==A\n");
        }

        void show() {
            System.out.print("=====A\n");
        }
    }

    class B extends A implements C {
        public void show(A a) {
            super.show(a);
            System.out.print("===b==A\n");
        }

        public void show() {
            System.out.print("=======B");
        }

        void show(B a) {
            System.out.print("===b==B\n");
        }
    }

    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    public int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 非递归实现前序遍历
     */
    protected static void iterativePreorder(TreeNode p) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        if (p != null) {
            stack.push(p);
            while (!stack.empty()) {
                p = stack.pop();
                System.out.println("line =" + ": " + p.name);
                if (p.rightNode != null)
                    stack.push(p.rightNode);
                if (p.leftNode != null)  //为什么p.getLeft() 在后，getRight()在前应为while 循环第一句就是pop visit所以要把left放上，先访问。之中方法是即压即访问法。
                    stack.push(p.leftNode);
            }
        }
    }

    void inOrderTraverse(TreeNode node) {
        if (node == null)
            return;
        inOrderTraverse(node.leftNode);
        System.out.println("inOrderTraverse =" + ": " + node.name);
        inOrderTraverse(node.rightNode);
    }

    void endOrderTraverse(TreeNode node) {
        if (node == null) {
            return;
        }
        endOrderTraverse(node.leftNode);
        endOrderTraverse(node.rightNode);
        System.out.println("endOrderTraverse =" + ": " + node.name);


    }

    TreeNode main() {
        TreeNode root = new TreeNode("D");
        TreeNode anode = new TreeNode("A");
        TreeNode bnode = new TreeNode("B");
        TreeNode cnode = new TreeNode("C");
        TreeNode enode = new TreeNode("E");
        TreeNode fnode = new TreeNode("F");
        TreeNode gnode = new TreeNode("G");
        TreeNode hnode = new TreeNode("H");

        root.leftNode = anode;
        root.rightNode = cnode;

        anode.leftNode = bnode;
        bnode.leftNode = enode;

        cnode.leftNode = fnode;
        cnode.rightNode = gnode;
        fnode.rightNode = hnode;
        return root;
    }

    /**
     * 先序遍历
     *
     * @param node
     */
    void preOrderTraverse(TreeNode node) {
        if (node == null)
            return;
        System.out.println("preOrderTraverse =" + ": " + node.name);
        preOrderTraverse(node.leftNode);
        preOrderTraverse(node.rightNode);
    }

    class TreeNode {
        public TreeNode(String name) {
            this.name = name;
        }

        String name = "";
        TreeNode rightNode;
        TreeNode leftNode;
    }


    int bsearchWithoutRecursion(int[] array, int start, int end, int value) {
        while (end >= start) {
            int mid = (start + end) / 2;
            if (array[mid] > value) {
                end = mid - 1;
            } else if (array[mid] < value) {
                start = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

//    int bsearchWithoutRecursion(int array[], int low, int high, int target) {
//        while (low <= high) {
//            int mid = (low + high) / 2;
//            if (array[mid] > target)
//                high = mid - 1;
//            else if (array[mid] < target)
//                low = mid + 1;
//            else //find the target
//                return mid;
//        }
//        //the array does not contain the target
//        return -1;
//    }

    public int[] sortBubble(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                }
            }
        }
        return array;

    }

    public int[] sort(int[] array, int start, int end) {
        if (start >= end) {
            return array;
        }
        int baselocation = sortcheck(array, start, end);
        sort(array, start, baselocation);
        sort(array, baselocation + 1, end);
        return array;
    }

    public int sortcheck(int[] array, int start, int end) {
        int base = array[start];
        while (end > start) {
            while (start < end && array[end] >= base) {
                end--;
            }
            setvalue(array, start, end);
            while (start < end && array[start] <= base) {
                start++;
            }
            setvalue(array, start, end);
        }

        return start;

    }

    void setvalue(int[] array, int start, int end) {
        if (start == end) {
            return;
        }
        int temp = array[start];
        array[start] = array[end];
        array[end] = temp;
    }

    /**
     * @param int[] 未排序数组
     * @return int[] 排完序数组
     */

    public int[] sortSelect(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int miniPost = i;
            for (int m = i + 1; m < arr.length; m++) {
                if (arr[m] < arr[miniPost]) {
                    miniPost = m;
                }
            }

            if (arr[i] > arr[miniPost]) {
                int temp;
                temp = arr[i];
                arr[i] = arr[miniPost];
                arr[miniPost] = temp;
            }
        }
        return arr;
    }

    void unzipapk() {
        ZipFile zip = null;
        try {
            zip = new ZipFile(new File("/Users/apple/Downloads/Blue_1.1.apk"), "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<ZipEntry> enumeration = zip.getEntries();
        while (enumeration.hasMoreElements()) {
            org.apache.tools.zip.ZipEntry zipEntry = enumeration.nextElement();
            try {
                InputStream inputStream = zip.getInputStream(zipEntry);
                File file = new File("/Users/apple/Downloads/Blue/" + zipEntry.getName());
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);

                byte[] temp = new byte[10 * 1024];

                int i = 0;

                while ((i = inputStream.read(temp)) > 0) {

                    fos.write(temp, 0, i);

                }
                fos.flush();
                fos.close();
                inputStream.close();
            } catch (ZipException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if (zipEntry.getName().equals("assets/json/test.txt")) {
        }
//        try {
//            Runtime.getRuntime().exec("");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            fileToZip("/Users/apple/Downloads/Blue", "/Users/apple/Downloads", "Blue");
//            System.out.println("============");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    void zipfile(File sourceFiles, ZipOutputStream zos, String parentname) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        try {
            if (sourceFiles.isDirectory()) {
                for (File file : sourceFiles.listFiles()) {
                    zipfile(file, zos, parentname);
                }
            } else {
                if (sourceFiles.getName().startsWith(".")) {
                    return;
                }
                byte[] bufs = new byte[1024 * 10];
                //创建ZIP实体，并添加进压缩包
                String parent = sourceFiles.getParentFile().getPath();
                String det = "";
                int index = parent.indexOf(parentname);
                if (index < 0) {

                } else {
                    det = parent.substring(index + parentname.length());
                }
                ZipEntry zipEntry = new ZipEntry(det + "/" + sourceFiles.getName());
                zos.putNextEntry(zipEntry);
                //读取待压缩的文件并写进压缩包里
                fis = new FileInputStream(sourceFiles);
                bis = new BufferedInputStream(fis, 1024 * 10);
                int read = 0;
                while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                    zos.write(bufs, 0, read);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            //关闭流
            try {
                if (null != bis) bis.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     *
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath    :压缩后存放路径
     * @param fileName       :压缩后文件的名称
     * @return
     */
    public boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) throws FileNotFoundException {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);


        if (sourceFile.exists() == false) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
        } else {
            File zipFile = new File(zipFilePath + "/" + fileName + ".apk");
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
            for (File file : sourceFile.listFiles()) {
                zipfile(file, zos, fileName + "/");
            }
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
//    /**
//     * 解压apk
//     * @param apkPath
//     * @param outPath
//     */
//    public void unZipApk(String apkPath,String outPath){
//        File file = new File(apkPath);
//        if(!file.exists()){
//            System.out.println("not found "+apkPath);
//            return;
//        }
//        try {
//
//            File outputDir = new File(outPath);
//            outputDir.mkdir();
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
//            ArchiveInputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, bufferedInputStream);
//            ZipArchiveEntry zipArchiveEntry = null;
//            try {
//                while((zipArchiveEntry = (ZipArchiveEntry) inputStream.getNextEntry()) != null){
//
//                    if(zipArchiveEntry.isDirectory()){
//                        new File(outputDir,zipArchiveEntry.getName()).mkdirs();
//                    }else{
//                        File tempFile = new File(outputDir,zipArchiveEntry.getName());
//                        if(!tempFile.getParentFile().exists()){
//                            tempFile.getParentFile().mkdirs();
//                        }
//
//                        FileOutputStream fos = null;
//                        try {
//                            fos = new FileOutputStream(tempFile);
//                            //IOUtils.copy(inputStream, fos);
//                            byte[] b = new byte[2048];
//                            int len = 0;
//                            while((len = inputStream.read(b)) != -1){
//                                fos.write(b, 0, len);
//                            }
//                            fos.flush();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//
//                        }finally{
//                            if(fos != null){
//                                fos.close();
//                            }
//                        }
//
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }finally{
//                if(inputStream != null){
//                    inputStream.close();
//                }
//            }
//
//            System.out.println("unZipApk done !");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }


}
