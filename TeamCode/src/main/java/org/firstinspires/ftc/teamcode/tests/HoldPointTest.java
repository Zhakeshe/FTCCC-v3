package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name="Hold Point Test")
public class HoldPointTest extends LinearOpMode {
    private Follower follower;

    @Override
    public void runOpMode() {
        follower=Constants.createFollower(hardwareMap);

        Pose holdPose=new Pose(72,72,0);
        follower.setPose(holdPose);

        telemetry.addLine("Ready");
        telemetry.addLine("Robot will hold X=72 Y=72 Heading=0");
        telemetry.update();

        waitForStart();

        follower.hold(holdPose);

        while(opModeIsActive()) {
            follower.update();

            Pose pose=follower.pose();

            telemetry.addData("X",pose.x());
            telemetry.addData("Y",pose.y());
            telemetry.addData("Heading",Math.toDegrees(pose.heading()));
            telemetry.addData("Error X",holdPose.x()-pose.x());
            telemetry.addData("Error Y",holdPose.y()-pose.y());
            telemetry.addData("Error Heading",Math.toDegrees(holdPose.heading()-pose.heading()));
            telemetry.update();
        }
    }
}
