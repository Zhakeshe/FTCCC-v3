package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo servo;

    public Claw(HardwareMap hardwareMap) {
        servo=hardwareMap.get(Servo.class,"claw");
    }

    public void open() {
        servo.setPosition(0.8);
    }

    public void close() {
        servo.setPosition(0.2);
    }
}
