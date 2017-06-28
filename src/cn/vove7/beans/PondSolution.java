package cn.vove7.beans;

/**
 * Created by Administrator on 2017/6/28.
 *
 */
public class PondSolution {
    private String solutionId;
    private String solutionJson;

    public PondSolution(String solutionId, String solutionJson) {
        this.solutionId = solutionId;
        this.solutionJson = solutionJson;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public String getSolutionJson() {
        return solutionJson;
    }
}
