package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/*
 * Created on 12/14/17
 */

@TeleOp(name="Team 11000", group="Programs")
public class Hardware extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    /* Defining core motors */
    private DcMotor RightMotor;
    private DcMotor LeftMotor;

    /* Motor to pull pulley string opposite direction */
    private DcMotor arm;
//    private DcMotor extend;

    /* 180 Glyph hands servos */
    private Servo left_hand;
    private Servo right_hand;
//    private Servo claw;
//    private Servo over;

    private Servo ColorSensorServo;
    private ColorSensor colorSensor;


    @Override
    public void runOpMode() throws InterruptedException
    {
        /* configuring motors */

        // Perspective: In front of the glyph hands
        RightMotor = hardwareMap.get(DcMotor.class, "rightmotor");
        LeftMotor = hardwareMap.get(DcMotor.class, "leftmotor");
        RightMotor.setDirection(DcMotor.Direction.FORWARD);
        LeftMotor.setDirection(DcMotor.Direction.REVERSE);

        arm = hardwareMap.get(DcMotor.class, "arm");

//        extend = hardwareMap.get(DcMotor.class, "extend");


        /* Configuring servos */
        left_hand = hardwareMap.servo.get("left_hand");
        left_hand.setPosition(.9);
        right_hand = hardwareMap.servo.get("right_hand");
        right_hand.setPosition(0);

//        claw = hardwareMap.servo.get("claw");
//        claw.setPosition(0);
//
//        over = hardwareMap.servo.get("over");
//        over.setPosition(.1);

        ColorSensorServo = hardwareMap.servo.get("CSservo");
        ColorSensorServo.setPosition(1);

        /* Configuring external devices */
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");


        telemetry.addData("Status", "Initialized");
        waitForStart();
        while(opModeIsActive())
        {
            /* GamePad 1 controls */

            // Motor movement
            LeftMotor.setPower(-gamepad1.left_stick_y);
            RightMotor.setPower(-gamepad1.right_stick_y);

//            if(gamepad1.left_trigger > .5){
//                //down
//                over.setPosition(.1);
//            }else if (gamepad1.right_trigger > .5){
//                //up
//                over.setPosition(.9);
//            }

//            if(gamepad1.x){
//                //open
//                over.setPosition(.25);
//            }

            /* GamePad 2 controls */

            // Back motor
            arm.setPower(-gamepad2.left_stick_y/3.0);

            // Left and right hands
            if(gamepad2.right_trigger > .5){
                //close
                left_hand.setPosition(.9);
                right_hand.setPosition(0);
            }else if (gamepad2.left_trigger > .5){
                //open
                left_hand.setPosition(.2);
                right_hand.setPosition(.6);
            }

//            extend.setPower(-gamepad2.right_stick_y/3);

//            if(gamepad2.left_bumper){
//                //open
//                claw.setPosition(0);
//
//            }else if (gamepad2.right_bumper){
//                //close
//                claw.setPosition(.5);
//            }

            idle();

        }

    }

}
