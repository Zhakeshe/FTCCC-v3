package org.firstinspires.ftc.teamcode.tests;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Constants;

@TeleOp(name="Localization Check")
public class LocalizationCheck extends LinearOpMode {
    @Override
    public void runOpMode() {
        Follower follower;

        try {
            follower=Constants.createFollower(hardwareMap);

            telemetry.addLine("Follower CREATED");
            telemetry.addData("Localizer",follower.localizer);
            telemetry.update();
        }catch(Exception e) {
            telemetry.addLine("ERROR:");
            telemetry.addData("Type",e.getClass().getSimpleName());
            telemetry.addData("Message",e.getMessage());
            telemetry.update();

            while(!isStopRequested()) {
                sleep(100);
            }
            return;
        }

        waitForStart();

        while(opModeIsActive()) {
            follower.update();

            telemetry.addData("X",follower.pose().x());
            telemetry.addData("Y",follower.pose().y());
            telemetry.addData("Heading",follower.pose().heading());
            telemetry.update();
        }
    }
}
