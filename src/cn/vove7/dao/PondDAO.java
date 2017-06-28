package cn.vove7.dao;

import cn.vove7.beans.PondSolution;
import cn.vove7.dbmanager.DbManager;
import cn.vove7.utils.StringCompress;

import java.sql.ResultSet;

/**
 * Created by Administrator on 2017/6/28.
 * 数据库DAO
 */
public class PondDAO {
    private DbManager dbManager = new DbManager();
    private String ss;//布局矩阵

    public PondDAO(String ss) {
        this.ss = ss;
    }

    public PondSolution getSolution() {//是否存在已有

        String sql = "select * from pond_solution where pond_s=?";
        ResultSet resultSet = dbManager.executeQuery(sql, new String[]{ss});

        Object[] objects = dbManager.getOneRecord(resultSet, new String[]{"solution_json", "solution_id"});
        if (objects == null) {
            return null;
        }
        byte[] solutionBytes = (byte[]) objects[0];
        String solutionId = Integer.toString((int)objects[1]) ;

        String solutionJson = StringCompress.decompress(solutionBytes);//解压
        return new PondSolution(solutionId, solutionJson);
    }


    public String addSolution(String ss, String solutionJson) {
        byte[] solutionBytes = StringCompress.compress(solutionJson);

//        System.out.println(solutionBytes.length);
        String sql = "insert into pond_solution(pond_s,solution_json) values(?,?)";
        dbManager.executeUpdate(sql, ss, solutionBytes);
        return getSolutionId();
    }

    public void addRequestLog(String solutionId, String userIp) {
        String sql = "insert into request_log(ip,request_time,solution_id) values(?,now(),?)";

        dbManager.executeUpdate(sql, new String[]{userIp, solutionId});
    }

    private String getSolutionId() {
        String sql = "select solution_id from pond_solution where pond_s=?";
        ResultSet resultSet = dbManager.executeQuery(sql, new String[]{ss});
        Object[] objects = dbManager.getOneRecord(resultSet, new String[]{"solution_id"});
        return objects == null ? null : Integer.toString((int) objects[0]);
    }
}
