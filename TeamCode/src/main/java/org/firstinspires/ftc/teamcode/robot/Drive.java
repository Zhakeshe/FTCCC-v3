package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Constants;

public class Drive {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public Drive(HardwareMap hardwareMap) {
        frontLeft=hardwareMap.get(DcMotor.class,Constants.FRONT_LEFT);
        frontRight=hardwareMap.get(DcMotor.class,Constants.FRONT_RIGHT);
        backLeft=hardwareMap.get(DcMotor.class,Constants.BACK_LEFT);
        backRight=hardwareMap.get(DcMotor.class,Constants.BACK_RIGHT);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void drive(double y,double x,double rx,double speed) {
        double denominator=Math.max(Math.abs(y)+Math.abs(x)+Math.abs(rx),1);
        double fl=(y+x+rx)/denominator;
        double bl=(y-x+rx)/denominator;
        double fr=(y-x-rx)/denominator;
        double br=(y+x-rx)/denominator;

        frontLeft.setPower(fl*speed);
        backLeft.setPower(bl*speed);
        frontRight.setPower(fr*speed);
        backRight.setPower(br*speed);
    }

    public void stop() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }
}
