
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Autodrive Blue", group="Pushbot")
//@Disabled
public class AutoDrivebyEncoder extends LinearOpMode {

    HardwareTest robot   = new HardwareTest();
    private ElapsedTime     runtime = new ElapsedTime();
    ColorSensor colorsensor;

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.4;     //full speed is 1.0
    static final double     TURN_SPEED              = 0.125;
    static final double     arm_speed              = .35;
    static boolean jewel = false;
    static boolean blue = false;

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        // Map color sensor and servo
        colorsensor = hardwareMap.get(ColorSensor.class, "colorSensor");
        colorsensor.enableLed(true);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        // Resets encoders and initalizes them zero values out
        robot.left_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.right_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.left_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.right_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.left_drive.getCurrentPosition(),
                robot.right_drive.getCurrentPosition());

        telemetry.update();
        waitForStart();

        /* AUTONOMOUS MODE SEQUENCE */

        claws(.3, .9, 0.0); //close hands
        //speed, inches, timeout
        armdrive(4.1, 1.4, 1.6);

        CSservo(2, 0.06);   //moves the CSservo down to read jewel

        /*
        Jewel time, CSservotime, CSposition
        knocks opposite color off and picks hand up and moves back to original position
        */
        jewel(1.2, 1, .8);

        /*
        Jewel fallback when it cannot find color values
        Moves CSservo up and down to see if the location it was set was too far
        Uses the jewel boolean as a switch to see if found or not
        */
        if (jewel == false) {
            CSservo(1.2, 0.12);
            jewel(1.2, 1.1, .8);
            if (jewel == false) {
                CSservo(1, .8); //fallback of the fallback lol
            }
        }

//        // Movement Based on jewel because values change based on jewel color
        if (blue = true) {
            encoderDrive(TURN_SPEED, 1.3, 1.3, 1.15);
        } else {
            encoderDrive(TURN_SPEED, 1.3, 1.3, 1.3); //move forward from plate
        }

        //move back to hit plate to stabalize movement
        encoderDrive(TURN_SPEED, -.5, -.5, .9);

        //move forward
        encoderDrive(TURN_SPEED, 1, 1, .7);


        if(jewel == false){
            encoderDrive(TURN_SPEED,  1.2,  -1.2, 1.2); // Turning
        }else{
            encoderDrive(TURN_SPEED,  1.5,  -1.5, 1.5);
        }

        armdrive(1.9, -1, .9);

        claws(.3, .2, .6);                 //open hands
        encoderDrive(.125,  .23,  .23, .92);   //pushes block

        encoderDrive(.08,  -.1,  -.1, .52); //moves back so the glyph can drop

        encoderDrive(.1,  .15,  .15, .4);   //parks robot
        encoderDrive(.1,  -.1,  -.1, .4);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }


    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        /*
        Used for drive train to move (inches)
        */
        int newLeftTarget;
        int newRightTarget;

        if (opModeIsActive()) {

            newLeftTarget = robot.left_drive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.right_drive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.left_drive.setTargetPosition(newLeftTarget);
            robot.right_drive.setTargetPosition(newRightTarget);

            robot.left_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.right_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.left_drive.setPower(Math.abs(speed));
            robot.right_drive.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.left_drive.isBusy() && robot.right_drive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.left_drive.getCurrentPosition(),
                        robot.right_drive.getCurrentPosition());
                telemetry.update();
            }

            robot.left_drive.setPower(0);
            robot.right_drive.setPower(0);

            robot.left_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.right_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void jewel(double holdTime, double servotime, double servoposition){
        //parameters from CSservo to the method jewel to make it versitle

        ElapsedTime holdTimer = new ElapsedTime();
        holdTimer.reset();

        while(opModeIsActive() && holdTimer.time() < holdTime){
            if((colorsensor.red()>=1)&&(colorsensor.red()<=200)){
                jewel = true;
                blue = true;
                encoderDrive(TURN_SPEED,-.5, .5, .9); //twists forward

                CSservo(servotime, servoposition);    //time, position

                encoderDrive(TURN_SPEED,.5, -.5, .9); //twists back to position
            }
            else if((colorsensor.blue()>=1)&&(colorsensor.blue()<=200)){
                jewel = true;
                encoderDrive(TURN_SPEED,.5, -.5, .9); //twists back

                CSservo(servotime, servoposition);    //time, position

                encoderDrive(TURN_SPEED,-.5, .5, .9); //twists back to position
            }
        }
    }

    public void CSservo(double holdTime, double position){
        /* hMethod to control the color sensor servo */
        ElapsedTime holdTimerServo = new ElapsedTime();
        holdTimerServo.reset();
        while(opModeIsActive() && holdTimerServo.time() < holdTime){
            robot.ColorServo.setPosition(position);
        }
    }


    public void claws(double holdTime, double leftclaw, double rightclaw){
        /* Method to control the claws */
        ElapsedTime clawTimer = new ElapsedTime();
        clawTimer.reset();
        while(opModeIsActive() && clawTimer.time() < holdTime){
            robot.leftClaw.setPosition(leftclaw);
            robot.rightClaw.setPosition(rightclaw);
        }
    }

    public void armdrive(double speed,
                         double downinches, double timeoutS) {
        /*
        Used for drive train to move (inches)
        */
        int Target;

        if (opModeIsActive()) {

            Target = robot.arm_drive.getCurrentPosition() + (int)(downinches * COUNTS_PER_INCH);
            robot.arm_drive.setTargetPosition(Target);

            robot.arm_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.arm_drive.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.arm_drive.isBusy())) {

                //Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d",  Target);
                telemetry.addData("Path2",  "Running at %7d",
                        robot.arm_drive.getCurrentPosition());
                telemetry.update();
            }

            robot.arm_drive.setPower(0);

            robot.arm_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }
}
