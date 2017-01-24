//import android.app.ProgressDialog;
//
//import java.io.File;
//import java.io.FilterOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//import java.nio.charset.Charset;
//
//import org.apache.http.entity.mime.HttpMultipartMode;
//
//import org.apache.http.entity.mime.MultipartEntity;
//
//import static java.lang.System.out;
//
//
//public class CustomMultipartEntity extends MultipartEntity {
//
//    private final ProgressListener listener;
//
//    public CustomMultipartEntity(final ProgressListener listener) {
//
//        super();
//
//        this.listener = listener;
//
//    }
//
//    public CustomMultipartEntity(final HttpMultipartMode mode, final ProgressListener listener)     {
//
//        super(mode);
//
//        this.listener = listener;
//
//    }
//
//    public CustomMultipartEntity(HttpMultipartMode mode, final String boundary,
//
//                                 final Charset charset, final ProgressListener listener) {
//
//        super(mode, boundary, charset);
//
//        this.listener = listener;
//
//    }
//
//    @Override
//
//    public void writeTo(final OutputStream outstream) throws IOException {
//
//        super.writeTo(new CountingOutputStream(outstream, this.listener));
//
//    }
//
//    public  interface ProgressListener {
//
//        void transferred(long num);
//
//    }
//
//
//
//    public static class CountingOutputStream extends FilterOutputStream {
//
//        private final ProgressListener listener;
//
//        private long transferred;
//
//        public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
//
//            super(out);
//
//            this.listener = listener;
//
//            this.transferred = 0;
//
//        }
//
//        public void write(byte[] b, int off, int len) throws IOException {
//
//            out.write(b, off, len);
//
//            this.transferred += len;
//
//            this.listener.transferred(this.transferred);
//
//        }
//
//        public void write(int b) throws IOException {
//
//            out.write(b);
//
//            this.transferred++;
//
//            this.listener.transferred(this.transferred);
//
//        }
//
//    }
//
//}
//
//
//
//
//public  class HttpMultipartPost extends AsyncTask<HttpResponse, Integer, TypeUploadImage> {
//
//
//
//    ProgressDialog pd;
//
//
//
//    long totalSize;
//
//
//
//    @Override
//    protected void onPreExecute(){
//
//        pd= new ProgressDialog(this);
//
//        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//
//        pd.setMessage("Uploading Picture...");
//
//        pd.setCancelable(false);
//
//        pd.show();
//
//    }
//
//
//
//    @Override
//
//    protected TypeUploadImage doInBackground(HttpResponse... arg0) {
//
//        HttpClient httpClient = new DefaultHttpClient();
//
//        HttpContext httpContext = new BasicHttpContext();
//
//        HttpPost httpPost = new HttpPost("http://herpderp.com/UploadImage.php");
//
//
//
//        try{
//
//            CustomMultipartEntity multipartContent = new CustomMultipartEntity(
//
//                    new CustomMultipartEntity.ProgressListener() {
//
//
//
//                @Override
//
//                public void transferred(long num){
//
////                        publishProgress((int) ((num / (float) totalSize) * 100));
//
//                }
//
//            });
//
//
//
//            // We use FileBody to transfer an image
//
//            multipartContent.addPart("uploaded_file", newFileBody(
//
//                    new File(m_userSelectedImagePath)));
//
//            totalSize= multipartContent.getContentLength();
//
//
//
//            // Send it
//
//            httpPost.setEntity(multipartContent);
//
//            HttpResponse response = httpClient.execute(httpPost, httpContext);
//
//            String serverResponse = EntityUtils.toString(response.getEntity());
//
//
//
//            ResponseFactory rp = new ResponseFactory(serverResponse);
//
//            return(TypeImage) rp.getData();
//
//        }
//
//
//
//        catch(Exception e) {
//
//            out.println(e);
//
//        }
//
//        return null;
//
//    }
//
//
//
//    @Override
//    protected void  onProgressUpdate(Integer... progress){
//
//        pd.setProgress((int) (progress[0]));
//
//    }
//
//
//
//    @Override
//    protected void onPostExecute(TypeUploadImage ui) {
//
//        pd.dismiss();
//
//    }
//
//}