package HollowMod.patches;
import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import HollowMod.hollowMod;

@SpirePatch(
        clz = TempMusic.class,
        method = "getSong")

public class BossMusicPatch {


      @SpirePostfixPatch
    public static SpireReturn<Music> Prefix(TempMusic __instance, String key) {
        hollowMod.logger.info("Music patch Temp hit");
        switch (key) {
            case "BOSS_FALSE": {
                return SpireReturn.Return(MainMusic.newMusic("HollowModResources/audio/sounds/FalseBGM.ogg"));
            }
            case "BOSS_GRIMM": {
                return SpireReturn.Return(MainMusic.newMusic("HollowModResources/audio/sounds/GrimmBGM.ogg"));
            }
            case "BOSS_RAD": {
                return SpireReturn.Return(MainMusic.newMusic("HollowModResources/audio/sounds/RadBGM.ogg"));
            }
            default: {
                return SpireReturn.Continue();
            }
        }

    }

}

