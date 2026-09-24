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
            expectationDropdown.setItems(FXCollections.observableArrayList(
                "Friends", "Lovers", "Acquaintances", "Married", "Enemies", "Soulmates"
            ));
            expectationDropdown.setValue("Friends");
        }

        if (mediaView != null) {
            mediaView.setFitWidth(800);
            mediaView.setFitHeight(600);
            mediaView.setPreserveRatio(false);
        }

        if (startButton != null) {
            startButton.setCursor(Cursor.HAND);
        }

        playBGM("GAME_START_MUSIC.wav");

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

    // --- Step 2: Start Button Action ---
    @FXML
    private void handleStartAction(ActionEvent event) {
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

    private void showGenderSelectionScene() {
        currentScene = 1;
        playBGM("GAME_START_MUSIC.wav");
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

    // --- Step 3: Gender Selection ---
    @FXML private void handleGirlBoyChoice(ActionEvent event) { processPairingSelection("Girl & Boy"); }
    @FXML private void handleBoyBoyChoice(ActionEvent event) { processPairingSelection("Boy & Boy"); }
    @FXML private void handleGirlGirlChoice(ActionEvent event) { processPairingSelection("Girl & Girl"); }

    private void processPairingSelection(String pairing) {
        this.genderPairing = pairing;

        boolean p1IsGirl = pairing.startsWith("Girl");
        boolean p2IsBoy  = pairing.endsWith("Boy");

        p1ChoiceAsset          = p1IsGirl ? girlChoiceAsset : boyChoiceAsset;
        p2ChoiceAsset          = p2IsBoy  ? boyChoiceAsset  : girlChoiceAsset;

        char1StandingOnlyAsset = p1IsGirl ? "GirlStanding.png" : "BoyStanding.png";
        char2StandingOnlyAsset = p2IsBoy  ? "BoyStanding.png"  : "GirlStanding.png";

        char1RaisingPath  = p1IsGirl ? "GirlRaisingHerHandNoSpace.png" : "BoyRaisingHandNoSpace.png";
        char1SadPath      = p1IsGirl ? "GirlSadNoSpace.png"            : "BoySadNoSpace.png";
        char1StandingPath = p1IsGirl ? "GirlStandingNoSpace.png"       : "BoyStandingNoSpace.png";

        char2RaisingPath  = p2IsBoy  ? "BoyRaisingHandNoSpace.png"      : "GirlRaisingHerHandNoSpace.png";
        char2SadPath      = p2IsBoy  ? "BoySadNoSpace.png"              : "GirlSadNoSpace.png";
        char2StandingPath = p2IsBoy  ? "BoyStandingNoSpace.png"         : "GirlStandingNoSpace.png";
        
        // Updated Angry Assets using GirlAngry.png and BoyAngry.png
        char1AngryPath    = p1IsGirl ? "GirlAngry.png" : "BoyAngry.png";
        char2AngryPath    = p2IsBoy  ? "BoyAngry.png"  : "GirlAngry.png"; 

        if (genderSelectionBox != null) {
            genderSelectionBox.setVisible(false);
            genderSelectionBox.setDisable(true);
        }

        playScene1Video();
    }

    private void playScene1Video() {
        currentScene = 2;
        playBGM("EARTH_TRANSITION.wav");
        playVideo("scene1_video.mp4", true, this::startScene1Dialogue);
    }

    // --- Dialogue Scene 1 ---
    private void startScene1Dialogue() {
        stopVideo();
        hideVideoControls();

        playBGM("POST_APOC_CASUAL_TALK.wav");
        setBackdrop("earthbackground.jpeg");

        this.storyDialogue = new String[][]{
            {"Player 1", "*Sigh* we're in such a sad state right now huh.."},
            {"Player 2", "Yeah, such a catastrophe destroyed the earth. We’re so lucky to be alive right now."},
            {"Player 1", "Imagining it right now, how did we even survive that? Were we protected by our personal guardian angels?"},
            {"Player 2", "It doesn't matter now, look around us. Everyone's dead, and we're the only ones alive."},
            {"Player 1", "*Contemplates*"}
        };
        showDialogueBox();
    }

    // --- Spaceship Encounter (Scene 3) ---
    private void transitionToScene3Video() {
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

    private void startScene3LanceEncounter() {
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
            {"Player 1", "Oh my gosh, is that a spaceship I see??!"},
            {"Player 2", "Yes!! It is a spaceship!!!"},
            {"Player 1", "This will be our chance, we are not gonna die!"},
            {"Lance", "Hey you two, I will be giving you a chance to live and ride on the spaceship, but you will be playing a FLAMES game first before I permit you to."},
            {"Player 1", "Waah? FLAMES game?"},
            {"Lance", "Yes, and the two of you will input your names for it."},
            {"Player 1", "Ok, lets do it."}
        };

        showDialogueBox();
    }

    // --- FLAMES Input ---
    private void showFlamesInputScreen() {
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
    private void handleFlamesSubmit(ActionEvent event) {
        String name1 = player1Input.getText().trim();
        String name2 = player2Input.getText().trim();

        // Hide previous error message
        hideInputError();

        // Check if either name is empty
        if (name1.isEmpty() || name2.isEmpty()) {
            showInputError("Please enter both names.");
            return;
        }

        // Check if names contain anything other than letters and spaces
        if (!name1.matches("[a-zA-Z ]+") || !name2.matches("[a-zA-Z ]+")) {
            showInputError("Names can only contain letters and spaces.");
            return;
        }

        if (expectationDropdown != null && expectationDropdown.getValue() != null) {
            this.userExpectation = expectationDropdown.getValue();
        }

        executeGameLogic(name1, name2, this.userExpectation);

        if (flamesInputBox != null) {
            flamesInputBox.setVisible(false);
        }

        playBGM("SPACESHIP_DROP.wav");
        resumeLanceEncounterVideo();
    }

    private void resumeLanceEncounterVideo() {
        if (mediaView != null) {
            mediaView.setVisible(true);
            mediaView.toBack();
        }

        if (videoPlayer != null) {
            videoPlayer.setOnEndOfMedia(this::startSpaceshipBackgroundScene);
            videoPlayer.play();
        } else {
            startSpaceshipBackgroundScene();
        }
    }

    // --- Spaceship Background Scene ---
    private void startSpaceshipBackgroundScene() {
        currentScene = 4;
        stopVideo();

        playBGM("SPACESHIP_DROP.wav");
        setBackdrop("SPACESHIP BACKGROUND.png");

        if (gameLogic.isRandomResult()) {
            this.storyDialogue = new String[][]{
                {"Lance", "Wooow, you guys really don't have any similar characters in your names, huh?"},
                {"Player 1", "What are you talking about?"},
                {"Lance", "Oh well! You guys are " + flamesMeaning + " from now on."},
                {"Lance", "Now then, let's spin the wheel for which random planet we'll land on!"},
                {"Player 2", "HEY-!"}
            };
        } else {
            this.storyDialogue = new String[][]{
                {"Lance", "Oh, so you guys are " + flamesMeaning + ", huh?"},
                {"Player 1", "Whaaaat?"},
                {"Lance", "Now then, let's spin the wheel for which random planet we'll land on!"},
                {"Player 2", "HEY-!"}
            };
        }

        showDialogueBox();
    }

    // --- Space Scene ---
    private void startSpaceScene() {
        currentScene = 5;
        stopVideo();

        playBGM("SPACESHIP_DROP.wav");
        setBackdrop("space.png");

        showFinalPlanetOutcome();
    }
    
//game logic plug
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

    @FXML
private void handleContinueToPlanet(ActionEvent event) {
    startPlanetScene(); // Advances scene state correctly to Scene 7
}
    
private void showFinalPlanetOutcome() {
    currentScene = 6;
    hideAllPanels(); // Clears previous state

    // 1. Resolve planet image asset path
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

    // 2. Load and set planet image on charRightView BEFORE playing video
    if (!planetImageAsset.isEmpty() && charRightView != null) {
        loadImage(charRightView, planetImageAsset);
        charRightView.setVisible(true);
        charRightView.setOpacity(1.0);
    }

    // 3. Play travel video, maintaining correct z-order layer
    playVideo("spaceship_travel.mp4", false, () -> {
        Platform.runLater(() -> {
            if (charRightView != null) {
                clearImageView(charRightView);
                charRightView.setVisible(false);
            }
            startPlanetScene();
        });
    });

    // 4. Force charRightView over top of the mediaView layer explicitly
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
            {"Player 1", "text?"},
            {"Player 2", "text-!"}
        };

        showDialogueBox();
    }
    
    private void startLHS1140Scene() {
        currentScene = 7;

        setBackdrop("LHS1140.png");

        this.storyDialogue = new String[][]{
            {"Player 1", "Wow! A coincidence we landed on a planet that is similar to our planet Earth!"},
            {"Player 2", "Exactly! We can rebuild our lives here"}
        };

        showDialogueBox();
    }
    
    private void startAlphaWolfScene() {
        currentScene = 7;

        setBackdrop("ALPHAWOLF.png");

        this.storyDialogue = new String[][]{
            {"Player 1", "Oh my gosh! Its so cold in here *brrrr*"},
            {"Player 2", "*suffocates*"}
        };

        showDialogueBox();
    }
    
    private void startMercuryScene() {
        currentScene = 7;

        setBackdrop("MERCURY.png");

        this.storyDialogue = new String[][]{
            {"Player 1", "Did we just land on the planet near our star?"},
            {"Player 2", "Oh no, I can see our star bigger than ever.. I can already feel the heat."}
        };

        showDialogueBox();
    }
    
    private void startErisScene() {
        currentScene = 7;

        setBackdrop("ERIS.png");

        this.storyDialogue = new String[][]{
            {"Player 1", "Wow! A coincidence we landed on a planet that is similar to our planet Earth!"},
            {"Player 2", "Exactly! We can rebuild our lives here"}
        };

        showDialogueBox();
    }
    
    private void startSlytherinScene() {
        currentScene = 7;

        setBackdrop("SLYTHERIN.png");

        this.storyDialogue = new String[][]{
            {"Player 1", "THIS PLANET IS FULL OF STORMS!"},
            {"Player 2", "OUR SPACESHIP IS BEING CARRIED AWAY!! *screams*"}
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

    // 1. Show the stats box in lower half
    if (endingResultBox != null) {
        endingResultBox.setVisible(true);
        bringToTopLevelFront(endingResultBox);
    }

    // 2. Load image and display upper half banner
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

    // --- Dialogue Handler ---
    private void showDialogueBox() {
        if (charLeftView != null) charLeftView.setVisible(true);
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

            // Progressive Anger sequence logic:
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
                              isLanceSpeaking            ? 1.0 : 0.5);

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
                              isLanceSpeaking            ? 1.0 : 0.5);

        } else {
            if (charCenterView != null) charCenterView.setVisible(false);
            if (charLeftView != null) charLeftView.setVisible(true);
            if (charRightView != null) charRightView.setVisible(true);

            // Check if we are in the planet outcome scene (Scene 7) and if it resulted in a loss
            boolean isPlanetScene = (currentScene == 7);
            boolean isBadPlanet = isPlanetScene && "Lose".equalsIgnoreCase(this.gameState);

            if (isBadPlanet) {
                // On bad planets, both characters stay sad regardless of who is speaking
                if (speaker.equals("Player 1")) {
                    setSpeakerOpacity(1.0, 0.0, 0.5);
                } else if (speaker.equals("Player 2")) {
                    setSpeakerOpacity(0.5, 0.0, 1.0);
                }
                loadImage(charLeftView, char1SadPath);
                loadImage(charRightView, char2SadPath);

            } else {
                // Standard or good planet dialogue: raise hand when speaking, stand normally when listening
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

    // --- Audio & Media Handlers ---
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

    private void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
        }
    }

    private void stopVideo() {
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

    private void bringToTopLevelFront(Node node) {
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

    private void showVideoControls() {
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

    private void hideVideoControls() {
        if (skipButton != null) skipButton.setVisible(false);
        if (pauseButton != null) pauseButton.setVisible(false);
    }

    private void setBackdrop(String filename) {
    // Hide mediaView so it doesn't block the background image
    if (mediaView != null) {
        mediaView.setVisible(false);
    }

    if (backgroundImage != null) {
        loadImage(backgroundImage, filename);
        backgroundImage.setVisible(true);
        backgroundImage.toBack(); // Keep it behind UI panels & dialogue, but above mediaView if mediaView is hidden
    }
}

    private void loadImage(ImageView view, String filename) {
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

    private void clearImageView(ImageView view) {
        if (view != null) {
            view.setImage(null);
            view.setVisible(false);
        }
    }

private void hideAllPanels() {
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
private void showInputError(String message) {
    inputErrorLabel.setText(message);
    inputErrorLabel.setVisible(true);
    inputErrorLabel.setManaged(true);
}
    
private void hideInputError() {
    inputErrorLabel.setText("");
    inputErrorLabel.setVisible(false);
    inputErrorLabel.setManaged(false);
}
    
    
}

