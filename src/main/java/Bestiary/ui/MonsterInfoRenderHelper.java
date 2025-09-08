package Bestiary.ui;

import Bestiary.database.AscensionMoveSet;
import Bestiary.database.MonsterInfo;
import Bestiary.database.Move;
import Bestiary.database.MoveEffect;
import Bestiary.utils.ExtraColors;
import Bestiary.utils.ExtraFonts;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.util.ArrayList;

public class MonsterInfoRenderHelper {
    private MonsterInfo currMonster;
    private ArrayList<Label> labels = new ArrayList<>();

    public MonsterInfoRenderHelper(MonsterInfo monster) {
        setCurrMonster(monster);
        //System.out.println("Set current monster to " + monster);
    }

    // --------------------------------------------------------------------------------
    // UI layout information (WIP - to be tweaked as needed)

    private static final float movesLeft = MonsterOverlay.startX + (96.0f * Settings.scale);
    private static final float titleBottom = MonsterOverlay.startY + ((MonsterOverlay.texHeight - 96.0f) * Settings.scale);

    private static final float firstMoveBottom = MonsterOverlay.startY + ((MonsterOverlay.texHeight - 200.0f) * Settings.scale);
    private static final float movesVertSpacing = 80.0f * Settings.scale;

    //private static final float firstMoveEffectLeft = movesLeft + 170.0f;
    private static final float additionalMoveEffectPadding = 40.0f * Settings.scale;
    private static final float moveEffectHorizSpacing = 50.0f * Settings.scale;

    private static final float descLeft = MonsterOverlay.startX + (MonsterOverlay.texWidth * 0.55f * Settings.scale) + (96.0f * Settings.scale);
    private static final float descTop = MonsterOverlay.startY + ((MonsterOverlay.texHeight - 100.0f) * Settings.scale);
    private static final float notesTop = MonsterOverlay.startY + ((MonsterOverlay.texHeight - 700.0f) * Settings.scale);

    private static final float descWidth = (MonsterOverlay.texWidth * 0.45f * Settings.scale) - (96.0f * Settings.scale * 2.0f);
    private static final float descSpacing = 30.0f * Settings.scale;

    // --------------------------------------------------------------------------------

    private float computeMoveEffectLeft(AscensionMoveSet moveSet) {
        if (moveSet == null)
            return movesLeft + additionalMoveEffectPadding;

        float max = -1.0f;

        for (Move m: moveSet.getMoves()) {
            float w = FontHelper.getSmartWidth(FontHelper.tipBodyFont, m.getName(), 1000000, 30) / Settings.scale;
            if (w > max) {
                max = w;
            }
        }

        return movesLeft + max + additionalMoveEffectPadding;
    }

    // Loads the proper details about the selected monster and caches them for rendering (so we don't have to recompute
    //   where to put all the labels and such each frame)
    public void setCurrMonster(MonsterInfo monster) {
        this.currMonster = monster;
        labels.clear();

        if (CardCrawlGame.isInARun()) {
            int asc_level = (AbstractDungeon.isAscensionMode) ? AbstractDungeon.ascensionLevel : 0;
            AscensionMoveSet moveSet = monster.getApplicableMoveSet(asc_level);

            if (moveSet != null) {
                // Name of the mob (TODO: better [custom?] font)
                labels.add(new Label(currMonster.getName() + "  " + moveSet.getHp(), ExtraFonts.overlayTitleFont(), movesLeft, titleBottom, Settings.GOLD_COLOR));

                // AI description
                labels.add(new SmartLabel(moveSet.getDesc(),
                        FontHelper.tipBodyFont,
                        descLeft,
                        descTop,
                        descWidth,
                        descSpacing,
                        ExtraColors.OJB_GRAY_COLOR));

                // Notes (if applicable)
                if (moveSet.hasNotes()) {
                    labels.add(new SmartLabel(moveSet.getNotes(), FontHelper.tipBodyFont, descLeft, notesTop, descWidth, descSpacing, ExtraColors.OJB_GRAY_COLOR));
                }

                float movesY = firstMoveBottom;

                for (Move m : moveSet.getMoves()) {
                    // Name of the move
                    labels.add(new Label(m.getName(), FontHelper.tipBodyFont, movesLeft, movesY, Settings.CREAM_COLOR));

                    // All its effects
                    float effectX = computeMoveEffectLeft(moveSet);

                    for (MoveEffect e : m.getMoveEffects()) {
                        Label effectLabel = new Label(e.getName(), FontHelper.tipBodyFont, effectX, movesY, ExtraColors.stringToColor(e.getColor()));
                        effectX += effectLabel.getTextWidth() + moveEffectHorizSpacing;
                        labels.add(effectLabel);
                    }

                    movesY -= movesVertSpacing;
                }
            }
            else {
                // Should show some placeholder text I guess
                // Name of the mob (TODO: better [custom?] font)
                labels.add(new Label("404: Mob Not Found", ExtraFonts.overlayTitleFont(), movesLeft, titleBottom, Settings.GOLD_COLOR));

                // AI description
                labels.add(new SmartLabel("This mob is not currently in our database! If this is a vanilla monster, please leave some feedback on the Workshop page or in a Github issue so we can get this fixed! This mod does not currently support any modded monsters, sorry!",
                        FontHelper.tipBodyFont,
                        descLeft,
                        descTop,
                        descWidth,
                        descSpacing,
                        ExtraColors.OJB_GRAY_COLOR));
            }
        }

    }


    public void render(SpriteBatch sb) {
        for (Label l : labels)
            l.render(sb);
    }
}
