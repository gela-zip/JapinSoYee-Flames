package flamesgame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class FXMLDocumentController implements Initializable {

    @FXML private MediaView mediaView;
    @FXML private ImageView backgroundImage;
    @FXML private Button startButton, skipButton, pauseButton, dialogueNextButton;
    @FXML private VBox genderSelectionBox, flamesInputBox, endingResultBox, dialogueBox;

    @FXML private ImageView gbPreview1, gbPreview2, bbPreview1, bbPreview2, ggPreview1, ggPreview2;
    @FXML private TextField player1Input, player2Input;
    @FXML private ComboBox<String> expectationDropdown;
    @FXML private ImageView p1PortraitView, p2PortraitView;

    @FXML private Label planetNameLabel, flamesResultLabel, expectationResultLabel, scoreLabel, inputErrorLabel;
    @FXML private ImageView charLeftView, charCenterView, charRightView;
    @FXML private Label speakerLabel, dialogueTextLabel;

    private MediaPlayer videoPlayer;
    private MediaPlayer bgmPlayer;

    private String genderPairing = "Girl & Boy";
    private String flamesMeaning = "";
    private String planetName = "";
    private String gameState = "";
    private String userExpectation = "Friends";
    
    private int points = 0;
    private int currentScene = 0;
    private int dialogueIndex = 0;

    private final String girlChoiceAsset = "GirlChooseGender.png";
    private final String boyChoiceAsset  = "BoyGenderChoose.png";
    private String p1ChoiceAsset = girlChoiceAsset;
    private String p2ChoiceAsset = boyChoiceAsset;

    private String char1StandingOnlyAsset = "GirlStanding.png";
    private String char2StandingOnlyAsset = "BoyStanding.png";

    private String char1RaisingPath = "", char1SadPath = "", char1StandingPath = "", char1AngryPath = "";
    private String char2RaisingPath = "", char2SadPath = "", char2StandingPath = "", char2AngryPath = "";
    
    private final String lanceStandingPath = "LanceStanding.png";
    private final String lanceRaisingPath  = "LanceRaisingHisArms.png";

    private String[][] storyDialogue;
    private GameLogic gameLogic = new GameLogic();
    
    @FXML private VBox winLoseBannerBox;
    @FXML private ImageView winLoseBannerView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hideAllPanels();

        if (expectationDropdown != null) {
            expectationDropdown.setItems(FXCollections.observableArrayList("Friends", "Lovers", "Acquaintances", "Married", "Enemies", "Soulmates"));
            expectationDropdown.setValue("Friends");
        }

        if (mediaView != null) { //Game window size
            mediaView.setFitWidth(800);
            mediaView.setFitHeight(600);
            mediaView.setPreserveRatio(false);
        }

        if (startButton != null) {
            startButton.setCursor(Cursor.HAND);
        }

        playBGM("GAME_START_MUSIC.wav"); //GAME START

        playVideo("intro_start.mp4", false, () -> {
            Platform.runLater(() -> {
                if (videoPlayer != null) {
                    videoPlayer.pause();
                }
                hideVideoControls();
                if (startButton != null) {
                    startButton.setVisible(true);
                    startButton.toFront();
                }
            });
        });
    }

    @FXML
    private void handleStartAction(ActionEvent event) { //START BUTTON
        Platform.runLater(() -> {
            if (startButton != null) {
                startButton.setVisible(false);
            }

            if (videoPlayer != null) {
                videoPlayer.stop();
                videoPlayer.dispose();
                videoPlayer = null;
            }
            if (mediaView != null) {
                mediaView.setMediaPlayer(null);
                mediaView.setVisible(false);
                mediaView.toBack();
            }

            showGenderSelectionScene();
        });
    }

    private void showGenderSelectionScene() { //SCENE 0: Character Selection
        currentScene = 1;
        setBackdrop("earthbackground.jpeg");

        loadImage(gbPreview1, girlChoiceAsset);
        loadImage(gbPreview2, boyChoiceAsset);
        loadImage(bbPreview1, boyChoiceAsset);
        loadImage(bbPreview2, boyChoiceAsset);
        loadImage(ggPreview1, girlChoiceAsset);
        loadImage(ggPreview2, girlChoiceAsset);

        if (genderSelectionBox != null) {
            genderSelectionBox.setVisible(true);
            genderSelectionBox.setDisable(false);
            genderSelectionBox.toFront(); 
        }
    }

    //SCENE 0 Choices
    @FXML private void handleGirlBoyChoice(ActionEvent event) { processPairingSelection("Girl & Boy"); }
    @FXML private void handleBoyBoyChoice(ActionEvent event) { processPairingSelection("Boy & Boy"); }
    @FXML private void handleGirlGirlChoice(ActionEvent event) { processPairingSelection("Girl & Girl"); }

    private void processPairingSelection(String pairing) {
        this.genderPairing = pairing;
       
        boolean p1IsGirl = pairing.startsWith("Girl");
        boolean p2IsBoy  = pairing.endsWith("Boy");
        //Handling player genders
        p1ChoiceAsset = p1IsGirl ? girlChoiceAsset : boyChoiceAsset;
        p2ChoiceAsset = p2IsBoy  ? boyChoiceAsset  : girlChoiceAsset;

        char1StandingOnlyAsset = p1IsGirl ? "GirlStanding.png" : "BoyStanding.png";
        char2StandingOnlyAsset = p2IsBoy  ? "BoyStanding.png" : "GirlStanding.png";

        char1RaisingPath  = p1IsGirl ? "GirlRaisingHerHandNoSpace.png" : "BoyRaisingHandNoSpace.png";
        char1SadPath      = p1IsGirl ? "GirlSadNoSpace.png" : "BoySadNoSpace.png"; //ok conan gray
        char1StandingPath = p1IsGirl ? "GirlStandingNoSpace.png" : "BoyStandingNoSpace.png";

        char2RaisingPath  = p2IsBoy  ? "BoyRaisingHandNoSpace.png" : "GirlRaisingHerHandNoSpace.png";
        char2SadPath      = p2IsBoy  ? "BoySadNoSpace.png" : "GirlSadNoSpace.png";
        char2StandingPath = p2IsBoy  ? "BoyStandingNoSpace.png" : "GirlStandingNoSpace.png";
        
        char1AngryPath    = p1IsGirl ? "GirlAngry.png" : "BoyAngry.png";
        char2AngryPath    = p2IsBoy  ? "BoyAngry.png"  : "GirlAngry.png"; 

        if (genderSelectionBox != null) {
            genderSelectionBox.setVisible(false);
            genderSelectionBox.setDisable(true);
        }

        playScene1Video(); //move to next SCENE 1 Introduction
    }

    private void playScene1Video() { //SCENE 1 BACKGROUND MEDIA
        currentScene = 2;
        playBGM("EARTH_TRANSITION.wav");
        playVideo("scene1_video.mp4", true, this::startScene1Dialogue);
    }

    private void startScene1Dialogue() { //SCENE 1 DIALOGUE
        stopVideo();
        hideVideoControls();

        playBGM("POST_APOC_CASUAL_TALK.wav");
        setBackdrop("earthbackground.jpeg");

        this.storyDialogue = new String[][]{
            {"Player 1", "*Sigh* Look at this place... I still can't believe it."},
            {"Player 2", "I know. Everything's gone... Honestly, it's a miracle we made it out at all."},
            {"Player 1", "Right? How are we even standing here? Feels like someone up there was watching out for us."},
            {"Player 2", "Maybe... but look around. It's just us now. We're completely on our own."},
            {"Player 1", "..."}
        };
        showDialogueBox();
    }

    private void transitionToScene3Video() { //Transition to SCENE 2 Lance Arrives, BACKGROUND MEDIA
        hideAllPanels();
        
        playBGM("SPACESHIP_DROP.wav");
        playVideo("scene3_video.mp4", false, null);

        if (videoPlayer != null) {
            videoPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null && newVal.toSeconds() >= 6.0 && currentScene == 2) {
                    Platform.runLater(() -> {
                        if (videoPlayer != null) {
                            videoPlayer.pause();
                        }
                        startScene3LanceEncounter();
                    });
                }
            });
        }
    }

    private void startScene3LanceEncounter() { //SCENE 2 DIALOGUE
        currentScene = 3;
        hideVideoControls();

        if (mediaView != null) {
            mediaView.setVisible(true);
            mediaView.toBack();
        }

        if (backgroundImage != null) {
            backgroundImage.setVisible(false);
        }

        this.storyDialogue = new String[][]{
            {"Player 1", "Wait... l-look over there! Is that-?"},
            {"Player 2", "A spaceship! It's actually landing right here!"},
            {"Player 1", "Finally, a way off this dead rock!"},
            {"Lance", "Greetings, Earthlings. I can grant you sanctuary on board, but first, a mandatory protocol."},
            {"Player 1", "Protocol? What kind of protocol?"},
            {"Lance", "A compatibility assessment. You know it as... FLAMES."},
            {"Player 1", "Huh?! You mean that childhood name game?"},
            {"Lance", "Indeed. Enter your names into the interface now."},
            {"Player 2", "Well, it's better than staying here. Let's give it a shot."}
        };

        showDialogueBox();
    }

    private void showFlamesInputScreen() { //SCENE 2 FLAMES Interface
        if (dialogueBox != null) dialogueBox.setVisible(false);
        clearImageView(charLeftView);
        clearImageView(charCenterView);
        clearImageView(charRightView);

        loadImage(p1PortraitView, p1ChoiceAsset);
        loadImage(p2PortraitView, p2ChoiceAsset);

        playBGM("FLAMES_GAME_MATCH.wav");

        if (flamesInputBox != null) {
            bringToTopLevelFront(flamesInputBox);
        }
    }

    @FXML
    private void handleFlamesSubmit(ActionEvent event) { //SCENE 2 FLAMES ERROR TRAPPING
        String name1 = player1Input.getText().trim();
        String name2 = player2Input.getText().trim();

        hideInputError(); //if mironch error

        if (name1.isEmpty() || name2.isEmpty()) { //blank input
            showInputError("Please enter both names.");
            return;
        }

        if (!name1.matches("[a-zA-Z ]+") || !name2.matches("[a-zA-Z ]+")) { //names can only have letters and spaces
            showInputError("Names can only contain letters and spaces.");
            return;
        }

        if (expectationDropdown != null && expectationDropdown.getValue() != null) {
            this.userExpectation = expectationDropdown.getValue();
        }

        executeGameLogic(name1, name2, this.userExpectation); //call executeGameLogic from GameLogic.java

        if (flamesInputBox != null) {
            flamesInputBox.setVisible(false);
        }

        playBGM("SPACESHIP_DROP.wav"); //Nisud na silas spaceship haha
        resumeLanceEncounterVideo(); //close FLAMES interface
    }

    //game logic plug from GameLogic.java
    private void executeGameLogic(String name1, String name2, String expectation) {
        gameLogic.resetGame(); //init
        
        int tally = gameLogic.getTally(name1, name2); //get tally
        this.flamesMeaning = gameLogic.flamesResult(tally); //index tally on FLAMES
        gameLogic.calculateRelationshipPoints(flamesMeaning, expectation); //set points

        Planet planet = gameLogic.choosePlanet(); //random planet chooser
        this.planetName = planet.getName();
        gameLogic.calculatePlanetPoints(planet); //set planet points

        this.points = gameLogic.getUserPoints(); //get points to figure if game is won

        if (gameLogic.isGameWon()) {
            this.gameState = "Win";
        } else {
            this.gameState = "Lose";
        }
    }    
    
    private void resumeLanceEncounterVideo() { //Cleaning up
        if (mediaView != null) {
            mediaView.setVisible(true);
            mediaView.toBack();
        }

        if (videoPlayer != null) {
            videoPlayer.setOnEndOfMedia(this::startSpaceshipBackgroundScene);
            videoPlayer.play();
        } else {
            startSpaceshipBackgroundScene(); //MOVE TO SCENE 3
        }
    }

    private void startSpaceshipBackgroundScene() { //SCENE 3 MEDIA BACKGROUND AND DIALOGUE
        currentScene = 4;
        stopVideo();

        playBGM("SPACESHIP_DROP.wav");
        setBackdrop("SPACESHIP BACKGROUND.png");

        if (gameLogic.isRandomResult()) {
            this.storyDialogue = new String[][]{
                {"Lance", "Wooow, you guys don't have a single matching letter in your names!"},
                {"Player 1", "Wait, what does that even mean? So what?"},
                {"Lance", "Oh well! The cosmic wheel of fate dictates you are" + flamesMeaning + " from now on."},
                {"Lance", "Now then, let's spin the wheel to see which random planet we'll land on!"},
                {"Player 2", "HEY-!"}
            };
        } else {
            this.storyDialogue = new String[][]{
                {"Lance", "Well, look at that! The calculations are complete."},
                {"Player 1", "And? What did it say?"},
                {"Lance", "Oh, so you two are " + flamesMeaning + ", huh?"},
                {"Lance", "Now then, let's spin the wheel to see which random planet we'll land on!"},
                {"Player 2", "HEY-!"}
            };
        }

        showDialogueBox();
    }

    private void startSpaceScene() { //TRANSITION FOR SCENE 4 with planet in corner haha
        currentScene = 5;
        stopVideo();

        playBGM("SPACESHIP_DROP.wav");
        setBackdrop("space.png");

        showFinalPlanetOutcome();
    }
    
    @FXML
    private void handleContinueToPlanet(ActionEvent event) {
        startPlanetScene(); // Advances scene state correctly to Scene 7
    }
    
    private void showFinalPlanetOutcome() {
        currentScene = 6;
        hideAllPanels(); 

        String planetImageAsset = "";
        if ("Fomalhaut".equalsIgnoreCase(planetName)) {
            planetImageAsset = "PLANET_FOMALHAUT.png";
        } else if ("LHS 1140 b".equalsIgnoreCase(planetName)) {
            planetImageAsset = "PLANET_LHS1140.png";
        } else if ("Alpha Wolf".equalsIgnoreCase(planetName)) {
            planetImageAsset = "PLANET_ALPHAWOLF.png";
        } else if ("Mercury".equalsIgnoreCase(planetName)) {
            planetImageAsset = "PLANET_MERCURY.png";
        } else if ("ERIS".equalsIgnoreCase(planetName)) {
            planetImageAsset = "PLANET_ERIS.png";
        } else if ("Slytherin".equalsIgnoreCase(planetName)) {
            planetImageAsset = "PLANET_SLYTHERIN.png";
        }

        if (charLeftView != null) charLeftView.setVisible(false);
        if (charCenterView != null) charCenterView.setVisible(false);

        if (!planetImageAsset.isEmpty() && charRightView != null) { //overlays planet on the bottom right
            loadImage(charRightView, planetImageAsset);
            charRightView.setVisible(true);
            charRightView.setOpacity(1.0);
        }

        playVideo("spaceship_travel.mp4", false, () -> {
            Platform.runLater(() -> {
                if (charRightView != null) {
                    clearImageView(charRightView);
                    charRightView.setVisible(false);
                }
                startPlanetScene();
            });
        });

        if (charRightView != null) {
            bringToTopLevelFront(charRightView);
        }
    }    
    
    private void startPlanetScene() {
        currentScene = 7;

        if (endingResultBox != null) {
            endingResultBox.setVisible(false);
        }

        if (planetName.equals("Fomalhaut")) {
            startFomalhautScene();
        } else if (planetName.equals("LHS 1140 b")) {
            startLHS1140Scene();
        } else if (planetName.equals("Alpha Wolf")) {
            startAlphaWolfScene();
        } else if (planetName.equals("Mercury")) {
            startMercuryScene();
        } else if (planetName.equals("ERIS")) {
            startErisScene();
        } else if (planetName.equals("Slytherin")) {
            startSlytherinScene();
        }
    }
    //planet branches
    private void startFomalhautScene() {
        currentScene = 7;
        setBackdrop("FOMALHAUT.png");
        this.storyDialogue = new String[][]{
            {"Player 1", "What is this planet?"},
            {"Player 2", "Disgursting sh1et.."}
        };
            showDialogueBox();
        }

    private void startLHS1140Scene() {
        currentScene = 7;
        setBackdrop("LHS1140.png");
        this.storyDialogue = new String[][]{
            {"Player 1", "We landed on a planet that is similar to our planet Earth!"},
            {"Player 2", "We can rebuild our lives here"}
        };
            showDialogueBox();
        }
    
    private void startAlphaWolfScene() {
        currentScene = 7;
        setBackdrop("ALPHAWOLF.png");
        this.storyDialogue = new String[][]{
            {"Player 1", "It's so cold in here *brrrr*"},
            {"Player 2", "*Suffocates*"}
        };
        showDialogueBox();
    }
    
    private void startMercuryScene() {
        currentScene = 7;
        setBackdrop("MERCURY.png");
        this.storyDialogue = new String[][]{
            {"Player 1", "Did we just land on the planet near the sun?"},
            {"Player 2", "I can see the sun right up close.. It's so hot!"}
        };
        showDialogueBox();
    }
    
    private void startErisScene() {
        currentScene = 7;
        setBackdrop("ERIS.png");
        this.storyDialogue = new String[][]{
            {"Player 1", "Woah.. look at the mountains!"},
            {"Player 2", "The air here is so fresh!"}
        };
        showDialogueBox();
    }
    
    private void startSlytherinScene() {
        currentScene = 7;
        setBackdrop("SLYTHERIN.png");
        this.storyDialogue = new String[][]{
            {"Player 1", "THIS PLANET IS FULL OF STORMS!"},
            {"Player 2", "OUR SPACESHIP IS BEING CARRIED AWAY!! *Screams*"}
        };
        showDialogueBox();
    }
    
    private void startFinalResultsScene() {
        currentScene = 8;
        hideAllPanels();
        stopAudio();

        setBackdrop("space.png");

        if (charLeftView != null) charLeftView.setVisible(false);
        if (charCenterView != null) charCenterView.setVisible(false);
        if (charRightView != null) charRightView.setVisible(false);

        boolean matchedExpectation = this.flamesMeaning != null && 
                                     this.flamesMeaning.equalsIgnoreCase(this.userExpectation);
        boolean isWin = "Win".equalsIgnoreCase(this.gameState);

        if (isWin) {
            playBGM("YOU_WIN.wav");
        } else {
            playBGM("YOU_LOSE.wav");
        }

        if (planetNameLabel != null) {
            planetNameLabel.setText("Landed on: " + (planetName != null ? planetName : "Unknown"));
        }

        if (flamesResultLabel != null) {
            flamesResultLabel.setText("FLAMES Result: " + (flamesMeaning != null ? flamesMeaning : "None"));
        }

        if (expectationResultLabel != null) {
            expectationResultLabel.setText("Expected: " + (userExpectation != null ? userExpectation : "None") 
                + (matchedExpectation ? " ✓" : " ✗"));
        }

        if (scoreLabel != null) {
            scoreLabel.setText("Total Points: " + points + " / 100");
        }

        if (endingResultBox != null) {
            endingResultBox.setVisible(true);
            bringToTopLevelFront(endingResultBox);
        }

        String bannerImageAsset = isWin ? "You_Win.png" : "You_Lose.png";

        if (winLoseBannerView != null) {
            loadImage(winLoseBannerView, bannerImageAsset);
            winLoseBannerView.setVisible(true);
        }

        if (winLoseBannerBox != null) {
            winLoseBannerBox.setVisible(true);
            winLoseBannerBox.setManaged(true);
            winLoseBannerBox.toFront();
            bringToTopLevelFront(winLoseBannerBox);
        }
    }

    @FXML
    private void onRetryButtonClicked() {
        stopAudio();
        hideAllPanels();

        // Reset game state variables
        this.points = 0;
        this.gameState = "";
        this.planetName = "";
        this.flamesMeaning = "";
        this.userExpectation = "";

        if (player1Input != null) player1Input.clear();
        if (player2Input != null) player2Input.clear();

        // Reset scene state to Start Game Scene (Scene 0)
        currentScene = 0;
        playBGM("GAME_START_MUSIC.wav");

        // Play intro video/backdrop and show the START button
        playVideo("intro_start.mp4", false, () -> {
            Platform.runLater(() -> {
                if (videoPlayer != null) {
                    videoPlayer.pause();
                }
                hideVideoControls();
                if (startButton != null) {
                    startButton.setVisible(true);
                    startButton.toFront();
                }
            });
        });
    }

    @FXML
    private void onExitGameButtonClicked() {
        stopAudio();
        stopVideo();

        javafx.application.Platform.exit();
        System.exit(0);
    }

    private void stopAudio() {
        stopBGM();
        stopVideo();
    }

    private void showDialogueBox() { //handles the dialogue and display speaking character sprite
        if (charCenterView != null) charCenterView.setVisible(true);
        if (charRightView != null) charRightView.setVisible(true);

        if (dialogueBox != null) {
            bringToTopLevelFront(dialogueBox);
        }

        dialogueIndex = 0;
        updateDialogueView();
    }

    private void updateDialogueView() {
        if (dialogueIndex >= storyDialogue.length) {
            advanceSceneFromDialogue();
            return;
        }

        String speaker = storyDialogue[dialogueIndex][0];
        String text = storyDialogue[dialogueIndex][1];

        if (speakerLabel != null) speakerLabel.setText(speaker);
        if (dialogueTextLabel != null) dialogueTextLabel.setText(text);

        if (currentScene == 4) {
            if (charLeftView != null) charLeftView.setVisible(true);
            if (charCenterView != null) charCenterView.setVisible(true);
            if (charRightView != null) charRightView.setVisible(true);

            boolean isLanceSpeaking = speaker.equals("Lance");
            loadImage(charRightView, isLanceSpeaking ? lanceRaisingPath : lanceStandingPath);

            if (dialogueIndex == 0) {
                loadImage(charLeftView, char1StandingOnlyAsset);
                loadImage(charCenterView, char2StandingOnlyAsset);
            } else if (dialogueIndex == 1) {
                loadImage(charLeftView, char1AngryPath);
                loadImage(charCenterView, char2StandingOnlyAsset);
            } else if (dialogueIndex >= 2) {
                loadImage(charLeftView, char1AngryPath);
                loadImage(charCenterView, char2AngryPath);
            }

            setSpeakerOpacity(speaker.equals("Player 1") ? 1.0 : 0.5,
                speaker.equals("Player 2") ? 1.0 : 0.5,
                isLanceSpeaking ? 1.0 : 0.5);
        } else if (currentScene == 3) {
            if (charLeftView != null) charLeftView.setVisible(true);
            if (charCenterView != null) charCenterView.setVisible(true);

            loadImage(charLeftView, char1StandingPath);
            loadImage(charCenterView, char2StandingPath);

            boolean isLanceSpeaking = speaker.equals("Lance");
            
            if (charRightView != null) {
                if (dialogueIndex < 3) {
                    charRightView.setVisible(false);
                } else {
                    charRightView.setVisible(true);
                    loadImage(charRightView, isLanceSpeaking ? lanceRaisingPath : lanceStandingPath);
                }
            }

            if (speaker.equals("Player 1")) {
                loadImage(charLeftView, char1RaisingPath);
            } else if (speaker.equals("Player 2")) {
                loadImage(charCenterView, char2RaisingPath);
            }

            setSpeakerOpacity(speaker.equals("Player 1") ? 1.0 : 0.5,
                speaker.equals("Player 2") ? 1.0 : 0.5,
                isLanceSpeaking ? 1.0 : 0.5);
        } else {
            if (charCenterView != null) charCenterView.setVisible(false);
            if (charLeftView != null) charLeftView.setVisible(true);
            if (charRightView != null) charRightView.setVisible(true);

            // Check if we are in the planet outcome scene (Scene 7) and if it resulted in a loss
            boolean isPlanetScene = (currentScene == 7);
            boolean isBadPlanet = isPlanetScene && "Lose".equalsIgnoreCase(this.gameState);

            if (isBadPlanet) {
                if (speaker.equals("Player 1")) {
                    setSpeakerOpacity(1.0, 0.0, 0.5);
                } else if (speaker.equals("Player 2")) {
                    setSpeakerOpacity(0.5, 0.0, 1.0);
                }
                loadImage(charLeftView, char1SadPath);
                loadImage(charRightView, char2SadPath);
            } else {
                if (speaker.equals("Player 1")) {
                    setSpeakerOpacity(1.0, 0.0, 0.5);
                    loadImage(charLeftView, (dialogueIndex == 0 && currentScene == 2) ? char1SadPath : char1RaisingPath);
                    loadImage(charRightView, char2StandingPath);
                } else if (speaker.equals("Player 2")) {
                    setSpeakerOpacity(0.5, 0.0, 1.0);
                    loadImage(charLeftView, char1StandingPath);
                    loadImage(charRightView, char2RaisingPath);
                }
            }
        }

        if (charLeftView != null) charLeftView.toFront();
        if (charCenterView != null) charCenterView.toFront();
        if (charRightView != null) charRightView.toFront();

        if (dialogueNextButton != null) {
            dialogueNextButton.setVisible(true);
            dialogueNextButton.toFront();
        }
    }

    private void setSpeakerOpacity(double left, double center, double right) {
        if (charLeftView != null) charLeftView.setOpacity(left);
        if (charCenterView != null) charCenterView.setOpacity(center);
        if (charRightView != null) charRightView.setOpacity(right);
    }

    private void advanceSceneFromDialogue() {
    switch (currentScene) {
        case 2: transitionToScene3Video(); break;
        case 3: showFlamesInputScreen(); break;
        case 4: startSpaceScene(); break;
        case 5: showFinalPlanetOutcome(); break;
        case 6: startPlanetScene(); break;
        case 7: startFinalResultsScene(); break;
    }
}

    @FXML
    private void handleNextDialogue(ActionEvent event) {
        dialogueIndex++;
        updateDialogueView();
    }

    @FXML
    private void handleSkipAction(ActionEvent event) {
        stopVideo();
        hideVideoControls();

        switch (currentScene) {
            case 0: showGenderSelectionScene(); break;
            case 1: playScene1Video(); break;
            case 2: startScene1Dialogue(); break;
            case 3: showFlamesInputScreen(); break;
            case 4: startSpaceScene(); break;
            case 5: showFinalPlanetOutcome(); break;
        }
    }

    @FXML
    private void handlePauseToggle(ActionEvent event) {
        if (videoPlayer == null) return;
        if (videoPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            videoPlayer.pause();
            if (pauseButton != null) pauseButton.setText("▶ PLAY");
        } else if (videoPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            videoPlayer.play();
            if (pauseButton != null) pauseButton.setText("⏸ PAUSE");
        }
    }

    private void playBGM(String filename) {
        if (bgmPlayer != null && bgmPlayer.getMedia().getSource().contains(filename)) {
            if (bgmPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                return; 
            }
        }

        stopBGM();
        try {
            URL resource = getClass().getResource("/flamesgame/assets/" + filename);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                bgmPlayer = new MediaPlayer(media);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.play();
            }
        } catch (Exception e) {
            System.out.println("Could not load BGM: " + filename);
        }
    }

    private void playVideo(String filename, boolean showControls, Runnable onEndAction) {
        stopVideo(); 

        if (mediaView != null) {
            mediaView.setVisible(true);
            mediaView.toFront();
        }

        try {
            URL resource = getClass().getResource("/flamesgame/assets/" + filename);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                videoPlayer = new MediaPlayer(media);
                
                videoPlayer.setMute(false);
                videoPlayer.setVolume(0.8);
                mediaView.setMediaPlayer(videoPlayer);

                videoPlayer.setOnEndOfMedia(() -> {
                    hideVideoControls();
                    if (onEndAction != null) {
                        onEndAction.run();
                    }
                });

                videoPlayer.setOnError(() -> {
                    System.out.println("Video playback error: " + videoPlayer.getError());
                    hideVideoControls();
                    if (onEndAction != null) onEndAction.run();
                });

                videoPlayer.play();

                if (showControls) {
                    showVideoControls();
                } else {
                    hideVideoControls();
                }
            } else {
                System.out.println("Video file not found: " + filename);
                if (onEndAction != null) onEndAction.run();
            }
        } catch (Exception e) {
            System.out.println("Exception playing video: " + e.getMessage());
            hideVideoControls();
            if (onEndAction != null) onEndAction.run();
        }
    }

    private void stopBGM(){
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
        }
    }

    private void stopVideo(){
        if (mediaView != null) {
            mediaView.setVisible(false);
            mediaView.toBack();
        }
        if (videoPlayer != null) {
            videoPlayer.stop();
            videoPlayer.dispose();
            videoPlayer = null;
        }
        hideVideoControls();
    }

    private void bringToTopLevelFront(Node node){
        if (node == null) return;
        node.setVisible(true);
        node.setDisable(false);
        node.toFront();
        
        Node current = node;
        while (current.getParent() != null) {
            Node parent = current.getParent();
            parent.setVisible(true);
            parent.setDisable(false);
            parent.toFront();
            current = parent;
        }
    }

    private void showVideoControls(){
        if (mediaView != null) {
            mediaView.setVisible(true);
            mediaView.toFront();
        }

        if (skipButton != null) {
            bringToTopLevelFront(skipButton);
        }

        if (pauseButton != null) {
            pauseButton.setText("⏸ PAUSE");
            bringToTopLevelFront(pauseButton);
        }
    }

    private void hideVideoControls(){
        if (skipButton != null) skipButton.setVisible(false);
        if (pauseButton != null) pauseButton.setVisible(false);
    }

    private void setBackdrop(String filename){ //hide mediaView so it doesn't block the background image
        if (mediaView != null) {
            mediaView.setVisible(false);
    }

    if (backgroundImage != null){ //keep it behind UI panels & dialogue, but above mediaView if mediaView is hidden
        loadImage(backgroundImage, filename);
        backgroundImage.setVisible(true);
        backgroundImage.toBack();
    }
}

    private void loadImage(ImageView view, String filename){
        if (view == null || filename == null || filename.isEmpty()) return;
        try {
            URL resource = getClass().getResource("/flamesgame/assets/" + filename);
            if (resource != null) {
                view.setImage(new Image(resource.toExternalForm()));
            }
        } catch (Exception e) {
            System.out.println("Could not load image: " + filename);
        }
    }

    private void clearImageView(ImageView view){
        if (view != null) {
            view.setImage(null);
            view.setVisible(false);
        }
    }

    private void hideAllPanels(){
        if (genderSelectionBox != null) genderSelectionBox.setVisible(false);
        if (flamesInputBox != null) flamesInputBox.setVisible(false);
        if (endingResultBox != null) endingResultBox.setVisible(false);
        if (dialogueBox != null) dialogueBox.setVisible(false);
        if (winLoseBannerBox != null) winLoseBannerBox.setVisible(false);

        clearImageView(charLeftView);
        clearImageView(charCenterView);
        clearImageView(charRightView);
        clearImageView(winLoseBannerView);
    }

    //helpers sa error
    private void showInputError(String message){
        inputErrorLabel.setText(message);
        inputErrorLabel.setVisible(true);
        inputErrorLabel.setManaged(true);
    }
    
    private void hideInputError(){
        inputErrorLabel.setText("");
        inputErrorLabel.setVisible(false);
        inputErrorLabel.setManaged(false);
    }  
}

