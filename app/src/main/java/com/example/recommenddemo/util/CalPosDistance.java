package com.example.recommenddemo.util;

import android.util.Log;

import java.util.Arrays;

public class CalPosDistance {
    private int cityNum; // 城市数量
    private double[][] distance; // 距离矩阵

    private int[] colable;//代表列，也表示是否走过，走过置0
    private int[] row;//代表行，选过置0
    private int[] citySort;
    public CalPosDistance(int n) {
        cityNum = n;
        citySort = new int[n];
    }
    public void init(double[] xPos, double[] yPos){
        // 读取数据
        double[] x = xPos;
        double[] y = yPos;
        distance = new double[cityNum][cityNum];
        // 计算距离矩阵
        // ，针对具体问题，距离计算方法也不一样，此处用的是att48作为案例，它有48个城市，距离计算方法为伪欧氏距离，最优值为10628
        for (int i = 0; i < cityNum - 1; i++) {
            distance[i][i] = 0; // 对角线为0
            for (int j = i + 1; j < cityNum; j++) {
                distance[i][j] = EUC_2D_dist(x[i] , x[j] ,y[i] , y[j]);
                distance[j][i] = distance[i][j];
            }
        }

        distance[cityNum - 1][cityNum - 1] = 0;

        colable = new int[cityNum];
        colable[0] = 0;
        for (int i = 1; i < cityNum; i++) {
            colable[i] = 1;
        }

        row = new int[cityNum];
        for (int i = 0; i < cityNum; i++) {
            row[i] = 1;
        }

    }
    public int[] solve(){

        double[] temp = new double[cityNum];
        String path="0";
        int citySortNum = 0;
        citySort[citySortNum++]=0;
        double s=0;//计算距离
        int i=0;//当前节点
        int j=0;//下一个节点
        //默认从0开始
        while(row[i]==1){
            //复制一行
            for (int k = 0; k < cityNum; k++) {
                temp[k] = distance[i][k];
            }
            //选择下一个节点，要求不是已经走过，并且与i不同
            j = selectmin(temp);
            if(j==-1)
                break;
            //找出下一节点
            row[i] = 0;//行置0，表示已经选过
            colable[j] = 0;//列0，表示已经走过

            path+="-->" + j;
            s = s + distance[i][j];
            i = j;//当前节点指向下一节点
            citySort[citySortNum++]=i;
        }
        Log.e("TAG", path.toString() );
        Log.e("TAG", s+"");
        Log.e("TAG", Arrays.toString(citySort));
        return citySort;
    }

    public int selectmin(double[] p){
        int j = 0, k = 0;
        double m = p[0];
        //寻找第一个可用节点，注意最后一次寻找，没有可用节点
        while (colable[j] == 0) {
            j++;
            //System.out.print(j+" ");
            if(j>=cityNum){
                //没有可用节点，说明已结束
                return -1;
            }
            else{
                m = p[j];
            }
        }
        //从可用节点J开始往后扫描，找出距离最小节点
        for (; j < cityNum; j++) {
            if (colable[j] == 1) {
                if (m >= p[j]) {
                    m = p[j];
                    k = j;
                }
            }
        }
        return k;
    }
    public double EUC_2D_dist(double x1,double x2, double y1,double y2) {
         return  ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        //return (int) Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2)	* (y1 - y2)));
    }
}
