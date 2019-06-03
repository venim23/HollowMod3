package HollowMod.patches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.exordium.Mushrooms;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.Instanceof;
import HollowMod.hollowMod;

@SpirePatch(clz = ProceedButton.class, method = "update")
public class VexCombatFix {
    // Note: this should really be moved to BaseMod
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(Instanceof i) throws CannotCompileException {
                try {
                    if (i.getType().getName().equals(Mushrooms.class.getName())) {
                        i.replace("$_ = $proceed($$) || currentRoom.event instanceof HollowMod.events.ZoteMeetingEvent;");
                    }
                } catch (NotFoundException e) {
                    hollowMod.logger.error("Combat proceed button patch broken.", e);
                }
            }
        };
    }
}