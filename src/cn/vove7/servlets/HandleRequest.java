package cn.vove7.servlets;

import cn.vove7.beans.PondSolution;
import cn.vove7.dao.PondDAO;
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
    private Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        //获取请求Json
        String jsonString = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        Snode startNode = gson.fromJson(jsonString, Snode.class);//json转换起点Snode
//        System.out.println(jsonString.length());
        String userIp = request.getRemoteAddr();//用户ip

        if (startNode == null) {
            return;
        }
        String ss = startNode.getString_S();//获取布局矩阵
        PondDAO pondDAO = new PondDAO(ss);

        String solutionJson ;
        PondSolution pondSolution = pondDAO.getSolution();//检查是否存在已有
        if (pondSolution == null) {
            ResponseMessage resultPath = new SearchPath().searchPath(startNode);//搜索路径

            solutionJson = gson.toJson(resultPath);//结果路径转换json
            if (resultPath.isHaveResult()) {
                pondSolution = new PondSolution(pondDAO.addSolution(ss, solutionJson), solutionJson);
                pondDAO.addRequestLog(pondSolution.getSolutionId(), userIp);//添加请求记录
            } else {
                pondDAO.addRequestLog("-1", userIp);//无结果时
            }
        }else {
            solutionJson=pondSolution.getSolutionJson();
        }

        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();//!out对象在设置charset后获取

        out.print(solutionJson);
        out.close();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private String getRequestJSON(HttpServletRequest request) {
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
