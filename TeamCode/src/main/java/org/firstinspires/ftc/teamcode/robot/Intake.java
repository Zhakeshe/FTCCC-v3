package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Constants;

public class Intake {
    private DcMotor motor;
    private Servo stopper;


    public Intake(HardwareMap hardwareMap) {
        motor=hardwareMap.get(DcMotor.class,Constants.INTAKE);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        stopper = hardwareMap.get(Servo.class, "stopper");
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    public void in() {
        motor.setPower(Constants.INTAKE_IN);
    }

    public void setStopper_close(){
        stopper.setPosition(0);
    }

    public void setStopper_open(){
        stopper.setPosition(0.3);
    }

    public void out() {
        motor.setPower(Constants.INTAKE_OUT);
    }

    public void stop() {
        motor.setPower(0);
    }
}
