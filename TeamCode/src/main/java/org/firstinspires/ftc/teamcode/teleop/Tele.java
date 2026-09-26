package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;

@TeleOp(name="Competition TeleOp")
public class Tele extends LinearOpMode {
    private Robot robot;
    private Follower follower;

    @Override
    public void runOpMode() {
        robot=new Robot(hardwareMap);
        follower=Constants.createFollower(hardwareMap);

        telemetry.addLine("VSE GOOD");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            double y=-gamepad1.left_stick_y;
            double x=gamepad1.left_stick_x;
            double rx=gamepad1.right_stick_x;

            double speed=Constants.DRIVE_SPEED;

            if(gamepad1.left_bumper) {
                speed=Constants.SLOW_SPEED;
            }

            robot.drive.drive(y,x,rx,speed);

            follower.update();

            telemetry.addData("X",follower.pose().x());
            telemetry.addData("Y",follower.pose().y());
            telemetry.addData("Heading",
                    Math.toDegrees(follower.pose().heading()));
            telemetry.update();
        }
    }
}
