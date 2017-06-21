package cn.vove7.utils;


import java.util.ArrayList;


/**
 * Created by Vove on 2017/4/16.
 * 矩阵信息&操作
 */

public class Snode {
    private final static int N = 6;
    private static SnodeArrayList L = new SnodeArrayList();//队列&数组
    private static ArrayList<Snode> resultList;

    private int s[][] = new int[N][N];//
    private int bnum;//快数
    private Bump bump[] = new Bump[20];
    private char actions[] = new char[2];
    private int moveStepNum = 1;
    private Snode fNode;


    public int getMoveStepNum() {
        return moveStepNum;
    }

    public void setMoveStepNum(int moveStepNum) {
        this.moveStepNum = moveStepNum;
    }

    public int getBnum() {
        return bnum;
    }

    public void setBnum(int bnum) {
        this.bnum = bnum;
    }

    public static int getN() {
        return N;
    }

    public static SnodeArrayList getL() {
        return L;
    }

    public static void setL(SnodeArrayList l) {
        L = l;
    }

    public int[][] getS() {
        return s;
    }

    public int getbNum() {
        return bnum;
    }

    public void setbNum(int bNum) {
        this.bnum = bNum;
    }

    public Bump[] getBump() {
        return bump;
    }

    public void setBump(Bump[] bump) {
        this.bump = bump;
    }

    public char[] getActions() {
        return actions;
    }

    public void setActions(char[] actions) {
        this.actions = actions;
    }

    public Snode getfNode() {
        return fNode;
    }

    public void setfNode(Snode fNode) {
        this.fNode = fNode;
    }

    public void setResultList(ArrayList<Snode> resultList) {
        this.resultList = resultList;
    }

    public char getAction(int index) {
        return actions[index];
    }

    public ArrayList<Snode> getResultList() {
        return resultList;
    }

    void setS(int[][] s) {
        this.s = s;
    }

    private void copyNewNode(Snode newNode) {
        //this->newNode
        for (int i = 0; i < bnum; i++) {
            newNode.bump[i] = bump[i].copyBump();
        }
        for (int i = bnum + 1; i < 20; i++) {
            newNode.bump[i] = null;
        }
        for (int i = 0; i < s.length; i++) {
            System.arraycopy(s[i], 0, newNode.s[i], 0, s[i].length);
        }
        System.arraycopy(actions, 0, newNode.actions, 0, actions.length);
        newNode.fNode = fNode;
        newNode.bnum = bnum;

    }


    Snode beginSearch() {
        if (bnum < 3) {
            System.out.println("Snode:" + "bump<3");
            return null;
        }
        resultList = resultList == null ? new ArrayList<>() : resultList;
        resultList.clear();
        L.clear();
        L.add(this);

        return search() ? reversePath() : null;
//        if (search())
//            return reversePath();
//        else return null;
    }

    private boolean search() {
        while (!(L.queueIsEmpty())) {//队列不为空
            Snode p = L.DeQueue();
            for (int I = 0; I < bnum; I++) {//块数
                if ((I != p.actions[0] - 1) && p.judgeDir(I))
                    return true;
            }
        }
        return false;
    }

    private boolean judgeDir(int I) {
        int span = (bump[I].state == 's' || bump[I].state == 'h') ? 1 : 2;
        Snode p, newNode;
        switch (bump[I].state) {
            case 's':
            case 'S':
                for (p = this; p.bump[I].coor[0] != 0 && p.s[p.bump[I].coor[0] - 1][p.bump[I].coor[1]] == 0; p = newNode) {//同一方向深度搜索  (未使用迭代
                    newNode = new Snode();
                    if (p.moveUp(I, newNode))
                        return true;
                }
                //向下
                for (p = this; (p.bump[I].coor[0] + span) != N - 1 && p.s[p.bump[I].coor[0] + span + 1][p.bump[I].coor[1]] == 0; p = newNode) {
                    newNode = new Snode();
                    if (p.moveDown(I, newNode))
                        return true;
                }
//                return (bump[I].coor[0] + span) != N - 1 && s[bump[I].coor[0] + span + 1][bump[I].coor[1]] == 0 && (moveDown(I));
                break;
            case 'h':
            case 'H':
                for (p = this; p.bump[I].coor[1] != 0 && p.s[p.bump[I].coor[0]][p.bump[I].coor[1] - 1] == 0; p = newNode) {
                    newNode = new Snode();
                    if (p.moveLeft(I, newNode))
                        return true;
                }

                for (p = this; (p.bump[I].coor[1] + span) != N - 1 && p.s[p.bump[I].coor[0]][p.bump[I].coor[1] + span + 1] == 0; p = newNode) {
                    newNode = new Snode();
                    if (p.moveRight(I, newNode))
                        return true;
                }

//                return (bump[I].coor[1] + span) != N - 1 && s[bump[I].coor[0]][bump[I].coor[1] + span + 1] == 0 && (moveRight(I));

        }
        return false;
    }

