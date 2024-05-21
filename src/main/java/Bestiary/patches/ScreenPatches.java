package Bestiary.patches;

import Bestiary.BestiaryMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.screens.DrawPileViewScreen;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.buttons.DynamicBanner;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

// Special thanks to blanktheevil (infinitespire)

public class ScreenPatches {


    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "update"
    )
    public static class AbstractDungeonUpdatePatch {
        @SpirePrefixPatch
        public static void Prefix() {
            BestiaryMod.update();
        }
    }


    @SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
    )
    @SpirePatch(
        clz = AbstractRoom.class,
        method = "eventControllerInput"
    )
    @SpirePatch(
        clz = AbstractScene.class,
        method = "update"
    )
    @SpirePatch(
        clz = TopPanel.class,
        method = "update"
    )
    @SpirePatch(
        clz = FtueTip.class,
        method = "update"
    )
    @SpirePatch(
        clz = DungeonMapScreen.class,
        method = "update"
    )
    @SpirePatch(
        clz = OverlayMenu.class,
        method = "update"
    )
    public static class UpdateSuppressPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix() {
            if (BestiaryMod.showOverlay) 
                return SpireReturn.Return(null);
            else
                return SpireReturn.Continue();
        }
    }

}

