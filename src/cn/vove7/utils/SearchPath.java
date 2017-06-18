package cn.vove7.utils;


/**
 * Created by Vove on 2017/6/14.
 * 搜索路线
 */
public class SearchPath {

    public ResponseMessage searchPath(Snode startNode) {
        long beginTime=System.currentTimeMillis();
        Snode resultNode = startNode.beginSearch();
        long endTime=System.currentTimeMillis();

        ResponseMessage resultPath=new ResponseMessage();
        if(resultNode!=null) {
            resultPath = resultNode.translatePath();
        }
        resultPath.setUseTime(Long.toString((endTime-beginTime)/1000)+"s");
        resultPath.setHaveResult(resultNode != null);
        resultPath.setMessage(resultNode != null? "搜索成功":"搜索失败");

        return resultPath;
    }

}
