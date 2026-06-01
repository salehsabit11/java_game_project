import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScoreManager {

    private final Path highScorePath;

    private int score;
    private int highScore;
    private int passedPipePairs;

    private Difficulty difficulty;

    public ScoreManager() {

        highScorePath = Paths.get("highscore.txt");

        score = 0;
        passedPipePairs = 0;
        difficulty = Difficulty.NORMAL;

        highScore = loadHighScore();
    }

    private int loadHighScore() {

        try {

            if (Files.exists(highScorePath)) {

                String text =
                        Files.readString(highScorePath).trim();

                return Integer.parseInt(text);
            }

        } catch (IOException | NumberFormatException e) {
            return 0;
        }

        return 0;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void resetScore() {
        score = 0;
        passedPipePairs = 0;
    }

    public void registerPassedPipePair() {

        passedPipePairs++;

        if (passedPipePairs >= difficulty.getPipePairsPerPoint()) {

            score++;
            passedPipePairs = 0;
        }
    }

    public void saveHighScoreIfNeeded() {

        if (score > highScore) {

            highScore = score;

            try {

                Files.writeString(
                        highScorePath,
                        String.valueOf(highScore)
                );

            } catch (IOException e) {

                System.out.println("Could not save high score");
            }
        }
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }
}