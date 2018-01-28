
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareTest
{
    /* Public OpMode members. */
    public DcMotor  left_drive = null;
    public DcMotor  right_drive = null;
    public DcMotor  arm_drive = null;

    public Servo    leftClaw;
    public Servo    rightClaw;
    public Servo ColorServo   = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.29 ;
    public static final double ARM_DOWN_POWER  = -0.29 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareTest(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        left_drive  = hwMap.get(DcMotor.class, "leftmotor");
        right_drive = hwMap.get(DcMotor.class, "rightmotor");
        arm_drive = hwMap.get(DcMotor.class, "arm");
        arm_drive.setDirection(DcMotor.Direction.FORWARD);

//        ColorServo = hwMap.servo.get("CSservo");
//        ColorServo.setPosition(1);

        left_drive.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        right_drive.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors


        // Set all motors to zero power
        left_drive.setPower(0);
        right_drive.setPower(0);


        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        left_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Define and initialize ALL installed servos.
        leftClaw  = hwMap.get(Servo.class, "left_hand");
        rightClaw = hwMap.get(Servo.class, "right_hand");
        ColorServo = hwMap.get(Servo.class, "CSservo");
        ColorServo.setPosition(1);
        leftClaw.setPosition(.2); //open hands
        rightClaw.setPosition(.6);
    }
 }

