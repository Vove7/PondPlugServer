package cn.vove7.servlets;

import cn.vove7.utils.ResponseMessage;
import cn.vove7.utils.SearchPath;
import cn.vove7.utils.Snode;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by Vove on 2017/6/12.
 * 处理请求
 */
public class HandleRequest extends HttpServlet {
    private Gson gson=new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        //获取请求Json
        String jsonString= IOUtils.toString(request.getInputStream(),request.getCharacterEncoding());

//        System.out.println("接收:");
//        System.out.println("jsonString"+jsonString);
        Snode startNode=gson.fromJson(jsonString,Snode.class);//json转换起点Snode

        if(startNode==null){
            return;
        }
        ResponseMessage resultPath=new SearchPath().searchPath(startNode);//搜索路径

        String responseJson=gson.toJson(resultPath);//结果路径转换json

        response.setHeader("Content-Type","application/json;charset=UTF-8");
        PrintWriter out=response.getWriter();//!out对象在设置charset后获取
//        System.out.println("response--->"+responseJson);
        out.print(responseJson);
        out.close();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private String getRequestJSON(HttpServletRequest request){
        String acceptJson = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            acceptJson = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acceptJson;
    }
}
