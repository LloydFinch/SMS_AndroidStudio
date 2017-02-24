//package moon.volley.network;
//
//import android.util.Log;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.NetworkResponse;
//import com.android.volley.ParseError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.toolbox.HttpHeaderParser;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
//import moon.volley.form.FormImage;
//
//
///**
// * Created by gyzhong on 15/3/1.
// */
//public class PostUploadRequest extends Request<String> {
//
//    /**
//     * 正确数据的时候回掉用
//     */
//    private ResponseListener mListener ;
//    /*请求 数据通过参数的形式传入*/
//    private List<FormImage> mListItem ;
//
//    private String BOUNDARY = "--------------520-13-14"; //数据分隔线
//    private String MULTIPART_FORM_DATA = "multipart/form-data";
//
//    public PostUploadRequest(String url, List<FormImage> listItem, ResponseListener listener) {
//        super(Method.POST, url, listener);
//        this.mListener = listener ;
//        setShouldCache(false);
//        mListItem = listItem ;
//        setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//
//    /**
//     * 这里开始解析数据
//     * @param response Response from the network
//     * @return
//     */
//    @Override
//    protected Response<String> parseNetworkResponse(NetworkResponse response) {
//        try {
//            String mString =
//                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            Log.v("zgy", "====mString===" + mString);
//
//            return Response.success(mString,
//                    HttpHeaderParser.parseCacheHeaders(response));
//        } catch (UnsupportedEncodingException e) {
//            return Response.error(new ParseError(e));
//        }
//    }
//
//    /**
//     * 回调正确的数据
//     * @param response The parsed response returned by
//     */
//    @Override
//    protected void deliverResponse(String response) {
//        mListener.onResponse(response);
//    }
//
//    @Override
//    public byte[] getBody() throws AuthFailureError {
//        if (mListItem == null||mListItem.size() == 0){
//            return super.getBody() ;
//        }
//        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
//        int N = mListItem.size() ;
//        FormImage formImage ;
//        for (int i = 0; i < N ;i++){
//            formImage = mListItem.get(i) ;
//            StringBuffer sb= new StringBuffer() ;
//            /*第一行*/
//            sb.append("--"+BOUNDARY);
//            sb.append("\r\n") ;
//            /*第二行*/
//            sb.append("Content-Disposition: form-data;");
//            sb.append(" name=\"");
//            sb.append(formImage.getName()) ;
//            sb.append("\"") ;
//            sb.append("; filename=\"") ;
//            sb.append(formImage.getFileName()) ;
//            sb.append("\"");
//            sb.append("\r\n") ;
//            /*第三行*/
//            sb.append("Content-Type: ");
//            sb.append(formImage.getMime()) ;
//            sb.append("\r\n") ;
//            /*第四行*/
//            sb.append("\r\n") ;
//            try {
//                bos.write(sb.toString().getBytes("utf-8"));
//                /*第五行*/
//                bos.write(formImage.getValue());
//                bos.write("\r\n".getBytes("utf-8"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//        /*结尾行*/
//        String endLine = "--" + BOUNDARY + "--" + "\r\n" ;
//        try {
//            bos.write(endLine.toString().getBytes("utf-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.v("zgy","=====formImage====\n"+bos.toString()) ;
//        return bos.toByteArray();
//    }
//
//    @Override
//    public String getBodyContentType() {
//        return MULTIPART_FORM_DATA+"; boundary="+BOUNDARY;
//    }
//}