    private boolean moveUp(int n, Snode newNode) {
        this.copyNewNode(newNode);
        int step = this.bump[n].state == 's' ? 1 : 2;//移动跨度

        newNode.s[bump[n].coor[0] + step][bump[n].coor[1]] = 0;
        newNode.s[bump[n].coor[0] - 1][bump[n].coor[1]] = 1 + n;
        newNode.bump[n].coor[0]--;//横坐标上移 0 横坐标
        //
        newNode.actions[0] = (char) (n + 1);
        newNode.actions[1] = 'U';

        return judgeIsExistSameAndIsComplete(newNode);
    }

    private void judgeMoveSameDirection(Snode newNode) {
        if (this.actions != null && (newNode.actions[0] == actions[0]) && actions[1] == newNode.actions[1]) {
            newNode.moveStepNum = moveStepNum + 1;//父步数+1
            newNode.fNode = this.fNode;//与父同级
        }else {
            newNode.fNode = this;
        }
    }

    private boolean judgeIsExistSameAndIsComplete(Snode newNode) {
        this.judgeMoveSameDirection(newNode);//判断移动相同方向
        if (!newNode.isExistSame()) {//不存在相同
            L.add(newNode);   //进表进队
            return newNode.IsComplete();
        }
        return false;
    }

    private boolean moveDown(int n, Snode newNode) {
        this.copyNewNode(newNode);
        int step = this.bump[n].state == 's' ? 1 : 2;//移动跨度

        newNode.s[bump[n].coor[0]][bump[n].coor[1]] = 0;
        newNode.s[bump[n].coor[0] + step + 1][bump[n].coor[1]] = 1 + n;
        newNode.bump[n].coor[0]++;//坐标下移 0 横坐标
        //

        newNode.actions[0] = (char) (n + 1);
        newNode.actions[1] = 'D';

        return judgeIsExistSameAndIsComplete(newNode);
    }

    private boolean moveLeft(int n, Snode newNode) {
        this.copyNewNode(newNode);
        int step = this.bump[n].state == 'h' ? 1 : 2;//移动跨度

        newNode.s[bump[n].coor[0]][bump[n].coor[1] + step] = 0;
        newNode.s[bump[n].coor[0]][bump[n].coor[1] - 1] = 1 + n;
        newNode.bump[n].coor[1]--;//块坐标左移 1 为纵坐标

        //

        newNode.actions[0] = (char) (n + 1);
        newNode.actions[1] = 'L';

        return judgeIsExistSameAndIsComplete(newNode);
    }

    private boolean moveRight(int n, Snode newNode) {
        this.copyNewNode(newNode);
        int step = this.bump[n].state == 'h' ? 1 : 2;//移动跨度

        newNode.s[bump[n].coor[0]][bump[n].coor[1]] = 0;
        newNode.s[bump[n].coor[0]][bump[n].coor[1] + step + 1] = 1 + n;
        newNode.bump[n].coor[1]++;//块坐标友移 1 为纵坐标

        //
        newNode.actions[0] = (char) (n + 1);
        newNode.actions[1] = 'R';

        return judgeIsExistSameAndIsComplete(newNode);
    }

    private boolean isExistSame() {
        for (Snode p : L) {
            if (IsSame(p)) {
                return true;//相同
            }
        }
        return false;
    }

    private boolean IsSame(Snode p) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                if (s[i][j] != p.s[i][j])
                    return false;
        }
        return true;//相同
    }

    private boolean IsComplete() {
        for (int i = bump[0].coor[1] + 2; i < N; i++) {
            if (s[2][i] != 0) {
                return false;
            }
        }
        return true;//通
    }

    private Snode reversePath() {

        System.out.println("searchNodeNum->>>" + Integer.toString(L.size()));

        Snode p = L.get(L.size() - 1);
        L.clear();//清空

        Snode q = p.fNode;
        Snode h = new Snode();//作为转置头节点
        h.fNode = null;
        while (p != null) {
            p.fNode = h.fNode;
            h.fNode = p;
            p = q;
            if (q != null)
                q = q.fNode;
        }
        return h.fNode;
    }


    ResponseMessage translatePath() {
        Snode p = this;
        ResponseMessage resultPath = new ResponseMessage();
        ArrayList<Step> steps = new ArrayList<>();
        int i = 1;

        Snode q = p;
        for (; (p = p.fNode) != null; i++) {
            Step step = new Step();
            step.setIndex(i);
            step.setStepNum(p.getMoveStepNum());
            int bumpNo = (int) p.actions[0];
            step.setBumpNo(bumpNo);
            step.setDirection(p.actions[1]);

            //记录坐标，需错位
            step.setBumpCoor(new Integer[]{q.bump[bumpNo - 1].coor[0], q.bump[bumpNo - 1].coor[1]});

            q = p;
            steps.add(step);
        }
        resultPath.setLastFishCoor(q.bump[0].coor);//最后鱼块坐标
        resultPath.setStepNum(i);
        resultPath.setSteps(steps);
        return resultPath;
    }

}

