import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CannonGameController {

    @FXML
    private Rectangle blockerRect;

    @FXML
    private Rectangle cannonRect;

    @FXML
    private Pane pane;

    @FXML
    private Label scoreBoardLabel;

    @FXML
    private Button startButton;

    @FXML
    private Rectangle target1Rect;

    @FXML
    private Rectangle target2Rect;

    @FXML
    private Rectangle target3Rect;

    @FXML
    private Rectangle target4Rect;

    @FXML
    private Rectangle target5Rect;

    @FXML
    private Rectangle target6Rect;

    @FXML
    private Rectangle target7Rect;

    @FXML
    private Rectangle target8Rect;

    @FXML
    private Label timeLabel;

    // Velocities of 8 targets and the blocker
    int v1 = 1;
    int v2 = 5;
    int v3 = 6;
    int v4 = 12;
    int v5 = 4;
    int v6 = 5;
    int v7 = 7;
    int v8 = 2;
    int vBlock = 3;

    // Create a default cannonBall to avoid the NullPointerException
    private Circle cannonBall = new Circle();

    private int startTime = 1000;
    private int score = 0;
    private double cannonAngle;

    // Audio for each events
    AudioClip cannonFireSound = new AudioClip(getClass().getResource("/cannon_fire.wav").toString());
    AudioClip blockerHitSound = new AudioClip(getClass().getResource("/blocker_hit.wav").toString());
    AudioClip targetHitSound = new AudioClip(getClass().getResource("/target_hit.wav").toString());

    @FXML
    void changeCannonAngle(MouseEvent event) {
	// Get the mouse cursor's position
	double mouseX = event.getX();
	double mouseY = event.getY();

	// Get the cannon barrel's end and compute the angle relative to the mouse
	// cursor.
	// Divide
	double barrelX = cannonRect.getLayoutX() + cannonRect.getWidth() / 2;
	double barrelY = cannonRect.getLayoutY() + cannonRect.getHeight() / 2;
	// Use Math.atan2 to compute the angle of mouse's position relative to the
	// layoutX and layoutY of the rectangle.
	cannonAngle = Math.toDegrees(Math.atan2(mouseY - barrelY, mouseX - barrelX));
	cannonRect.setRotate(cannonAngle);
    }

    @FXML
    void shootCannonBall(MouseEvent event) {
	// Every time user presses on the pane, create a new Circle and add it to the
	// pathTransition in order to move the circle from the cannon to the mouse
	// cursor's position.
	cannonBall = new Circle();
	cannonBall.setRadius(13);
	cannonBall.setCenterX(cannonRect.getLayoutX() + cannonRect.getWidth() / 2);
	cannonBall.setCenterY(cannonRect.getLayoutY() + cannonRect.getHeight() / 2);
	cannonBall.setFill(Color.BLACK);

	pane.getChildren().add(cannonBall);
	// Creating a new path, move the cannonBall from the barrel to the mouse
	// cursor's position.
	Path path = new Path(new MoveTo(0, cannonRect.getLayoutY() + cannonRect.getHeight() / 2),
		new LineTo(event.getX(), event.getY()), new MoveTo(), new LineTo());
	PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
	pathTransition.setNode(cannonBall);

	pathTransition.play();
	cannonFireSound.play();

    }

    public boolean collisionCheck(Circle c, Rectangle r) {
	// Check the bounds of the rectangle and the circle.
	// True : if match
	// False : if not match
	if (r.getBoundsInParent().intersects(c.getBoundsInParent())) {
	    return true;
	} else {
	    return false;
	}
    }

    @FXML
    void startButtonPressed(ActionEvent event) {
	timeLabel.setText("Time remaining :" + startTime);

	// Define a TimeLine animation
	AnimationTimer timelineAnimation = new AnimationTimer() {

	    @Override
	    public void handle(long now) {
		// Making the targets and the blocker to move vertically with their velocity.
		target1Rect.setLayoutY(target1Rect.getLayoutY() + v1);
		target2Rect.setLayoutY(target2Rect.getLayoutY() + v2);
		target3Rect.setLayoutY(target3Rect.getLayoutY() + v3);
		target4Rect.setLayoutY(target4Rect.getLayoutY() + v4);
		target5Rect.setLayoutY(target5Rect.getLayoutY() + v5);
		target6Rect.setLayoutY(target6Rect.getLayoutY() + v6);
		target7Rect.setLayoutY(target7Rect.getLayoutY() + v7);
		target8Rect.setLayoutY(target8Rect.getLayoutY() + v8);
		blockerRect.setLayoutY(blockerRect.getLayoutY() + vBlock);

		// Decrease the time of the game.
		startTime--;

		// +) If any of the targets or the blocker reach to the top or bottom of the
		// window
		// => bounce it back by assign -1 to each velocity.
		// +) Check collision that may happen, if collision occurs then play the sound
		// and relocate the position of the target (relocate outside of the pane) and
		// set its visible to false.
		// +) If a collision occurs, create a new Path that will allow the cannonBall to
		// stop and avoid going further. Then remove the cannonBall from the pane.

		if (target1Rect.getLayoutY() + target1Rect.getHeight() > pane.getHeight()
			|| target1Rect.getLayoutY() < 0) {
		    v1 *= -1;

		}
		if (collisionCheck(cannonBall, target1Rect)) {

		    targetHitSound.play();
		    target1Rect.setVisible(false); // If its hit set the visibility to false.
		    target1Rect.relocate(1000, 1000); // Move the target to location (1000,1000).
		    // Create a new path to stop the cannonBall to moving through the rectangle.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    // Remove the cannonBall from the pane.
		    pane.getChildren().remove(cannonBall);
		    // Increase the score.
		    scoreBoardLabel.setText(String.valueOf(++score));
		    // Increase the time.
		    startTime = startTime + 30;
		    timeLabel.setText(String.valueOf(startTime));

		}

		if (target2Rect.getLayoutY() + target2Rect.getHeight() > pane.getHeight()
			|| target2Rect.getLayoutY() < 0) {
		    v2 *= -1;

		}
		if (collisionCheck(cannonBall, target2Rect)) {

		    targetHitSound.play();
		    target2Rect.setVisible(false); // If its hit set the visibility to false.
		    target2Rect.relocate(1000, 1000); // Move the target to location (1000,1000).
		    // Create a new path to stop the cannonBall to moving through the rectangle.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    // Remove the cannonBall from the pane.
		    pane.getChildren().remove(cannonBall);
		    // Increase the score.
		    scoreBoardLabel.setText(String.valueOf(++score));
		    // Increase the time.
		    startTime = startTime + 30;
		    timeLabel.setText(String.valueOf(startTime));

		}

		if (target3Rect.getLayoutY() + target3Rect.getHeight() > pane.getHeight()
			|| target3Rect.getLayoutY() < 0) {
		    v3 *= -1;

		}
		if (collisionCheck(cannonBall, target3Rect)) {

		    targetHitSound.play();
		    target3Rect.setVisible(false); // If its hit set the visibility to false.
		    target3Rect.relocate(1000, 1000); // Move the target to location (1000,1000).
		    // Create a new path to stop the cannonBall to moving through the rectangle.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    // Remove the cannonBall from the pane.
		    pane.getChildren().remove(cannonBall);
		    // Increase the score.
		    scoreBoardLabel.setText(String.valueOf(++score));
		    // Increase the time.
		    startTime = startTime + 30;
		    timeLabel.setText(String.valueOf(startTime));

		}

		if (target4Rect.getLayoutY() + target4Rect.getHeight() > pane.getHeight()
			|| target4Rect.getLayoutY() < 0) {
		    v4 *= -1;

		}
		if (collisionCheck(cannonBall, target4Rect)) {

		    targetHitSound.play();
		    target4Rect.setVisible(false); // If its hit set the visibility to false.
		    target4Rect.relocate(1000, 1000); // Move the target to location (1000,1000).
		    // Create a new path to stop the cannonBall to moving through the rectangle.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    // Remove the cannonBall from the pane.
		    pane.getChildren().remove(cannonBall);
		    // Increase the score.
		    scoreBoardLabel.setText(String.valueOf(++score));
		    // Increase the time.
		    startTime = startTime + 30;
		    timeLabel.setText(String.valueOf(startTime));

		}

		if (target5Rect.getLayoutY() + target5Rect.getHeight() > pane.getHeight()
			|| target5Rect.getLayoutY() < 0) {
		    v5 *= -1;

		}
		if (collisionCheck(cannonBall, target5Rect)) {

		    targetHitSound.play();
		    target5Rect.setVisible(false); // If its hit set the visibility to false.
		    target5Rect.relocate(1000, 1000); // Move the target to location (1000,1000).
		    // Create a new path to stop the cannonBall to moving through the rectangle.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    // Remove the cannonBall from the pane.
		    pane.getChildren().remove(cannonBall);
		    // Increase the score.
		    scoreBoardLabel.setText(String.valueOf(++score));
		    // Increase the time.
		    startTime = startTime + 30;
		    timeLabel.setText(String.valueOf(startTime));

		}

		if (target6Rect.getLayoutY() + target6Rect.getHeight() > pane.getHeight()
			|| target6Rect.getLayoutY() < 0) {
		    v6 *= -1;

		}
		if (collisionCheck(cannonBall, target6Rect)) {
		    targetHitSound.play();
		    target6Rect.setVisible(false); // If its hit set the visibility to false.
		    target6Rect.relocate(1000, 1000); // Move the target to location (1000,1000).
		    // Create a new path to stop the cannonBall to moving through the rectangle.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    // Remove the cannonBall from the pane.
		    pane.getChildren().remove(cannonBall);
		    // Increase the score.
		    scoreBoardLabel.setText(String.valueOf(++score));
		    // Increase the time.
		    startTime = startTime + 30;
		    timeLabel.setText(String.valueOf(startTime));

		}

		if (target7Rect.getLayoutY() + target7Rect.getHeight() > pane.getHeight()
			|| target7Rect.getLayoutY() < 0) {
		    v7 *= -1;

		}
		if (collisionCheck(cannonBall, target7Rect)) {

		    targetHitSound.play();
		    target7Rect.setVisible(false); // If its hit set the visibility to false.
		    target7Rect.relocate(1000, 1000); // Move the target to location (1000,1000).
		    // Create a new path to stop the cannonBall to moving through the rectangle.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    // Remove the cannonBall from the pane.
		    pane.getChildren().remove(cannonBall);
		    // Increase the score.
		    scoreBoardLabel.setText(String.valueOf(++score));
		    // Increase the time.
		    startTime = startTime + 30;
		    timeLabel.setText(String.valueOf(startTime));

		}

		if (target8Rect.getLayoutY() + target8Rect.getHeight() > pane.getHeight()
			|| target8Rect.getLayoutY() < 0) {
		    v8 *= -1;

		}
		if (collisionCheck(cannonBall, target8Rect)) {

		    targetHitSound.play();
		    target8Rect.setVisible(false); // If its hit set the visibility to false.
		    target8Rect.relocate(1000, 1000); // Move the target to location (1000,1000).
		    // Create a new path to stop the cannonBall to moving through the rectangle.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    // Remove the cannonBall from the pane.
		    pane.getChildren().remove(cannonBall);
		    // Increase the score by 1.
		    scoreBoardLabel.setText(String.valueOf(++score));
		    // Increase the time.
		    startTime = startTime + 30;
		    timeLabel.setText(String.valueOf(startTime));

		}

		if (blockerRect.getLayoutY() + blockerRect.getHeight() > pane.getHeight()
			|| blockerRect.getLayoutY() < 0) {
		    vBlock *= -1;

		}
		if (collisionCheck(cannonBall, blockerRect)) {
		    blockerHitSound.play();
		    // If there's a collision between cannonBall and the blocker, then create a path
		    // for the cannonBall back to the cannon barrel.
		    Path path = new Path(new MoveTo(0, 0),
			    new LineTo(cannonRect.getLayoutX(), cannonRect.getLayoutY()));
		    PathTransition pathTransition = new PathTransition(Duration.seconds(1), path);
		    pathTransition.setNode(cannonBall);
		    pathTransition.play();
		    pane.getChildren().remove(cannonBall);
		    // Reduce the time.
		    startTime = startTime - 30;
		    timeLabel.setText(String.valueOf(startTime));
		}

		// Disable the start button during the running time of the game
		startButton.setDisable(true);
		// Display the time remaining and score board
		timeLabel.setText("Time remaining : " + startTime);
		scoreBoardLabel.setText("Score : " + score);
		// If time interval is less than or equal to 0 or the score equal to 8 => stops
		// the game
		if (startTime <= 0.0 || score == 8) {
		    stop();
		    // Start a new game after timeouts or user is able to destroy all the targets.
		    newGame();

		}
	    }

	};

	timelineAnimation.start();

    }

    public void newGame() {
	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	alert.setTitle("Game Over");
	alert.setHeaderText(null);

	if (score < 8) {
	    alert.setHeaderText("LOSS");
	    alert.setContentText("Score: " + score);
	} else if (score == 8) {
	    alert.setHeaderText("WIN");
	    alert.setContentText("Score: " + score);
	}
	alert.show();
	// Set the location of the target back to the original position.
	target1Rect.setVisible(true);
	target1Rect.relocate(359, 44);
	target2Rect.setVisible(true);
	target2Rect.relocate(390, 241);
	target3Rect.setVisible(true);
	target3Rect.relocate(444, 172);
	target4Rect.setVisible(true);
	target4Rect.relocate(490, 371);
	target5Rect.setVisible(true);
	target5Rect.relocate(524, 14);
	target6Rect.setVisible(true);
	target6Rect.relocate(584, 89);
	target7Rect.setVisible(true);
	target7Rect.relocate(629, 314);
	target8Rect.setVisible(true);
	target8Rect.relocate(671, 44);
	// Set the default time and score and display the start button
	startTime = 1000;
	score = 0;
	startButton.setDisable(false);
    }

}
