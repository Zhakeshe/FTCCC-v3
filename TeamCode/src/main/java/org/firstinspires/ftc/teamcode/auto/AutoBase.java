package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.AtomicPath;
import com.pedropathing.paths.CompoundPath;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.curves.Line;
import com.pedropathing.paths.curves.bezier.BezierCurve;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;

public abstract class AutoBase extends LinearOpMode {
    protected Follower follower;
    protected Robot robot;

    protected void initRobot() {
        robot=new Robot(hardwareMap);
        follower=Constants.createFollower(hardwareMap);
    }

    protected void goVia(double controlX, double controlY, double controlHeading,
                         double x, double y, double heading) {
        Pose start = follower.pose();
        Pose control = new Pose(Constants.cm(controlX), Constants.cm(controlY), Math.toRadians(controlHeading));
        Pose target = new Pose(Constants.cm(x), Constants.cm(y), Math.toRadians(heading));

        Path toControl = start.distance(control) > 1.0 ? line(start, control) : null;
        Pose from = toControl == null ? start : control;
        Path toTarget = from.distance(target) > 1.0 ? line(from, target) : null;

        if (toControl != null && toTarget != null) {
            follower.follow(new CompoundPath(toControl, toTarget));
        } else if (toControl != null) {
            follower.follow(toControl);
        } else if (toTarget != null) {
            follower.follow(toTarget);
        } else {
            holdHere();
        }
    }

    private Path line(Pose start, Pose end) {
        return new AtomicPath(new Line(start, end)).linear(start, end);
    }

    protected void followCurve(double controlX, double controlY, double x, double y, double headingDegrees) {
        Pose current = follower.pose();
        Pose control = new Pose(controlX, controlY, current.heading());
        Pose target = new Pose(x, y, Math.toRadians(headingDegrees));
        follower.follow(new AtomicPath(new BezierCurve(current, control, target)).linear(current, target));
    }

    protected void followLine(double x, double y, double headingDegrees) {
        Pose current = follower.pose();
        Pose target = new Pose(x, y, Math.toRadians(headingDegrees));

        if (current.distance(target) <= 1.0) {
            follower.hold(target);
            return;
        }

        follower.follow(new AtomicPath(new Line(current, target)).linear(current, target));
    }

    protected void goTo(double x,double y,double heading) {
        Pose currentPose=follower.pose();
        Pose targetPose=new Pose(
                Constants.cm(x),
                Constants.cm(y),
                Math.toRadians(heading)
        );

        if(currentPose.distance(targetPose)<1e-6) {
            follower.hold(targetPose);
            return;
        }

        follower.follow(
                new AtomicPath(new Line(currentPose,targetPose))
                        .linear(currentPose,targetPose)
        );
    }

    protected void holdHere() {
        follower.hold(follower.pose());
    }

    protected void turn(double degrees) {
        follower.hold(follower.pose().withHeading(Math.toRadians(degrees)));
    }
}
