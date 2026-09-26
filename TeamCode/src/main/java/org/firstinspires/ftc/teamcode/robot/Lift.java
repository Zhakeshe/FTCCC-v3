package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lift {
    private DcMotor motor;

    public Lift(HardwareMap hardwareMap) {
        motor=hardwareMap.get(DcMotor.class,"lift");
    }

    public void up() {
        motor.setPower(1);
    }

    public void down() {
        motor.setPower(-1);
    }

    public void stop() {
        motor.setPower(0);
    }
}
