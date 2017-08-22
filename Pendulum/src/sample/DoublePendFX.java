package sample;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;

/**
 * Created by jmcdo29 on 5/4/2017.
 */

public class DoublePendFX extends Application {
    final private double PI=Math.PI;
    private double x1, y1, x2, y2, l1, l2, theta1_0, theta2_0, x0, y0, omega1=0, omega2=0,  theta1, theta2;

    @Override
    public void start(Stage stage) throws Exception{
        startAnimation(stage);
    }

    private void getLength1(){
        TextInputDialog inputDialog = new TextInputDialog("150");
        inputDialog.setTitle("Length Setting");
        inputDialog.setHeaderText("Please enter the length you would like each pendulum to be.");
        inputDialog.setContentText("Length 1:");
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent()){
            l1=Integer.parseInt(result.get());
        }
        getLength2();
    }

    private void getLength2(){
        TextInputDialog inputDialog = new TextInputDialog("150");
        inputDialog.setTitle("Length Setting");
        inputDialog.setHeaderText("Please enter the length you would like each pendulum to be.");
        inputDialog.setContentText("Length 2:");
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent()){
            l2=Integer.parseInt(result.get());
        }
        getTheta1();
    }

    private void getTheta1(){
        TextInputDialog inputDialog = new TextInputDialog("0.5");
        inputDialog.setTitle("Theta Setting");
        inputDialog.setHeaderText("Please enter the theta for pendulum 1 to start at.\nNote: PI/2 is considered to be the x-axis. Enter as a decimal value of PI");
        inputDialog.setContentText("Theta 1:");
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent()){
            theta1_0=PI*Double.parseDouble(result.get());
        }
        getTheta2();
    }

    private void getTheta2(){
        TextInputDialog inputDialog = new TextInputDialog("0.5");
        inputDialog.setTitle("Theta Setting");
        inputDialog.setHeaderText("Please enter the theta for pendulum 2 to start at.\nNote: PI/2 is considered to be the x-axis. Enter as a decimal value of PI");
        inputDialog.setContentText("Theta 1:");
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent()){
            theta2_0=PI*Double.parseDouble(result.get());
        }
    }

    private void step(){
        double g=-9.81;
        double dt=0.01;
        double delta=theta2-theta1;
        double angleAccel1=(l1*omega1*omega1*Math.sin(delta)*Math.cos(delta)+g*Math.sin(theta2)*Math.cos(delta)+l2*omega2*omega2*Math.sin(delta)-2*g*Math.sin(theta1))/(2*l1-l1*Math.cos(delta)*Math.cos(delta));
        double angleAccel2=(-l2*omega2*omega2*Math.sin(delta)*Math.cos(delta)+2*(g*Math.sin(theta1)*Math.cos(delta)-l1*omega1*omega1*Math.sin(delta)-g*Math.sin(theta2)))/(2*l2-l2*Math.cos(delta)*Math.cos(delta));
        omega1+=angleAccel1*dt;
        omega2+=angleAccel2*dt;
        theta1+=omega1*dt;
        theta2+=omega2*dt;
        x1=x0+l1*Math.sin(theta1);
        y1=y0-l1*Math.cos(theta1);
        x2=x0+l1*Math.sin(theta1)+l2*Math.sin(theta2);
        y2=y0-l1*Math.cos(theta1)-l2*Math.cos(theta2);
    }

    public void restart(Stage stage){
        stage.hide();
        cleanup();
        startAnimation(stage);

    }

    public void cleanup(){
    }

    public void startAnimation(Stage stage){
        try {
            getLength1();
            Group group = new Group();
            Line xAxis = new Line(0, (l1 + l2), (l1 + l2) * 2, (l1 + l2));
            Line yAxis = new Line((l1 + l2), 0, (l1 + l2), (l1 + l2) * 2);
            Label xlabel = new Label("X Axis");
            Label ylabel = new Label("Y Axis");
            xlabel.relocate((l1+l2)*2-50, l1+l2);
            ylabel.relocate(l1+l2,0);
            for (int i = 0; i < (l1 + l2) * 2 + 1; i += 10) {
                Line xAxisMarks = new Line(i, 0, i, (l1 + l2) * 2);
                xAxisMarks.setStroke(Color.LIGHTGRAY);
                Line yAxisMarks = new Line(0, i, (l1 + l2) * 2, i);
                yAxisMarks.setStroke(Color.LIGHTGRAY);
                group.getChildren().addAll(xAxisMarks, yAxisMarks);
            }
            theta1 = theta1_0;
            x0 = (l1 + l2);
            theta2 = theta2_0;
            y0 = (l1 + l2);
            x1 = x0 + l1 * Math.sin(theta1);
            x2 = x0 + l1 * Math.sin(theta1) + l2 * Math.sin(theta2);
            y1 = y0 + l1 * Math.cos(theta1);
            y2 = y0 + l1 * Math.cos(theta1) + l2 * Math.cos(theta2);
            Circle origin = new Circle(x0, y0, 3, Color.BLUE);
            Circle circ1 = new Circle(x1, y1, 5, Color.DARKRED);
            Circle circ2 = new Circle(x2, y2, 5, Color.BLUEVIOLET);
            group.getChildren().addAll(xAxis, yAxis, origin);
            Path circ1path = new Path();
            Path circ2path = new Path();
            circ1path.getElements().add(new MoveTo(x1, y1));
            circ1path.setStroke(Color.DARKRED);
            circ2path.getElements().add(new MoveTo(x2, y2));
            circ2path.setStroke(Color.BLUEVIOLET);
            for (int i = 0 ; i<50000 ; i++){
                step();
                LineTo lineTo1 = new LineTo(x1, y1);
                circ1path.getElements().add(lineTo1);
                LineTo lineTo2 = new LineTo(x2, y2);
                circ2path.getElements().add(lineTo2);
            }
            PathTransition pathCirc1Trans = new PathTransition();
            PathTransition pathCirc2Trans = new PathTransition();
            pathCirc1Trans.setDuration(Duration.millis(30000));
            pathCirc1Trans.setPath(circ1path);
            pathCirc1Trans.setNode(circ1);
            pathCirc1Trans.setAutoReverse(false);
            pathCirc1Trans.play();
            pathCirc2Trans.setDuration(Duration.millis(30000));
            pathCirc2Trans.setPath(circ2path);
            pathCirc2Trans.setNode(circ2);
            pathCirc2Trans.setAutoReverse(false);
            pathCirc2Trans.play();
            Button reset = new Button();
            reset.setPrefHeight(26);
            reset.setPrefWidth(100);
            reset.setText("Reset");
            reset.relocate((l1 + l2) - 75, (l1 + l2) * 2 + 13);
            reset.setOnAction((ActionEvent clicked) -> {
                pathCirc1Trans.stop();
                pathCirc1Trans.setPath(null);
                pathCirc2Trans.stop();
                pathCirc2Trans.setPath(null);
                restart(stage);
            });
            group.getChildren().addAll(circ1, circ2, circ1path, circ2path, reset, xlabel, ylabel);
            Scene scene = new Scene(group, (l1 + l2) * 2, (l1 + l2) * 2 + 50);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Double Pendulum");
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){launch(args);}
}
