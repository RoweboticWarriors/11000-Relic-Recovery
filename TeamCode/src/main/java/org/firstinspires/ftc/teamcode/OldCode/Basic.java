package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


// TODO: Get OpenCV to analyze images, maybe

/*
 * Created on 11/8/17
 */
@Disabled
@TeleOp(name="old Team 11000", group="Programs")
public class Basic extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    /* Defining core motors */
    private DcMotor RightMotor;
    private DcMotor LeftMotor;

    /* Motor to pull pulley string opposite direction */
    private DcMotor Pulley;

    private DcMotor UpnDown;

    /* 180 Glyph hands servos */
    private Servo left_hand;
    private Servo right_hand;

    /* 180 defining the servo that is used to drop the color sensor at the beginning of the game. */
    private Servo ColorSensorServo;

    /* 180 servo used to grab the yellow guy */
    private Servo RelicClaw;

    /* 360 servo for extending the crane mechanism */
    private Servo CraneExt;

    /* 360 servo that drops down the claw */
    private Servo DropTop;

    /* Color sensor to tell what color ball it is */
    private ColorSensor colorSensor;

    /* Position for the glyph hands */
    double servoPosition = 0.0;


    @Override
    public void runOpMode() throws InterruptedException
    {
        /* configuring motors */

        // Perspective: In front of the glyph hands
        RightMotor = hardwareMap.get(DcMotor.class, "rightmotor");
        LeftMotor = hardwareMap.get(DcMotor.class, "leftmotor");
        RightMotor.setDirection(DcMotor.Direction.FORWARD);
        LeftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Configure motor used for the pulley
        Pulley = hardwareMap.get(DcMotor.class, "move_pulley");
        Pulley.setDirection(DcMotor.Direction.FORWARD);

        // Back motor
        UpnDown = hardwareMap.get(DcMotor.class, "Up");
        UpnDown.setDirection(DcMotor.Direction.FORWARD);

        /* Configuring servos */

        // Configure starting servos for the "hands"
        left_hand = hardwareMap.servo.get("left_hand");
        left_hand.setPosition(.9);
        right_hand = hardwareMap.servo.get("right_hand");
        right_hand.setPosition(0);

        // Configure claw machine
        RelicClaw = hardwareMap.servo.get("Claw");
        RelicClaw.setPosition(servoPosition);

        // Configure crane servo
        //.5 not right .6
        CraneExt = hardwareMap.servo.get("Extend");
        telemetry.addData("Servo Position", CraneExt.getPosition());
        CraneExt.setPosition(.5);

        // Configure Color sensor servo
        ColorSensorServo = hardwareMap.servo.get("CSservo");
        ColorSensorServo.setPosition(1);

        DropTop = hardwareMap.servo.get("Drops");
        DropTop.setPosition(.5);

        /* Configuring external devices */

        // Configuring color sensor
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");


        telemetry.addData("Status", "Initialized");
        waitForStart();
        while(opModeIsActive())
        {
            /* GamePad 1 controls */

            // Motor movement
            LeftMotor.setPower(-gamepad1.left_stick_y);
            RightMotor.setPower(-gamepad1.right_stick_y);

            // Left and right hands
            if(gamepad1.left_trigger >.5){
                left_hand.setPosition(.9);
                right_hand.setPosition(0);
            }else if (gamepad1.right_trigger > .5){
                left_hand.setPosition(.2);
                right_hand.setPosition(.6);
            }

            /* GamePad 2 controls */

            // Back motor
            UpnDown.setPower(-gamepad2.left_stick_y);

            // Pulley for the hands
            Pulley.setPower(-gamepad2.right_stick_y);

//            CraneExt.setPosition(-gamepad2.right_stick_y);
            telemetry.addData("Servo Position", CraneExt.getPosition());

            //Crane extender
            if(gamepad2.y){
                // .5 stops the 360 servo
                CraneExt.setPosition(.5);
            }else if (gamepad2.x){
                CraneExt.setPosition(.6);
            }else if (gamepad2.b){
                CraneExt.setPosition(.4);
            }

            // Color sensor
//            if (gamepad2.a){
//                ColorSensorServo.setPosition(.6);
//            }else if (gamepad2.b){
//                ColorSensorServo.setPosition(-.6);
//            }

            // Relic claw
            if (gamepad2.left_trigger > .5){
                RelicClaw.setPosition(1);
            }else if(gamepad2.right_trigger > .5){
                RelicClaw.setPosition(0);
            }

            // Dropping servo to drop the claw machine
            if (gamepad2.left_bumper){
                DropTop.setPosition(0);
            }else if (gamepad2.right_bumper){
                DropTop.setPosition(.8);
            }
            idle();

        }

    }

}
