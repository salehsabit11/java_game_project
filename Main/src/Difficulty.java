public enum Difficulty {

    NORMAL("Normal", -3, 190, 1),
    HARD("Hard", -5, 140, 2);

    private final String displayName;
    private final int pipeVelocityX;
    private final int openingSpace;
    private final int pipePairsPerPoint;

    Difficulty(
            String displayName,
            int pipeVelocityX,
            int openingSpace,
            int pipePairsPerPoint) {

        this.displayName = displayName;
        this.pipeVelocityX = pipeVelocityX;
        this.openingSpace = openingSpace;
        this.pipePairsPerPoint = pipePairsPerPoint;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPipeVelocityX() {
        return pipeVelocityX;
    }

    public int getOpeningSpace() {
        return openingSpace;
    }

    public int getPipePairsPerPoint() {
        return pipePairsPerPoint;
    }

    @Override
    public String toString() {
        return displayName;
    }
}