package gameworld.quest;

public enum QUEST_NAME {
    Tutorial(1), TheFakeNecklace(2), TheAudition(3), HillcrestPuzzle(4);
    public final int val;

    QUEST_NAME(int val) {
        this.val = val;
    }
}
