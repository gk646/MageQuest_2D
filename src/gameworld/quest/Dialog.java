/*
 * MIT License
 *
 * Copyright (c) 2023 gk646
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gameworld.quest;

import gameworld.entities.ENTITY;
import gameworld.player.Player;
import input.InputHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.system.sound.Sound;
import main.system.ui.Colors;
import main.system.ui.FonT;

public class Dialog {
    public int dialogRenderCounter = 0;
    public String dialogLine = "...";
    public boolean drawChoice;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private int choicePointer = 0;
    private int maxChoices;

    /**
     * The dialog framework
     * <p>
     * mainGame to access cross-class information
     * to choose the type for dialog text (also quest id)
     */
    public Dialog() {
    }

    public static String insertNewLine(String str, int number) {
        StringBuilder sb = new StringBuilder();
        String[] words = str.split("\\s+");
        int count = 0;
        for (String word : words) {
            if (count + word.length() > number) {
                sb.append("\n");
                count = 0;
            }
            count += word.length();
            sb.append(word);
            sb.append(" ");
            count++;
        }
        return sb.toString();
    }

    public void drawDialog(GraphicsContext gc, ENTITY entity) {
        Player.interactingWithNPC = true;
        gc.setFont(FonT.varnished14);
        gc.setFill(Colors.black_transparent);
        gc.fillRoundRect(entity.worldX - Player.worldX + Player.screenX - 24 - 124, entity.worldY - Player.worldY + Player.screenY - 24 - 115, 373, 120, 25, 25);
        gc.setLineWidth(2);
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        int x = (int) (entity.worldX - Player.worldX + Player.screenX - 24 + 5 - 124);
        gc.strokeRoundRect(entity.worldX - Player.worldX + Player.screenX - 24 - 124, entity.worldY - Player.worldY + Player.screenY - 24 - 115, 373, 120, 25, 25);
        if (dialogRenderCounter == 2_000) {
            int stringY = (int) (entity.worldY - Player.worldY + Player.screenY - 24 - 115 + 6);
            for (String string : dialogLine.split("\n")) {
                gc.fillText(string, x, stringY += 16);
            }
            if (drawChoice) {
                gc.fillText(choice1, x + 15, stringY + 20);
                Text text = new Text(choice1);
                text.setFont(gc.getFont());
                gc.fillText(choice2, x + 35 + text.getLayoutBounds().getWidth(), stringY + 20);
                if (maxChoices > 2) {
                    gc.fillText(choice3, x + 15, stringY + 40);
                }
                if (maxChoices > 3) {
                    text = new Text(choice3);
                    text.setFont(gc.getFont());
                    gc.fillText(choice4, x + 35 + text.getLayoutBounds().getWidth(), stringY + 40);
                }
                if (choicePointer == 0) {
                    gc.fillText(">", x, stringY + 20);
                } else if (choicePointer == 1) {
                    text = new Text(choice1);
                    text.setFont(gc.getFont());
                    gc.fillText(">", x + 20 + text.getLayoutBounds().getWidth(), stringY + 20);
                } else if (choicePointer == 2) {
                    gc.fillText(">", x, stringY + 40);
                } else if (choicePointer == 3) {
                    text = new Text(choice3);
                    text.setFont(gc.getFont());
                    gc.fillText(">", x + 20 + text.getLayoutBounds().getWidth(), stringY + 40);
                }
                if (InputHandler.q_typed) {
                    if (choicePointer < maxChoices - 1) {
                        choicePointer++;
                    } else {
                        choicePointer = 0;
                    }
                    InputHandler.q_typed = false;
                }
                if (InputHandler.f_typed) {
                    if (choicePointer == 0) {
                        choicePointer = 10;
                    } else if (choicePointer == 1) {
                        choicePointer = 20;
                    } else if (choicePointer == 2) {
                        choicePointer = 30;
                    } else if (choicePointer == 3) {
                        choicePointer = 40;
                    }
                    InputHandler.f_typed = false;
                }
            }
        } else {
            gc.strokeRoundRect(entity.worldX - Player.worldX + Player.screenX - 24 - 124, entity.worldY - Player.worldY + Player.screenY - 24 - 115, 373, 120, 25, 25);
            int stringY = (int) (entity.worldY - Player.worldY + Player.screenY - 24 - 115 + 6);
            for (String string : dialogLine.substring(0, Math.min(dialogLine.length(), dialogRenderCounter / 4)).split("\n")) {
                gc.fillText(string, x, stringY += 16);
            }
            if (dialogRenderCounter / 4 >= dialogLine.length() && dialogRenderCounter < 2000) {
                dialogRenderCounter = 2_000;
            } else {
                if (dialogRenderCounter % 8 == 0) {
                    MediaPlayer mediaPlayer = new MediaPlayer(Sound.dialogBeep);
                    mediaPlayer.play();
                    mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
                }
                dialogRenderCounter++;
            }
        }
    }

    public void drawDialog(GraphicsContext gc) {
        gc.setFont(FonT.varnished14);
        gc.setFill(Colors.black_transparent);
        gc.fillRoundRect(Player.screenX - 24 - 124, Player.screenY - 24 - 115, 373, 120, 25, 25);
        gc.setLineWidth(2);
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        int x = Player.screenX - 24 + 5 - 124;
        gc.strokeRoundRect(Player.screenX - 24 - 124, Player.screenY - 24 - 115, 373, 120, 25, 25);
        if (dialogRenderCounter == 2_000) {
            int stringY = Player.screenY - 24 - 115 + 6;
            for (String string : dialogLine.split("\n")) {
                gc.fillText(string, x, stringY += 16);
            }
            if (drawChoice) {
                gc.fillText(choice1, x + 15, stringY += 20);
                gc.fillText(choice2, x + 155, stringY);
                if (choicePointer == 0) {
                    gc.fillText(">", x, stringY);
                } else if (choicePointer == 1) {
                    gc.fillText(">", x + 140, stringY);
                }
                if (InputHandler.q_typed) {
                    if (choicePointer < maxChoices - 1) {
                        choicePointer++;
                    } else {
                        choicePointer = 0;
                    }
                    InputHandler.q_typed = false;
                }
                if (InputHandler.f_typed) {
                    if (choicePointer == 0) {
                        choicePointer = 10;
                    } else if (choicePointer == 1) {
                        choicePointer = 20;
                    } else if (choicePointer == 2) {
                        choicePointer = 30;
                    } else if (choicePointer == 3) {
                        choicePointer = 40;
                    }
                    InputHandler.f_typed = false;
                }
            }
        } else {
            gc.strokeRoundRect(Player.screenX - 24 - 124, Player.screenY - 24 - 115, 373, 120, 25, 25);
            int stringY = Player.screenY - 24 - 115 + 6;
            for (String string : dialogLine.substring(0, Math.min(dialogLine.length(), dialogRenderCounter / 4)).split("\n")) {
                gc.fillText(string, x, stringY += 16);
            }
            if (dialogRenderCounter / 4 >= dialogLine.length() && dialogRenderCounter < 2_000) {
                dialogRenderCounter = 2_000;
            } else {
                dialogRenderCounter++;
            }
        }
    }

    public void loadNewLine(String dialogLine) {
        this.dialogLine = insertNewLine(dialogLine, 45);
        this.dialogRenderCounter = 0;
    }

    public int drawChoice(String option1, String option2, String option3, String option4) {
        if (drawChoice) {
            if (choicePointer == 10 || choicePointer == 20 || choicePointer == 30 || choicePointer == 40) {
                drawChoice = false;
                choice1 = null;
                choice2 = null;
                choice3 = null;
                choice4 = null;
                return choicePointer;
            } else {
                return 0;
            }
        } else {
            drawChoice = true;
            maxChoices = 1;
            choice1 = option1;
            choicePointer = 0;
            if (option2 != null) {
                choice2 = option2;
                maxChoices++;
            }
            if (option3 != null) {
                maxChoices++;
                choice3 = option3;
            }
            if (option4 != null) {
                maxChoices++;
                choice4 = option4;
            }
        }
        return 0;
    }
}
