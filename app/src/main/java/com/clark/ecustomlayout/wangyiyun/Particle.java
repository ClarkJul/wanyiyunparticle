package com.clark.ecustomlayout.wangyiyun;

public class Particle {

/*    var x:Float,//X坐标
    var y:Float,//Y坐标
    var radius:Float,//半径
    var speed:Float,//速度
    var alpha: Int//透明度*/
   public float x;
   public float y;
   public float radius;
   public float speed;
   public int alpha;
   public double angle;
   public float offset;

    public Particle() {
    }

    public Particle(float x, float y, float radius, float speed, int alpha, double angle, float offset) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.alpha = alpha;
        this.angle = angle;
        this.offset = offset;
    }

    public Particle(float x, float y, float radius, float speed, int alpha, double angle) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.alpha = alpha;
        this.angle = angle;
    }

    public Particle(float x, float y, float radius, float speed, int alpha) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.alpha = alpha;
    }
}
