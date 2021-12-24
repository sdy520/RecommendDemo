package com.example.recommenddemo.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class HeNanPos implements Comparable, Serializable {
    @NonNull
    @PrimaryKey
    private String name;
    private String intro;
    private String image_link;
    private String pos_link;
    private String rank;
    private String body_content;
    private Double longitude;
    private Double latitude;
    private String city_name;
    private float rating;
    private String url;
    private int xiashangzhou;
    private int chunqiuzhanguo;
    private int qin;
    private int han;
    private int sanguo;
    private int jin;
    private int nanbeichao;
    private int sui;
    private int tang;
    private int song;
    private int yuan;
    private int ming;
    private int qing;
    private float play_time;
    private float recommend_score;
    private int day;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getPos_link() {
        return pos_link;
    }

    public void setPos_link(String pos_link) {
        this.pos_link = pos_link;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getBody_content() {
        return body_content;
    }

    public void setBody_content(String body_content) {
        this.body_content = body_content;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getXiashangzhou() {
        return xiashangzhou;
    }

    public void setXiashangzhou(int xiashangzhou) {
        this.xiashangzhou = xiashangzhou;
    }

    public int getChunqiuzhanguo() {
        return chunqiuzhanguo;
    }

    public void setChunqiuzhanguo(int chunqiuzhanguo) {
        this.chunqiuzhanguo = chunqiuzhanguo;
    }

    public int getQin() {
        return qin;
    }

    public void setQin(int qin) {
        this.qin = qin;
    }

    public int getHan() {
        return han;
    }

    public void setHan(int han) {
        this.han = han;
    }

    public int getSanguo() {
        return sanguo;
    }

    public void setSanguo(int sanguo) {
        this.sanguo = sanguo;
    }

    public int getJin() {
        return jin;
    }

    public void setJin(int jin) {
        this.jin = jin;
    }

    public int getNanbeichao() {
        return nanbeichao;
    }

    public void setNanbeichao(int nanbeichao) {
        this.nanbeichao = nanbeichao;
    }

    public int getSui() {
        return sui;
    }

    public void setSui(int sui) {
        this.sui = sui;
    }

    public int getTang() {
        return tang;
    }

    public void setTang(int tang) {
        this.tang = tang;
    }

    public int getSong() {
        return song;
    }

    public void setSong(int song) {
        this.song = song;
    }

    public int getYuan() {
        return yuan;
    }

    public void setYuan(int yuan) {
        this.yuan = yuan;
    }

    public int getMing() {
        return ming;
    }

    public void setMing(int ming) {
        this.ming = ming;
    }

    public int getQing() {
        return qing;
    }

    public void setQing(int qing) {
        this.qing = qing;
    }

    public float getPlay_time() {
        return play_time;
    }

    public void setPlay_time(float play_time) {
        this.play_time = play_time;
    }

    public float getRecommend_score() {
        return recommend_score;
    }

    public void setRecommend_score(float recommend_score) {
        this.recommend_score = recommend_score;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public HeNanPos(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "HeNanPos{" +
                "name='" + name + '\'' +
                ", intro='" + intro + '\'' +
                ", image_link='" + image_link + '\'' +
                ", pos_link='" + pos_link + '\'' +
                ", rank='" + rank + '\'' +
                ", body_content='" + body_content + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", city_name='" + city_name + '\'' +
                ", rating=" + rating +
                ", url='" + url + '\'' +
                ", xiashangzhou=" + xiashangzhou +
                ", chunqiuzhanguo=" + chunqiuzhanguo +
                ", qin=" + qin +
                ", han=" + han +
                ", sanguo=" + sanguo +
                ", jin=" + jin +
                ", nanbeichao=" + nanbeichao +
                ", sui=" + sui +
                ", tang=" + tang +
                ", song=" + song +
                ", yuan=" + yuan +
                ", ming=" + ming +
                ", qing=" + qing +
                ", play_time=" + play_time +
                ", recommend_score=" + recommend_score +
                ", day=" + day +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return Float.compare(this.getRecommend_score(), ((HeNanPos) o).getRecommend_score());
    }
}
